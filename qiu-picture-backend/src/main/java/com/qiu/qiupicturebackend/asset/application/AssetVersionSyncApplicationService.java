package com.qiu.qiupicturebackend.asset.application;

import com.qiu.qiupicturebackend.activity.application.ActivityRecordApplicationService;
import com.qiu.qiupicturebackend.activity.application.command.RecordActivityCommand;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.entity.AssetMetadataEntity;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.entity.AssetStorageObjectEntity;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.entity.AssetVersionEntity;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.mapper.AssetMetadataMapper;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.mapper.AssetStorageObjectMapper;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.mapper.AssetVersionMapper;
import com.qiu.qiupicturebackend.model.entity.Picture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class AssetVersionSyncApplicationService {

    @Resource
    private AssetVersionMapper assetVersionMapper;

    @Resource
    private AssetStorageObjectMapper assetStorageObjectMapper;

    @Resource
    private AssetMetadataMapper assetMetadataMapper;

    @Resource
    private ActivityRecordApplicationService activityRecordApplicationService;

    @Async
    public void syncInitialVersion(Picture picture) {
        if (picture == null || picture.getId() == null) {
            return;
        }

        // Idempotency: skip if a version already exists for this picture
        Long count = assetVersionMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<AssetVersionEntity>()
                        .eq(AssetVersionEntity::getLegacyPictureId, picture.getId())
        );
        if (count != null && count > 0) {
            return;
        }

        try {
            AssetStorageObjectEntity storageObject = new AssetStorageObjectEntity();
            storageObject.setLegacyUrl(picture.getUrl());
            storageObject.setFileSize(picture.getPicSize());
            storageObject.setWidth(picture.getPicWidth());
            storageObject.setHeight(picture.getPicHeight());
            storageObject.setFormat(picture.getPicFormat());
            storageObject.setDominantColor(picture.getPicColor());
            assetStorageObjectMapper.insert(storageObject);

            Long thumbnailStorageObjectId = null;
            if (picture.getThumbnailUrl() != null && !picture.getThumbnailUrl().isEmpty()) {
                AssetStorageObjectEntity thumbnailObject = new AssetStorageObjectEntity();
                thumbnailObject.setLegacyUrl(picture.getThumbnailUrl());
                assetStorageObjectMapper.insert(thumbnailObject);
                thumbnailStorageObjectId = thumbnailObject.getId();
            }

            AssetMetadataEntity metadata = new AssetMetadataEntity();
            metadata.setAssetId(picture.getId());
            metadata.setWidth(picture.getPicWidth());
            metadata.setHeight(picture.getPicHeight());
            metadata.setScale(picture.getPicScale());
            metadata.setFormat(picture.getPicFormat());
            metadata.setFileSize(picture.getPicSize());
            metadata.setDominantColor(picture.getPicColor());
            metadata.setCategory(picture.getCategory());
            metadata.setTags(picture.getTags());
            metadata.setDescription(picture.getIntroduction());
            assetMetadataMapper.insert(metadata);

            AssetVersionEntity version = new AssetVersionEntity();
            version.setAssetId(picture.getId());
            version.setLegacyPictureId(picture.getId());
            version.setVersionNo(1);
            version.setVersionType("original");
            version.setStorageObjectId(storageObject.getId());
            version.setThumbnailStorageObjectId(thumbnailStorageObjectId);
            version.setMetadataId(metadata.getId());
            version.setCreatedBy(picture.getUserId());
            version.setIsCurrent(1);
            assetVersionMapper.insert(version);

            metadata.setVersionId(version.getId());
            assetMetadataMapper.updateById(metadata);

            activityRecordApplicationService.record(RecordActivityCommand.builder()
                    .workspaceId(picture.getSpaceId())
                    .actorUserId(picture.getUserId())
                    .actionType("asset.version.created")
                    .targetType("asset")
                    .targetId(picture.getId())
                    .targetName(picture.getName())
                    .summary("创建了资产「" + picture.getName() + "」的初始版本")
                    .occurredAt(new Date())
                    .build());

        } catch (Exception e) {
            log.error("版本同步失败, pictureId: {}, url: {}", picture.getId(), picture.getUrl(), e);
        }
    }
}
