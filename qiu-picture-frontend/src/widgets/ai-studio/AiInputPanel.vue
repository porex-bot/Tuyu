<template>
  <div class="ai-input-panel">
    <div class="panel-label">源素材</div>
    <div v-if="sourceAssetId">
      <a-tag closable @close="$emit('clearAsset')">素材 #{{ sourceAssetId }}</a-tag>
    </div>
    <div v-else class="no-asset">未选择源素材</div>
    <div class="panel-hint">来源素材将在创建任务时作为输入传递给编辑工具。</div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  sourceAssetId: number | null
}>()

defineEmits<{
  clearAsset: []
}>()
</script>

<style scoped>
.ai-input-panel {
  padding: 16px 20px;
  border-bottom: 1px solid #EEF0F3;
}
.panel-label {
  font-size: 13px;
  font-weight: 600;
  color: #1F2933;
  margin-bottom: 8px;
}
.no-asset {
  font-size: 13px;
  color: #8A94A6;
}
.panel-hint {
  font-size: 12px;
  color: #8A94A6;
  margin-top: 8px;
}
</style>
