# Phase 3: Asset Versioning and Storage References

> Additive schema only. No destructive changes. Old `picture` table remains source of truth.

## New Tables

### asset_version

| Column | Type | Description |
|---|---|---|
| id | BIGINT PK AUTO_INCREMENT | Version ID |
| asset_id | BIGINT NOT NULL | Link to visual asset (legacy picture.id in Phase 3) |
| legacy_picture_id | BIGINT NULL | Backward reference to picture table |
| version_no | INT NOT NULL DEFAULT 1 | Monotonic version number per asset |
| version_type | VARCHAR(32) NOT NULL DEFAULT 'original' | original/replacement/manual_edit/crop/ai_generated/format_conversion |
| storage_object_id | BIGINT NULL | FK to asset_storage_object |
| thumbnail_storage_object_id | BIGINT NULL | FK to thumbnail storage object |
| metadata_id | BIGINT NULL | FK to asset_metadata |
| created_by | BIGINT NULL | User who created this version |
| is_current | TINYINT NOT NULL DEFAULT 1 | Whether this is the current active version |
| create_time | DATETIME DEFAULT NOW() | |
| update_time | DATETIME DEFAULT NOW() ON UPDATE | |
| is_delete | TINYINT DEFAULT 0 | Soft delete |

### asset_storage_object

| Column | Type | Description |
|---|---|---|
| id | BIGINT PK AUTO_INCREMENT | Storage object ID |
| legacy_url | VARCHAR(1024) NOT NULL | Full URL in object storage |
| storage_key | VARCHAR(512) NULL | Object key (parsed from URL, deferred) |
| bucket | VARCHAR(128) NULL | Bucket name |
| region | VARCHAR(64) NULL | Region |
| file_size | BIGINT NULL | Size in bytes |
| content_type | VARCHAR(128) NULL | MIME type |
| width | INT NULL | |
| height | INT NULL | |
| format | VARCHAR(32) NULL | |
| dominant_color | VARCHAR(16) NULL | |
| create_time | DATETIME DEFAULT NOW() | |
| update_time | DATETIME DEFAULT NOW() ON UPDATE | |
| is_delete | TINYINT DEFAULT 0 | |

### asset_metadata

| Column | Type | Description |
|---|---|---|
| id | BIGINT PK AUTO_INCREMENT | Metadata record ID |
| asset_id | BIGINT NOT NULL | |
| version_id | BIGINT NULL | |
| width | INT NULL | |
| height | INT NULL | |
| scale | DOUBLE NULL | |
| format | VARCHAR(32) NULL | |
| file_size | BIGINT NULL | |
| dominant_color | VARCHAR(16) NULL | |
| category | VARCHAR(64) NULL | |
| tags | VARCHAR(512) NULL | JSON array |
| description | VARCHAR(512) NULL | |
| create_time | DATETIME DEFAULT NOW() | |
| update_time | DATETIME DEFAULT NOW() ON UPDATE | |
| is_delete | TINYINT DEFAULT 0 | |

### asset_version_backfill_log

| Column | Type | Description |
|---|---|---|
| id | BIGINT PK AUTO_INCREMENT | |
| legacy_picture_id | BIGINT NOT NULL UNIQUE | Tracked picture.id that was backfilled |
| version_id | BIGINT NULL | Created version id |
| status | VARCHAR(32) NOT NULL DEFAULT 'success' | success / error |
| error_message | VARCHAR(512) NULL | |
| create_time | DATETIME DEFAULT NOW() | |

## Backfill Rules

1. Read `picture` rows in batches (500 per batch)
2. For each picture, check `asset_version_backfill_log` — skip if already processed
3. Create `asset_storage_object` from `picture.url`
4. Create thumbnail `asset_storage_object` from `picture.thumbnailUrl` if present
5. Create `asset_metadata` from `picWidth`, `picHeight`, `picScale`, `picSize`, `picFormat`, `picColor`
6. Create `asset_version` with `version_type = 'original'`, `version_no = 1`, `is_current = 1`
7. Insert into `asset_version_backfill_log`
8. All operations are idempotent

## Rollback

- No new tables are dropped
- No old columns are removed
- Old `picture` table remains source of truth
- Ignoring version tables restores Phase 2 behavior
