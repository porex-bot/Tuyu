package com.qiu.qiupicturebackend.ai.domain.model;

public enum AiJobStatus {
    CREATED("created", "已创建"),
    QUEUED("queued", "排队中"),
    RUNNING("running", "处理中"),
    SUCCEEDED("succeeded", "已完成"),
    FAILED("failed", "失败"),
    CANCELLED("cancelled", "已取消"),
    APPLIED("applied", "已应用"),
    DISCARDED("discarded", "已丢弃");

    private final String code;
    private final String label;

    AiJobStatus(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() { return code; }
    public String getLabel() { return label; }

    public static AiJobStatus fromCode(String code) {
        for (AiJobStatus s : values()) {
            if (s.code.equals(code)) return s;
        }
        return null;
    }

    public boolean isTerminal() {
        return this == SUCCEEDED || this == FAILED || this == CANCELLED || this == DISCARDED;
    }
}
