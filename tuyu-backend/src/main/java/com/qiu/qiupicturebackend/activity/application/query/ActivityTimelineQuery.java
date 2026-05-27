package com.qiu.qiupicturebackend.activity.application.query;

import lombok.Data;

@Data
public class ActivityTimelineQuery {

    private int offset = 0;
    private int limit = 20;
    private String targetType;
    private Long targetId;
}
