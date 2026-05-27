package com.qiu.qiupicturebackend.asset.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AssetStorageObjectView implements Serializable {

    private Long storageObjectId;
    private String legacyUrl;
    private String storageKey;
    private String bucket;
    private String region;
    private Long fileSize;
    private String contentType;
    private Integer width;
    private Integer height;
    private String format;
    private String dominantColor;
    private Date createdAt;

    private static final long serialVersionUID = 1L;
}
