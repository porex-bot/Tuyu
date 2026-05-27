<template>
  <div id="workspaceHomePage">
    <a-spin :spinning="workspaceStore.loading">
      <template v-if="workspaceStore.currentWorkspace">
        <a-page-header
          :title="workspaceStore.currentWorkspace.workspace.name"
          sub-title="工作区首页"
        />

        <a-row :gutter="24" style="margin-top: 16px">
          <!-- 用量卡片 -->
          <a-col :span="12">
            <a-card title="存储用量">
              <template v-if="usage">
                <div class="usage-row">
                  <span>已用</span>
                  <span>{{ formatSize(usage.totalSize) }} / {{ formatSize(usage.maxSize) }}</span>
                </div>
                <a-progress
                  :percent="Math.round(usage.storageRatio * 100)"
                  :status="usage.storageRatio > 0.9 ? 'exception' : 'active'"
                />
              </template>
              <span v-else class="ws-dim">加载中…</span>
            </a-card>
          </a-col>

          <a-col :span="12">
            <a-card title="图片数量">
              <template v-if="usage">
                <div class="usage-row">
                  <span>已用</span>
                  <span>{{ usage.totalCount }} / {{ usage.maxCount }}</span>
                </div>
                <a-progress
                  :percent="Math.round(usage.countRatio * 100)"
                  :status="usage.countRatio > 0.9 ? 'exception' : 'active'"
                />
              </template>
              <span v-else class="ws-dim">加载中…</span>
            </a-card>
          </a-col>
        </a-row>

        <!-- 权限概要 -->
        <a-card title="我的权限" style="margin-top: 16px">
          <a-tag
            v-for="perm in permissionsList"
            :key="perm"
            color="#6B8EA4"
            style="margin: 4px"
          >
            {{ perm }}
          </a-tag>
          <span v-if="permissionsList.length === 0" class="ws-dim">无权限</span>
        </a-card>
      </template>

      <!-- 无数据 -->
      <a-empty
        v-else-if="!workspaceStore.loading"
        description="工作区数据加载失败"
      />
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useWorkspaceStore } from '@/entities/workspace/model/useWorkspaceStore'

const workspaceStore = useWorkspaceStore()

const usage = computed(() => workspaceStore.usage)
const permissionsList = computed(() => workspaceStore.permissions?.permissions ?? [])

function formatSize(bytes: number): string {
  if (!bytes || bytes <= 0) return '0 B'
  const units = ['B', 'KB', 'MB', 'GB']
  const i = Math.min(units.length - 1, Math.floor(Math.log(bytes) / Math.log(1024)))
  return (bytes / Math.pow(1024, i)).toFixed(i > 0 ? 1 : 0) + ' ' + units[i]
}
</script>

<style scoped>
#workspaceHomePage {
  max-width: 960px;
}

.usage-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  color: #6B7280;
  font-size: 14px;
}

.ws-dim {
  color: #B6BEC9;
}
</style>
