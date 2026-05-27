/**
 * 视觉资产 API 适配器 —— 封装 /api/v1/workspaces/{workspaceId}/assets/* 调用。
 * asset 组件不应直接导入旧 pictureController.ts。
 */
import request from '@/request'
import type {
  ApiResponse,
  AssetPage,
  AssetDetail,
  AssetVersion,
} from '@/entities/asset/model/types'

/** 工作区范围内的资产卡片搜索 */
export async function searchAssets(
  workspaceId: number,
  query: {
    current?: number
    pageSize?: number
    searchText?: string
    category?: string
    format?: string
    dominantColor?: string
    sortField?: string
    sortOrder?: string
  }
) {
  return request<ApiResponse<AssetPage>>(
    `/api/v1/workspaces/${workspaceId}/assets/search`,
    {
      method: 'POST',
      data: query,
    }
  )
}

/** 获取单个资产详情 */
export async function getAssetDetail(workspaceId: number, assetId: number) {
  return request<ApiResponse<AssetDetail>>(
    `/api/v1/workspaces/${workspaceId}/assets/${assetId}`,
    { method: 'GET' }
  )
}

/** 获取当前用户对指定资产的权限列表 */
export async function getAssetPermissions(workspaceId: number, assetId: number) {
  return request<ApiResponse<string[]>>(
    `/api/v1/workspaces/${workspaceId}/assets/${assetId}/permissions`,
    { method: 'GET' }
  )
}

/** 获取资产的版本列表 */
export async function listAssetVersions(workspaceId: number, assetId: number) {
  return request<ApiResponse<AssetVersion[]>>(
    `/api/v1/workspaces/${workspaceId}/assets/${assetId}/versions`,
    { method: 'GET' }
  )
}
