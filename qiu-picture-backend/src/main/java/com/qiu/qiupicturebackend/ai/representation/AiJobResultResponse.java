package com.qiu.qiupicturebackend.ai.representation;

public class AiJobResultResponse {

    private Long resultId;
    private Long jobId;
    private String resultType;
    private String outputUrl;
    private String applyStatus;
    private Long assetVersionId;
    private String createdAt;

    public Long getResultId() { return resultId; }
    public void setResultId(Long resultId) { this.resultId = resultId; }
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public String getResultType() { return resultType; }
    public void setResultType(String resultType) { this.resultType = resultType; }
    public String getOutputUrl() { return outputUrl; }
    public void setOutputUrl(String outputUrl) { this.outputUrl = outputUrl; }
    public String getApplyStatus() { return applyStatus; }
    public void setApplyStatus(String applyStatus) { this.applyStatus = applyStatus; }
    public Long getAssetVersionId() { return assetVersionId; }
    public void setAssetVersionId(Long assetVersionId) { this.assetVersionId = assetVersionId; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
