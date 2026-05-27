package com.qiu.qiupicturebackend.collection.domain.model;

public enum CollectionLayout {
    GRID("网格"),
    BOARD("看板"),
    MOODBOARD("情绪板");

    private final String label;

    CollectionLayout(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
