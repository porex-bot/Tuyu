-- Phase 6: AI Job System
-- Additive DDL only. Provider-neutral AI job tables. Old outpainting APIs untouched.
-- Column names use camelCase to match project's MyBatis-Plus map-underscore-to-camel-case: false

use qiu_picture;

-- Registered AI capabilities (outpainting active, others deferred)
create table if not exists ai_capability
(
    id             bigint auto_increment comment 'id' primary key,
    capabilityKey  varchar(64)  not null comment '稳定标识: outpainting/similar_search/auto_tagging/caption',
    displayName    varchar(128) not null comment '展示名称',
    description    varchar(512) null comment '简短描述',
    provider       varchar(64)  not null comment '后端提供商: aliyun',
    isActive       tinyint(1)   not null default 0 comment '是否启用',
    createdAt      datetime              default CURRENT_TIMESTAMP not null,
    updatedAt      datetime              default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    unique index uk_capability_key (capabilityKey)
) comment 'AI 能力注册表' collate = utf8mb4_unicode_ci;

-- AI job lifecycle tracking
create table if not exists ai_job
(
    id                     bigint auto_increment comment 'id' primary key,
    workspaceId            bigint       not null comment '工作区 ID',
    creatorUserId          bigint       not null comment '创建者用户 ID',
    capabilityKey          varchar(64)  not null comment 'AI 能力标识',
    status                 varchar(32)  not null default 'created' comment 'created/queued/running/succeeded/failed/cancelled/applied/discarded',
    sourceAssetId          bigint       null comment '源资产 ID',
    sourceAssetVersionId   bigint       null comment '源资产版本 ID',
    provider               varchar(64)  null comment '处理该任务的提供商',
    parametersJson         text         null comment '任务参数 JSON',
    idempotencyKey         varchar(128) null comment '客户端去重键',
    errorCode              varchar(64)  null comment '归一化错误码',
    errorMessage           varchar(1024) null comment '错误描述',
    createdAt              datetime              default CURRENT_TIMESTAMP not null,
    startedAt              datetime     null,
    finishedAt             datetime     null,
    updatedAt              datetime              default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    index idx_ai_job_workspace_status (workspaceId, status),
    index idx_ai_job_creator (creatorUserId),
    unique index uk_ai_job_idempotency (idempotencyKey),
    index idx_ai_job_source_asset (sourceAssetId)
) comment 'AI 任务' collate = utf8mb4_unicode_ci;

-- AI job results
create table if not exists ai_job_result
(
    id                       bigint auto_increment comment 'id' primary key,
    jobId                    bigint       not null comment '关联任务 ID',
    resultType               varchar(32)  not null default 'image' comment '结果类型: image/text',
    outputUrl                varchar(1024) null comment '输出 URL',
    outputStorageObjectId    bigint       null comment '已持久化的存储对象 ID',
    outputMetadataJson       text         null comment '输出元数据 JSON',
    applyStatus              varchar(32)  not null default 'pending' comment 'pending/applied/discarded',
    assetVersionId           bigint       null comment '应用后创建的资产版本 ID',
    createdAt                datetime              default CURRENT_TIMESTAMP not null,
    index idx_ai_job_result_job (jobId)
) comment 'AI 任务结果' collate = utf8mb4_unicode_ci;

-- Maps internal jobs to provider-side task identifiers
create table if not exists ai_provider_task
(
    id                    bigint auto_increment comment 'id' primary key,
    jobId                 bigint       not null comment '内部任务 ID',
    provider              varchar(64)  not null comment '提供商名称',
    providerTaskId        varchar(256) not null comment '提供商侧任务 ID',
    providerStatus        varchar(64)  null comment '提供商最新状态',
    providerResponseJson  text         null comment '提供商原始响应（调试用）',
    createdAt             datetime              default CURRENT_TIMESTAMP not null,
    updatedAt             datetime              default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    index idx_ai_provider_task_job (jobId),
    index idx_ai_provider_task_provider (provider, providerTaskId)
) comment 'AI 提供商任务映射' collate = utf8mb4_unicode_ci;

-- Usage/billing tracking
create table if not exists ai_usage_record
(
    id             bigint auto_increment comment 'id' primary key,
    workspaceId    bigint        not null comment '工作区 ID',
    jobId          bigint        null comment '关联任务 ID',
    userId         bigint        not null comment '使用用户 ID',
    capabilityKey  varchar(64)   not null comment 'AI 能力标识',
    provider       varchar(64)   not null comment '提供商',
    usageType      varchar(32)   not null comment '用量类型: api_call/image_generated',
    usageAmount    decimal(10,2) not null default 1.00 comment '用量数值',
    usageUnit      varchar(32)   not null default 'call' comment '用量单位',
    recordedAt     datetime      not null comment '用量发生时间',
    createdAt      datetime               default CURRENT_TIMESTAMP not null,
    index idx_ai_usage_workspace (workspaceId),
    index idx_ai_usage_job (jobId)
) comment 'AI 用量记录' collate = utf8mb4_unicode_ci;

-- Seed capabilities: only outpainting active
insert into ai_capability (capabilityKey, displayName, description, provider, isActive) values
('outpainting', 'AI 扩图', '基于阿里云 AI 的图像扩展生成', 'aliyun', 1),
('similar_search', '以图搜图', '基于视觉特征的相似图像检索', 'aliyun', 0),
('auto_tagging', '自动打标', 'AI 自动识别并添加标签', 'aliyun', 0),
('caption', 'AI 描述', 'AI 自动生成图片描述', 'aliyun', 0);
