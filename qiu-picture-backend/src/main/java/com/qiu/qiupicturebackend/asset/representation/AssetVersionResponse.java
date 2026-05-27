package com.qiu.qiupicturebackend.asset.representation;

import lombok.Data;

import java.io.Serializable;

@Data
public class AssetVersionResponse implements Serializable {

    private Long versionId;
    private Long assetId;
    private Long legacyPictureId;
    private Integer versionNo;
    private String versionType;
    private String storageUrl;
    private String thumbnailUrl;
    private Integer width;
    private Integer height;
    private Long fileSize;
    private String format;
    private String dominantColor;
    private Long createdBy;
    private String createdAt;
    private Boolean isCurrent;

    private static final long serialVersionUID = 1L;
}
