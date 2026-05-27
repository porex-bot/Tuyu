package com.qiu.qiupicturebackend.asset.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@TableName(value = "asset_metadata")
@Data
public class AssetMetadataEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

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

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
