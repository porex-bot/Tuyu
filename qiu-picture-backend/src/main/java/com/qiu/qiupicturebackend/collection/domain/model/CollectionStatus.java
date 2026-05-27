package com.qiu.qiupicturebackend.collection.domain.model;

public enum CollectionStatus {
    DRAFT("草稿"),
    ACTIVE("活跃"),
    ARCHIVED("已归档");

    private final String label;

    CollectionStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
