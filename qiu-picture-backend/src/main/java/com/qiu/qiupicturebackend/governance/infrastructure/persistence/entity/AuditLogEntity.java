package com.qiu.qiupicturebackend.governance.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value = "audit_log")
@Data
public class AuditLogEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long workspaceId;

    private Long actorUserId;

    private String action;

    private String targetType;

    private Long targetId;

    private String detail;

    private Date createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
