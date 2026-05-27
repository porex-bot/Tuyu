<template>
  <div class="collection-board">
    <template v-if="loading">
      <a-spin style="display: block; margin: 64px auto" />
    </template>

    <template v-else-if="error">
      <a-result status="error" :title="error">
        <template #extra>
          <a-button @click="$emit('retry')">重试</a-button>
        </template>
      </a-result>
    </template>

    <template v-else-if="board">
      <CollectionHeader :name="board.name" :subtitle="statusLabel">
        <template #actions>
          <slot name="actions" />
        </template>
      </CollectionHeader>

      <div v-if="board.description" class="board-desc">{{ board.description }}</div>

      <CollectionSection
        v-for="section in board.sections"
        :key="section.sectionId ?? section.name"
        :section="section"
        :show-remove="showRemove"
        @remove="id => $emit('remove', id)"
      />

      <CollectionSection
        v-if="board.unsorted && board.unsorted.items && board.unsorted.items.length > 0"
        :section="board.unsorted"
        :show-remove="showRemove"
        @remove="id => $emit('remove', id)"
      />
    </template>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import CollectionHeader from './CollectionHeader.vue'
import CollectionSection from './CollectionSection.vue'
import type { CollectionBoard as Board } from '@/entities/collection/model/types'

const props = defineProps<{
  board: Board | null
  loading: boolean
  error: string | null
  showRemove?: boolean
}>()

defineEmits<{
  retry: []
  remove: [itemId: number]
}>()

const statusLabels: Record<string, string> = {
  draft: '草稿',
  active: '活跃',
  archived: '已归档',
}

const statusLabel = computed(() => {
  if (!props.board?.status) return ''
  return statusLabels[props.board.status] ?? props.board.status
})
</script>

<style scoped>
.collection-board {
  max-width: 900px;
}
.board-desc {
  font-size: 13px;
  color: #6B7280;
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #EEF0F3;
}
</style>
