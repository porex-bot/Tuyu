/**
 * 视觉资产前端类型定义 —— 对应后端 asset 领域模型。
 * 不依赖旧 API pictureController.ts 自动生成类型。
 */

/** 资产卡片 —— AssetGrid 网格项 */
export interface AssetCard {
  assetId: number
  legacyPictureId: number
  workspaceId: number
  name: string
  thumbnailUrl: string
  url: string
  format: string
  width: number
  height: number
  size: number
  sizeDisplay: string
  dominantColor: string
  lifecycleStatus: string
  reviewStatusText: string
  createdBy: string
  createdAt: string
  updatedAt: string
  permissionList: string[]
}

/** 资产详情 —— Inspector 预览面板 */
export interface AssetDetail {
  assetId: number
  legacyPictureId: number
  workspaceId: number
  name: string
  description: string | null
  category: string | null
  tags: string
  url: string
  thumbnailUrl: string
  width: number
  height: number
  scale: number
  format: string
  size: number
  sizeDisplay: string
  dominantColor: string
  lifecycleStatus: string
  reviewStatusText: string
  reviewMessage: string | null
  metadata: AssetMetadata
  createdBy: string
  createdAt: string
  updatedAt: string
  permissionList: string[]
  currentVersion?: AssetVersion
}

/** 资产版本 */
export interface AssetVersion {
  versionId: number
  assetId: number
  legacyPictureId: number | null
  versionNo: number
  versionType: string
  storageUrl: string | null
  thumbnailUrl: string | null
  width: number | null
  height: number | null
  fileSize: number | null
  format: string | null
  dominantColor: string | null
  createdBy: number | null
  createdAt: string | null
  isCurrent: boolean
}

/** 存储对象引用 */
export interface AssetStorageObject {
  storageObjectId: number
  legacyUrl: string
  storageKey: string | null
  bucket: string | null
  region: string | null
  fileSize: number | null
  contentType: string | null
  width: number | null
  height: number | null
  format: string | null
  dominantColor: string | null
  createdAt: string | null
}

/** 资产元数据 */
export interface AssetMetadata {
  width: number
  height: number
  scale: number
  format: string
  size: number
  sizeDisplay: string
  dominantColor: string
  category: string | null
  tags: string
}

/** 资产分页响应 */
export interface AssetPage {
  records: AssetCard[]
  total: number
  current: number
  pageSize: number
}

/** 资产查询参数 */
export interface AssetQuery {
  workspaceId?: number
  searchText?: string
  category?: string
  tags?: string
  format?: string
  dominantColor?: string
  width?: number
  height?: number
  userId?: number
  lifecycleStatus?: string
  current?: number
  pageSize?: number
  sortField?: string
  sortOrder?: string
}

/** 后端 BaseResponse 包装 */
export interface ApiResponse<T> {
  code: number
  data: T
  message: string
}
