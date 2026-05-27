package com.qiu.qiupicturebackend.ai.domain.model;

public enum AiResultApplyStatus {
    PENDING("pending", "待处理"),
    APPLIED("applied", "已应用"),
    DISCARDED("discarded", "已丢弃");

    private final String code;
    private final String label;

    AiResultApplyStatus(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() { return code; }
    public String getLabel() { return label; }

    public static AiResultApplyStatus fromCode(String code) {
        for (AiResultApplyStatus s : values()) {
            if (s.code.equals(code)) return s;
        }
        return null;
    }
}
