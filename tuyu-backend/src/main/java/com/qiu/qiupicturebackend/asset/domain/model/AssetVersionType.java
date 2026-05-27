package com.qiu.qiupicturebackend.asset.domain.model;

public enum AssetVersionType {

    ORIGINAL("原始版本"),
    REPLACEMENT("替换版本"),
    MANUAL_EDIT("手动编辑"),
    CROP("裁剪版本"),
    AI_GENERATED("AI 生成"),
    FORMAT_CONVERSION("格式转换");

    private final String label;

    AssetVersionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
