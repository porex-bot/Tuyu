package com.qiu.qiupicturebackend.ai.application;

import com.qiu.qiupicturebackend.activity.application.ActivityRecordApplicationService;
import com.qiu.qiupicturebackend.activity.application.command.RecordActivityCommand;
import com.qiu.qiupicturebackend.ai.domain.model.AiJobResultView;
import com.qiu.qiupicturebackend.ai.domain.model.AiJobView;
import com.qiu.qiupicturebackend.ai.domain.repository.AiJobRepository;
import com.qiu.qiupicturebackend.ai.domain.repository.AiJobResultRepository;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.entity.AssetStorageObjectEntity;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.entity.AssetVersionEntity;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.mapper.AssetStorageObjectMapper;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.mapper.AssetVersionMapper;
import com.qiu.qiupicturebackend.exception.BusinessException;
import com.qiu.qiupicturebackend.exception.ErrorCode;
import com.qiu.qiupicturebackend.model.entity.Picture;
import com.qiu.qiupicturebackend.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class AiResultApplicationService {

    @Resource
    private AiJobRepository aiJobRepository;

    @Resource
    private AiJobResultRepository aiJobResultRepository;

    @Resource
    private AssetVersionMapper assetVersionMapper;

    @Resource
    private AssetStorageObjectMapper assetStorageObjectMapper;

    @Resource
    private ActivityRecordApplicationService activityRecordApplicationService;

    @Resource
    private PictureService pictureService;

    @Transactional
    public AiJobResultView applyResult(Long workspaceId, Long jobId, Long resultId, Long loginUserId) {
        AiJobView job = aiJobRepository.findById(jobId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR, "任务不存在"));
        if (!job.getWorkspaceId().equals(workspaceId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "任务不属于该工作区");
        }
        if (!"succeeded".equals(job.getStatus())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "只有成功的任务结果才能应用");
        }

        AiJobResultView result = aiJobResultRepository.findById(resultId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR, "结果不存在"));
        if (!result.getJobId().equals(jobId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "结果不属于该任务");
        }
        if (!"pending".equals(result.getApplyStatus())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该结果已被应用或丢弃");
        }

        Long assetId = job.getSourceAssetId();
        if (assetId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "任务缺少源资产信息");
        }

        // Look up the source picture to get the original asset for versioning
        Picture picture = pictureService.getById(assetId);
        if (picture == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "源资产不存在");
        }

        // Persist AI output URL as a storage object
        AssetStorageObjectEntity storageObject = new AssetStorageObjectEntity();
        storageObject.setLegacyUrl(result.getOutputUrl());
        storageObject.setFormat("png");
        if (picture.getPicWidth() != null) storageObject.setWidth(picture.getPicWidth());
        if (picture.getPicHeight() != null) storageObject.setHeight(picture.getPicHeight());
        assetStorageObjectMapper.insert(storageObject);

        // Find current max version number for this asset
        Long currentMaxVersion = assetVersionMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<AssetVersionEntity>()
                        .eq(AssetVersionEntity::getAssetId, assetId)
        );

        // Create AI-generated asset version
        AssetVersionEntity version = new AssetVersionEntity();
        version.setAssetId(assetId);
        version.setLegacyPictureId(picture.getId());
        version.setVersionNo(currentMaxVersion != null ? currentMaxVersion.intValue() + 1 : 2);
        version.setVersionType("ai_generated");
        version.setStorageObjectId(storageObject.getId());
        version.setCreatedBy(loginUserId);
        version.setIsCurrent(0);
        assetVersionMapper.insert(version);

        // Mark result as applied
        result.setApplyStatus("applied");
        result.setAssetVersionId(version.getId());
        aiJobResultRepository.update(result);

        // Mark job as applied
        job.setStatus("applied");
        aiJobRepository.update(job);

        // Record activity (best-effort)
        try {
            activityRecordApplicationService.record(RecordActivityCommand.builder()
                    .workspaceId(workspaceId)
                    .actorUserId(loginUserId)
                    .actionType("ai.result.applied")
                    .targetType("asset")
                    .targetId(assetId)
                    .secondaryTargetType("asset_version")
                    .secondaryTargetId(version.getId())
                    .summary("应用了 AI 生成结果到资产")
                    .occurredAt(new Date())
                    .build());
        } catch (Exception e) {
            log.warn("Activity record failed for ai result apply: {}", e.getMessage());
        }

        return result;
    }
}
