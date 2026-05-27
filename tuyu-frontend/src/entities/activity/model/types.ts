/**
 * 活动记录前端类型定义 —— 对应后端 activity 领域模型。
 */

export interface ActivityRecord {
  activityId: number
  workspaceId: number
  actor: ActivityActor | null
  actionType: string
  target: ActivityTarget | null
  secondaryTarget: ActivityTarget | null
  summary: string | null
  visibility: string
  occurredAt: string | null
}

export interface ActivityActor {
  userId: number
  userName: string | null
  userAvatar: string | null
}

export interface ActivityTarget {
  targetType: string
  targetId: number
  targetName: string | null
}

export interface ActivityTimeline {
  records: ActivityRecord[]
  total: number
  offset: number
  limit: number
}

export interface ApiResponse<T> {
  code: number
  data: T
  message: string
}
