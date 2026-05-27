package com.qiu.qiupicturebackend.governance.domain.repository;

import com.qiu.qiupicturebackend.governance.domain.model.ApprovalDecisionView;

import java.util.List;

public interface ApprovalDecisionRepository {

    ApprovalDecisionView save(ApprovalDecisionView decision);

    List<ApprovalDecisionView> findByApprovalId(Long approvalId);

    List<ApprovalDecisionView> findByStepId(Long stepId);
}
