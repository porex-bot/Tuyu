-- Phase 7: Governance and Approval Workflow
-- Additive DDL only. Approval tables are workspace-scoped. Old picture.reviewStatus untouched.
-- Column names use camelCase to match project's MyBatis-Plus map-underscore-to-camel-case: false

use qiu_picture;

-- Main approval request
create table if not exists approval_request
(
    id               bigint auto_increment comment 'id' primary key,
    workspaceId      bigint       not null comment '工作区 ID',
    targetType       varchar(32)  not null comment 'asset/collection/ai_result',
    targetId         bigint       not null comment '目标实体 ID',
    targetVersionId  bigint       null comment '目标版本 ID',
    requestType      varchar(32)  not null comment 'publish/delete/export',
    status           varchar(32)  not null default 'draft' comment 'draft/pending/approved/rejected/changes_requested/cancelled',
    submittedBy      bigint       not null comment '提交者用户 ID',
    submittedAt      datetime     null comment '提交时间',
    resolvedBy       bigint       null comment '决议者用户 ID',
    resolvedAt       datetime     null comment '决议时间',
    reason           varchar(512) null comment '提交原因',
    resultMessage    varchar(512) null comment '决议结果说明',
    createdAt        datetime              default CURRENT_TIMESTAMP not null,
    updatedAt        datetime              default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    index idx_approval_req_workspace_status (workspaceId, status),
    index idx_approval_req_target (workspaceId, targetType, targetId),
    index idx_approval_req_submitted_by (submittedBy)
) comment '审批请求' collate = utf8mb4_unicode_ci;

-- Steps within an approval workflow
create table if not exists approval_step
(
    id          bigint auto_increment comment 'id' primary key,
    approvalId  bigint      not null comment '关联审批请求 ID',
    stepOrder   int         not null default 1 comment '步骤顺序',
    reviewerId  bigint      null comment '指定/实际审核人用户 ID',
    status      varchar(32) not null default 'pending' comment 'pending/approved/rejected/changes_requested/skipped',
    createdAt   datetime             default CURRENT_TIMESTAMP not null,
    updatedAt   datetime             default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    index idx_approval_step_approval (approvalId),
    index idx_approval_step_reviewer (reviewerId)
) comment '审批步骤' collate = utf8mb4_unicode_ci;

-- Individual decisions on approval steps
create table if not exists approval_decision
(
    id            bigint auto_increment comment 'id' primary key,
    stepId        bigint       not null comment '关联步骤 ID',
    approvalId    bigint       not null comment '关联审批请求 ID',
    decidedBy     bigint       not null comment '决议者用户 ID',
    decisionType  varchar(32)  not null comment 'approve/reject/request_changes',
    comment       varchar(1024) null comment '审核意见',
    createdAt     datetime              default CURRENT_TIMESTAMP not null,
    index idx_approval_dec_approval (approvalId),
    index idx_approval_dec_step (stepId)
) comment '审批决议' collate = utf8mb4_unicode_ci;

-- Per-workspace governance policy
create table if not exists governance_policy
(
    id                             bigint auto_increment comment 'id' primary key,
    workspaceId                    bigint      not null comment '工作区 ID',
    mode                           varchar(32) not null default 'off' comment 'off/manual/auto_for_publication/strict',
    requireApprovalForAssets       tinyint(1)  not null default 0 comment '资产是否需要审批',
    requireApprovalForCollections  tinyint(1)  not null default 0 comment '集合是否需要审批',
    requireApprovalForAiResults    tinyint(1)  not null default 0 comment 'AI 结果是否需要审批',
    autoApproveTrustedUsers        tinyint(1)  not null default 0 comment '受信用户是否自动通过',
    updatedBy                      bigint      not null comment '最后更新者用户 ID',
    createdAt                      datetime             default CURRENT_TIMESTAMP not null,
    updatedAt                      datetime             default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    unique index uk_gov_policy_workspace (workspaceId)
) comment '工作区治理策略' collate = utf8mb4_unicode_ci;
