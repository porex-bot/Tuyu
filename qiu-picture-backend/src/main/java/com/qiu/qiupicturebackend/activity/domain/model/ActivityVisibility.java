package com.qiu.qiupicturebackend.activity.domain.model;

public enum ActivityVisibility {
    MEMBERS("members"),
    WORKSPACE("workspace");

    private final String code;

    ActivityVisibility(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
