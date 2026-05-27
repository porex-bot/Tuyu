package com.qiu.qiupicturebackend.governance.domain.repository;

import com.qiu.qiupicturebackend.governance.domain.model.ApprovalStepView;

import java.util.List;
import java.util.Optional;

public interface ApprovalStepRepository {

    ApprovalStepView save(ApprovalStepView step);

    ApprovalStepView update(ApprovalStepView step);

    Optional<ApprovalStepView> findById(Long stepId);

    List<ApprovalStepView> findByApprovalId(Long approvalId);

    List<ApprovalStepView> findPendingByReviewer(Long reviewerId, int offset, int limit);
}
