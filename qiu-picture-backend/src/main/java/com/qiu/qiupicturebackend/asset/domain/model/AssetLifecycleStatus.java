package com.qiu.qiupicturebackend.asset.domain.model;

/**
 * 视觉资产生命周期状态。
 * Phase 2 从 reviewStatus 映射，Phase 3 扩展为独立状态机。
 */
public enum AssetLifecycleStatus {

    PENDING_REVIEW("待审核"),
    APPROVED("已通过"),
    REJECTED("未通过"),
    ARCHIVED("已归档");

    private final String label;

    AssetLifecycleStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static AssetLifecycleStatus fromReviewStatus(Integer reviewStatus) {
        if (reviewStatus == null) {
            return PENDING_REVIEW;
        }
        switch (reviewStatus) {
            case 1: return APPROVED;
            case 2: return REJECTED;
            default: return PENDING_REVIEW;
        }
    }
}
