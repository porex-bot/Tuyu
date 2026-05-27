package com.qiu.qiupicturebackend.workspace.representation;

import lombok.Data;

import java.io.Serializable;

/**
 * 工作区用量 API 响应
 */
@Data
public class WorkspaceUsageResponse implements Serializable {

    /**
     * 工作区 ID
     */
    private Long workspaceId;

    /**
     * 最大存储容量（字节）
     */
    private Long maxSize;

    /**
     * 当前已用存储（字节）
     */
    private Long totalSize;

    /**
     * 最大图片数量
     */
    private Long maxCount;

    /**
     * 当前图片数量
     */
    private Long totalCount;

    /**
     * 存储使用比例（0.0 ~ 1.0）
     */
    private Double storageRatio;

    /**
     * 数量使用比例（0.0 ~ 1.0）
     */
    private Double countRatio;

    private static final long serialVersionUID = 1L;
}
