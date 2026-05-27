/** 审批步骤 */
export interface ApprovalStep {
  stepId: number
  approvalId: number
  stepOrder: number
  reviewerId: number | null
  status: string
  createdAt: string | null
}

/** 审批决策 */
export interface ApprovalDecision {
  decisionId: number
  stepId: number | null
  approvalId: number
  decidedBy: number
  decisionType: string
  comment: string | null
  createdAt: string | null
}

/** 审批请求 */
export interface ApprovalRequest {
  approvalId: number
  workspaceId: number
  targetType: string
  targetId: number
  targetVersionId: number | null
  requestType: string
  status: string
  submittedBy: number
  submittedAt: string | null
  resolvedBy: number | null
  resolvedAt: string | null
  reason: string | null
  resultMessage: string | null
  createdAt: string | null
  steps: ApprovalStep[]
  decisions: ApprovalDecision[]
}

/** 治理策略 */
export interface GovernancePolicy {
  policyId: number | null
  workspaceId: number
  mode: string
  requireApprovalForAssets: number
  requireApprovalForCollections: number
  requireApprovalForAiResults: number
  autoApproveTrustedUsers: number
  createdBy: number | null
  updatedBy: number | null
  createdAt: string | null
  updatedAt: string | null
}

/** 后端 BaseResponse 包装 */
export interface ApiResponse<T> {
  code: number
  data: T
  message: string
}
