package com.qiu.qiupicturebackend.governance.domain.model;

public enum GovernancePolicyMode {
    OFF("off", "关闭"),
    MANUAL("manual", "手动审批"),
    AUTO_FOR_PUBLICATION("auto_for_publication", "发布时自动审批"),
    STRICT("strict", "严格审批");

    private final String code;
    private final String label;

    GovernancePolicyMode(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() { return code; }
    public String getLabel() { return label; }

    public static GovernancePolicyMode fromCode(String code) {
        for (GovernancePolicyMode m : values()) {
            if (m.code.equals(code)) return m;
        }
        return OFF;
    }
}
