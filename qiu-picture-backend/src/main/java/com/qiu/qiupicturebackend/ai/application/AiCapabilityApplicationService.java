package com.qiu.qiupicturebackend.ai.application;

import com.qiu.qiupicturebackend.ai.domain.model.AiCapabilityView;
import com.qiu.qiupicturebackend.ai.domain.repository.AiCapabilityRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class AiCapabilityApplicationService {

    @Resource
    private AiCapabilityRepository aiCapabilityRepository;

    public List<AiCapabilityView> getActiveCapabilities(Long workspaceId) {
        if (workspaceId == null || workspaceId <= 0) {
            return Collections.emptyList();
        }
        return aiCapabilityRepository.findActive();
    }

    public List<AiCapabilityView> getAllCapabilities(Long workspaceId) {
        if (workspaceId == null || workspaceId <= 0) {
            return Collections.emptyList();
        }
        return aiCapabilityRepository.findAll();
    }
}
