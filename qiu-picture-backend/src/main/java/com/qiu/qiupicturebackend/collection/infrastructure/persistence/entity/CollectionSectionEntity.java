package com.qiu.qiupicturebackend.collection.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@TableName(value = "collection_section")
@Data
public class CollectionSectionEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long collectionId;

    private String name;

    private Long sortOrder;

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
