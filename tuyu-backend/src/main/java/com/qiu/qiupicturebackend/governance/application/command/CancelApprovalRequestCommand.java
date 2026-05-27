package com.qiu.qiupicturebackend.governance.application.command;

/**
 * Command to cancel a pending approval request.
 */
public class CancelApprovalRequestCommand {

    private Long workspaceId;
    private Long approvalId;

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
}
