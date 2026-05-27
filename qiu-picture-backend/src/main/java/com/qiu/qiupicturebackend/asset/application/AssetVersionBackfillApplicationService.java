package com.qiu.qiupicturebackend.asset.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiu.qiupicturebackend.asset.application.command.BackfillAssetVersionsCommand;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.entity.AssetMetadataEntity;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.entity.AssetStorageObjectEntity;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.entity.AssetVersionBackfillLogEntity;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.entity.AssetVersionEntity;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.mapper.AssetMetadataMapper;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.mapper.AssetStorageObjectMapper;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.mapper.AssetVersionBackfillLogMapper;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.mapper.AssetVersionMapper;
import com.qiu.qiupicturebackend.mapper.PictureMapper;
import com.qiu.qiupicturebackend.model.entity.Picture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AssetVersionBackfillApplicationService {

    @Resource
    private PictureMapper pictureMapper;

    @Resource
    private AssetVersionMapper assetVersionMapper;

    @Resource
    private AssetStorageObjectMapper assetStorageObjectMapper;

    @Resource
    private AssetMetadataMapper assetMetadataMapper;

    @Resource
    private AssetVersionBackfillLogMapper backfillLogMapper;

    public Map<String, Object> backfill(BackfillAssetVersionsCommand command) {
        int batchSize = Math.min(command.getBatchSize(), 500);
        int maxBatches = command.getMaxBatches() > 0 ? command.getMaxBatches() : Integer.MAX_VALUE;
        boolean dryRun = command.isDryRun();

        int processed = 0;
        int skipped = 0;
        int errors = 0;
        int currentBatch = 0;

        long totalPictures = pictureMapper.selectCount(new LambdaQueryWrapper<>());
        log.info("开始回填，总 picture 数: {}, 批次大小: {}, dryRun: {}", totalPictures, batchSize, dryRun);

        long maxPictureId = 0;
        while (currentBatch < maxBatches) {
            List<Picture> batch = pictureMapper.selectList(
                    new LambdaQueryWrapper<Picture>()
                            .gt(Picture::getId, maxPictureId)
                            .orderByAsc(Picture::getId)
                            .last("limit " + batchSize)
            );

            if (batch.isEmpty()) {
                break;
            }

            for (Picture picture : batch) {
                maxPictureId = Math.max(maxPictureId, picture.getId());

                if (isAlreadyProcessed(picture.getId())) {
                    skipped++;
                    continue;
                }

                try {
                    if (!dryRun) {
                        backfillPicture(picture);
                    }
                    processed++;
                } catch (Exception e) {
                    errors++;
                    log.error("回填失败, pictureId: {}, url: {}", picture.getId(), picture.getUrl(), e);
                    try {
                        recordError(picture.getId(), e.getMessage());
                    } catch (Exception ignored) {
                    }
                }
            }

            currentBatch++;
            log.info("批次 {} 完成, processed: {}, skipped: {}, errors: {}", currentBatch, processed, skipped, errors);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalPictures", totalPictures);
        result.put("processed", processed);
        result.put("skipped", skipped);
        result.put("errors", errors);
        result.put("dryRun", dryRun);
        return result;
    }

    private boolean isAlreadyProcessed(Long legacyPictureId) {
        return backfillLogMapper.selectOne(
                new LambdaQueryWrapper<AssetVersionBackfillLogEntity>()
                        .eq(AssetVersionBackfillLogEntity::getLegacyPictureId, legacyPictureId)
        ) != null;
    }

    private void backfillPicture(Picture picture) {
        // 1. Create storage object from picture.url
        AssetStorageObjectEntity storageObject = new AssetStorageObjectEntity();
        storageObject.setLegacyUrl(picture.getUrl());
        storageObject.setFileSize(picture.getPicSize());
        storageObject.setWidth(picture.getPicWidth());
        storageObject.setHeight(picture.getPicHeight());
        storageObject.setFormat(picture.getPicFormat());
        storageObject.setDominantColor(picture.getPicColor());
        assetStorageObjectMapper.insert(storageObject);

        // 2. Create thumbnail storage object if present
        Long thumbnailStorageObjectId = null;
        if (picture.getThumbnailUrl() != null && !picture.getThumbnailUrl().isEmpty()) {
            AssetStorageObjectEntity thumbnailObject = new AssetStorageObjectEntity();
            thumbnailObject.setLegacyUrl(picture.getThumbnailUrl());
            assetStorageObjectMapper.insert(thumbnailObject);
            thumbnailStorageObjectId = thumbnailObject.getId();
        }

        // 3. Create metadata
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

        // 4. Create version
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

        // 5. Update metadata with version_id
        metadata.setVersionId(version.getId());
        assetMetadataMapper.updateById(metadata);

        // 6. Record backfill
        AssetVersionBackfillLogEntity logEntry = new AssetVersionBackfillLogEntity();
        logEntry.setLegacyPictureId(picture.getId());
        logEntry.setVersionId(version.getId());
        logEntry.setStatus("success");
        backfillLogMapper.insert(logEntry);
    }

    private void recordError(Long legacyPictureId, String errorMessage) {
        AssetVersionBackfillLogEntity logEntry = new AssetVersionBackfillLogEntity();
        logEntry.setLegacyPictureId(legacyPictureId);
        logEntry.setStatus("error");
        logEntry.setErrorMessage(errorMessage != null && errorMessage.length() > 512
                ? errorMessage.substring(0, 512) : errorMessage);
        backfillLogMapper.insert(logEntry);
    }
}
