package com.qiu.qiupicturebackend.asset.representation;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 视觉资产详情响应 —— Inspector 预览面板使用。
 */
@Data
public class AssetDetailResponse implements Serializable {

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
    private String sizeDisplay;
    private String dominantColor;
    private String lifecycleStatus;
    private String reviewStatusText;
    private String reviewMessage;
    private AssetMetadataResponse metadata;
    private String createdBy;
    private String createdAt;
    private String updatedAt;
    private List<String> permissionList;
    private AssetVersionResponse currentVersion;

    private static final long serialVersionUID = 1L;
}
