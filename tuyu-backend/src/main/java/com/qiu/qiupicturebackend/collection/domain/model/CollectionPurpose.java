package com.qiu.qiupicturebackend.collection.domain.model;

public enum CollectionPurpose {
    PROJECT("项目"),
    BRAND("品牌"),
    CAMPAIGN("营销活动"),
    DELIVERY("交付"),
    REFERENCE("参考");

    private final String label;

    CollectionPurpose(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
