package com.qiu.qiupicturebackend.ai.application.command;

import java.util.Map;

public class CreateAiJobCommand {

    private String capabilityKey;
    private Long sourceAssetId;
    private Long sourceAssetVersionId;
    private Map<String, Object> parameters;
    private String idempotencyKey;

    public String getCapabilityKey() { return capabilityKey; }
    public void setCapabilityKey(String capabilityKey) { this.capabilityKey = capabilityKey; }
    public Long getSourceAssetId() { return sourceAssetId; }
    public void setSourceAssetId(Long sourceAssetId) { this.sourceAssetId = sourceAssetId; }
    public Long getSourceAssetVersionId() { return sourceAssetVersionId; }
    public void setSourceAssetVersionId(Long sourceAssetVersionId) { this.sourceAssetVersionId = sourceAssetVersionId; }
    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
}
