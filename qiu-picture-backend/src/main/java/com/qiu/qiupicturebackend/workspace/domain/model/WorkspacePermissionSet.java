package com.qiu.qiupicturebackend.workspace.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 工作区权限集读模型（非持久化实体，不含 MyBatis 注解）
 */
@Data
public class WorkspacePermissionSet implements Serializable {

    /**
     * 工作区 ID
     */
    private Long workspaceId;

    /**
     * 当前用户在该工作区的角色键
     */
    private String role;

    /**
     * 权限键列表
     */
    private List<String> permissions = new ArrayList<>();

    private static final long serialVersionUID = 1L;
}
