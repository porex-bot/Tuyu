package com.qiu.qiupicturebackend.ai.domain.repository;

import com.qiu.qiupicturebackend.ai.domain.model.AiJobView;

import java.util.List;
import java.util.Optional;

public interface AiJobRepository {

    AiJobView save(AiJobView job);

    AiJobView update(AiJobView job);

    Optional<AiJobView> findById(Long jobId);

    List<AiJobView> findByWorkspaceId(Long workspaceId, int offset, int limit);

    List<AiJobView> findByWorkspaceAndStatus(Long workspaceId, String status, int offset, int limit);

    List<AiJobView> findByCreatorUserId(Long userId, int offset, int limit);

    Optional<AiJobView> findByIdempotencyKey(String idempotencyKey);

    List<AiJobView> findRunningJobs(int limit);

    long countByWorkspaceId(Long workspaceId);
}
