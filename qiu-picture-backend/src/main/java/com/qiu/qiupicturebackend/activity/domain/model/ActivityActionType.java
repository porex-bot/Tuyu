package com.qiu.qiupicturebackend.activity.domain.model;

public enum ActivityActionType {
    WORKSPACE_CREATED("workspace.created", "创建工作区"),
    ASSET_INGESTED("asset.ingested", "上传资产"),
    ASSET_UPDATED("asset.updated", "更新资产"),
    ASSET_VERSION_CREATED("asset.version.created", "创建资产版本"),
    COLLECTION_CREATED("collection.created", "创建集合"),
    COLLECTION_ITEM_ADDED("collection.item.added", "添加资产到集合"),
    COLLECTION_ITEM_REMOVED("collection.item.removed", "从集合移除资产"),
    COLLECTION_ITEMS_REORDERED("collection.items.reordered", "重新排序集合"),
    AI_JOB_CREATED("ai.job.created", "创建 AI 任务"),
    AI_JOB_SUCCEEDED("ai.job.succeeded", "AI 任务完成"),
    AI_JOB_FAILED("ai.job.failed", "AI 任务失败"),
    AI_RESULT_APPLIED("ai.result.applied", "应用 AI 结果"),
    APPROVAL_REQUESTED("approval.requested", "提交审批"),
    APPROVAL_APPROVED("approval.approved", "审批通过"),
    APPROVAL_REJECTED("approval.rejected", "审批驳回"),
    APPROVAL_CHANGES_REQUESTED("approval.changes_requested", "审批要求修改"),
    APPROVAL_CANCELLED("approval.cancelled", "取消审批"),
    GOVERNANCE_POLICY_UPDATED("governance.policy.updated", "更新治理策略");

    private final String code;
    private final String label;

    ActivityActionType(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static ActivityActionType fromCode(String code) {
        for (ActivityActionType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
