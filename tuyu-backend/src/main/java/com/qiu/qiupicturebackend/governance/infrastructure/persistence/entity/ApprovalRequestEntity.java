package com.qiu.qiupicturebackend.governance.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value = "approval_request")
@Data
public class ApprovalRequestEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long workspaceId;

    private String targetType;

    private Long targetId;

    private Long targetVersionId;

    private String requestType;

    private String status;

    private Long submittedBy;

    private Date submittedAt;

    private Long resolvedBy;

    private Date resolvedAt;

    private String reason;

    private String resultMessage;

    private Date createdAt;

    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
