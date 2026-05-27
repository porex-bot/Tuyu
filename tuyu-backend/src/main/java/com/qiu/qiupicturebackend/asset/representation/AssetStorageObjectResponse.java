package com.qiu.qiupicturebackend.asset.representation;

import lombok.Data;

import java.io.Serializable;

@Data
public class AssetStorageObjectResponse implements Serializable {

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
    private String createdAt;

    private static final long serialVersionUID = 1L;
}
