package com.qiu.qiupicturebackend.governance.application.query;

/**
 * Query to retrieve the approval inbox for a workspace with pagination.
 */
public class ApprovalInboxQuery {

    private Long workspaceId;
    private int offset;
    private int limit;

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
