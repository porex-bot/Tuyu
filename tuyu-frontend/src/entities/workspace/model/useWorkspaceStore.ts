import { ref } from 'vue'
import { defineStore } from 'pinia'
import {
  getWorkspaceById,
  getWorkspacePermissions,
  getWorkspaceUsage,
} from '@/entities/workspace/api/workspaceApi'
import type {
  WorkspaceResponse,
  WorkspacePermissionSet,
  WorkspaceUsageResponse,
} from '@/entities/workspace/model/types'

/**
 * 工作区状态管理 —— 集中持有当前工作区、权限和用量。
 * 不与 useLoginUserStore 合并，避免循环依赖。
 */
export const useWorkspaceStore = defineStore('workspace', () => {
  const currentWorkspace = ref<WorkspaceResponse | null>(null)
  const permissions = ref<WorkspacePermissionSet | null>(null)
  const usage = ref<WorkspaceUsageResponse | null>(null)
  const loading = ref(false)

  /**
   * 加载工作区及其权限和用量
   */
  async function loadWorkspace(workspaceId: number) {
    loading.value = true
    try {
      const [wsRes, permRes, usageRes] = await Promise.all([
        getWorkspaceById(workspaceId),
        getWorkspacePermissions(workspaceId),
        getWorkspaceUsage(workspaceId),
      ])

      if (wsRes.data.code === 0 && wsRes.data.data) {
        currentWorkspace.value = wsRes.data.data
      }
      if (permRes.data.code === 0 && permRes.data.data) {
        permissions.value = permRes.data.data
      }
      if (usageRes.data.code === 0 && usageRes.data.data) {
        usage.value = usageRes.data.data
      }
    } finally {
      loading.value = false
    }
  }

  /**
   * 清除当前工作区状态
   */
  function clearWorkspace() {
    currentWorkspace.value = null
    permissions.value = null
    usage.value = null
    loading.value = false
  }

  return { currentWorkspace, permissions, usage, loading, loadWorkspace, clearWorkspace }
})
