/**
 * 活动 API 适配器 —— 封装 /api/v1/workspaces/{workspaceId}/activities/* 调用。
 */
import request from '@/request'
import type { ApiResponse, ActivityTimeline } from '@/entities/activity/model/types'

export async function getWorkspaceTimeline(
  workspaceId: number,
  params?: { offset?: number; limit?: number }
) {
  return request<ApiResponse<ActivityTimeline>>(
    `/api/v1/workspaces/${workspaceId}/activities`,
    { method: 'GET', params }
  )
}

export async function getTargetTimeline(
  workspaceId: number,
  targetType: string,
  targetId: number,
  params?: { offset?: number; limit?: number }
) {
  return request<ApiResponse<ActivityTimeline>>(
    `/api/v1/workspaces/${workspaceId}/activities/targets/${targetType}/${targetId}`,
    { method: 'GET', params }
  )
}
