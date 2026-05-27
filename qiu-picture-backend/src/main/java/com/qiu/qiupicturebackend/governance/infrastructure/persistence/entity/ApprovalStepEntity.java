package com.qiu.qiupicturebackend.governance.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value = "approval_step")
@Data
public class ApprovalStepEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long approvalId;

    private Integer stepOrder;

    private Long reviewerId;

    private String status;

    private Date createdAt;

    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
