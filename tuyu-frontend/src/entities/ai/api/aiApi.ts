/**
 * AI API 适配器 —— 封装 /api/v1/workspaces/{workspaceId}/ai/* 调用。
 */
import request from '@/request'
import type { ApiResponse, AiCapability, AiJob, AiJobResult, CreateAiJobRequest } from '@/entities/ai/model/types'

export async function getCapabilities(workspaceId: number) {
  return request<ApiResponse<AiCapability[]>>(
    `/api/v1/workspaces/${workspaceId}/ai/capabilities`,
    { method: 'GET' }
  )
}

export async function listJobs(
  workspaceId: number,
  params?: { offset?: number; limit?: number; status?: string }
) {
  return request<ApiResponse<AiJob[]>>(
    `/api/v1/workspaces/${workspaceId}/ai/jobs`,
    { method: 'GET', params }
  )
}

export async function createJob(workspaceId: number, data: CreateAiJobRequest) {
  return request<ApiResponse<AiJob>>(
    `/api/v1/workspaces/${workspaceId}/ai/jobs`,
    { method: 'POST', data }
  )
}

export async function getJob(workspaceId: number, jobId: number) {
  return request<ApiResponse<AiJob>>(
    `/api/v1/workspaces/${workspaceId}/ai/jobs/${jobId}`,
    { method: 'GET' }
  )
}

export async function cancelJob(workspaceId: number, jobId: number) {
  return request<ApiResponse<AiJob>>(
    `/api/v1/workspaces/${workspaceId}/ai/jobs/${jobId}/cancel`,
    { method: 'POST' }
  )
}

export async function retryJob(workspaceId: number, jobId: number) {
  return request<ApiResponse<AiJob>>(
    `/api/v1/workspaces/${workspaceId}/ai/jobs/${jobId}/retry`,
    { method: 'POST' }
  )
}

export async function applyResult(workspaceId: number, jobId: number, resultId: number) {
  return request<ApiResponse<AiJobResult>>(
    `/api/v1/workspaces/${workspaceId}/ai/jobs/${jobId}/results/${resultId}/apply`,
    { method: 'POST' }
  )
}
