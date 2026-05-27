<template>
  <div id="approvalTargetPanel">
    <a-spin :spinning="loading">
      <div v-if="error" class="panel-error">
        <a-alert type="warning" :message="error" banner />
      </div>

      <div v-else class="panel-content">
        <div class="panel-header">
          <span class="panel-title">审批状态</span>
          <a-button size="small" type="primary" @click="submitVisible = true">
            提交审批
          </a-button>
        </div>

        <div v-if="latestApproval" class="approval-status">
          <ApprovalStatusBadge :status="latestApproval.status" />
          <span class="status-detail" v-if="latestApproval.resultMessage">
            — {{ latestApproval.resultMessage }}
          </span>
        </div>
        <div v-else class="no-approval">
          <a-typography-text type="secondary">暂无审批记录</a-typography-text>
        </div>
      </div>
    </a-spin>

    <SubmitApprovalButton
      :visible="submitVisible"
      :workspace-id="workspaceId"
      :target-type="targetType"
      :target-id="targetId"
      @close="submitVisible = false"
      @submitted="onSubmitted"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { getTargetApprovals } from '@/entities/governance/api/governanceApi'
import ApprovalStatusBadge from '@/entities/governance/ui/ApprovalStatusBadge.vue'
import SubmitApprovalButton from '@/features/approval-submit/SubmitApprovalButton.vue'
import type { ApprovalRequest } from '@/entities/governance/model/types'

const props = defineProps<{
  workspaceId: number
  targetType: string
  targetId: number
}>()

const latestApproval = ref<ApprovalRequest | null>(null)
const loading = ref(false)
const error = ref<string | null>(null)
const submitVisible = ref(false)

async function loadStatus() {
  loading.value = true
  error.value = null
  try {
    const res = await getTargetApprovals(props.workspaceId, props.targetType, props.targetId)
    if (res.data.code === 0 && res.data.data && res.data.data.length > 0) {
      latestApproval.value = res.data.data[0]
    } else {
      latestApproval.value = null
    }
  } catch (e: any) {
    error.value = e?.message || '加载审批状态失败'
  } finally {
    loading.value = false
  }
}

function onSubmitted() {
  loadStatus()
}

watch(() => [props.workspaceId, props.targetType, props.targetId] as const, loadStatus, { immediate: true })
</script>

<style scoped>
.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}
.panel-title {
  font-weight: 600;
  font-size: 14px;
}
.approval-status {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 0;
}
.status-detail {
  color: #6B7280;
  font-size: 13px;
}
.no-approval {
  padding: 8px 0;
}
.panel-error {
  padding: 4px 0;
}
</style>
