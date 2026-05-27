import { ref } from 'vue'
import type { ActivityTimeline } from '@/entities/activity/model/types'
import { getWorkspaceTimeline, getTargetTimeline } from '@/entities/activity/api/activityApi'

// Singleton state shared across WorkspaceShell and child pages
const visible = ref(false)
const timeline = ref<ActivityTimeline | null>(null)
const loading = ref(false)
const error = ref<string | null>(null)

const scopeTargetType = ref<string | null>(null)
const scopeTargetId = ref<number | null>(null)
const scopeLabel = ref<string | null>(null)

async function loadWorkspaceTimeline(workspaceId: number) {
  loading.value = true
  error.value = null
  try {
    const res = await getWorkspaceTimeline(workspaceId, { offset: 0, limit: 30 })
    if (res.data.code === 0 && res.data.data) {
      timeline.value = res.data.data
    } else {
      error.value = res.data.message ?? '加载失败'
    }
  } catch {
    error.value = '网络错误'
  } finally {
    loading.value = false
  }
}

async function loadTargetTimeline(workspaceId: number, targetType: string, targetId: number) {
  loading.value = true
  error.value = null
  try {
    const res = await getTargetTimeline(workspaceId, targetType, targetId, { offset: 0, limit: 30 })
    if (res.data.code === 0 && res.data.data) {
      timeline.value = res.data.data
    } else {
      error.value = res.data.message ?? '加载失败'
    }
  } catch {
    error.value = '网络错误'
  } finally {
    loading.value = false
  }
}

function setScope(type: string | null, id: number | null, label?: string | null) {
  scopeTargetType.value = type
  scopeTargetId.value = id
  scopeLabel.value = label ?? null
}

function clearScope() {
  scopeTargetType.value = null
  scopeTargetId.value = null
  scopeLabel.value = null
}

function toggle() {
  visible.value = !visible.value
}

export function useActivityRailStore() {
  return {
    visible,
    timeline,
    loading,
    error,
    scopeTargetType,
    scopeTargetId,
    scopeLabel,
    loadWorkspaceTimeline,
    loadTargetTimeline,
    setScope,
    clearScope,
    toggle,
  }
}
