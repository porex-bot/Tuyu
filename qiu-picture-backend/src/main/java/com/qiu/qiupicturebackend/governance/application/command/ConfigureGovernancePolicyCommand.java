package com.qiu.qiupicturebackend.governance.application.command;

public class ConfigureGovernancePolicyCommand {

    private Long workspaceId;
    private String mode;
    private Boolean requireApprovalForAssets;
    private Boolean requireApprovalForCollections;
    private Boolean requireApprovalForAiResults;
    private Boolean autoApproveTrustedUsers;

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
}
