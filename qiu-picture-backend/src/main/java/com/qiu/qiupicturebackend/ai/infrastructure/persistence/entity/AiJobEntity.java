package com.qiu.qiupicturebackend.ai.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value = "ai_job")
@Data
public class AiJobEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long workspaceId;

    private Long creatorUserId;

    private String capabilityKey;

    private String status;

    private Long sourceAssetId;

    private Long sourceAssetVersionId;

    private String provider;

    private String parametersJson;

    private String idempotencyKey;

    private String errorCode;

    private String errorMessage;

    private Date createdAt;

    private Date startedAt;

    private Date finishedAt;

    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
