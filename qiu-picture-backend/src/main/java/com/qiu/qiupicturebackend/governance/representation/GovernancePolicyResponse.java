package com.qiu.qiupicturebackend.governance.representation;

public class GovernancePolicyResponse {

    private Long policyId;
    private Long workspaceId;
    private String mode;
    private Boolean requireApprovalForAssets;
    private Boolean requireApprovalForCollections;
    private Boolean requireApprovalForAiResults;
    private Boolean autoApproveTrustedUsers;
    private Long updatedBy;
    private String createdAt;
    private String updatedAt;

    public Long getPolicyId() { return policyId; }
    public void setPolicyId(Long policyId) { this.policyId = policyId; }
    public Long getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(Long workspaceId) { this.workspaceId = workspaceId; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public Boolean getRequireApprovalForAssets() { return requireApprovalForAssets; }
    public void setRequireApprovalForAssets(Boolean requireApprovalForAssets) { this.requireApprovalForAssets = requireApprovalForAssets; }
    public Boolean getRequireApprovalForCollections() { return requireApprovalForCollections; }
    public void setRequireApprovalForCollections(Boolean requireApprovalForCollections) { this.requireApprovalForCollections = requireApprovalForCollections; }
    public Boolean getRequireApprovalForAiResults() { return requireApprovalForAiResults; }
    public void setRequireApprovalForAiResults(Boolean requireApprovalForAiResults) { this.requireApprovalForAiResults = requireApprovalForAiResults; }
    public Boolean getAutoApproveTrustedUsers() { return autoApproveTrustedUsers; }
    public void setAutoApproveTrustedUsers(Boolean autoApproveTrustedUsers) { this.autoApproveTrustedUsers = autoApproveTrustedUsers; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
