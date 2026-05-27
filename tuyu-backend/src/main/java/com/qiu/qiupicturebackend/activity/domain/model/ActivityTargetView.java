package com.qiu.qiupicturebackend.activity.domain.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ActivityTargetView implements Serializable {

    private String targetType;
    private Long targetId;
    private String targetName;

    private static final long serialVersionUID = 1L;
}
