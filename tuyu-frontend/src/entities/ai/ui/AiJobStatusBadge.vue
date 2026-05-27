<template>
  <a-tag :color="color">{{ label }}</a-tag>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { getJobStatusLabel } from '@/entities/ai/model/aiLabels'

const props = defineProps<{
  status: string
}>()

const label = computed(() => getJobStatusLabel(props.status))

const color = computed(() => {
  switch (props.status) {
    case 'succeeded':
    case 'applied':
      return 'green'
    case 'failed':
      return 'red'
    case 'running':
    case 'queued':
      return 'processing'
    case 'cancelled':
    case 'discarded':
      return 'default'
    default:
      return 'default'
  }
})
</script>
