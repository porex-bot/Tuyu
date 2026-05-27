/**
 * AI 任务前端类型定义 —— 对应后端 ai 领域模型。
 */

export interface AiCapability {
  capabilityKey: string
  displayName: string
  description: string | null
  provider: string
  active: boolean
}

export interface AiJob {
  jobId: number
  workspaceId: number
  creatorUserId: number
  capabilityKey: string
  status: string
  sourceAssetId: number | null
  sourceAssetVersionId: number | null
  provider: string | null
  parametersJson: string | null
  idempotencyKey: string | null
  errorCode: string | null
  errorMessage: string | null
  createdAt: string | null
  startedAt: string | null
  finishedAt: string | null
  results: AiJobResult[]
}

export interface AiJobResult {
  resultId: number
  jobId: number
  resultType: string
  outputUrl: string | null
  applyStatus: string
  assetVersionId: number | null
  createdAt: string | null
}

export interface CreateAiJobRequest {
  capabilityKey: string
  sourceAssetId?: number
  sourceAssetVersionId?: number
  parameters?: Record<string, unknown>
  idempotencyKey?: string
}

export interface ApiResponse<T> {
  code: number
  data: T
  message: string
}
