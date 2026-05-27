<template>
  <div class="item-card">
    <div class="item-thumb">
      <img v-if="thumbnailUrl" :src="thumbnailUrl" class="item-img" />
      <div v-else class="item-placeholder">
        <FileImageOutlined />
      </div>
    </div>
    <div class="item-body">
      <span class="item-name">{{ item.assetId }}</span>
      <span v-if="item.note" class="item-note">{{ item.note }}</span>
    </div>
    <a-button
      v-if="showRemove"
      type="link"
      danger
      size="small"
      @click.stop="$emit('remove', item.itemId)"
    >
      <template #icon><DeleteOutlined /></template>
    </a-button>
  </div>
</template>

<script setup lang="ts">
import { FileImageOutlined, DeleteOutlined } from '@ant-design/icons-vue'
import type { CollectionItem } from '@/entities/collection/model/types'

defineProps<{
  item: CollectionItem
  showRemove?: boolean
  thumbnailUrl?: string
}>()

defineEmits<{
  remove: [itemId: number]
}>()
</script>

<style scoped>
.item-card {
  display: flex;
  align-items: center;
  padding: 8px;
  border: 1px solid #EEF0F3;
  border-radius: 6px;
  margin-bottom: 8px;
  background: #fff;
  gap: 8px;
}
.item-thumb {
  width: 48px;
  height: 48px;
  border-radius: 4px;
  overflow: hidden;
  flex-shrink: 0;
  background: #F7F8FA;
  display: flex;
  align-items: center;
  justify-content: center;
}
.item-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.item-placeholder {
  font-size: 20px;
  color: #B6BEC9;
}
.item-body {
  flex: 1;
  min-width: 0;
}
.item-name {
  font-size: 13px;
  color: #1F2933;
  display: block;
}
.item-note {
  font-size: 11px;
  color: #8A94A6;
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
