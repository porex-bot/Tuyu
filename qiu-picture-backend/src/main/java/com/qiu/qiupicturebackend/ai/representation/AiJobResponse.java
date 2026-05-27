package com.qiu.qiupicturebackend.ai.representation;

import java.util.List;

public class AiJobResponse {

    private Long jobId;
    private Long workspaceId;
    private Long creatorUserId;
    private String capabilityKey;
    private String status;
    private Long sourceAssetId;
    private Long sourceAssetVersionId;
    private String provider;
    private String parametersJson;
    private String idempotencyKey;
    private String errorCode;
    private String errorMessage;
    private String createdAt;
    private String startedAt;
    private String finishedAt;
    private List<AiJobResultResponse> results;

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public Long getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(Long workspaceId) { this.workspaceId = workspaceId; }
    public Long getCreatorUserId() { return creatorUserId; }
    public void setCreatorUserId(Long creatorUserId) { this.creatorUserId = creatorUserId; }
    public String getCapabilityKey() { return capabilityKey; }
    public void setCapabilityKey(String capabilityKey) { this.capabilityKey = capabilityKey; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getSourceAssetId() { return sourceAssetId; }
    public void setSourceAssetId(Long sourceAssetId) { this.sourceAssetId = sourceAssetId; }
    public Long getSourceAssetVersionId() { return sourceAssetVersionId; }
    public void setSourceAssetVersionId(Long sourceAssetVersionId) { this.sourceAssetVersionId = sourceAssetVersionId; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getParametersJson() { return parametersJson; }
    public void setParametersJson(String parametersJson) { this.parametersJson = parametersJson; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getStartedAt() { return startedAt; }
    public void setStartedAt(String startedAt) { this.startedAt = startedAt; }
    public String getFinishedAt() { return finishedAt; }
    public void setFinishedAt(String finishedAt) { this.finishedAt = finishedAt; }
    public List<AiJobResultResponse> getResults() { return results; }
    public void setResults(List<AiJobResultResponse> results) { this.results = results; }
}
