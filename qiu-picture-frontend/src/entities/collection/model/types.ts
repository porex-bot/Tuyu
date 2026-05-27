/**
 * 集合前端类型定义 —— 对应后端 collection 领域模型。
 */

export interface Collection {
  collectionId: number
  workspaceId: number
  name: string
  description: string | null
  purpose: string
  layout: string
  status: string
  coverAssetId: number | null
  itemCount: number
  createdBy: number | null
  createdAt: string | null
  updatedAt: string | null
}

export interface CollectionItem {
  itemId: number
  collectionId: number
  assetId: number
  assetVersionId: number | null
  sectionId: number | null
  sortOrder: number
  note: string | null
  addedBy: number | null
  addedAt: string | null
}

export interface CollectionSection {
  sectionId: number
  collectionId: number
  name: string
  sortOrder: number
  createdAt: string | null
}

export interface CollectionBoard {
  collectionId: number
  workspaceId: number
  name: string
  description: string | null
  purpose: string | null
  layout: string | null
  status: string | null
  sections: BoardSection[]
  unsorted: BoardSection
}

export interface BoardSection {
  sectionId?: number
  name: string
  sortOrder?: number
  items: CollectionItem[]
}

export interface ApiResponse<T> {
  code: number
  data: T
  message: string
}
