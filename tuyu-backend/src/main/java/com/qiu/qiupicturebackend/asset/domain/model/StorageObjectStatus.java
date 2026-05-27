package com.qiu.qiupicturebackend.asset.domain.model;

public enum StorageObjectStatus {

    ACTIVE("活跃"),
    ORPHANED("孤立"),
    ARCHIVED("已归档");

    private final String label;

    StorageObjectStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
