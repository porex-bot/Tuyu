package com.qiu.qiupicturebackend.asset.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@TableName(value = "asset_storage_object")
@Data
public class AssetStorageObjectEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

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

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
