package com.qiu.qiupicturebackend.asset.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AssetVersionView implements Serializable {

    private Long versionId;
    private Long assetId;
    private Long legacyPictureId;
    private Integer versionNo;
    private AssetVersionType versionType;
    private String storageUrl;
    private String thumbnailUrl;
    private Integer width;
    private Integer height;
    private Long fileSize;
    private String format;
    private String dominantColor;
    private Long createdBy;
    private Date createdAt;
    private Boolean isCurrent;

    private static final long serialVersionUID = 1L;
}
