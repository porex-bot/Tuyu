<template>
  <div v-if="loading" class="collection-list-loading">
    <a-spin style="display: block; margin: 64px auto" />
  </div>

  <div v-else-if="error" class="collection-list-error">
    <a-result status="error" :title="error">
      <template #extra>
        <a-button @click="$emit('retry')">重试</a-button>
      </template>
    </a-result>
  </div>

  <div v-else-if="collections.length === 0" class="collection-list-empty">
    <a-empty description="暂无集合，点击上方按钮创建第一个集合" />
  </div>

  <div v-else class="collection-list-grid">
    <CollectionCard
      v-for="c in collections"
      :key="c.collectionId"
      :collection="c"
      @click="$emit('cardClick', c)"
    />
  </div>
</template>

<script setup lang="ts">
import CollectionCard from './CollectionCard.vue'
import type { Collection } from '@/entities/collection/model/types'

defineProps<{
  collections: Collection[]
  loading: boolean
  error: string | null
}>()

defineEmits<{
  cardClick: [collection: Collection]
  retry: []
}>()
</script>

<style scoped>
.collection-list-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 16px;
}
</style>
