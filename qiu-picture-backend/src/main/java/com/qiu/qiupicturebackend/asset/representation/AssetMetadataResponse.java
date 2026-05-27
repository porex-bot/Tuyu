package com.qiu.qiupicturebackend.asset.representation;

import lombok.Data;

import java.io.Serializable;

/**
 * 视觉资产元数据响应 —— 内嵌于详情或独立展示。
 */
@Data
public class AssetMetadataResponse implements Serializable {

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
