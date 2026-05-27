package com.qiu.qiupicturebackend.asset.application.query;

import lombok.Data;

import java.io.Serializable;

/**
 * 视觉资产查询基础模型 —— 使用 asset 术语，Phase 2 内部映射到 PictureQueryRequest。
 */
@Data
public class AssetQuery implements Serializable {

    private Long workspaceId;
    private String searchText;
    private String category;
    private String tags;
    private String format;
    private String dominantColor;
    private Integer width;
    private Integer height;
    private Long userId;
    private String lifecycleStatus;

    private static final long serialVersionUID = 1L;
}
