# Phase 4: Collection Board

> Additive schema only. No destructive changes. Collections reference existing assets through compatibility IDs and do not own files.

## New Tables

### collection

| Column | Type | Description |
|---|---|---|
| id | BIGINT PK AUTO_INCREMENT | Collection ID |
| workspace_id | BIGINT NOT NULL | Workspace scope |
| name | VARCHAR(256) NOT NULL | Collection name |
| description | VARCHAR(1024) NULL | Optional description |
| purpose | VARCHAR(32) NOT NULL DEFAULT 'project' | project/brand/campaign/delivery/reference |
| layout | VARCHAR(32) NOT NULL DEFAULT 'grid' | grid/board/moodboard |
| status | VARCHAR(32) NOT NULL DEFAULT 'draft' | draft/active/archived |
| cover_asset_id | BIGINT NULL | Cover asset reference (legacy picture.id) |
| item_count | INT NOT NULL DEFAULT 0 | Denormalized item count |
| created_by | BIGINT NULL | Creator user ID |
| updated_by | BIGINT NULL | Last updater user ID |
| create_time | DATETIME DEFAULT NOW() | |
| update_time | DATETIME DEFAULT NOW() ON UPDATE | |
| is_delete | TINYINT DEFAULT 0 | Soft delete |

### collection_item

| Column | Type | Description |
|---|---|---|
| id | BIGINT PK AUTO_INCREMENT | Item ID |
| collection_id | BIGINT NOT NULL | FK to collection |
| asset_id | BIGINT NOT NULL | Reference to visual asset (legacy picture.id) |
| asset_version_id | BIGINT NULL | FK to asset_version (optional pin) |
| section_id | BIGINT NULL | FK to collection_section |
| sort_order | BIGINT NOT NULL DEFAULT 0 | Sparse sort order for reordering |
| note | VARCHAR(512) NULL | Optional item note |
| added_by | BIGINT NULL | User who added this item |
| added_at | DATETIME DEFAULT NOW() | When item was added |
| create_time | DATETIME DEFAULT NOW() | |
| update_time | DATETIME DEFAULT NOW() ON UPDATE | |
| is_delete | TINYINT DEFAULT 0 | Soft delete |

### collection_section

| Column | Type | Description |
|---|---|---|
| id | BIGINT PK AUTO_INCREMENT | Section ID |
| collection_id | BIGINT NOT NULL | FK to collection |
| name | VARCHAR(256) NOT NULL | Section name |
| sort_order | BIGINT NOT NULL DEFAULT 0 | Section ordering |
| create_time | DATETIME DEFAULT NOW() | |
| update_time | DATETIME DEFAULT NOW() ON UPDATE | |
| is_delete | TINYINT DEFAULT 0 | Soft delete |

## Deferred Tables (not in Phase 4)

- `collection_share` — sharing links
- `collection_snapshot` — published snapshots
- `collection_snapshot_item` — snapshot items

## Design Rules

- Collections reference assets by `asset_id` (legacy picture.id); they do not own or copy files.
- Removing an item from a collection only marks/deletes the `collection_item` row; it never deletes assets or storage objects.
- `sort_order` uses sparse values (gaps of 1000) to minimize reindexing on reorder.
- Duplicate active items (same collection + same asset) are prevented at the application layer.
- Workspace validation is enforced on all collection endpoints.

## Rollback

- Do not apply the DDL migration.
- Hide collection routes and sidebar entry.
- Existing asset, picture, and space features remain unchanged.
