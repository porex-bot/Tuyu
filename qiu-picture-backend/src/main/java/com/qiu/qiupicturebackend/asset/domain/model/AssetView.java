package com.qiu.qiupicturebackend.asset.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 视觉资产读模型（非持久化，不含 MyBatis 注解）。
 * Phase 2 从 picture 表计算，不创建新表。
 */
@Data
public class AssetView implements Serializable {

    private Long assetId;
    private Long legacyPictureId;
    private Long workspaceId;
    private String name;
    private String description;
    private String category;
    private String tags;
    private String url;
    private String thumbnailUrl;
    private Integer width;
    private Integer height;
    private Double scale;
    private String format;
    private Long size;
    private String dominantColor;
    private Long createdBy;
    private Date createdAt;
    private Date updatedAt;
    private AssetLifecycleStatus lifecycleStatus;

    private static final long serialVersionUID = 1L;
}
