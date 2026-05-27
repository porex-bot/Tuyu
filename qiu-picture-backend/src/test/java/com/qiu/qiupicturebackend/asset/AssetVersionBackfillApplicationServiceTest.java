package com.qiu.qiupicturebackend.asset;

import com.qiu.qiupicturebackend.asset.application.AssetVersionBackfillApplicationService;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetVersionBackfillApplicationServiceTest {

    @Mock
    private PictureMapper pictureMapper;

    @Mock
    private AssetVersionMapper assetVersionMapper;

    @Mock
    private AssetStorageObjectMapper assetStorageObjectMapper;

    @Mock
    private AssetMetadataMapper assetMetadataMapper;

    @Mock
    private AssetVersionBackfillLogMapper backfillLogMapper;

    @InjectMocks
    private AssetVersionBackfillApplicationService service;

    @Test
    void shouldSkipAlreadyProcessedPictures() {
        when(pictureMapper.selectCount(any())).thenReturn(1L);

        Picture picture = buildPicture(1L);
        when(pictureMapper.selectList(any())).thenReturn(List.of(picture), Collections.emptyList());

        AssetVersionBackfillLogEntity existingLog = new AssetVersionBackfillLogEntity();
        existingLog.setLegacyPictureId(1L);
        when(backfillLogMapper.selectOne(any())).thenReturn(existingLog);

        BackfillAssetVersionsCommand command = new BackfillAssetVersionsCommand();
        Map<String, Object> result = service.backfill(command);

        assertEquals(0, result.get("processed"));
        assertEquals(1, result.get("skipped"));
        verify(assetStorageObjectMapper, never()).insert(any(AssetStorageObjectEntity.class));
        verify(assetVersionMapper, never()).insert(any(AssetVersionEntity.class));
    }

    @Test
    void shouldProcessNewPictures() {
        when(pictureMapper.selectCount(any())).thenReturn(1L);

        Picture picture = buildPicture(1L);
        when(pictureMapper.selectList(any())).thenReturn(List.of(picture), Collections.emptyList());

        when(backfillLogMapper.selectOne(any())).thenReturn(null);
        when(assetStorageObjectMapper.insert(any(AssetStorageObjectEntity.class))).thenAnswer(inv -> {
            inv.getArgument(0, AssetStorageObjectEntity.class).setId(100L);
            return 1;
        });
        when(assetMetadataMapper.insert(any(AssetMetadataEntity.class))).thenAnswer(inv -> {
            inv.getArgument(0, AssetMetadataEntity.class).setId(200L);
            return 1;
        });
        when(assetVersionMapper.insert(any(AssetVersionEntity.class))).thenAnswer(inv -> {
            inv.getArgument(0, AssetVersionEntity.class).setId(300L);
            return 1;
        });
        when(backfillLogMapper.insert(any(AssetVersionBackfillLogEntity.class))).thenReturn(1);

        BackfillAssetVersionsCommand command = new BackfillAssetVersionsCommand();
        Map<String, Object> result = service.backfill(command);

        assertEquals(1, result.get("processed"));
        assertEquals(0, result.get("errors"));
        verify(assetStorageObjectMapper, atLeastOnce()).insert(any(AssetStorageObjectEntity.class));
        verify(assetVersionMapper).insert(any(AssetVersionEntity.class));
        verify(assetMetadataMapper).insert(any(AssetMetadataEntity.class));
        verify(backfillLogMapper).insert(any(AssetVersionBackfillLogEntity.class));
    }

    @Test
    void shouldHandleEmptyPictureTable() {
        when(pictureMapper.selectCount(any())).thenReturn(0L);
        when(pictureMapper.selectList(any())).thenReturn(Collections.emptyList());

        BackfillAssetVersionsCommand command = new BackfillAssetVersionsCommand();
        Map<String, Object> result = service.backfill(command);

        assertEquals(0, result.get("processed"));
        assertEquals(0, result.get("errors"));
        verify(assetVersionMapper, never()).insert(any(AssetVersionEntity.class));
    }

    @Test
    void shouldReportDryRun() {
        when(pictureMapper.selectCount(any())).thenReturn(0L);
        when(pictureMapper.selectList(any())).thenReturn(Collections.emptyList());

        BackfillAssetVersionsCommand command = new BackfillAssetVersionsCommand();
        command.setDryRun(true);
        Map<String, Object> result = service.backfill(command);

        assertEquals(true, result.get("dryRun"));
    }

    @Test
    void shouldRespectMaxBatches() {
        when(pictureMapper.selectCount(any())).thenReturn(100L);

        Picture picture = buildPicture(1L);
        when(pictureMapper.selectList(any())).thenReturn(List.of(picture), Collections.emptyList());
        when(backfillLogMapper.selectOne(any())).thenReturn(null);
        when(assetStorageObjectMapper.insert(any(AssetStorageObjectEntity.class))).thenAnswer(inv -> {
            inv.getArgument(0, AssetStorageObjectEntity.class).setId(100L);
            return 1;
        });
        when(assetMetadataMapper.insert(any(AssetMetadataEntity.class))).thenAnswer(inv -> {
            inv.getArgument(0, AssetMetadataEntity.class).setId(200L);
            return 1;
        });
        when(assetVersionMapper.insert(any(AssetVersionEntity.class))).thenAnswer(inv -> {
            inv.getArgument(0, AssetVersionEntity.class).setId(300L);
            return 1;
        });
        when(backfillLogMapper.insert(any(AssetVersionBackfillLogEntity.class))).thenReturn(1);

        BackfillAssetVersionsCommand command = new BackfillAssetVersionsCommand();
        command.setMaxBatches(1);
        service.backfill(command);

        verify(pictureMapper, times(1)).selectList(any());
    }

    @Test
    void shouldCreateThumbnailStorageObjectWhenPresent() {
        when(pictureMapper.selectCount(any())).thenReturn(1L);

        Picture picture = buildPicture(1L);
        picture.setThumbnailUrl("https://example.com/thumb.jpg");
        when(pictureMapper.selectList(any())).thenReturn(List.of(picture), Collections.emptyList());

        when(backfillLogMapper.selectOne(any())).thenReturn(null);
        when(assetStorageObjectMapper.insert(any(AssetStorageObjectEntity.class))).thenAnswer(inv -> {
            inv.getArgument(0, AssetStorageObjectEntity.class).setId(100L);
            return 1;
        });
        when(assetMetadataMapper.insert(any(AssetMetadataEntity.class))).thenAnswer(inv -> {
            inv.getArgument(0, AssetMetadataEntity.class).setId(200L);
            return 1;
        });
        when(assetVersionMapper.insert(any(AssetVersionEntity.class))).thenAnswer(inv -> {
            inv.getArgument(0, AssetVersionEntity.class).setId(300L);
            return 1;
        });
        when(backfillLogMapper.insert(any(AssetVersionBackfillLogEntity.class))).thenReturn(1);

        BackfillAssetVersionsCommand command = new BackfillAssetVersionsCommand();
        service.backfill(command);

        verify(assetStorageObjectMapper, times(2)).insert(any(AssetStorageObjectEntity.class));
    }

    private Picture buildPicture(Long id) {
        Picture picture = new Picture();
        picture.setId(id);
        picture.setUrl("https://example.com/pic" + id + ".jpg");
        picture.setPicSize(1024L);
        picture.setPicWidth(800);
        picture.setPicHeight(600);
        picture.setPicScale(1.33);
        picture.setPicFormat("jpg");
        picture.setPicColor("#FF0000");
        picture.setCategory("test");
        picture.setUserId(1L);
        return picture;
    }
}
