package com.qiu.qiupicturebackend.asset.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AssetMetadataSnapshot implements Serializable {

    private Long metadataId;
    private Long assetId;
    private Long versionId;
    private Integer width;
    private Integer height;
    private Double scale;
    private String format;
    private Long fileSize;
    private String dominantColor;
    private String category;
    private String tags;
    private String description;
    private Date createdAt;

    private static final long serialVersionUID = 1L;
}
