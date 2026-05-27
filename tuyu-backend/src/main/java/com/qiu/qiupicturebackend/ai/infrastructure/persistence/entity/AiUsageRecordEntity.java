package com.qiu.qiupicturebackend.ai.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value = "ai_usage_record")
@Data
public class AiUsageRecordEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long workspaceId;

    private Long jobId;

    private Long userId;

    private String capabilityKey;

    private String provider;

    private String usageType;

    private java.math.BigDecimal usageAmount;

    private String usageUnit;

    private Date recordedAt;

    private Date createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
