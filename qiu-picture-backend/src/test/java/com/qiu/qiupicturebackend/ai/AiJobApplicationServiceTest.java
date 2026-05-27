package com.qiu.qiupicturebackend.ai;

import com.qiu.qiupicturebackend.activity.application.ActivityRecordApplicationService;
import com.qiu.qiupicturebackend.ai.application.AiJobApplicationService;
import com.qiu.qiupicturebackend.ai.application.command.CreateAiJobCommand;
import com.qiu.qiupicturebackend.ai.domain.model.AiCapabilityView;
import com.qiu.qiupicturebackend.ai.domain.model.AiJobView;
import com.qiu.qiupicturebackend.ai.domain.provider.AiProviderGateway;
import com.qiu.qiupicturebackend.ai.domain.provider.AiProviderTaskResult;
import com.qiu.qiupicturebackend.ai.domain.repository.AiCapabilityRepository;
import com.qiu.qiupicturebackend.ai.domain.repository.AiJobRepository;
import com.qiu.qiupicturebackend.ai.domain.repository.AiJobResultRepository;
import com.qiu.qiupicturebackend.ai.domain.repository.AiProviderTaskRepository;
import com.qiu.qiupicturebackend.ai.domain.repository.AiUsageRecordRepository;
import com.qiu.qiupicturebackend.asset.domain.repository.AssetVersionRepository;
import com.qiu.qiupicturebackend.model.entity.Picture;
import com.qiu.qiupicturebackend.service.PictureService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AiJobApplicationServiceTest {

    @Mock private AiJobRepository aiJobRepository;
    @Mock private AiJobResultRepository aiJobResultRepository;
    @Mock private AiCapabilityRepository aiCapabilityRepository;
    @Mock private AiProviderTaskRepository aiProviderTaskRepository;
    @Mock private AiUsageRecordRepository aiUsageRecordRepository;
    @Mock private AiProviderGateway aiProviderGateway;
    @Mock private ActivityRecordApplicationService activityRecordApplicationService;
    @Mock private AssetVersionRepository assetVersionRepository;
    @Mock private PictureService pictureService;

    @InjectMocks
    private AiJobApplicationService service;

    @Test
    void shouldCreateOutpaintingJob() {
        AiCapabilityView capability = new AiCapabilityView();
        capability.setCapabilityKey("outpainting");
        capability.setProvider("aliyun");
        capability.setActive(true);

        when(aiCapabilityRepository.findByKey("outpainting")).thenReturn(Optional.of(capability));
        when(aiJobRepository.save(any(AiJobView.class))).thenAnswer(inv -> {
            AiJobView job = inv.getArgument(0);
            job.setJobId(1L);
            return job;
        });
        when(aiProviderGateway.submitTask(any(), any())).thenReturn(
                AiProviderTaskResult.running("provider-task-123"));

        CreateAiJobCommand command = new CreateAiJobCommand();
        command.setCapabilityKey("outpainting");
        command.setSourceAssetId(100L);

        AiJobView job = service.createJob(1L, command, 10L);

        assertNotNull(job);
        assertEquals("outpainting", job.getCapabilityKey());
        assertEquals("running", job.getStatus());
        verify(aiProviderTaskRepository).save(eq(1L), eq("aliyun"), eq("provider-task-123"), eq("running"));
    }

    @Test
    void shouldReturnExistingJobOnDuplicateIdempotencyKey() {
        AiJobView existing = new AiJobView();
        existing.setJobId(5L);
        existing.setWorkspaceId(1L);
        existing.setCapabilityKey("outpainting");
        existing.setStatus("running");

        when(aiJobRepository.findByIdempotencyKey("dedup-1")).thenReturn(Optional.of(existing));
        when(aiJobResultRepository.findByJobId(5L)).thenReturn(java.util.Collections.emptyList());

        CreateAiJobCommand command = new CreateAiJobCommand();
        command.setCapabilityKey("outpainting");
        command.setIdempotencyKey("dedup-1");

        AiJobView job = service.createJob(1L, command, 10L);

        assertEquals(5L, job.getJobId());
        verify(aiCapabilityRepository, never()).findByKey(any());
    }

    @Test
    void shouldListJobsByWorkspace() {
        AiJobView job = new AiJobView();
        job.setJobId(1L);
        job.setWorkspaceId(1L);

        when(aiJobRepository.findByWorkspaceId(eq(1L), anyInt(), anyInt()))
                .thenReturn(java.util.Collections.singletonList(job));
        when(aiJobResultRepository.findByJobId(1L)).thenReturn(java.util.Collections.emptyList());

        var query = new com.qiu.qiupicturebackend.ai.application.query.AiJobQuery();
        var jobs = service.listJobs(1L, query);

        assertFalse(jobs.isEmpty());
        assertEquals(1L, jobs.get(0).getJobId());
    }

    @Test
    void shouldGetJobById() {
        AiJobView job = new AiJobView();
        job.setJobId(1L);
        job.setWorkspaceId(1L);

        when(aiJobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(aiJobResultRepository.findByJobId(1L)).thenReturn(java.util.Collections.emptyList());

        AiJobView result = service.getJob(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getJobId());
    }
}
