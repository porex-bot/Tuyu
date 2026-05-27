package com.qiu.qiupicturebackend.governance.domain.repository;

import com.qiu.qiupicturebackend.governance.domain.model.ApprovalRequestView;

import java.util.List;
import java.util.Optional;

public interface ApprovalRequestRepository {

    ApprovalRequestView save(ApprovalRequestView request);

    ApprovalRequestView update(ApprovalRequestView request);

    Optional<ApprovalRequestView> findById(Long approvalId);

    List<ApprovalRequestView> findByWorkspaceId(Long workspaceId, int offset, int limit);

    List<ApprovalRequestView> findByTarget(Long workspaceId, String targetType, Long targetId);

    List<ApprovalRequestView> findPendingByWorkspace(Long workspaceId, int offset, int limit);

    List<ApprovalRequestView> findBySubmitter(Long userId, int offset, int limit);
}
