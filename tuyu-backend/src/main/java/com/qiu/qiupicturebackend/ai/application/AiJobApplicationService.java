package com.qiu.qiupicturebackend.ai.application;

import com.qiu.qiupicturebackend.activity.application.ActivityRecordApplicationService;
import com.qiu.qiupicturebackend.activity.application.command.RecordActivityCommand;
import com.qiu.qiupicturebackend.ai.application.command.CreateAiJobCommand;
import com.qiu.qiupicturebackend.ai.application.query.AiJobQuery;
import com.qiu.qiupicturebackend.ai.domain.model.AiJobResultView;
import com.qiu.qiupicturebackend.ai.domain.model.AiJobView;
import com.qiu.qiupicturebackend.ai.domain.model.AiCapabilityView;
import com.qiu.qiupicturebackend.ai.domain.provider.AiProviderGateway;
import com.qiu.qiupicturebackend.ai.domain.provider.AiProviderTaskResult;
import com.qiu.qiupicturebackend.ai.domain.repository.AiCapabilityRepository;
import com.qiu.qiupicturebackend.ai.domain.repository.AiJobRepository;
import com.qiu.qiupicturebackend.ai.domain.repository.AiJobResultRepository;
import com.qiu.qiupicturebackend.ai.domain.repository.AiProviderTaskRepository;
import com.qiu.qiupicturebackend.ai.domain.repository.AiUsageRecordRepository;
import com.qiu.qiupicturebackend.asset.domain.model.AssetVersionView;
import com.qiu.qiupicturebackend.asset.domain.repository.AssetVersionRepository;
import com.qiu.qiupicturebackend.exception.BusinessException;
import com.qiu.qiupicturebackend.exception.ErrorCode;
import com.qiu.qiupicturebackend.model.entity.Picture;
import com.qiu.qiupicturebackend.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class AiJobApplicationService {

    @Resource
    private AiJobRepository aiJobRepository;

    @Resource
    private AiJobResultRepository aiJobResultRepository;

    @Resource
    private AiCapabilityRepository aiCapabilityRepository;

    @Resource
    private AiProviderTaskRepository aiProviderTaskRepository;

    @Resource
    private AiUsageRecordRepository aiUsageRecordRepository;

    @Resource
    private AiProviderGateway aiProviderGateway;

    @Resource
    private ActivityRecordApplicationService activityRecordApplicationService;

    @Resource
    private AssetVersionRepository assetVersionRepository;

    @Resource
    private PictureService pictureService;

    public AiJobView createJob(Long workspaceId, CreateAiJobCommand command, Long loginUserId) {
        if (workspaceId == null || workspaceId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "工作区 ID 无效");
        }
        if (command == null || command.getCapabilityKey() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "AI 能力未指定");
        }

        // Idempotency check — must happen before capability validation so
        // duplicate submissions return the existing job regardless of state.
        if (command.getIdempotencyKey() != null && !command.getIdempotencyKey().isBlank()) {
            Optional<AiJobView> existing = aiJobRepository.findByIdempotencyKey(command.getIdempotencyKey());
            if (existing.isPresent()) {
                AiJobView job = existing.get();
                job.setResults(aiJobResultRepository.findByJobId(job.getJobId()));
                return job;
            }
        }

        // Validate capability exists and is active
        AiCapabilityView capability = aiCapabilityRepository.findByKey(command.getCapabilityKey())
                .orElseThrow(() -> new BusinessException(ErrorCode.PARAMS_ERROR, "AI 能力不存在"));
        if (!capability.isActive()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该 AI 能力暂未开放");
        }

        Date now = new Date();
        AiJobView job = new AiJobView();
        job.setWorkspaceId(workspaceId);
        job.setCreatorUserId(loginUserId);
        job.setCapabilityKey(command.getCapabilityKey());
        job.setStatus("created");
        job.setSourceAssetId(command.getSourceAssetId());
        job.setSourceAssetVersionId(command.getSourceAssetVersionId());
        job.setProvider(capability.getProvider());
        job.setParametersJson(parametersToJson(command.getParameters()));
        job.setIdempotencyKey(command.getIdempotencyKey());
        job.setCreatedAt(now);

        AiJobView saved = aiJobRepository.save(job);

        // Submit to provider
        try {
            String imageUrl = resolveImageUrl(command.getSourceAssetId());
            AiProviderTaskResult result = aiProviderGateway.submitTask(imageUrl, command.getParameters());
            if (result.getProviderTaskId() != null) {
                aiProviderTaskRepository.save(saved.getJobId(), capability.getProvider(),
                        result.getProviderTaskId(), result.getStatus());
                saved.setStatus("running");
                saved.setStartedAt(now);
                aiJobRepository.update(saved);
            } else {
                saved.setStatus("failed");
                saved.setErrorCode(result.getErrorCode());
                saved.setErrorMessage(result.getErrorMessage());
                saved.setFinishedAt(now);
                aiJobRepository.update(saved);
            }
        } catch (Exception e) {
            log.warn("Provider submit failed for job {}: {}", saved.getJobId(), e.getMessage());
            saved.setStatus("failed");
            saved.setErrorCode("SUBMIT_FAILED");
            saved.setErrorMessage(e.getMessage());
            saved.setFinishedAt(now);
            aiJobRepository.update(saved);
        }

        // Record usage
        try {
            aiUsageRecordRepository.save(saved, loginUserId, "api_call");
        } catch (Exception e) {
            log.warn("Usage record failed for job {}: {}", saved.getJobId(), e.getMessage());
        }

        // Record activity (best-effort)
        try {
            activityRecordApplicationService.record(RecordActivityCommand.builder()
                    .workspaceId(workspaceId)
                    .actorUserId(loginUserId)
                    .actionType("ai.job.created")
                    .targetType("asset")
                    .targetId(command.getSourceAssetId())
                    .secondaryTargetType("ai_job")
                    .secondaryTargetId(saved.getJobId())
                    .summary("创建了 AI 任务")
                    .occurredAt(now)
                    .build());
        } catch (Exception e) {
            log.warn("Activity record failed for job {}: {}", saved.getJobId(), e.getMessage());
        }

        saved.setResults(Collections.emptyList());
        return saved;
    }

    public List<AiJobView> listJobs(Long workspaceId, AiJobQuery query) {
        if (workspaceId == null || workspaceId <= 0) {
            return Collections.emptyList();
        }
        int offset = query != null ? query.getOffset() : 0;
        int limit = query != null ? query.getLimit() : 20;

        List<AiJobView> jobs;
        if (query != null && query.getStatus() != null && !query.getStatus().isBlank()) {
            jobs = aiJobRepository.findByWorkspaceAndStatus(workspaceId, query.getStatus(), offset, limit);
        } else {
            jobs = aiJobRepository.findByWorkspaceId(workspaceId, offset, limit);
        }

        for (AiJobView job : jobs) {
            job.setResults(aiJobResultRepository.findByJobId(job.getJobId()));
        }
        return jobs;
    }

    public AiJobView getJob(Long workspaceId, Long jobId) {
        AiJobView job = aiJobRepository.findById(jobId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR, "任务不存在"));
        if (!job.getWorkspaceId().equals(workspaceId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "任务不属于该工作区");
        }
        job.setResults(aiJobResultRepository.findByJobId(jobId));
        return job;
    }

    private String parametersToJson(Map<String, Object> parameters) {
        if (parameters == null || parameters.isEmpty()) return null;
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(parameters);
        } catch (Exception e) {
            return null;
        }
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
}
