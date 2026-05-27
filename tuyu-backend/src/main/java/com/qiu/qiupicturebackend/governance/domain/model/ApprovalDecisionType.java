package com.qiu.qiupicturebackend.governance.domain.model;

public enum ApprovalDecisionType {
    APPROVE("approve", "通过"),
    REJECT("reject", "驳回"),
    REQUEST_CHANGES("request_changes", "需修改");

    private final String code;
    private final String label;

    ApprovalDecisionType(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() { return code; }
    public String getLabel() { return label; }

    public static ApprovalDecisionType fromCode(String code) {
        for (ApprovalDecisionType t : values()) {
            if (t.code.equals(code)) return t;
        }
        return null;
    }
}
