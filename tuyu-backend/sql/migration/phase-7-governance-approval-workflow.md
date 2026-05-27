# Phase 7: Governance and Approval Workflow

> Additive schema only. Approval workflow is workspace-scoped and policy-driven. Old `picture.reviewStatus` remains untouched; legacy review bridge is optional and non-breaking. Default governance policy mode is `off`.

## New Tables

### approval_request

| Column | Type | Description |
|---|---|---|
| id | BIGINT PK AUTO_INCREMENT | Approval request ID |
| workspace_id | BIGINT NOT NULL | Workspace scope |
| target_type | VARCHAR(32) NOT NULL | `asset`, `collection`, `ai_result` |
| target_id | BIGINT NOT NULL | Target entity ID |
| target_version_id | BIGINT NULL | Optional version of the target |
| request_type | VARCHAR(32) NOT NULL | Action: `publish`, `delete`, `export` |
| status | VARCHAR(32) NOT NULL DEFAULT 'draft' | `draft`, `pending`, `approved`, `rejected`, `changes_requested`, `cancelled` |
| submitted_by | BIGINT NOT NULL | User who submitted |
| submitted_at | DATETIME NULL | When request was submitted |
| resolved_by | BIGINT NULL | User who made final decision |
| resolved_at | DATETIME NULL | When final decision was made |
| reason | VARCHAR(512) NULL | Submission reason |
| result_message | VARCHAR(512) NULL | Final result message |
| created_at | DATETIME DEFAULT NOW() | |
| updated_at | DATETIME DEFAULT NOW() ON UPDATE NOW() | |

### approval_step

| Column | Type | Description |
|---|---|---|
| id | BIGINT PK AUTO_INCREMENT | Step ID |
| approval_id | BIGINT NOT NULL | Parent approval request |
| step_order | INT NOT NULL DEFAULT 1 | Step sequence number |
| reviewer_id | BIGINT NULL | Assigned or acting reviewer |
| status | VARCHAR(32) NOT NULL DEFAULT 'pending' | `pending`, `approved`, `rejected`, `changes_requested`, `skipped` |
| created_at | DATETIME DEFAULT NOW() | |
| updated_at | DATETIME DEFAULT NOW() ON UPDATE NOW() | |

### approval_decision

| Column | Type | Description |
|---|---|---|
| id | BIGINT PK AUTO_INCREMENT | Decision ID |
| step_id | BIGINT NOT NULL | Parent step |
| approval_id | BIGINT NOT NULL | Parent approval request |
| decided_by | BIGINT NOT NULL | User who made decision |
| decision_type | VARCHAR(32) NOT NULL | `approve`, `reject`, `request_changes` |
| comment | VARCHAR(1024) NULL | Reviewer comment |
| created_at | DATETIME DEFAULT NOW() | |

### governance_policy

| Column | Type | Description |
|---|---|---|
| id | BIGINT PK AUTO_INCREMENT | Policy ID |
| workspace_id | BIGINT NOT NULL UNIQUE | Workspace scope |
| mode | VARCHAR(32) NOT NULL DEFAULT 'off' | `off`, `manual`, `auto_for_publication`, `strict` |
| require_approval_for_assets | TINYINT(1) NOT NULL DEFAULT 0 | |
| require_approval_for_collections | TINYINT(1) NOT NULL DEFAULT 0 | |
| require_approval_for_ai_results | TINYINT(1) NOT NULL DEFAULT 0 | |
| auto_approve_trusted_users | TINYINT(1) NOT NULL DEFAULT 0 | |
| updated_by | BIGINT NOT NULL | User who last updated policy |
| created_at | DATETIME DEFAULT NOW() | |
| updated_at | DATETIME DEFAULT NOW() ON UPDATE NOW() | |

## Deferred Tables

```text
content_moderation_case
audit_log
```

## Design Rules

1. Old `picture.reviewStatus` is preserved; legacy review bridge is optional.
2. Default governance policy mode is `off` — no blocking of existing flows.
3. Approval lifecycle: `draft → pending → approved/rejected/changes_requested → cancelled`.
4. Every decision creates an approval_decision record for audit trail.
5. Activity recording is best-effort (try-catch, log warn, never re-throw).

## Seed Data

None required. Governance policy defaults to `off` in application code when no row exists.

## Rollback

- Do not apply `phase-7-create-governance-tables.sql`.
- Hide approval UI routes.
- Keep old `picture.reviewStatus` and `/picture/review` active.
