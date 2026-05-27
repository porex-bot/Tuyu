# Phase 6: AI Job System

> Additive schema only. AI jobs are workspace-scoped, provider-neutral, and explicitly applied by users. Existing Aliyun outpainting is wrapped behind a provider gateway; old direct APIs remain untouched.

## New Tables

### ai_capability

| Column | Type | Description |
|---|---|---|
| id | BIGINT PK AUTO_INCREMENT | Capability record ID |
| capability_key | VARCHAR(64) NOT NULL UNIQUE | Stable key: `outpainting`, `similar_search`, `auto_tagging`, `caption` |
| display_name | VARCHAR(128) NOT NULL | Human-readable name |
| description | VARCHAR(512) NULL | Short description for UI |
| provider | VARCHAR(64) NOT NULL | Backend provider: `aliyun`, etc. |
| is_active | TINYINT(1) NOT NULL DEFAULT 0 | Whether the capability is enabled for use |
| created_at | DATETIME DEFAULT NOW() | |
| updated_at | DATETIME DEFAULT NOW() ON UPDATE NOW() | |

### ai_job

| Column | Type | Description |
|---|---|---|
| id | BIGINT PK AUTO_INCREMENT | Job ID |
| workspace_id | BIGINT NOT NULL | Workspace scope |
| creator_user_id | BIGINT NOT NULL | User who created the job |
| capability_key | VARCHAR(64) NOT NULL | Which capability this job uses |
| status | VARCHAR(32) NOT NULL DEFAULT 'created' | `created`, `queued`, `running`, `succeeded`, `failed`, `cancelled`, `applied`, `discarded` |
| source_asset_id | BIGINT NULL | Source asset for the job |
| source_asset_version_id | BIGINT NULL | Specific version of source asset |
| provider | VARCHAR(64) NULL | Provider handling this job |
| parameters_json | TEXT NULL | Provider-neutral job parameters (JSON) |
| idempotency_key | VARCHAR(128) NULL | Client-supplied dedup key |
| error_code | VARCHAR(64) NULL | Normalized error code |
| error_message | VARCHAR(1024) NULL | Human-readable error |
| created_at | DATETIME DEFAULT NOW() | |
| started_at | DATETIME NULL | |
| finished_at | DATETIME NULL | |
| updated_at | DATETIME DEFAULT NOW() ON UPDATE NOW() | |

### ai_job_result

| Column | Type | Description |
|---|---|---|
| id | BIGINT PK AUTO_INCREMENT | Result ID |
| job_id | BIGINT NOT NULL | Parent job |
| result_type | VARCHAR(32) NOT NULL DEFAULT 'image' | `image`, `text`, etc. |
| output_url | VARCHAR(1024) NULL | Direct output URL |
| output_storage_object_id | BIGINT NULL | Reference to asset_storage_object if persisted |
| output_metadata_json | TEXT NULL | Structured output metadata |
| apply_status | VARCHAR(32) NOT NULL DEFAULT 'pending' | `pending`, `applied`, `discarded` |
| asset_version_id | BIGINT NULL | The asset_version created when this result was applied |
| created_at | DATETIME DEFAULT NOW() | |

### ai_provider_task

| Column | Type | Description |
|---|---|---|
| id | BIGINT PK AUTO_INCREMENT | Mapping record ID |
| job_id | BIGINT NOT NULL | Internal job |
| provider | VARCHAR(64) NOT NULL | Provider name |
| provider_task_id | VARCHAR(256) NOT NULL | Provider-side task identifier |
| provider_status | VARCHAR(64) NULL | Last known provider status |
| provider_response_json | TEXT NULL | Raw provider response (for debugging) |
| created_at | DATETIME DEFAULT NOW() | |
| updated_at | DATETIME DEFAULT NOW() ON UPDATE NOW() | |

### ai_usage_record

| Column | Type | Description |
|---|---|---|
| id | BIGINT PK AUTO_INCREMENT | Usage record ID |
| workspace_id | BIGINT NOT NULL | Workspace scope |
| job_id | BIGINT NULL | Related job |
| user_id | BIGINT NOT NULL | User who incurred usage |
| capability_key | VARCHAR(64) NOT NULL | Capability used |
| provider | VARCHAR(64) NOT NULL | Provider used |
| usage_type | VARCHAR(32) NOT NULL | `api_call`, `image_generated`, etc. |
| usage_amount | DECIMAL(10,2) NOT NULL DEFAULT 1.00 | Quantified usage |
| usage_unit | VARCHAR(32) NOT NULL DEFAULT 'call' | Unit of measurement |
| recorded_at | DATETIME NOT NULL | When usage occurred |
| created_at | DATETIME DEFAULT NOW() | |

## Indexes

```sql
INDEX idx_ai_job_workspace_status (workspace_id, status)
INDEX idx_ai_job_creator (creator_user_id)
UNIQUE INDEX uk_ai_job_idempotency (idempotency_key)
INDEX idx_ai_job_source_asset (source_asset_id)
INDEX idx_ai_job_result_job (job_id)
INDEX idx_ai_provider_task_job (job_id)
INDEX idx_ai_provider_task_provider (provider, provider_task_id)
INDEX idx_ai_usage_workspace (workspace_id)
INDEX idx_ai_usage_job (job_id)
```

## Initial Seed Data

```sql
INSERT INTO ai_capability (capability_key, display_name, description, provider, is_active) VALUES
('outpainting', 'AI 扩图', '基于阿里云 AI 的图像扩展生成', 'aliyun', 1),
('similar_search', '以图搜图', '基于视觉特征的相似图像检索', 'aliyun', 0),
('auto_tagging', '自动打标', 'AI 自动识别并添加标签', 'aliyun', 0),
('caption', 'AI 描述', 'AI 自动生成图片描述', 'aliyun', 0);
```

## Deferred (not in Phase 6)

- `ai_prompt_template` — reusable prompt templates
- `ai_safety_case` — content safety review records
- `ai_batch_workflow` — batch AI job orchestration
- Governance approval for AI output

## Design Rules

- AI jobs are workspace-scoped. All endpoints validate workspace permission.
- Provider gateway abstracts Aliyun-specific details behind a provider-neutral interface.
- AI results are never auto-applied; users explicitly apply approved output.
- Applying a result creates a new `asset_version` with `version_type = 'ai_generated'`.
- Original `picture.url` is never overwritten by AI output.
- Idempotency key prevents duplicate job creation on repeated clicks.
- Jobs track full lifecycle: `created → queued → running → succeeded/failed/cancelled → applied/discarded`.
- Scheduler polls running jobs at a conservative interval (configurable, default 30s).

## Rollback

- Do not apply migration.
- Hide AiStudio route and disable AI job APIs.
- Old `/picture/out_painting/*` endpoints remain unchanged and fully functional.
- Existing asset, collection, activity, and workspace features remain unchanged.
