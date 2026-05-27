package com.qiu.qiupicturebackend.asset.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@TableName(value = "asset_version_backfill_log")
@Data
public class AssetVersionBackfillLogEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long legacyPictureId;

    private Long versionId;

    private String status;

    private String errorMessage;

    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
