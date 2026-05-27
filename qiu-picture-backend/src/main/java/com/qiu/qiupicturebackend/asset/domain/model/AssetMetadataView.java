package com.qiu.qiupicturebackend.asset.domain.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 视觉资产元数据读模型（非持久化）。
 * 聚合 EXIF、尺寸、颜色等可展示的元数据信息。
 */
@Data
public class AssetMetadataView implements Serializable {

    private Integer width;
    private Integer height;
    private Double scale;
    private String format;
    private Long size;
    private String sizeDisplay;
    private String dominantColor;
    private String category;
    private String tags;

    private static final long serialVersionUID = 1L;
}
