<template>
  <div class="ai-capability-sidebar">
    <div class="sidebar-title">编辑工具</div>
    <div class="capability-list">
      <div
        v-for="cap in capabilities"
        :key="cap.capabilityKey"
        class="capability-item"
        :class="{ active: selected?.capabilityKey === cap.capabilityKey, disabled: !cap.active }"
        @click="selectCapability(cap)"
      >
        <span class="cap-name">{{ cap.displayName }}</span>
        <span class="cap-desc">{{ cap.description }}</span>
      </div>
    </div>
    <div v-if="capabilities.length === 0 && !loading" class="sidebar-empty">
      <a-empty description="暂无可用工具" :image="aEmptyImage" />
    </div>
    <div v-if="loading" class="sidebar-loading">
      <a-spin />
    </div>
  </div>
</template>

<script setup lang="ts">
import { Empty } from 'ant-design-vue'
import type { AiCapability } from '@/entities/ai/model/types'

defineProps<{
  capabilities: AiCapability[]
  selected: AiCapability | null
  loading: boolean
}>()

const emit = defineEmits<{
  select: [cap: AiCapability]
}>()

const aEmptyImage = Empty.PRESENTED_IMAGE_SIMPLE

function selectCapability(cap: AiCapability) {
  if (cap.active) {
    emit('select', cap)
  }
}
</script>

<style scoped>
.ai-capability-sidebar {
  width: 200px;
  border-right: 1px solid #EEF0F3;
  padding: 16px 0;
  flex-shrink: 0;
  background: #F7F8FA;
}
.sidebar-title {
  font-size: 12px;
  font-weight: 600;
  color: #8A94A6;
  padding: 0 16px 12px;
  letter-spacing: 0.5px;
}
.capability-item {
  padding: 10px 16px;
  cursor: pointer;
  border-left: 3px solid transparent;
  transition: all 0.15s ease;
}
.capability-item:hover {
  background: #E2EFF2;
}
.capability-item.active {
  background: #EEF5F7;
  border-left-color: #3B82A0;
}
.capability-item.disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
.cap-name {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #1F2933;
}
.cap-desc {
  display: block;
  font-size: 12px;
  color: #8A94A6;
  margin-top: 2px;
}
.sidebar-empty,
.sidebar-loading {
  padding: 24px 16px;
  display: flex;
  justify-content: center;
}
</style>
