package com.qiu.qiupicturebackend.ai;

import com.qiu.qiupicturebackend.activity.application.ActivityRecordApplicationService;
import com.qiu.qiupicturebackend.ai.application.AiResultApplicationService;
import com.qiu.qiupicturebackend.ai.domain.model.AiJobResultView;
import com.qiu.qiupicturebackend.ai.domain.model.AiJobView;
import com.qiu.qiupicturebackend.ai.domain.repository.AiJobRepository;
import com.qiu.qiupicturebackend.ai.domain.repository.AiJobResultRepository;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.entity.AssetStorageObjectEntity;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.entity.AssetVersionEntity;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.mapper.AssetStorageObjectMapper;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.mapper.AssetVersionMapper;
import com.qiu.qiupicturebackend.exception.BusinessException;
import com.qiu.qiupicturebackend.model.entity.Picture;
import com.qiu.qiupicturebackend.service.PictureService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AiResultApplicationServiceTest {

    @Mock private AiJobRepository aiJobRepository;
    @Mock private AiJobResultRepository aiJobResultRepository;
    @Mock private AssetVersionMapper assetVersionMapper;
    @Mock private AssetStorageObjectMapper assetStorageObjectMapper;
    @Mock private PictureService pictureService;
    @Mock private ActivityRecordApplicationService activityRecordApplicationService;

    @InjectMocks
    private AiResultApplicationService service;

    @Test
    void shouldApplyResultAndCreateAiGeneratedVersion() {
        AiJobView job = new AiJobView();
        job.setJobId(1L);
        job.setWorkspaceId(1L);
        job.setStatus("succeeded");
        job.setSourceAssetId(100L);

        AiJobResultView result = new AiJobResultView();
        result.setResultId(10L);
        result.setJobId(1L);
        result.setApplyStatus("pending");
        result.setOutputUrl("http://output.url/result.png");

        Picture picture = new Picture();
        picture.setId(100L);
        picture.setPicWidth(1920);
        picture.setPicHeight(1080);

        when(aiJobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(aiJobResultRepository.findById(10L)).thenReturn(Optional.of(result));
        when(pictureService.getById(100L)).thenReturn(picture);
        when(assetVersionMapper.selectCount(any())).thenReturn(1L);
        when(assetStorageObjectMapper.insert(any(AssetStorageObjectEntity.class))).thenAnswer(inv -> {
            AssetStorageObjectEntity e = inv.getArgument(0);
            e.setId(300L);
            return 1;
        });
        when(assetVersionMapper.insert(any(AssetVersionEntity.class))).thenAnswer(inv -> {
            AssetVersionEntity entity = inv.getArgument(0);
            entity.setId(200L);
            return 1;
        });

        AiJobResultView applied = service.applyResult(1L, 1L, 10L, 10L);

        assertEquals("applied", applied.getApplyStatus());
        assertEquals(200L, applied.getAssetVersionId());
        verify(assetStorageObjectMapper).insert(any(AssetStorageObjectEntity.class));
        verify(assetVersionMapper).insert(any(AssetVersionEntity.class));
        verify(aiJobResultRepository).update(any(AiJobResultView.class));
    }

    @Test
    void shouldNotApplyResultTwice() {
        AiJobView job = new AiJobView();
        job.setJobId(1L);
        job.setWorkspaceId(1L);
        job.setStatus("succeeded");

        AiJobResultView result = new AiJobResultView();
        result.setResultId(10L);
        result.setJobId(1L);
        result.setApplyStatus("applied");

        when(aiJobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(aiJobResultRepository.findById(10L)).thenReturn(Optional.of(result));

        assertThrows(BusinessException.class, () -> service.applyResult(1L, 1L, 10L, 10L));
    }

    @Test
    void shouldNotApplyResultForFailedJob() {
        AiJobView job = new AiJobView();
        job.setJobId(1L);
        job.setWorkspaceId(1L);
        job.setStatus("failed");

        when(aiJobRepository.findById(1L)).thenReturn(Optional.of(job));

        assertThrows(BusinessException.class, () -> service.applyResult(1L, 1L, 10L, 10L));
    }

    @Test
    void shouldNotApplyResultFromWrongWorkspace() {
        AiJobView job = new AiJobView();
        job.setJobId(1L);
        job.setWorkspaceId(99L);
        job.setStatus("succeeded");

        when(aiJobRepository.findById(1L)).thenReturn(Optional.of(job));

        assertThrows(BusinessException.class, () -> service.applyResult(1L, 1L, 10L, 10L));
    }
}
