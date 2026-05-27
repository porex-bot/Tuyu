<template>
  <div class="decision-panel">
    <a-divider />
    <a-form layout="vertical">
      <a-form-item label="审批意见">
        <a-textarea
          v-model:value="comment"
          placeholder="可选填写审批意见"
          :rows="2"
          :maxlength="500"
        />
      </a-form-item>
      <a-form-item>
        <a-space>
          <a-button
            type="primary"
            :loading="deciding === 'approve'"
            @click="handleDecide('approve')"
          >
            通过
          </a-button>
          <a-button
            :loading="deciding === 'request_changes'"
            @click="handleDecide('request_changes')"
          >
            要求修改
          </a-button>
          <a-button
            danger
            :loading="deciding === 'reject'"
            @click="handleDecide('reject')"
          >
            驳回
          </a-button>
        </a-space>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { message } from 'ant-design-vue'
import { approveRequest, rejectRequest, requestChanges } from '@/entities/governance/api/governanceApi'

const props = defineProps<{
  workspaceId: number
  approvalId: number
}>()
const emit = defineEmits<{ decided: [] }>()

const comment = ref('')
const deciding = ref<string | null>(null)

async function handleDecide(decisionType: string) {
  deciding.value = decisionType
  try {
    let res
    switch (decisionType) {
      case 'approve':
        res = await approveRequest(props.workspaceId, props.approvalId, comment.value || undefined)
        break
      case 'reject':
        res = await rejectRequest(props.workspaceId, props.approvalId, comment.value || undefined)
        break
      case 'request_changes':
        res = await requestChanges(props.workspaceId, props.approvalId, comment.value || undefined)
        break
    }
    if (res!.data.code === 0) {
      const labels: Record<string, string> = { approve: '已通过', reject: '已驳回', request_changes: '已要求修改' }
      message.success(labels[decisionType] || '操作成功')
      emit('decided')
    } else {
      message.error(res!.data.message || '操作失败')
    }
  } catch (e: any) {
    message.error(e?.message || '网络错误')
  } finally {
    deciding.value = null
  }
}
</script>

<style scoped>
.decision-panel {
  margin-top: 8px;
}
</style>
