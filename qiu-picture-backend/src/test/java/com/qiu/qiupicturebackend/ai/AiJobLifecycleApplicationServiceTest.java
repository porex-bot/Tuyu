package com.qiu.qiupicturebackend.ai;

import com.qiu.qiupicturebackend.ai.application.AiJobLifecycleApplicationService;
import com.qiu.qiupicturebackend.ai.domain.model.AiJobView;
import com.qiu.qiupicturebackend.ai.domain.provider.AiProviderGateway;
import com.qiu.qiupicturebackend.ai.domain.provider.AiProviderTaskResult;
import com.qiu.qiupicturebackend.ai.domain.repository.AiJobRepository;
import com.qiu.qiupicturebackend.ai.domain.repository.AiJobResultRepository;
import com.qiu.qiupicturebackend.ai.domain.repository.AiProviderTaskRepository;
import com.qiu.qiupicturebackend.asset.domain.model.AssetVersionView;
import com.qiu.qiupicturebackend.asset.domain.repository.AssetVersionRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AiJobLifecycleApplicationServiceTest {

    @Mock private AiJobRepository aiJobRepository;
    @Mock private AiJobResultRepository aiJobResultRepository;
    @Mock private AiProviderTaskRepository aiProviderTaskRepository;
    @Mock private AiProviderGateway aiProviderGateway;
    @Mock private AssetVersionRepository assetVersionRepository;
    @Mock private PictureService pictureService;

    @InjectMocks
    private AiJobLifecycleApplicationService service;

    @Test
    void shouldCancelRunningJob() {
        AiJobView job = new AiJobView();
        job.setJobId(1L);
        job.setWorkspaceId(1L);
        job.setStatus("running");

        when(aiJobRepository.findById(1L)).thenReturn(Optional.of(job));

        AiJobView result = service.cancelJob(1L, 1L);

        assertEquals("cancelled", result.getStatus());
        verify(aiJobRepository).update(any(AiJobView.class));
    }

    @Test
    void shouldNotCancelSucceededJob() {
        AiJobView job = new AiJobView();
        job.setJobId(1L);
        job.setWorkspaceId(1L);
        job.setStatus("succeeded");

        when(aiJobRepository.findById(1L)).thenReturn(Optional.of(job));

        assertThrows(BusinessException.class, () -> service.cancelJob(1L, 1L));
    }

    @Test
    void shouldRetryFailedJobWithImageUrl() {
        AiJobView job = new AiJobView();
        job.setJobId(1L);
        job.setWorkspaceId(1L);
        job.setStatus("failed");
        job.setProvider("aliyun");
        job.setSourceAssetId(100L);

        AssetVersionView version = new AssetVersionView();
        version.setStorageUrl("http://storage.url/image.png");

        when(aiJobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(assetVersionRepository.findCurrentByAssetId(100L)).thenReturn(Optional.of(version));
        when(aiProviderGateway.submitTask(eq("http://storage.url/image.png"), any())).thenReturn(
                AiProviderTaskResult.running("provider-task-456"));

        AiJobView result = service.retryJob(1L, 1L);

        assertEquals("running", result.getStatus());
        assertNull(result.getErrorCode());
        verify(aiProviderGateway).submitTask(eq("http://storage.url/image.png"), any());
        verify(aiProviderTaskRepository).save(eq(1L), eq("aliyun"), eq("provider-task-456"), eq("running"));
    }

    @Test
    void shouldRetryFailedJobWithLegacyPictureUrl() {
        AiJobView job = new AiJobView();
        job.setJobId(1L);
        job.setWorkspaceId(1L);
        job.setStatus("failed");
        job.setProvider("aliyun");
        job.setSourceAssetId(100L);

        Picture picture = new Picture();
        picture.setUrl("http://legacy.url/pic.png");

        when(aiJobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(assetVersionRepository.findCurrentByAssetId(100L)).thenReturn(Optional.empty());
        when(pictureService.getById(100L)).thenReturn(picture);
        when(aiProviderGateway.submitTask(eq("http://legacy.url/pic.png"), any())).thenReturn(
                AiProviderTaskResult.running("provider-task-789"));

        AiJobView result = service.retryJob(1L, 1L);

        assertEquals("running", result.getStatus());
        verify(aiProviderGateway).submitTask(eq("http://legacy.url/pic.png"), any());
    }

    @Test
    void shouldFailRetryWhenNoImageUrl() {
        AiJobView job = new AiJobView();
        job.setJobId(1L);
        job.setWorkspaceId(1L);
        job.setStatus("failed");
        job.setProvider("aliyun");
        job.setSourceAssetId(100L);

        when(aiJobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(assetVersionRepository.findCurrentByAssetId(100L)).thenReturn(Optional.empty());
        when(pictureService.getById(100L)).thenReturn(null);
        // submitTask receives null URL — simulate a RuntimeException
        when(aiProviderGateway.submitTask(any(), any())).thenThrow(new RuntimeException("No image URL"));

        AiJobView result = service.retryJob(1L, 1L);

        assertEquals("failed", result.getStatus());
        assertEquals("SUBMIT_FAILED", result.getErrorCode());
    }

    @Test
    void shouldCompleteJobOnSuccess() {
        AiJobView job = new AiJobView();
        job.setJobId(1L);
        job.setWorkspaceId(1L);
        job.setStatus("running");

        when(aiJobRepository.findById(1L)).thenReturn(Optional.of(job));

        AiProviderTaskResult providerResult = AiProviderTaskResult.succeeded("task-1", "http://output.url/img.png");
        AiJobView result = service.completeJob(1L, providerResult);

        assertEquals("succeeded", result.getStatus());
        verify(aiJobResultRepository).save(any());
    }

    @Test
    void shouldCompleteJobOnFailure() {
        AiJobView job = new AiJobView();
        job.setJobId(1L);
        job.setWorkspaceId(1L);
        job.setStatus("running");

        when(aiJobRepository.findById(1L)).thenReturn(Optional.of(job));

        AiProviderTaskResult providerResult = AiProviderTaskResult.failed("task-1", "PROCESS_FAILED", "处理失败");
        AiJobView result = service.completeJob(1L, providerResult);

        assertEquals("failed", result.getStatus());
        assertEquals("PROCESS_FAILED", result.getErrorCode());
    }
}
