-- Phase 4: Collection Board
-- Additive DDL only. No destructive changes.

use qiu_picture;

-- 集合表
create table if not exists collection
(
    id             bigint auto_increment comment 'id' primary key,
    workspace_id   bigint not null comment '工作区 ID',
    name           varchar(256) not null comment '集合名称',
    description    varchar(1024) null comment '集合描述',
    purpose        varchar(32) not null default 'project' comment '用途: project/brand/campaign/delivery/reference',
    layout         varchar(32) not null default 'grid' comment '布局: grid/board/moodboard',
    status         varchar(32) not null default 'draft' comment '状态: draft/active/archived',
    cover_asset_id bigint null comment '封面资产 ID',
    item_count     int not null default 0 comment '资产数量',
    created_by     bigint null comment '创建人',
    updated_by     bigint null comment '更新人',
    create_time    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete      tinyint default 0 not null comment '是否删除',
    index idx_workspace_id (workspace_id),
    index idx_status (status)
) comment '集合' collate = utf8mb4_unicode_ci;

-- 集合资产条目表
create table if not exists collection_item
(
    id               bigint auto_increment comment 'id' primary key,
    collection_id    bigint not null comment '集合 ID',
    asset_id         bigint not null comment '资产 ID（legacy picture.id）',
    asset_version_id bigint null comment '关联版本 ID',
    section_id       bigint null comment '分区 ID',
    sort_order       bigint not null default 0 comment '排序值（稀疏排序）',
    note             varchar(512) null comment '备注',
    added_by         bigint null comment '添加人',
    added_at         datetime default CURRENT_TIMESTAMP null comment '添加时间',
    create_time      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete        tinyint default 0 not null comment '是否删除',
    index idx_collection_id (collection_id),
    index idx_asset_id (asset_id),
    index idx_collection_asset (collection_id, asset_id),
    index idx_section_sort (section_id, sort_order)
) comment '集合资产条目' collate = utf8mb4_unicode_ci;

-- 集合分区表
create table if not exists collection_section
(
    id            bigint auto_increment comment 'id' primary key,
    collection_id bigint not null comment '集合 ID',
    name          varchar(256) not null comment '分区名称',
    sort_order    bigint not null default 0 comment '排序值',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint default 0 not null comment '是否删除',
    index idx_collection_id (collection_id)
) comment '集合分区' collate = utf8mb4_unicode_ci;
