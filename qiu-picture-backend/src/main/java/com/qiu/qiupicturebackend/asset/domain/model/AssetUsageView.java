package com.qiu.qiupicturebackend.asset.domain.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 视觉资产用量快照（非持久化）。
 * Phase 2 提供轻量统计，Phase 3 扩展为真实 metrics。
 */
@Data
public class AssetUsageView implements Serializable {

    private Long workspaceId;
    private Long totalAssets;
    private Long totalSize;
    private Long storageLimit;
    private Long assetCountLimit;
    private Double storageRatio;
    private Double countRatio;

    private static final long serialVersionUID = 1L;
}
