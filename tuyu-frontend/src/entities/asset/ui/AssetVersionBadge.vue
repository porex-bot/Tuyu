<template>
  <span class="version-badge" :class="typeClass">{{ label }}</span>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  versionType: string
  isCurrent?: boolean
}>()

const label = computed(() => {
  const typeLabel = typeLabels[props.versionType] ?? props.versionType
  return props.isCurrent ? `${typeLabel} (当前)` : typeLabel
})

const typeClass = computed(() => {
  switch (props.versionType) {
    case 'original': return 'type-original'
    case 'replacement': return 'type-replacement'
    case 'manual_edit': return 'type-manual'
    case 'crop': return 'type-crop'
    case 'ai_generated': return 'type-ai'
    case 'format_conversion': return 'type-conversion'
    default: return 'type-original'
  }
})

const typeLabels: Record<string, string> = {
  original: '原始',
  replacement: '替换',
  manual_edit: '编辑',
  crop: '裁剪',
  ai_generated: 'AI 生成',
  format_conversion: '格式转换',
}
</script>

<style scoped>
.version-badge {
  display: inline-block;
  padding: 0 6px;
  font-size: 11px;
  border-radius: 3px;
  line-height: 18px;
  white-space: nowrap;
}
.type-original { color: #3B82A0; background: #EEF5F7; }
.type-replacement { color: #E08A1A; background: #FFF8EE; }
.type-manual { color: #34A853; background: #EEF7F0; }
.type-crop { color: #6B8EA4; background: #F0F4F7; }
.type-ai { color: #C94F6E; background: #FDF2F5; }
.type-conversion { color: #6B7280; background: #F3F5F7; }
</style>
