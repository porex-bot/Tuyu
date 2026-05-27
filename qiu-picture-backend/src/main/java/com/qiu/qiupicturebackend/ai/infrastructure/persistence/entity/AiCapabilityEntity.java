package com.qiu.qiupicturebackend.ai.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value = "ai_capability")
@Data
public class AiCapabilityEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String capabilityKey;

    private String displayName;

    private String description;

    private String provider;

    private Integer isActive;

    private Date createdAt;

    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
