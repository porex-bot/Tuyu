package com.qiu.qiupicturebackend.governance.domain.model;

public enum ApprovalStatus {
    DRAFT("draft", "草稿"),
    PENDING("pending", "待审批"),
    APPROVED("approved", "已通过"),
    REJECTED("rejected", "已驳回"),
    CHANGES_REQUESTED("changes_requested", "需修改"),
    CANCELLED("cancelled", "已取消");

    private final String code;
    private final String label;

    ApprovalStatus(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() { return code; }
    public String getLabel() { return label; }

    public static ApprovalStatus fromCode(String code) {
        for (ApprovalStatus s : values()) {
            if (s.code.equals(code)) return s;
        }
        return null;
    }

    public boolean isTerminal() {
        return this == APPROVED || this == REJECTED || this == CANCELLED;
    }
}
