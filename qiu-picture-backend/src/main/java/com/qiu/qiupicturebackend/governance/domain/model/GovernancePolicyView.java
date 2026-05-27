package com.qiu.qiupicturebackend.governance.domain.model;

import java.util.Date;

public class GovernancePolicyView {

    private Long policyId;
    private Long workspaceId;
    private String mode;
    private Integer requireApprovalForAssets;
    private Integer requireApprovalForCollections;
    private Integer requireApprovalForAiResults;
    private Integer autoApproveTrustedUsers;
    private Long updatedBy;
    private Date createdAt;
    private Date updatedAt;

    public Long getPolicyId() { return policyId; }
    public void setPolicyId(Long policyId) { this.policyId = policyId; }
    public Long getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(Long workspaceId) { this.workspaceId = workspaceId; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public Integer getRequireApprovalForAssets() { return requireApprovalForAssets; }
    public void setRequireApprovalForAssets(Integer requireApprovalForAssets) { this.requireApprovalForAssets = requireApprovalForAssets; }
    public Integer getRequireApprovalForCollections() { return requireApprovalForCollections; }
    public void setRequireApprovalForCollections(Integer requireApprovalForCollections) { this.requireApprovalForCollections = requireApprovalForCollections; }
    public Integer getRequireApprovalForAiResults() { return requireApprovalForAiResults; }
    public void setRequireApprovalForAiResults(Integer requireApprovalForAiResults) { this.requireApprovalForAiResults = requireApprovalForAiResults; }
    public Integer getAutoApproveTrustedUsers() { return autoApproveTrustedUsers; }
    public void setAutoApproveTrustedUsers(Integer autoApproveTrustedUsers) { this.autoApproveTrustedUsers = autoApproveTrustedUsers; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
