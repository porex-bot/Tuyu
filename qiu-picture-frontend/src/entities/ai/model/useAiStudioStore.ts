/**
 * AiStudio 共享状态 —— 管理当前选中能力、参数草稿和 UI 状态。
 * 采用模块级单例 ref，跨组件共享。
 */
import { ref } from 'vue'
import type { AiCapability, AiJob } from '@/entities/ai/model/types'

// ---- 模块级单例 ----
const selectedCapability = ref<AiCapability | null>(null)
const sourceAssetId = ref<number | null>(null)
const sourceAssetVersionId = ref<number | null>(null)
const parameters = ref<Record<string, unknown>>({})
const selectedJobId = ref<number | null>(null)

// ---- 操作函数 ----

export function useAiStudioStore() {
  function selectCapability(cap: AiCapability | null) {
    selectedCapability.value = cap
    parameters.value = {}
  }

  function setSourceAsset(assetId: number | null, versionId?: number) {
    sourceAssetId.value = assetId
    sourceAssetVersionId.value = versionId ?? null
  }

  function updateParameter(key: string, value: unknown) {
    parameters.value = { ...parameters.value, [key]: value }
  }

  function selectJob(jobId: number | null) {
    selectedJobId.value = jobId
  }

  function reset() {
    selectedCapability.value = null
    sourceAssetId.value = null
    sourceAssetVersionId.value = null
    parameters.value = {}
    selectedJobId.value = null
  }

  return {
    selectedCapability,
    sourceAssetId,
    sourceAssetVersionId,
    parameters,
    selectedJobId,
    selectCapability,
    setSourceAsset,
    updateParameter,
    selectJob,
    reset,
  }
}
