-- Phase 5: ActivityRail
-- Additive DDL only. Append-only table, no soft deletes.
-- Column names use camelCase to match project's MyBatis-Plus map-underscore-to-camel-case: false

use qiu_picture;

create table if not exists activity_record
(
    id                     bigint auto_increment comment 'id' primary key,
    workspaceId            bigint not null comment '工作区 ID',
    actorUserId            bigint null comment '操作人用户 ID',
    actionType             varchar(64) not null comment '操作类型，如 collection.created',
    targetType             varchar(32) null comment '目标类型: workspace/asset/collection/collection_item',
    targetId               bigint null comment '目标实体 ID',
    secondaryTargetType    varchar(32) null comment '次要目标类型',
    secondaryTargetId      bigint null comment '次要目标实体 ID',
    summary                varchar(512) null comment '人类可读摘要',
    payloadJson            text null comment '可选结构化元数据',
    visibility             varchar(16) not null default 'members' comment '可见性: members/workspace',
    occurredAt             datetime(3) not null comment '事件发生时间',
    createTime             datetime default CURRENT_TIMESTAMP not null comment '记录创建时间',
    index idx_workspace_timeline (workspaceId, occurredAt desc),
    index idx_target_timeline (targetType, targetId, occurredAt desc),
    index idx_action_type (actionType)
) comment '活动记录（产品时间线，非审计日志）' collate = utf8mb4_unicode_ci;
