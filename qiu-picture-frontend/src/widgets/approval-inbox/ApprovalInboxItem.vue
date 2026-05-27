<template>
  <a-card :id="`approvalInboxItem-${item.approvalId}`" class="inbox-item">
    <template #title>
      <div class="item-title">
        <a-tag>{{ targetTypeLabel }}</a-tag>
        <span class="item-id">#{{ item.approvalId }}</span>
        <ApprovalStatusBadge :status="item.status" />
      </div>
    </template>

    <template #extra>
      <a-space>
        <a-button size="small" @click="showDecision = !showDecision">
          {{ showDecision ? '收起' : '审批' }}
        </a-button>
        <a-button size="small" danger @click="handleCancel" :loading="cancelling">
          取消
        </a-button>
      </a-space>
    </template>

    <div class="item-body">
      <div class="item-meta">
        <span class="meta-label">提交原因：</span>
        <span>{{ item.reason || '未填写' }}</span>
      </div>
      <div class="item-meta">
        <span class="meta-label">提交时间：</span>
        <span>{{ item.submittedAt || '-' }}</span>
      </div>
      <div class="item-meta" v-if="item.targetVersionId">
        <span class="meta-label">目标版本：</span>
        <span>{{ item.targetVersionId }}</span>
      </div>
    </div>

    <ApprovalDecisionPanel
      v-if="showDecision"
      :workspace-id="workspaceId"
      :approval-id="item.approvalId"
      @decided="onDecided"
    />
  </a-card>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { message } from 'ant-design-vue'
import { cancelApproval } from '@/entities/governance/api/governanceApi'
import { getTargetTypeLabel } from '@/entities/governance/model/approvalLabels'
import ApprovalStatusBadge from '@/entities/governance/ui/ApprovalStatusBadge.vue'
import ApprovalDecisionPanel from '@/features/approval-decision/ApprovalDecisionPanel.vue'
import type { ApprovalRequest } from '@/entities/governance/model/types'

const props = defineProps<{
  item: ApprovalRequest
  workspaceId: number
}>()
const emit = defineEmits<{
  decide: []
  cancelled: []
}>()

const showDecision = ref(false)
const cancelling = ref(false)

const targetTypeLabel = computed(() => getTargetTypeLabel(props.item.targetType))

async function handleCancel() {
  cancelling.value = true
  try {
    const res = await cancelApproval(props.workspaceId, props.item.approvalId)
    if (res.data.code === 0) {
      message.success('已取消审批')
      emit('cancelled')
    } else {
      message.error(res.data.message || '取消失败')
    }
  } catch (e: any) {
    message.error(e?.message || '网络错误')
  } finally {
    cancelling.value = false
  }
}

function onDecided() {
  showDecision.value = false
  emit('decide')
}
</script>

<style scoped>
.inbox-item {
  margin-bottom: 0;
}
.item-title {
  display: flex;
  align-items: center;
  gap: 8px;
}
.item-id {
  color: #8A94A6;
  font-size: 13px;
}
.item-body {
  margin-bottom: 12px;
}
.item-meta {
  margin-bottom: 4px;
  font-size: 13px;
  color: #6B7280;
}
.meta-label {
  font-weight: 500;
  color: #1F2933;
}
</style>
