package com.qiu.qiupicturebackend.asset.application.query;

import lombok.Data;

import java.io.Serializable;

/**
 * 视觉资产筛选器查询 —— Phase 2 预留给前端筛选面板使用。
 * 不暴露后端尚不支持的筛选维度。
 */
@Data
public class AssetFilterQuery implements Serializable {

    private String format;
    private String dominantColor;
    private Integer minWidth;
    private Integer minHeight;
    private String lifecycleStatus;

    private static final long serialVersionUID = 1L;
}
