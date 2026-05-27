<template>
  <span v-if="target" class="activity-target">
    <span class="target-type">{{ targetTypeLabel }}</span>
    <span v-if="target.targetName" class="target-name">「{{ target.targetName }}」</span>
    <span v-else class="target-id">#{{ target.targetId }}</span>
  </span>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { ActivityTarget } from '@/entities/activity/model/types'
import { targetTypeLabels } from '@/entities/activity/model/activityLabels'

const props = defineProps<{
  target: ActivityTarget | null
}>()

const targetTypeLabel = computed(() => {
  if (!props.target) return ''
  return targetTypeLabels[props.target.targetType] ?? props.target.targetType
})
</script>

<style scoped>
.activity-target {
  font-size: 12px;
  color: #6B7280;
}
.target-name {
  color: #3B82A0;
}
.target-id {
  color: #8A94A6;
}
</style>
