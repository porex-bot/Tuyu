package com.qiu.qiupicturebackend.ai.application;

import com.qiu.qiupicturebackend.ai.domain.model.AiJobResultView;
import com.qiu.qiupicturebackend.ai.domain.model.AiJobView;
import com.qiu.qiupicturebackend.ai.domain.provider.AiProviderGateway;
import com.qiu.qiupicturebackend.ai.domain.provider.AiProviderTaskResult;
import com.qiu.qiupicturebackend.ai.domain.repository.AiJobRepository;
import com.qiu.qiupicturebackend.ai.domain.repository.AiJobResultRepository;
import com.qiu.qiupicturebackend.ai.domain.repository.AiProviderTaskRepository;
import com.qiu.qiupicturebackend.asset.domain.model.AssetVersionView;
import com.qiu.qiupicturebackend.asset.domain.repository.AssetVersionRepository;
import com.qiu.qiupicturebackend.exception.BusinessException;
import com.qiu.qiupicturebackend.exception.ErrorCode;
import com.qiu.qiupicturebackend.model.entity.Picture;
import com.qiu.qiupicturebackend.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class AiJobLifecycleApplicationService {

    @Resource
    private AiJobRepository aiJobRepository;

    @Resource
    private AiJobResultRepository aiJobResultRepository;

    @Resource
    private AiProviderTaskRepository aiProviderTaskRepository;

    @Resource
    private AiProviderGateway aiProviderGateway;

    @Resource
    private AssetVersionRepository assetVersionRepository;

    @Resource
    private PictureService pictureService;

    public AiJobView cancelJob(Long workspaceId, Long jobId) {
        AiJobView job = getAndValidate(workspaceId, jobId);
        if ("succeeded".equals(job.getStatus()) || "failed".equals(job.getStatus())
                || "cancelled".equals(job.getStatus()) || "applied".equals(job.getStatus())
                || "discarded".equals(job.getStatus())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该任务状态不允许取消");
        }
        job.setStatus("cancelled");
        job.setFinishedAt(new Date());
        aiJobRepository.update(job);
        return job;
    }

    public AiJobView retryJob(Long workspaceId, Long jobId) {
        AiJobView job = getAndValidate(workspaceId, jobId);
        if (!"failed".equals(job.getStatus()) && !"cancelled".equals(job.getStatus())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "只有失败或已取消的任务可以重试");
        }
        // Reset status and re-submit
        job.setStatus("running");
        job.setErrorCode(null);
        job.setErrorMessage(null);
        job.setStartedAt(new Date());
        aiJobRepository.update(job);

        try {
            String imageUrl = resolveImageUrl(job.getSourceAssetId());
            AiProviderTaskResult result = aiProviderGateway.submitTask(imageUrl,
                    parseParametersJson(job.getParametersJson()));
            if (result.getProviderTaskId() != null) {
                aiProviderTaskRepository.save(jobId, job.getProvider(),
                        result.getProviderTaskId(), result.getStatus());
            } else {
                job.setStatus("failed");
                job.setErrorCode(result.getErrorCode());
                job.setErrorMessage(result.getErrorMessage());
                job.setFinishedAt(new Date());
                aiJobRepository.update(job);
            }
        } catch (Exception e) {
            log.warn("Retry submit failed for job {}: {}", jobId, e.getMessage());
            job.setStatus("failed");
            job.setErrorCode("SUBMIT_FAILED");
            job.setErrorMessage(e.getMessage());
            job.setFinishedAt(new Date());
            aiJobRepository.update(job);
        }

        return job;
    }

    public AiJobView completeJob(Long jobId, AiProviderTaskResult providerResult) {
        AiJobView job = aiJobRepository.findById(jobId).orElse(null);
        if (job == null) return null;

        if ("succeeded".equals(providerResult.getStatus())) {
            job.setStatus("succeeded");
            job.setFinishedAt(new Date());

            AiJobResultView result = new AiJobResultView();
            result.setJobId(jobId);
            result.setResultType("image");
            result.setOutputUrl(providerResult.getOutputImageUrl());
            result.setApplyStatus("pending");
            result.setCreatedAt(new Date());
            aiJobResultRepository.save(result);
        } else if ("failed".equals(providerResult.getStatus())) {
            job.setStatus("failed");
            job.setErrorCode(providerResult.getErrorCode());
            job.setErrorMessage(providerResult.getErrorMessage());
            job.setFinishedAt(new Date());
        }

        aiJobRepository.update(job);
        return job;
    }

    private AiJobView getAndValidate(Long workspaceId, Long jobId) {
        AiJobView job = aiJobRepository.findById(jobId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR, "任务不存在"));
        if (!job.getWorkspaceId().equals(workspaceId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "任务不属于该工作区");
        }
        return job;
    }

    private String resolveImageUrl(Long assetId) {
        if (assetId == null) return null;

        // Try current asset version storage URL first
        Optional<AssetVersionView> currentVersion = assetVersionRepository.findCurrentByAssetId(assetId);
        if (currentVersion.isPresent()) {
            AssetVersionView version = currentVersion.get();
            if (version.getStorageUrl() != null && !version.getStorageUrl().isBlank()) {
                return version.getStorageUrl();
            }
            // Fallback to thumbnail if no storage URL
            if (version.getThumbnailUrl() != null && !version.getThumbnailUrl().isBlank()) {
                return version.getThumbnailUrl();
            }
        }

        // Fallback to legacy picture URL
        Picture picture = pictureService.getById(assetId);
        if (picture != null && picture.getUrl() != null && !picture.getUrl().isBlank()) {
            return picture.getUrl();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private java.util.Map<String, Object> parseParametersJson(String json) {
        if (json == null || json.isBlank()) return null;
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(json, java.util.Map.class);
        } catch (Exception e) {
            return null;
        }
    }
}
