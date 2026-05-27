<template>
  <a-card hoverable class="collection-card" @click="$emit('click')">
    <div class="collection-card-body">
      <h3 class="collection-name">{{ collection.name }}</h3>
      <p v-if="collection.description" class="collection-desc">{{ collection.description }}</p>
      <div class="collection-meta">
        <a-tag :color="purposeColor">{{ purposeLabel }}</a-tag>
        <span class="collection-count">{{ collection.itemCount ?? 0 }} 个资产</span>
      </div>
    </div>
  </a-card>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { Collection } from '@/entities/collection/model/types'

const props = defineProps<{
  collection: Collection
}>()

defineEmits<{
  click: []
}>()

const purposeLabels: Record<string, string> = {
  project: '项目',
  brand: '品牌',
  campaign: '营销活动',
  delivery: '交付',
  reference: '参考',
}

const purposeLabel = computed(() => purposeLabels[props.collection.purpose] ?? props.collection.purpose)

const purposeColor = computed(() => {
  switch (props.collection.purpose) {
    case 'project': return '#6B8EA4'
    case 'brand': return '#8B7EA4'
    case 'campaign': return '#E08A1A'
    case 'delivery': return '#34A853'
    case 'reference': return '#8A94A6'
    default: return '#8A94A6'
  }
})
</script>

<style scoped>
.collection-card {
  cursor: pointer;
}
.collection-card-body {
  min-height: 80px;
}
.collection-name {
  font-size: 15px;
  font-weight: 600;
  margin: 0 0 4px;
  color: #1F2933;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.collection-desc {
  font-size: 12px;
  color: #8A94A6;
  margin: 0 0 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.collection-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.collection-count {
  font-size: 12px;
  color: #B6BEC9;
}
</style>
