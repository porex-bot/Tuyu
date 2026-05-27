<template>
  <div class="ai-studio">
    <AiCapabilitySidebar
      :capabilities="capabilities"
      :selected="store.selectedCapability.value"
      :loading="capLoading"
      @select="store.selectCapability"
    />

    <div class="studio-main">
      <div class="studio-header">
        <h3>编辑工具</h3>
        <a-button type="primary" :disabled="!canCreate" :loading="creating" @click="handleCreate">
          创建任务
        </a-button>
      </div>

      <AiInputPanel
        :source-asset-id="store.sourceAssetId.value"
        @clear-asset="store.setSourceAsset(null)"
      />

      <AiParameterPanel
        v-if="store.selectedCapability.value"
        :capability-key="store.selectedCapability.value.capabilityKey"
        :params="store.parameters.value"
        @update-param="store.updateParameter"
      />

      <AiJobQueue
        :jobs="jobs"
        :loading="jobLoading"
        :workspace-id="workspaceId"
        @select-job="store.selectJob"
        @refresh="loadJobs"
      />

      <AiResultPanel
        v-if="selectedJob"
        :job="selectedJob"
        :workspace-id="workspaceId"
        @applied="loadJobs"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { useAiStudioStore } from '@/entities/ai/model/useAiStudioStore'
import { getCapabilities, listJobs, createJob } from '@/entities/ai/api/aiApi'
import type { AiCapability, AiJob } from '@/entities/ai/model/types'
import AiCapabilitySidebar from './AiCapabilitySidebar.vue'
import AiInputPanel from './AiInputPanel.vue'
import AiParameterPanel from './AiParameterPanel.vue'
import AiJobQueue from './AiJobQueue.vue'
import AiResultPanel from './AiResultPanel.vue'

const props = defineProps<{
  workspaceId: number
}>()

const store = useAiStudioStore()

const capabilities = ref<AiCapability[]>([])
const capLoading = ref(false)
const jobs = ref<AiJob[]>([])
const jobLoading = ref(false)
const creating = ref(false)

const canCreate = computed(() =>
  store.selectedCapability.value != null && store.sourceAssetId.value != null
)

const selectedJob = computed(() =>
  store.selectedJobId.value
    ? jobs.value.find(j => j.jobId === store.selectedJobId.value) ?? null
    : null
)

async function loadCapabilities() {
  capLoading.value = true
  try {
    const res = await getCapabilities(props.workspaceId)
    if (res.data.code === 0) {
      capabilities.value = res.data.data ?? []
    }
  } catch {
    // ignore
  } finally {
    capLoading.value = false
  }
}

async function loadJobs() {
  jobLoading.value = true
  try {
    const res = await listJobs(props.workspaceId, { offset: 0, limit: 30 })
    if (res.data.code === 0) {
      jobs.value = res.data.data ?? []
    }
  } catch {
    // ignore
  } finally {
    jobLoading.value = false
  }
}

async function handleCreate() {
  if (!canCreate.value) return
  creating.value = true
  try {
    const res = await createJob(props.workspaceId, {
      capabilityKey: store.selectedCapability.value!.capabilityKey,
      sourceAssetId: store.sourceAssetId.value ?? undefined,
      sourceAssetVersionId: store.sourceAssetVersionId.value ?? undefined,
      parameters: store.parameters.value,
      idempotencyKey: `studio-${Date.now()}`,
    })
    if (res.data.code === 0) {
      message.success('处理任务已创建')
      store.reset()
      await loadJobs()
    } else {
      message.error(res.data.message ?? '创建失败')
    }
  } catch {
    message.error('网络错误，请重试')
  } finally {
    creating.value = false
  }
}

watch(() => props.workspaceId, () => {
  store.reset()
  loadCapabilities()
  loadJobs()
}, { immediate: true })
</script>

<style scoped>
.ai-studio {
  display: flex;
  height: 100%;
  min-height: calc(100vh - 120px);
  background: #fff;
  border: 1px solid #E5E7EB;
  border-radius: 8px;
}
.studio-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}
.studio-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #EEF0F3;
}
.studio-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #1F2933;
}
</style>
