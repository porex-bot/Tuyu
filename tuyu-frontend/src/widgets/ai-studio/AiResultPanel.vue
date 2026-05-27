<template>
  <div v-if="job" class="ai-result-panel">
    <div class="result-header">
      <span class="result-title">结果预览</span>
      <AiJobStatusBadge :status="job.status" />
    </div>

    <template v-if="job.status === 'succeeded' && job.results.length > 0">
      <div v-for="result in job.results" :key="result.resultId" class="result-card">
        <div v-if="result.outputUrl" class="result-image">
          <img :src="result.outputUrl" alt="处理结果" />
        </div>
        <div class="result-actions">
          <a-tag v-if="result.applyStatus === 'applied'" color="#34A853">已应用</a-tag>
          <a-button
            v-else-if="result.applyStatus === 'pending'"
            type="primary"
            size="small"
            @click="handleApply(result.resultId)"
          >
            应用到素材
          </a-button>
          <a-tag v-else color="default">已丢弃</a-tag>
        </div>
      </div>
    </template>

    <template v-else-if="job.status === 'running' || job.status === 'queued'">
      <div class="result-loading">
        <a-spin />
        <span style="margin-left: 8px; color: #8A94A6">处理中…</span>
      </div>
    </template>

    <template v-else-if="job.status === 'failed'">
      <a-result status="error" :title="job.errorMessage ?? '任务失败'" size="small" />
    </template>

    <template v-else-if="job.status === 'created'">
      <a-empty description="等待开始" :image="aEmptyImage" />
    </template>
  </div>
</template>

<script setup lang="ts">
import { Empty, message } from 'ant-design-vue'
import AiJobStatusBadge from '@/entities/ai/ui/AiJobStatusBadge.vue'
import { applyResult } from '@/entities/ai/api/aiApi'
import type { AiJob } from '@/entities/ai/model/types'

const props = defineProps<{
  job: AiJob | null
  workspaceId: number
}>()

const emit = defineEmits<{
  applied: []
}>()

const aEmptyImage = Empty.PRESENTED_IMAGE_SIMPLE

async function handleApply(resultId: number) {
  if (!props.job) return
  try {
    const res = await applyResult(props.workspaceId, props.job.jobId, resultId)
    if (res.data.code === 0) {
      message.success('处理结果已应用为新的素材版本')
      emit('applied')
    } else {
      message.error(res.data.message ?? '应用失败')
    }
  } catch {
    message.error('网络错误，请重试')
  }
}
</script>

<style scoped>
.ai-result-panel {
  border-top: 1px solid #EEF0F3;
  padding: 16px 20px;
}
.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}
.result-title {
  font-size: 13px;
  font-weight: 600;
  color: #1F2933;
}
.result-card {
  margin-bottom: 12px;
  border: 1px solid #E5E7EB;
  border-radius: 8px;
  overflow: hidden;
}
.result-image img {
  width: 100%;
  max-height: 240px;
  object-fit: contain;
  background: #F7F8FA;
}
.result-actions {
  padding: 8px 12px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  border-top: 1px solid #EEF0F3;
}
.result-loading {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 24px;
  color: #8A94A6;
}
</style>
