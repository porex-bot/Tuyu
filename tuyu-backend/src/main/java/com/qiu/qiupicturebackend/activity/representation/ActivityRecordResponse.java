package com.qiu.qiupicturebackend.activity.representation;

import lombok.Data;

@Data
public class ActivityRecordResponse {

    private Long activityId;
    private Long workspaceId;
    private Actor actor;
    private String actionType;
    private Target target;
    private Target secondaryTarget;
    private String summary;
    private String visibility;
    private String occurredAt;

    @Data
    public static class Actor {
        private Long userId;
        private String userName;
        private String userAvatar;
    }

    @Data
    public static class Target {
        private String targetType;
        private Long targetId;
        private String targetName;
    }
}
