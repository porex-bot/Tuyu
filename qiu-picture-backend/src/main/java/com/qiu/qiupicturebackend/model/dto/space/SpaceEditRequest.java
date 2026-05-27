package com.qiu.qiupicturebackend.model.dto.space;

import lombok.Data;

import java.io.Serializable;

/**
 * 空间添加请求
 */
@Data
public class SpaceEditRequest implements Serializable {

    /**
     * 空间 id
     */
    private Long id;

    /**
     * 空间名称
     */
    private String spaceName;

    private static final long serialVersionUID = 1L;
}
