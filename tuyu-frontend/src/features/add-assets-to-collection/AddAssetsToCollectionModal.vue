<template>
  <a-modal
    :visible="visible"
    title="加入集合"
    :confirm-loading="submitting"
    @ok="handleAdd"
    @cancel="$emit('close')"
  >
    <a-form layout="vertical">
      <a-form-item label="选择目标集合">
        <a-select
          v-model:value="selectedCollectionId"
          placeholder="选择集合"
          :loading="loadingCollections"
          style="width: 100%"
        >
          <a-select-option
            v-for="c in collections"
            :key="c.collectionId"
            :value="c.collectionId"
          >
            {{ c.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="备注（可选）">
        <a-input v-model:value="note" placeholder="添加备注说明" :maxlength="512" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { message } from 'ant-design-vue'
import { listCollections, addAssetToCollection } from '@/entities/collection/api/collectionApi'
import type { Collection } from '@/entities/collection/model/types'

const props = defineProps<{
  visible: boolean
  workspaceId: number
  assetId: number | null
}>()

const emit = defineEmits<{
  close: []
  added: []
}>()

const collections = ref<Collection[]>([])
const loadingCollections = ref(false)
const selectedCollectionId = ref<number | null>(null)
const note = ref('')
const submitting = ref(false)

async function loadCollections() {
  loadingCollections.value = true
  try {
    const res = await listCollections(props.workspaceId)
    if (res.data.code === 0 && res.data.data) {
      collections.value = res.data.data
    }
  } catch {
    // silently fail
  } finally {
    loadingCollections.value = false
  }
}

async function handleAdd() {
  if (!selectedCollectionId.value || !props.assetId) {
    message.warning('请选择目标集合')
    return
  }
  submitting.value = true
  try {
    const res = await addAssetToCollection(props.workspaceId, selectedCollectionId.value, {
      assetId: props.assetId,
      note: note.value.trim() || undefined,
    })
    if (res.data.code === 0) {
      message.success('已加入集合')
      selectedCollectionId.value = null
      note.value = ''
      emit('added')
    } else {
      message.error(res.data.message ?? '操作失败')
    }
  } catch {
    message.error('网络错误，请重试')
  } finally {
    submitting.value = false
  }
}

watch(() => props.visible, (v) => {
  if (v) {
    loadCollections()
    selectedCollectionId.value = null
    note.value = ''
  }
})
</script>
