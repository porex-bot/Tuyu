package com.qiu.qiupicturebackend.activity.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value = "activity_record")
@Data
public class ActivityRecordEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long workspaceId;

    private Long actorUserId;

    private String actionType;

    private String targetType;

    private Long targetId;

    private String secondaryTargetType;

    private Long secondaryTargetId;

    private String summary;

    private String payloadJson;

    private String visibility;

    private Date occurredAt;

    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
