package com.qiu.qiupicturebackend.governance.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value = "approval_decision")
@Data
public class ApprovalDecisionEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long stepId;

    private Long approvalId;

    private Long decidedBy;

    private String decisionType;

    private String comment;

    private Date createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
