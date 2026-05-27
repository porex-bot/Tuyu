package com.qiu.qiupicturebackend.ai.domain.model;

import java.util.Date;
import java.util.List;

public class AiJobView {

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
    private Date createdAt;
    private Date startedAt;
    private Date finishedAt;
    private List<AiJobResultView> results;

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
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getStartedAt() { return startedAt; }
    public void setStartedAt(Date startedAt) { this.startedAt = startedAt; }
    public Date getFinishedAt() { return finishedAt; }
    public void setFinishedAt(Date finishedAt) { this.finishedAt = finishedAt; }
    public List<AiJobResultView> getResults() { return results; }
    public void setResults(List<AiJobResultView> results) { this.results = results; }
}
