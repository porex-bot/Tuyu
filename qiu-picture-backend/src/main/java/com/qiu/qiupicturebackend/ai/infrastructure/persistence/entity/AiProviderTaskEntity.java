package com.qiu.qiupicturebackend.ai.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value = "ai_provider_task")
@Data
public class AiProviderTaskEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long jobId;

    private String provider;

    private String providerTaskId;

    private String providerStatus;

    private String providerResponseJson;

    private Date createdAt;

    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
