/**
 * 集合 API 适配器 —— 封装 /api/v1/workspaces/{workspaceId}/collections/* 调用。
 */
import request from '@/request'
import type { ApiResponse, Collection, CollectionBoard, CollectionItem } from '@/entities/collection/model/types'

export async function listCollections(workspaceId: number) {
  return request<ApiResponse<Collection[]>>(
    `/api/v1/workspaces/${workspaceId}/collections`,
    { method: 'GET' }
  )
}

export async function createCollection(workspaceId: number, data: { name: string; description?: string; purpose?: string; layout?: string }) {
  return request<ApiResponse<Collection>>(
    `/api/v1/workspaces/${workspaceId}/collections`,
    { method: 'POST', data }
  )
}

export async function getCollection(workspaceId: number, collectionId: number) {
  return request<ApiResponse<Collection>>(
    `/api/v1/workspaces/${workspaceId}/collections/${collectionId}`,
    { method: 'GET' }
  )
}

export async function getCollectionBoard(workspaceId: number, collectionId: number) {
  return request<ApiResponse<CollectionBoard>>(
    `/api/v1/workspaces/${workspaceId}/collections/${collectionId}/board`,
    { method: 'GET' }
  )
}

export async function addAssetToCollection(
  workspaceId: number,
  collectionId: number,
  data: { assetId: number; assetVersionId?: number; sectionId?: number; note?: string }
) {
  return request<ApiResponse<CollectionItem>>(
    `/api/v1/workspaces/${workspaceId}/collections/${collectionId}/items`,
    { method: 'POST', data }
  )
}

export async function removeItemFromCollection(workspaceId: number, collectionId: number, itemId: number) {
  return request<ApiResponse<boolean>>(
    `/api/v1/workspaces/${workspaceId}/collections/${collectionId}/items/${itemId}`,
    { method: 'DELETE' }
  )
}

export async function reorderCollectionItems(
  workspaceId: number,
  collectionId: number,
  orders: { itemId: number; sortOrder: number }[]
) {
  return request<ApiResponse<boolean>>(
    `/api/v1/workspaces/${workspaceId}/collections/${collectionId}/items/reorder`,
    { method: 'POST', data: { orders } }
  )
}
