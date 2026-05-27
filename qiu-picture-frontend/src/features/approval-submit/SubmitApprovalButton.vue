<template>
  <a-modal
    :visible="visible"
    title="提交审批"
    :confirm-loading="submitting"
    @ok="handleSubmit"
    @cancel="$emit('close')"
  >
    <a-form layout="vertical">
      <a-form-item label="目标类型">
        <a-input :value="targetTypeLabel" disabled />
      </a-form-item>
      <a-form-item label="目标 ID">
        <a-input :value="targetId" disabled />
      </a-form-item>
      <a-form-item label="申请类型">
        <a-select v-model:value="requestType" placeholder="选择申请类型">
          <a-select-option value="publish">申请发布</a-select-option>
          <a-select-option value="review">申请审核</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="申请原因" required>
        <a-textarea
          v-model:value="reason"
          placeholder="请说明提交审批的原因"
          :rows="3"
          :maxlength="500"
        />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { message } from 'ant-design-vue'
import { submitApproval } from '@/entities/governance/api/governanceApi'
import { getTargetTypeLabel } from '@/entities/governance/model/approvalLabels'

const props = defineProps<{
  visible: boolean
  workspaceId: number
  targetType: string
  targetId: number
}>()
const emit = defineEmits<{
  close: []
  submitted: []
}>()

const reason = ref('')
const requestType = ref('publish')
const submitting = ref(false)

const targetTypeLabel = computed(() => getTargetTypeLabel(props.targetType))

watch(() => props.visible, (v) => {
  if (v) {
    reason.value = ''
    requestType.value = 'publish'
  }
})

async function handleSubmit() {
  if (!reason.value.trim()) {
    message.warning('请填写申请原因')
    return
  }
  submitting.value = true
  try {
    const res = await submitApproval(props.workspaceId, {
      targetType: props.targetType,
      targetId: props.targetId,
      requestType: requestType.value,
      reason: reason.value,
    })
    if (res.data.code === 0) {
      message.success('审批请求已提交')
      emit('submitted')
      emit('close')
    } else {
      message.error(res.data.message || '提交失败')
    }
  } catch (e: any) {
    message.error(e?.message || '网络错误')
  } finally {
    submitting.value = false
  }
}
</script>
