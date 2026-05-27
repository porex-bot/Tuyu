package com.qiu.qiupicturebackend.asset.application.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 视觉资产分页查询 —— 组合 AssetQuery 过滤条件与分页参数。
 * Phase 2 内部映射到 PictureQueryRequest + PageRequest。
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AssetPageQuery extends AssetQuery implements Serializable {

    private int current = 1;
    private int pageSize = 20;
    private String sortField;
    private String sortOrder = "descend";

    private static final long serialVersionUID = 1L;
}
