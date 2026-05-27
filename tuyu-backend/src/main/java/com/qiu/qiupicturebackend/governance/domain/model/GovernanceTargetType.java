package com.qiu.qiupicturebackend.governance.domain.model;

public enum GovernanceTargetType {
    ASSET("asset", "资产"),
    COLLECTION("collection", "集合"),
    AI_RESULT("ai_result", "AI 结果");

    private final String code;
    private final String label;

    GovernanceTargetType(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() { return code; }
    public String getLabel() { return label; }

    public static GovernanceTargetType fromCode(String code) {
        for (GovernanceTargetType t : values()) {
            if (t.code.equals(code)) return t;
        }
        return null;
    }
}
