package com.qiu.qiupicturebackend.workspace.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 工作区读模型（非持久化实体，不含 MyBatis 注解）
 */
@Data
public class WorkspaceView implements Serializable {

    /**
     * 工作区 ID
     */
    private Long workspaceId;

    /**
     * 旧版空间 ID（Phase 1 兼容桥接）
     */
    private Long legacySpaceId;

    /**
     * 工作区名称
     */
    private String name;

    /**
     * 工作区类型：0-私有 1-团队
     */
    private Integer type;

    /**
     * 工作区级别：0-普通版 1-专业版 2-旗舰版
     */
    private Integer level;

    /**
     * 创建用户 ID
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 编辑时间
     */
    private Date editTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
