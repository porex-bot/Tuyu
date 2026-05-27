<template>
  <div class="asset-toolbar">
    <a-row :wrap="false" align="middle" :gutter="12">
      <a-col flex="auto">
        <a-input-search
          v-model:value="localSearch"
          placeholder="搜索素材名称…"
          :loading="loading"
          allow-clear
          @search="emitSearch"
        />
      </a-col>
      <a-col>
        <a-button
          :type="filterVisible ? 'primary' : 'default'"
          @click="toggleFilter"
        >
          <template #icon><FilterOutlined /></template>
          筛选
        </a-button>
      </a-col>
      <a-col>
        <span class="asset-toolbar-count">{{ total }} 个素材</span>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { FilterOutlined } from '@ant-design/icons-vue'

defineProps<{
  total: number
  loading: boolean
  filterVisible: boolean
}>()

const emit = defineEmits<{
  search: [text: string]
  toggleFilter: []
}>()

const localSearch = ref('')

function emitSearch(text: string) {
  emit('search', text)
}

function toggleFilter() {
  emit('toggleFilter')
}
</script>

<style scoped>
.asset-toolbar {
  padding: 12px 16px;
  background: #fff;
  border-bottom: 1px solid #EEF0F3;
}
.asset-toolbar-count {
  font-size: 13px;
  color: #8A94A6;
  white-space: nowrap;
}
</style>
