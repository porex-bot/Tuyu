import request from '@/request'
import type {
  ApiResponse,
  ApprovalRequest,
  GovernancePolicy,
} from '@/entities/governance/model/types'

/** 获取审批收件箱 */
export async function getApprovalInbox(
  workspaceId: number,
  offset: number = 0,
  limit: number = 20
) {
  return request<ApiResponse<ApprovalRequest[]>>(
    `/api/v1/workspaces/${workspaceId}/approvals/inbox?offset=${offset}&limit=${limit}`,
    { method: 'GET' }
  )
}

/** 获取指定目标的审批记录 */
export async function getTargetApprovals(
  workspaceId: number,
  targetType: string,
  targetId: number
) {
  return request<ApiResponse<ApprovalRequest[]>>(
    `/api/v1/workspaces/${workspaceId}/approvals/targets/${targetType}/${targetId}`,
    { method: 'GET' }
  )
}

/** 提交审批请求 */
export async function submitApproval(
  workspaceId: number,
  data: {
    targetType: string
    targetId: number
    targetVersionId?: number
    requestType: string
    reason?: string
  }
) {
  return request<ApiResponse<ApprovalRequest>>(
    `/api/v1/workspaces/${workspaceId}/approvals`,
    { method: 'POST', data }
  )
}

/** 通过审批 */
export async function approveRequest(
  workspaceId: number,
  approvalId: number,
  comment?: string
) {
  return request<ApiResponse<ApprovalRequest>>(
    `/api/v1/workspaces/${workspaceId}/approvals/${approvalId}/approve`,
    { method: 'POST', data: { comment } }
  )
}

/** 驳回审批 */
export async function rejectRequest(
  workspaceId: number,
  approvalId: number,
  comment?: string
) {
  return request<ApiResponse<ApprovalRequest>>(
    `/api/v1/workspaces/${workspaceId}/approvals/${approvalId}/reject`,
    { method: 'POST', data: { comment } }
  )
}

/** 要求修改 */
export async function requestChanges(
  workspaceId: number,
  approvalId: number,
  comment?: string
) {
  return request<ApiResponse<ApprovalRequest>>(
    `/api/v1/workspaces/${workspaceId}/approvals/${approvalId}/request-changes`,
    { method: 'POST', data: { comment } }
  )
}

/** 取消审批请求 */
export async function cancelApproval(
  workspaceId: number,
  approvalId: number
) {
  return request<ApiResponse<ApprovalRequest>>(
    `/api/v1/workspaces/${workspaceId}/approvals/${approvalId}/cancel`,
    { method: 'POST' }
  )
}

/** 获取治理策略 */
export async function getGovernancePolicy(workspaceId: number) {
  return request<ApiResponse<GovernancePolicy>>(
    `/api/v1/workspaces/${workspaceId}/governance/policy`,
    { method: 'GET' }
  )
}

/** 更新治理策略 */
export async function updateGovernancePolicy(
  workspaceId: number,
  data: Partial<GovernancePolicy>
) {
  return request<ApiResponse<GovernancePolicy>>(
    `/api/v1/workspaces/${workspaceId}/governance/policy`,
    { method: 'PUT', data }
  )
}
