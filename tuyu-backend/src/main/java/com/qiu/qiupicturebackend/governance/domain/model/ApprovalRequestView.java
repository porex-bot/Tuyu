package com.qiu.qiupicturebackend.governance.domain.model;

import java.util.Date;
import java.util.List;

public class ApprovalRequestView {

    private Long approvalId;
    private Long workspaceId;
    private String targetType;
    private Long targetId;
    private Long targetVersionId;
    private String requestType;
    private String status;
    private Long submittedBy;
    private Date submittedAt;
    private Long resolvedBy;
    private Date resolvedAt;
    private String reason;
    private String resultMessage;
    private Date createdAt;
    private Date updatedAt;
    private List<ApprovalStepView> steps;
    private List<ApprovalDecisionView> decisions;

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
    public Date getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Date submittedAt) { this.submittedAt = submittedAt; }
    public Long getResolvedBy() { return resolvedBy; }
    public void setResolvedBy(Long resolvedBy) { this.resolvedBy = resolvedBy; }
    public Date getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(Date resolvedAt) { this.resolvedAt = resolvedAt; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getResultMessage() { return resultMessage; }
    public void setResultMessage(String resultMessage) { this.resultMessage = resultMessage; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public List<ApprovalStepView> getSteps() { return steps; }
    public void setSteps(List<ApprovalStepView> steps) { this.steps = steps; }
    public List<ApprovalDecisionView> getDecisions() { return decisions; }
    public void setDecisions(List<ApprovalDecisionView> decisions) { this.decisions = decisions; }
}
