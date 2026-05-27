<template>
  <span class="asset-status-badge" :class="statusClass">{{ displayText }}</span>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  lifecycleStatus?: string
  reviewStatusText?: string
}>()

const statusClass = computed(() => {
  switch (props.lifecycleStatus) {
    case 'APPROVED': return 'status-approved'
    case 'REJECTED': return 'status-rejected'
    case 'ARCHIVED': return 'status-archived'
    default: return 'status-pending'
  }
})

const displayText = computed(() => props.reviewStatusText ?? props.lifecycleStatus ?? '待审核')
</script>

<style scoped>
.asset-status-badge {
  display: inline-block;
  padding: 1px 8px;
  font-size: 12px;
  border-radius: 4px;
  line-height: 20px;
}
.status-approved {
  color: #34A853;
  background: #EEF7F0;
  border: 1px solid #C5E8CF;
}
.status-pending {
  color: #E08A1A;
  background: #FFF8EE;
  border: 1px solid #F5D89A;
}
.status-rejected {
  color: #E05555;
  background: #FDF2F2;
  border: 1px solid #F5C5C5;
}
.status-archived {
  color: #8A94A6;
  background: #F3F5F7;
  border: 1px solid #E5E7EB;
}
</style>
