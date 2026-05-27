/**
 * 工作区前端类型定义 —— 对应后端 workspace 领域模型。
 * 不依赖旧 API.SpaceVO / API.SpaceUserVO 等自动生成类型。
 */

/** 工作区读模型 */
export interface Workspace {
  workspaceId: number
  legacySpaceId: number
  name: string
  type: number
  level: number
  userId: number
  createTime: string
  editTime: string
  updateTime: string
}

/** 工作区成员读模型 */
export interface WorkspaceMember {
  id: number
  workspaceId: number
  userId: number
  role: string
  createTime: string
}

/** 工作区权限集 */
export interface WorkspacePermissionSet {
  workspaceId: number
  role: string
  permissions: string[]
}

/** 工作区用量 */
export interface WorkspaceUsage {
  workspaceId: number
  maxSize: number
  totalSize: number
  maxCount: number
  totalCount: number
  storageRatio: number
  countRatio: number
}

/** 用户简要信息（跨实体复用） */
export interface UserBrief {
  id: number
  userName: string
  userAvatar: string
}

/** 工作区 API 响应 */
export interface WorkspaceResponse {
  workspace: Workspace
  user: UserBrief
  permissionList: string[]
}

/** 工作区成员 API 响应 */
export interface WorkspaceMemberResponse {
  member: WorkspaceMember
  user: UserBrief
}

/** 后端 BaseResponse 包装 */
export interface ApiResponse<T> {
  code: number
  data: T
  message: string
}

/** 工作区用量 API 响应 */
export interface WorkspaceUsageResponse {
  workspaceId: number
  maxSize: number
  totalSize: number
  maxCount: number
  totalCount: number
  storageRatio: number
  countRatio: number
}
