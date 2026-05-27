package com.qiu.qiupicturebackend.activity.application.command;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class RecordActivityCommand {

    private Long workspaceId;
    private Long actorUserId;
    private String actionType;
    private String targetType;
    private Long targetId;
    private String targetName;
    private String secondaryTargetType;
    private Long secondaryTargetId;
    private String summary;
    private String payloadJson;
    private String visibility;
    private Date occurredAt;
}
