package com.qiu.qiupicturebackend.ai.domain.model;

public enum AiCapabilityKey {
    OUTPAINTING("outpainting", "AI 扩图"),
    SIMILAR_SEARCH("similar_search", "以图搜图"),
    AUTO_TAGGING("auto_tagging", "自动打标"),
    CAPTION("caption", "AI 描述");

    private final String code;
    private final String label;

    AiCapabilityKey(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() { return code; }
    public String getLabel() { return label; }

    public static AiCapabilityKey fromCode(String code) {
        for (AiCapabilityKey k : values()) {
            if (k.code.equals(code)) return k;
        }
        return null;
    }
}
