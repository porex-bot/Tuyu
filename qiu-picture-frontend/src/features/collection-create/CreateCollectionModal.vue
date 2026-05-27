<template>
  <a-modal
    :visible="visible"
    title="新建集合"
    :confirm-loading="submitting"
    @ok="handleCreate"
    @cancel="$emit('close')"
  >
    <a-form :model="form" layout="vertical">
      <a-form-item label="集合名称" required>
        <a-input v-model:value="form.name" placeholder="输入集合名称" :maxlength="256" />
      </a-form-item>
      <a-form-item label="描述">
        <a-textarea v-model:value="form.description" placeholder="可选描述" :rows="2" :maxlength="1024" />
      </a-form-item>
      <a-form-item label="用途">
        <a-select v-model:value="form.purpose">
          <a-select-option value="project">项目</a-select-option>
          <a-select-option value="brand">品牌</a-select-option>
          <a-select-option value="campaign">营销活动</a-select-option>
          <a-select-option value="delivery">交付</a-select-option>
          <a-select-option value="reference">参考</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="布局">
        <a-select v-model:value="form.layout">
          <a-select-option value="grid">网格</a-select-option>
          <a-select-option value="board">看板</a-select-option>
          <a-select-option value="moodboard">情绪板</a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import { createCollection } from '@/entities/collection/api/collectionApi'

const props = defineProps<{
  visible: boolean
  workspaceId: number
}>()

const emit = defineEmits<{
  close: []
  created: []
}>()

const submitting = ref(false)

const form = reactive({
  name: '',
  description: '',
  purpose: 'project',
  layout: 'grid',
})

async function handleCreate() {
  if (!form.name.trim()) {
    message.warning('请输入集合名称')
    return
  }
  submitting.value = true
  try {
    const res = await createCollection(props.workspaceId, {
      name: form.name.trim(),
      description: form.description.trim() || undefined,
      purpose: form.purpose,
      layout: form.layout,
    })
    if (res.data.code === 0) {
      message.success('集合创建成功')
      form.name = ''
      form.description = ''
      emit('created')
    } else {
      message.error(res.data.message ?? '创建失败')
    }
  } catch {
    message.error('网络错误，请重试')
  } finally {
    submitting.value = false
  }
}
</script>
