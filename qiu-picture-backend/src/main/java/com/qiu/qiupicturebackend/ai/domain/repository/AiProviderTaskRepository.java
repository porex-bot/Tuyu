package com.qiu.qiupicturebackend.ai.domain.repository;

import java.util.Optional;

public interface AiProviderTaskRepository {

    void save(Long jobId, String provider, String providerTaskId, String providerStatus);

    void updateStatus(Long jobId, String providerStatus, String providerResponseJson);

    Optional<String> findProviderTaskId(Long jobId);
}
