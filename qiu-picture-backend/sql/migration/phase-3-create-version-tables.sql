-- Phase 3: Asset Versioning and Storage References
-- Additive DDL only. No destructive changes.

use qiu_picture;

-- 资产存储对象表
create table if not exists asset_storage_object
(
    id            bigint auto_increment comment 'id' primary key,
    legacy_url    varchar(1024) not null comment '对象存储完整 URL',
    storage_key   varchar(512) null comment '对象存储 key',
    bucket        varchar(128) null comment 'Bucket 名称',
    region        varchar(64) null comment '区域',
    file_size     bigint null comment '文件大小（字节）',
    content_type  varchar(128) null comment 'MIME 类型',
    width         int null comment '宽度',
    height        int null comment '高度',
    format        varchar(32) null comment '文件格式',
    dominant_color varchar(16) null comment '主色调',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint default 0 not null comment '是否删除',
    index idx_legacy_url (legacy_url(255))
) comment '资产存储对象' collate = utf8mb4_unicode_ci;

-- 资产元数据表
create table if not exists asset_metadata
(
    id             bigint auto_increment comment 'id' primary key,
    asset_id       bigint not null comment '关联资产 ID',
    version_id     bigint null comment '关联版本 ID',
    width          int null comment '宽度',
    height         int null comment '高度',
    scale          double null comment '宽高比',
    format         varchar(32) null comment '格式',
    file_size      bigint null comment '文件大小',
    dominant_color varchar(16) null comment '主色调',
    category       varchar(64) null comment '分类',
    tags           varchar(512) null comment '标签 JSON',
    description    varchar(512) null comment '描述',
    create_time    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete      tinyint default 0 not null comment '是否删除',
    index idx_asset_id (asset_id),
    index idx_version_id (version_id)
) comment '资产元数据' collate = utf8mb4_unicode_ci;

-- 资产版本表
create table if not exists asset_version
(
    id                          bigint auto_increment comment 'id' primary key,
    asset_id                    bigint not null comment '资产 ID（legacy picture.id）',
    legacy_picture_id           bigint null comment '旧版图片 ID',
    version_no                  int not null default 1 comment '版本号',
    version_type                varchar(32) not null default 'original' comment '版本类型',
    storage_object_id           bigint null comment '存储对象 ID',
    thumbnail_storage_object_id bigint null comment '缩略图存储对象 ID',
    metadata_id                 bigint null comment '元数据 ID',
    created_by                  bigint null comment '创建用户',
    is_current                  tinyint not null default 1 comment '是否为当前版本',
    create_time                 datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time                 datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete                   tinyint default 0 not null comment '是否删除',
    index idx_asset_id (asset_id),
    index idx_legacy_picture_id (legacy_picture_id),
    index idx_is_current (is_current),
    index idx_version_type (version_type)
) comment '资产版本' collate = utf8mb4_unicode_ci;

-- 版本回填日志表
create table if not exists asset_version_backfill_log
(
    id                bigint auto_increment comment 'id' primary key,
    legacy_picture_id bigint not null comment '已处理的旧版图片 ID',
    version_id        bigint null comment '创建的版本 ID',
    status            varchar(32) not null default 'success' comment '状态：success/error',
    error_message     varchar(512) null comment '错误信息',
    create_time       datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    unique key uk_legacy_picture_id (legacy_picture_id)
) comment '版本回填日志' collate = utf8mb4_unicode_ci;
