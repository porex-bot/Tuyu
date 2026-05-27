package com.qiu.qiupicturebackend.asset.representation;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 视觉资产卡片响应 —— AssetGrid 网格视图使用。
 */
@Data
public class AssetCardResponse implements Serializable {

    private Long assetId;
    private Long legacyPictureId;
    private Long workspaceId;
    private String name;
    private String thumbnailUrl;
    private String url;
    private String format;
    private Integer width;
    private Integer height;
    private Long size;
    private String sizeDisplay;
    private String dominantColor;
    private String lifecycleStatus;
    private String reviewStatusText;
    private String createdBy;
    private String createdAt;
    private String updatedAt;
    private List<String> permissionList;

    private static final long serialVersionUID = 1L;
}
