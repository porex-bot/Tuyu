<template>
  <div class="version-timeline">
    <a-empty
      v-if="versions.length === 0"
      description="暂无版本记录"
      :image="aEmptyImage"
      style="margin: 16px 0"
    />

    <div v-else class="version-list">
      <div
        v-for="(v, idx) in versions"
        :key="v.versionId"
        class="version-item"
        :class="{ 'is-current': v.isCurrent }"
      >
        <div class="version-dot" />
        <div v-if="idx < versions.length - 1" class="version-line" />

        <div class="version-body">
          <div class="version-header">
            <span class="version-no">v{{ v.versionNo }}</span>
            <AssetVersionBadge :version-type="v.versionType" :is-current="v.isCurrent" />
          </div>

          <div class="version-meta">
            <span v-if="v.format" class="meta-item">{{ v.format.toUpperCase() }}</span>
            <span v-if="v.width && v.height" class="meta-item">{{ v.width }}x{{ v.height }}</span>
            <span v-if="v.fileSize" class="meta-item">{{ formatSize(v.fileSize) }}</span>
          </div>

          <div v-if="v.createdAt" class="version-time">{{ v.createdAt }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Empty } from 'ant-design-vue'
import type { AssetVersion } from '@/entities/asset/model/types'
import AssetVersionBadge from './AssetVersionBadge.vue'

defineProps<{
  versions: AssetVersion[]
}>()

const aEmptyImage = Empty.PRESENTED_IMAGE_SIMPLE

function formatSize(bytes: number): string {
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB'
  if (bytes < 1024 * 1024 * 1024) return (bytes / (1024 * 1024)).toFixed(1) + ' MB'
  return (bytes / (1024 * 1024 * 1024)).toFixed(2) + ' GB'
}
</script>

<style scoped>
.version-timeline {
  padding: 8px 0;
}
.version-list {
  position: relative;
  margin-left: 4px;
}
.version-item {
  position: relative;
  display: flex;
  padding-bottom: 12px;
}
.version-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #B6BEC9;
  margin-top: 6px;
  flex-shrink: 0;
}
.version-item.is-current .version-dot {
  background: #3B82A0;
  box-shadow: 0 0 0 3px rgba(59, 130, 160, 0.15);
}
.version-line {
  position: absolute;
  left: 3px;
  top: 20px;
  width: 2px;
  height: calc(100% - 14px);
  background: #EEF0F3;
}
.version-body {
  margin-left: 12px;
  flex: 1;
  min-width: 0;
}
.version-header {
  display: flex;
  align-items: center;
  gap: 8px;
}
.version-no {
  font-weight: 600;
  font-size: 13px;
  color: #1F2933;
}
.version-meta {
  display: flex;
  gap: 8px;
  margin-top: 2px;
}
.meta-item {
  font-size: 11px;
  color: #8A94A6;
}
.version-time {
  font-size: 11px;
  color: #B6BEC9;
  margin-top: 1px;
}
</style>
