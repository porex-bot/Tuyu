<template>
  <div class="board-section">
    <div class="section-header">
      <h4 class="section-title">{{ section.name }}</h4>
      <span class="section-count">{{ section.items.length }} 项</span>
    </div>
    <div class="section-items">
      <CollectionItemCard
        v-for="item in section.items"
        :key="item.itemId"
        :item="item"
        :show-remove="showRemove"
        @remove="id => $emit('remove', id)"
      />
      <a-empty
        v-if="section.items.length === 0"
        description="暂无资产"
        :image="aEmptyImage"
        style="margin: 8px 0"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { Empty } from 'ant-design-vue'
import CollectionItemCard from './CollectionItemCard.vue'
import type { BoardSection } from '@/entities/collection/model/types'

defineProps<{
  section: BoardSection
  showRemove?: boolean
}>()

defineEmits<{
  remove: [itemId: number]
}>()

const aEmptyImage = Empty.PRESENTED_IMAGE_SIMPLE
</script>

<style scoped>
.board-section {
  margin-bottom: 16px;
}
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}
.section-title {
  font-size: 14px;
  font-weight: 600;
  color: #1F2933;
  margin: 0;
}
.section-count {
  font-size: 12px;
  color: #8A94A6;
}
</style>
