/**
 * 工作区 API 适配器 —— 封装 /api/v1/workspaces/* 调用。
 * workspace 组件不应直接导入旧 spaceController.ts。
 */
import request from '@/request'
import type {
  ApiResponse,
  WorkspaceResponse,
  WorkspacePermissionSet,
  WorkspaceUsageResponse,
} from '@/entities/workspace/model/types'

/** 获取当前用户的工作区列表 */
export async function listMyWorkspaces() {
  return request<ApiResponse<WorkspaceResponse[]>>('/api/v1/workspaces/my', {
    method: 'GET',
  })
}

/** 按 ID 获取工作区详情 */
export async function getWorkspaceById(workspaceId: number) {
  return request<ApiResponse<WorkspaceResponse>>(
    `/api/v1/workspaces/${workspaceId}`,
    { method: 'GET' }
  )
}

/** 获取当前用户在工作区中的权限 */
export async function getWorkspacePermissions(workspaceId: number) {
  return request<ApiResponse<WorkspacePermissionSet>>(
    `/api/v1/workspaces/${workspaceId}/permissions`,
    { method: 'GET' }
  )
}

/** 获取工作区用量 */
export async function getWorkspaceUsage(workspaceId: number) {
  return request<ApiResponse<WorkspaceUsageResponse>>(
    `/api/v1/workspaces/${workspaceId}/usage`,
    { method: 'GET' }
  )
}
