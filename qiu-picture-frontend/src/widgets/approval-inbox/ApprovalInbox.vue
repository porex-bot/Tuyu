<template>
  <div id="approvalInbox">
    <a-spin :spinning="loading">
      <div v-if="error" class="inbox-error">
        <a-result status="error" title="加载失败" :sub-title="error">
          <template #extra>
            <a-button @click="loadInbox">重试</a-button>
          </template>
        </a-result>
      </div>

      <div v-else-if="!loading && items.length === 0" class="inbox-empty">
        <a-empty description="暂无待审批项" />
      </div>

      <div v-else class="inbox-list">
        <ApprovalInboxItem
          v-for="item in items"
          :key="item.approvalId"
          :item="item"
          :workspace-id="workspaceId"
          @decide="$emit('decide')"
          @cancelled="$emit('decide')"
        />
      </div>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { getApprovalInbox } from '@/entities/governance/api/governanceApi'
import ApprovalInboxItem from '@/widgets/approval-inbox/ApprovalInboxItem.vue'
import type { ApprovalRequest } from '@/entities/governance/model/types'

const props = defineProps<{ workspaceId: number }>()
const emit = defineEmits<{ decide: [] }>()

const items = ref<ApprovalRequest[]>([])
const loading = ref(false)
const error = ref<string | null>(null)

async function loadInbox() {
  loading.value = true
  error.value = null
  try {
    const res = await getApprovalInbox(props.workspaceId)
    if (res.data.code === 0 && res.data.data) {
      items.value = res.data.data
    } else {
      error.value = res.data.message || '加载失败'
    }
  } catch (e: any) {
    error.value = e?.message || '网络错误'
  } finally {
    loading.value = false
  }
}

watch(() => props.workspaceId, loadInbox, { immediate: true })
</script>

<style scoped>
.inbox-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.inbox-empty {
  padding: 64px 0;
}
.inbox-error {
  padding: 48px 0;
}
</style>
