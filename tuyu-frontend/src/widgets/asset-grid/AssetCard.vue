<template>
  <div class="asset-card" @click="$emit('click', card)">
    <div class="asset-card-thumb">
      <AssetThumbnail
        :src="card.thumbnailUrl ?? card.url"
        :alt="card.name"
        :format="card.format"
        :dominant-color="card.dominantColor"
      />
      <div class="asset-card-overlay">
        <a-tooltip title="加入集合">
          <a-button
            shape="circle"
            size="small"
            class="add-collection-btn"
            @click.stop="$emit('addToCollection', card)"
          >
            <template #icon><PlusOutlined /></template>
          </a-button>
        </a-tooltip>
      </div>
    </div>
    <div class="asset-card-body">
      <div class="asset-card-name">{{ card.name }}</div>
      <div class="asset-card-meta">
        <span>{{ card.sizeDisplay }}</span>
        <AssetStatusBadge
          :lifecycle-status="card.lifecycleStatus"
          :review-status-text="card.reviewStatusText"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { PlusOutlined } from '@ant-design/icons-vue'
import type { AssetCard } from '@/entities/asset/model/types'
import AssetThumbnail from '@/entities/asset/ui/AssetThumbnail.vue'
import AssetStatusBadge from '@/entities/asset/ui/AssetStatusBadge.vue'

defineProps<{
  card: AssetCard
}>()

defineEmits<{
  click: [card: AssetCard]
  addToCollection: [card: AssetCard]
}>()
</script>

<style scoped>
.asset-card {
  border-radius: 8px;
  overflow: hidden;
  background: #fff;
  border: 1px solid #EEF0F3;
  cursor: pointer;
  transition: all 0.15s ease;
}
.asset-card:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transform: translateY(-1px);
}
.asset-card-thumb {
  position: relative;
}
.asset-card-overlay {
  position: absolute;
  top: 6px;
  right: 6px;
  opacity: 0;
  transition: opacity 0.15s ease;
}
.asset-card:hover .asset-card-overlay {
  opacity: 1;
}
.add-collection-btn {
  background: rgba(255, 255, 255, 0.9);
  border-color: #E5E7EB;
}
.asset-card-body {
  padding: 8px 10px 10px;
}
.asset-card-name {
  font-size: 13px;
  font-weight: 500;
  color: #1F2933;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.asset-card-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 4px;
  font-size: 12px;
  color: #8A94A6;
}
</style>
