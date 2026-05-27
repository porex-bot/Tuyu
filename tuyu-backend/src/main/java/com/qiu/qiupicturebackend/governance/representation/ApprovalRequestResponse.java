package com.qiu.qiupicturebackend.governance.representation;

import java.util.List;

public class ApprovalRequestResponse {

    private Long approvalId;
    private Long workspaceId;
    private String targetType;
    private Long targetId;
    private Long targetVersionId;
    private String requestType;
    private String status;
    private Long submittedBy;
    private String submittedAt;
    private Long resolvedBy;
    private String resolvedAt;
    private String reason;
    private String resultMessage;
    private String createdAt;
    private List<ApprovalStepResponse> steps;
    private List<ApprovalDecisionResponse> decisions;

    public Long getApprovalId() { return approvalId; }
    public void setApprovalId(Long approvalId) { this.approvalId = approvalId; }
    public Long getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(Long workspaceId) { this.workspaceId = workspaceId; }
    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }
    public Long getTargetVersionId() { return targetVersionId; }
    public void setTargetVersionId(Long targetVersionId) { this.targetVersionId = targetVersionId; }
    public String getRequestType() { return requestType; }
    public void setRequestType(String requestType) { this.requestType = requestType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getSubmittedBy() { return submittedBy; }
    public void setSubmittedBy(Long submittedBy) { this.submittedBy = submittedBy; }
    public String getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(String submittedAt) { this.submittedAt = submittedAt; }
    public Long getResolvedBy() { return resolvedBy; }
    public void setResolvedBy(Long resolvedBy) { this.resolvedBy = resolvedBy; }
    public String getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(String resolvedAt) { this.resolvedAt = resolvedAt; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getResultMessage() { return resultMessage; }
    public void setResultMessage(String resultMessage) { this.resultMessage = resultMessage; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public List<ApprovalStepResponse> getSteps() { return steps; }
    public void setSteps(List<ApprovalStepResponse> steps) { this.steps = steps; }
    public List<ApprovalDecisionResponse> getDecisions() { return decisions; }
    public void setDecisions(List<ApprovalDecisionResponse> decisions) { this.decisions = decisions; }
}
