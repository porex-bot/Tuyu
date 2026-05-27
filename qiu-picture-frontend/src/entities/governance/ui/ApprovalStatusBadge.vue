<template>
  <a-badge :status="color" :text="label" />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { getStatusLabel, getStatusColor } from '@/entities/governance/model/approvalLabels'

const props = defineProps<{ status: string }>()

const label = computed(() => getStatusLabel(props.status))
const color = computed(() => getStatusColor(props.status) as 'success' | 'processing' | 'error' | 'default' | 'warning')
</script>
