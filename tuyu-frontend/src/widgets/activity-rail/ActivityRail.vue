<template>
  <div v-if="visible" class="activity-rail-panel">
    <div class="rail-header">
      <div class="rail-title">
        <span v-if="scopeLabel">活动 · {{ scopeLabel }}</span>
        <span v-else>工作区动态</span>
        <a-tag v-if="scopeLabel" closable size="small" style="margin-left: 8px" @close="emit('clearScope')">
          查看全部
        </a-tag>
      </div>
      <div class="rail-actions">
        <a-button type="text" size="small" :loading="loading" @click="handleRefresh">
          <template #icon><ReloadOutlined /></template>
        </a-button>
        <a-button type="text" size="small" @click="emit('toggle')">
          <template #icon><CloseOutlined /></template>
        </a-button>
      </div>
    </div>

    <div class="rail-body">
      <ActivityTimeline
        :records="filteredRecords"
        :loading="loading"
        :error="error"
        @retry="handleRefresh"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { ReloadOutlined, CloseOutlined } from '@ant-design/icons-vue'
import ActivityTimeline from './ActivityTimeline.vue'
import type { ActivityRecord } from '@/entities/activity/model/types'

const props = defineProps<{
  visible: boolean
  records: ActivityRecord[]
  loading: boolean
  error: string | null
  scopeLabel: string | null
  activeFilter: string
}>()

const emit = defineEmits<{
  toggle: []
  refresh: []
  clearScope: []
}>()

const filteredRecords = computed(() => {
  if (props.activeFilter === 'all') return props.records
  return props.records.filter(r => r.actionType.startsWith(props.activeFilter))
})

function handleRefresh() {
  emit('refresh')
}
</script>

<style scoped>
.activity-rail-panel {
  width: 320px;
  border-left: 1px solid #E5E7EB;
  background: #fff;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}
.rail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #EEF0F3;
  flex-shrink: 0;
}
.rail-title {
  font-size: 14px;
  font-weight: 600;
  color: #1F2933;
  display: flex;
  align-items: center;
}
.rail-actions {
  display: flex;
  gap: 4px;
}
.rail-body {
  flex: 1;
  overflow-y: auto;
  padding: 8px 16px;
}
</style>
