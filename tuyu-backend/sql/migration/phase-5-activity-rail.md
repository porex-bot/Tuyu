# Phase 5: ActivityRail

> Additive schema only. Activity records are product timeline events, not compliance audit logs. Append-only; no updates, no soft deletes.

## New Tables

### activity_record

| Column | Type | Description |
|---|---|---|
| id | BIGINT PK AUTO_INCREMENT | Activity record ID |
| workspace_id | BIGINT NOT NULL | Workspace scope |
| actor_user_id | BIGINT NULL | User who performed the action |
| action_type | VARCHAR(64) NOT NULL | Dot-notation action: `collection.created`, `asset.version.created`, etc. |
| target_type | VARCHAR(32) NULL | Target entity type: `workspace`, `asset`, `collection`, `collection_item` |
| target_id | BIGINT NULL | Target entity ID |
| secondary_target_type | VARCHAR(32) NULL | Optional secondary target type |
| secondary_target_id | BIGINT NULL | Optional secondary target ID |
| summary | VARCHAR(512) NULL | Human-readable summary for display |
| payload_json | TEXT NULL | Optional structured metadata (kept minimal) |
| visibility | VARCHAR(16) NOT NULL DEFAULT 'members' | `members` or `workspace` |
| occurred_at | DATETIME(3) NOT NULL | When the event happened |
| create_time | DATETIME DEFAULT NOW() | Record insert time |

## Indexes

```sql
INDEX idx_workspace_timeline (workspace_id, occurred_at DESC)
INDEX idx_target_timeline (target_type, target_id, occurred_at DESC)
INDEX idx_action_type (action_type)
```

## Design Rules

- Append-only: no UPDATE or soft-delete semantics. Records are immutable once written.
- Activity is a product timeline, not an audit log. No sensitive data in payload_json.
- `summary` is a pre-formatted display string; frontend should not need to parse `payload_json` for rendering.
- Activity write failures should be best-effort and must not break core business operations.
- Workspace context is validated on all read endpoints.

## Initial Action Types

| Action Type | Description |
|---|---|
| `workspace.created` | Workspace was created |
| `asset.ingested` | New asset uploaded/ingested |
| `asset.updated` | Asset metadata updated |
| `asset.version.created` | New asset version created |
| `collection.created` | Collection created |
| `collection.item.added` | Asset added to collection |
| `collection.item.removed` | Asset removed from collection |
| `collection.items.reordered` | Collection items reordered |

## Initial Target Types

| Target Type | Description |
|---|---|
| `workspace` | Workspace entity |
| `asset` | Visual asset (legacy picture.id) |
| `collection` | Collection |
| `collection_item` | Collection item |

## Deferred (not in Phase 5)

- `notification_inbox` — notification delivery
- `activity_target` — separate target lookup table
- `audit_log` — compliance audit trail
- `domain_event_outbox` — event sourcing / CQRS

## Rollback

- Do not apply migration.
- Hide ActivityRail and stop writing activity records.
- Existing asset, collection, picture, and workspace features remain unchanged.
