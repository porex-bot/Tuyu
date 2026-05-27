package com.qiu.qiupicturebackend.ai.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value = "ai_job_result")
@Data
public class AiJobResultEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long jobId;

    private String resultType;

    private String outputUrl;

    private Long outputStorageObjectId;

    private String outputMetadataJson;

    private String applyStatus;

    private Long assetVersionId;

    private Date createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
