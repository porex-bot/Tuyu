package com.qiu.qiupicturebackend.governance.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value = "governance_policy")
@Data
public class GovernancePolicyEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long workspaceId;

    private String mode;

    private Integer requireApprovalForAssets;

    private Integer requireApprovalForCollections;

    private Integer requireApprovalForAiResults;

    private Integer autoApproveTrustedUsers;

    private Long updatedBy;

    private Date createdAt;

    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
