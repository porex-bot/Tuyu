package com.qiu.qiupicturebackend.activity.domain.model;

public enum ActivityTargetType {
    WORKSPACE("workspace"),
    ASSET("asset"),
    COLLECTION("collection"),
    COLLECTION_ITEM("collection_item");

    private final String code;

    ActivityTargetType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ActivityTargetType fromCode(String code) {
        for (ActivityTargetType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
