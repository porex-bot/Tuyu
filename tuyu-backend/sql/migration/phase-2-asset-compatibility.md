# Phase 2: Asset Compatibility Foundation

> Planning document — no executable content. Runtime continues using `picture` table.

## Scope

### Included

- Asset domain read models (`AssetView`, `AssetMetadataView`, `AssetUsageView`, `AssetLifecycleStatus`)
- Asset API response models (`AssetCardResponse`, `AssetDetailResponse`, `AssetMetadataResponse`, `AssetPageResponse`)
- Asset query request models (`AssetQuery`, `AssetPageQuery`, `AssetFilterQuery`)
- `PictureAssetLegacyAdapter` converting `Picture` / `PictureVO` into asset responses
- Asset application services (`AssetQueryApplicationService`, `AssetPermissionApplicationService`)
- `AssetController` v1 read APIs under `/api/v1/workspaces/{workspaceId}/assets/*`
- Frontend asset types, API adapter, query state, AssetGrid components
- `WorkspaceAssetsPage` rendering AssetGrid at `/w/:workspaceId/assets`
- Backend unit tests for adapter and application services

### Explicitly Excluded (Deferred to Phase 3+)

- `visual_asset` table creation
- `asset_version` table creation
- Storage reference / CDN migration tables
- Object storage URL migration
- Asset upload / mutation APIs (create, update, delete)
- Asset versioning or revision history
- `AssetInspector` advanced tabs (metadata, versions, activity)
- `CollectionBoard` or board/collection views
- `AI Studio` integration
- Schema migration of any kind
- Any executable SQL

## Source of Truth

The `picture` table remains the **only** source of truth during Phase 2.
All asset read models are computed views over existing `Picture` rows.
No new tables are required for runtime.

## Old-to-New Field Mapping

| Old (Picture entity) | New (Asset model) | Notes |
|---|---|---|
| `id` | `assetId` | Primary identifier |
| `id` | `legacyPictureId` | Compatibility bridge |
| `url` | `url` | Unchanged |
| `thumbnailUrl` | `thumbnailUrl` | Unchanged |
| `name` | `name` | Unchanged |
| `introduction` | `description` | Semantic rename |
| `category` | `category` | Unchanged |
| `tags` | `tags` | Unchanged (comma-separated string) |
| `picSize` | `size` | Drop `pic` prefix |
| `picWidth` | `width` | Drop `pic` prefix |
| `picHeight` | `height` | Drop `pic` prefix |
| `picScale` | `scale` | Drop `pic` prefix |
| `picFormat` | `format` | Drop `pic` prefix |
| `picColor` | `dominantColor` | Semantic rename |
| `userId` | `createdBy` | Semantic rename |
| `spaceId` | `workspaceId` | Align with Phase 1 workspace vocabulary |
| `reviewStatus` | `lifecycleStatus` + `reviewStatusText` | Enum → status enum + display text |
| `createTime` | `createdAt` | Consistent timestamp naming |
| `editTime` / `updateTime` | `updatedAt` | Latest of edit/update |

## Permission Behavior

Asset API delegates to existing `SpaceUserAuthManager` using legacy `spaceId`.
No new permission rules are introduced in Phase 2.
Same user + same space = same visible pictures as old `/picture/*` APIs.

## Rollback Rule

Disabling new Asset APIs (`AssetController` removal) restores old-only behavior.
No data migration to undo. No tables to drop.

## Compatibility Guarantees

- Old `PictureController` and all `/picture/*` endpoints remain unchanged
- Old `PictureService` and `PictureServiceImpl` remain unchanged
- Old picture pages (SpaceDetailPage, PictureDetailPage, admin/PictureManagePage) remain functional
- No destructive write to `picture` table
- Application starts without creating any new tables
