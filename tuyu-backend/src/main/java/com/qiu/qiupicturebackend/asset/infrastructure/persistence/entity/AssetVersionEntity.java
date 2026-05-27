package com.qiu.qiupicturebackend.asset.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@TableName(value = "asset_version")
@Data
public class AssetVersionEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long assetId;

    private Long legacyPictureId;

    private Integer versionNo;

    private String versionType;

    private Long storageObjectId;

    private Long thumbnailStorageObjectId;

    private Long metadataId;

    private Long createdBy;

    private Integer isCurrent;

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
