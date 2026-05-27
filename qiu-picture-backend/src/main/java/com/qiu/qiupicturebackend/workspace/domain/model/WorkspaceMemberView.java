package com.qiu.qiupicturebackend.workspace.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 工作区成员读模型（非持久化实体，不含 MyBatis 注解）
 */
@Data
public class WorkspaceMemberView implements Serializable {

    /**
     * 成员记录 ID
     */
    private Long id;

    /**
     * 工作区 ID
     */
    private Long workspaceId;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 工作区角色：viewer / editor / admin
     */
    private String role;

    /**
     * 加入时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
