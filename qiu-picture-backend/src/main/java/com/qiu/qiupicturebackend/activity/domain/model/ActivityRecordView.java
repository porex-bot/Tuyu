package com.qiu.qiupicturebackend.activity.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ActivityRecordView implements Serializable {

    private Long activityId;
    private Long workspaceId;
    private ActivityActorView actor;
    private String actionType;
    private ActivityTargetView target;
    private ActivityTargetView secondaryTarget;
    private String summary;
    private String visibility;
    private Date occurredAt;

    private static final long serialVersionUID = 1L;
}
