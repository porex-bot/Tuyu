package com.qiu.qiupicturebackend.ai.domain.provider;

public class AiProviderTaskResult {

    private String providerTaskId;
    private String status;
    private String outputImageUrl;
    private String errorCode;
    private String errorMessage;

    public static AiProviderTaskResult running(String providerTaskId) {
        AiProviderTaskResult r = new AiProviderTaskResult();
        r.providerTaskId = providerTaskId;
        r.status = "running";
        return r;
    }

    public static AiProviderTaskResult succeeded(String providerTaskId, String outputImageUrl) {
        AiProviderTaskResult r = new AiProviderTaskResult();
        r.providerTaskId = providerTaskId;
        r.status = "succeeded";
        r.outputImageUrl = outputImageUrl;
        return r;
    }

    public static AiProviderTaskResult failed(String providerTaskId, String errorCode, String errorMessage) {
        AiProviderTaskResult r = new AiProviderTaskResult();
        r.providerTaskId = providerTaskId;
        r.status = "failed";
        r.errorCode = errorCode;
        r.errorMessage = errorMessage;
        return r;
    }

    public String getProviderTaskId() { return providerTaskId; }
    public void setProviderTaskId(String providerTaskId) { this.providerTaskId = providerTaskId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getOutputImageUrl() { return outputImageUrl; }
    public void setOutputImageUrl(String outputImageUrl) { this.outputImageUrl = outputImageUrl; }
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
