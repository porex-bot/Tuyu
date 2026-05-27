<template>
  <div class="ai-job-queue">
    <div class="queue-header">
      <span class="queue-title">任务历史</span>
      <a-button type="text" size="small" @click="$emit('refresh')">
        <template #icon><ReloadOutlined /></template>
      </a-button>
    </div>

    <div v-if="loading" class="queue-loading">
      <a-spin />
    </div>

    <div v-else-if="jobs.length === 0" class="queue-empty">
      <a-empty description="暂无处理任务" :image="aEmptyImage" />
    </div>

    <div v-else class="queue-list">
      <div
        v-for="job in jobs"
        :key="job.jobId"
        class="queue-item"
        :class="{ active: selectedJobId === job.jobId }"
        @click="$emit('selectJob', job.jobId)"
      >
        <div class="queue-item-top">
          <AiJobStatusBadge :status="job.status" />
          <span class="queue-item-cap">{{ capabilityLabel(job.capabilityKey) }}</span>
        </div>
        <div class="queue-item-meta">
          <span class="queue-item-time">{{ formatTime(job.createdAt) }}</span>
          <span v-if="job.errorMessage" class="queue-item-error">{{ job.errorMessage }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Empty } from 'ant-design-vue'
import { ReloadOutlined } from '@ant-design/icons-vue'
import AiJobStatusBadge from '@/entities/ai/ui/AiJobStatusBadge.vue'
import { getCapabilityLabel } from '@/entities/ai/model/aiLabels'
import type { AiJob } from '@/entities/ai/model/types'

defineProps<{
  jobs: AiJob[]
  loading: boolean
  selectedJobId?: number | null
}>()

defineEmits<{
  selectJob: [jobId: number]
  refresh: []
}>()

const aEmptyImage = Empty.PRESENTED_IMAGE_SIMPLE

function capabilityLabel(key: string) {
  return getCapabilityLabel(key)
}

function formatTime(time: string | null) {
  if (!time) return ''
  const d = new Date(time)
  const now = new Date()
  const diffMs = now.getTime() - d.getTime()
  const diffMin = Math.floor(diffMs / 60000)
  if (diffMin < 1) return '刚刚'
  if (diffMin < 60) return `${diffMin} 分钟前`
  const diffHr = Math.floor(diffMin / 60)
  if (diffHr < 24) return `${diffHr} 小时前`
  return d.toLocaleDateString('zh-CN')
}
</script>

<style scoped>
.ai-job-queue {
  border-top: 1px solid #EEF0F3;
  flex: 1;
}
.queue-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
}
.queue-title {
  font-size: 13px;
  font-weight: 600;
  color: #1F2933;
}
.queue-loading,
.queue-empty {
  padding: 24px;
  display: flex;
  justify-content: center;
}
.queue-list {
  max-height: 300px;
  overflow-y: auto;
}
.queue-item {
  padding: 10px 20px;
  cursor: pointer;
  border-bottom: 1px solid #EEF0F3;
  transition: background 0.15s ease;
}
.queue-item:hover {
  background: #F7F8FA;
}
.queue-item.active {
  background: #EEF5F7;
}
.queue-item-top {
  display: flex;
  align-items: center;
  gap: 8px;
}
.queue-item-cap {
  font-size: 13px;
  color: #1F2933;
}
.queue-item-meta {
  margin-top: 4px;
  font-size: 12px;
  color: #8A94A6;
}
.queue-item-error {
  color: #E05555;
  margin-left: 8px;
}
</style>
