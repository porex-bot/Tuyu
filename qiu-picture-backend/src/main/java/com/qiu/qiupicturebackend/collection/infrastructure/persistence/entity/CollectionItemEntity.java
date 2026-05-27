package com.qiu.qiupicturebackend.collection.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@TableName(value = "collection_item")
@Data
public class CollectionItemEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long collectionId;

    private Long assetId;

    private Long assetVersionId;

    private Long sectionId;

    private Long sortOrder;

    private String note;

    private Long addedBy;

    private Date addedAt;

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
