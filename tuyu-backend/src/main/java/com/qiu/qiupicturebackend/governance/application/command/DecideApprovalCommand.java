package com.qiu.qiupicturebackend.governance.application.command;

/**
 * Command to decide on an approval request (approve, reject, or request changes).
 */
public class DecideApprovalCommand {

    private Long workspaceId;
    private Long approvalId;
    private String decisionType;
    private String comment;

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public Long getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Long approvalId) {
        this.approvalId = approvalId;
    }

    public String getDecisionType() {
        return decisionType;
    }

    public void setDecisionType(String decisionType) {
        this.decisionType = decisionType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
