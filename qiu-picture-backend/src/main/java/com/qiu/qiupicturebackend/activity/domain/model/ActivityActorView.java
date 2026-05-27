package com.qiu.qiupicturebackend.activity.domain.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ActivityActorView implements Serializable {

    private Long userId;
    private String userName;
    private String userAvatar;

    private static final long serialVersionUID = 1L;
}
