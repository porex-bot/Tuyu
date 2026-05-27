package com.qiu.qiupicturebackend.collection.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@TableName(value = "collection")
@Data
public class CollectionEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long workspaceId;

    private String name;

    private String description;

    private String purpose;

    private String layout;

    private String status;

    private Long coverAssetId;

    private Integer itemCount;

    private Long createdBy;

    private Long updatedBy;

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
