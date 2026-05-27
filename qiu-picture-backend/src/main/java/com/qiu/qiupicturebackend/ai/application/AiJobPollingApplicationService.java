package com.qiu.qiupicturebackend.ai.application;

import com.qiu.qiupicturebackend.ai.domain.model.AiJobView;
import com.qiu.qiupicturebackend.ai.domain.provider.AiProviderGateway;
import com.qiu.qiupicturebackend.ai.domain.provider.AiProviderTaskResult;
import com.qiu.qiupicturebackend.ai.domain.repository.AiJobRepository;
import com.qiu.qiupicturebackend.ai.domain.repository.AiProviderTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AiJobPollingApplicationService {

    private static final int BATCH_SIZE = 20;

    @Resource
    private AiJobRepository aiJobRepository;

    @Resource
    private AiProviderTaskRepository aiProviderTaskRepository;

    @Resource
    private AiProviderGateway aiProviderGateway;

    @Resource
    private AiJobLifecycleApplicationService aiJobLifecycleApplicationService;

    public void pollRunningJobs() {
        List<AiJobView> runningJobs = aiJobRepository.findRunningJobs(BATCH_SIZE);
        if (runningJobs.isEmpty()) return;

        log.debug("Polling {} running AI jobs", runningJobs.size());
        for (AiJobView job : runningJobs) {
            try {
                Optional<String> taskIdOpt = aiProviderTaskRepository.findProviderTaskId(job.getJobId());
                if (taskIdOpt.isEmpty()) {
                    log.warn("No provider task found for running job {}", job.getJobId());
                    continue;
                }
                AiProviderTaskResult result = aiProviderGateway.queryTask(taskIdOpt.get());
                if ("succeeded".equals(result.getStatus()) || "failed".equals(result.getStatus())) {
                    aiProviderTaskRepository.updateStatus(job.getJobId(), result.getStatus(), null);
                    aiJobLifecycleApplicationService.completeJob(job.getJobId(), result);
                    log.info("Job {} completed with status: {}", job.getJobId(), result.getStatus());
                } else {
                    // Still running, update provider status only
                    aiProviderTaskRepository.updateStatus(job.getJobId(), result.getStatus(), null);
                }
            } catch (Exception e) {
                log.warn("Poll failed for job {}: {}", job.getJobId(), e.getMessage());
            }
        }
    }
}
