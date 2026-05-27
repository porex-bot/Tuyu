package com.qiu.qiupicturebackend.ai.domain.model;

public enum AiProviderType {
    ALIYUN("aliyun", "阿里云");

    private final String code;
    private final String label;

    AiProviderType(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() { return code; }
    public String getLabel() { return label; }

    public static AiProviderType fromCode(String code) {
        for (AiProviderType p : values()) {
            if (p.code.equals(code)) return p;
        }
        return null;
    }
}
