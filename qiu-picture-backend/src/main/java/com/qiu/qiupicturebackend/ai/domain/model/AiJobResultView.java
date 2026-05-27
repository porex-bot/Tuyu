package com.qiu.qiupicturebackend.ai.domain.model;

import java.util.Date;

public class AiJobResultView {

    private Long resultId;
    private Long jobId;
    private String resultType;
    private String outputUrl;
    private Long outputStorageObjectId;
    private String outputMetadataJson;
    private String applyStatus;
    private Long assetVersionId;
    private Date createdAt;

    public Long getResultId() { return resultId; }
    public void setResultId(Long resultId) { this.resultId = resultId; }
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public String getResultType() { return resultType; }
    public void setResultType(String resultType) { this.resultType = resultType; }
    public String getOutputUrl() { return outputUrl; }
    public void setOutputUrl(String outputUrl) { this.outputUrl = outputUrl; }
    public Long getOutputStorageObjectId() { return outputStorageObjectId; }
    public void setOutputStorageObjectId(Long outputStorageObjectId) { this.outputStorageObjectId = outputStorageObjectId; }
    public String getOutputMetadataJson() { return outputMetadataJson; }
    public void setOutputMetadataJson(String outputMetadataJson) { this.outputMetadataJson = outputMetadataJson; }
    public String getApplyStatus() { return applyStatus; }
    public void setApplyStatus(String applyStatus) { this.applyStatus = applyStatus; }
    public Long getAssetVersionId() { return assetVersionId; }
    public void setAssetVersionId(Long assetVersionId) { this.assetVersionId = assetVersionId; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
