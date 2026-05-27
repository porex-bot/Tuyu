package com.qiu.qiupicturebackend.ai.infrastructure.scheduler;

import com.qiu.qiupicturebackend.ai.application.AiJobPollingApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@ConditionalOnProperty(value = "qiu.ai.polling.enabled", havingValue = "true", matchIfMissing = true)
public class AiJobPollingScheduler {

    @Resource
    private AiJobPollingApplicationService aiJobPollingApplicationService;

    @Scheduled(fixedDelayString = "${qiu.ai.polling.interval-ms:30000}")
    public void pollJobs() {
        try {
            aiJobPollingApplicationService.pollRunningJobs();
        } catch (Exception e) {
            log.warn("AI job polling batch failed: {}", e.getMessage());
        }
    }
}
