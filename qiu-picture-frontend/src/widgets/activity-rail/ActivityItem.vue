<template>
  <div class="activity-item">
    <div class="activity-dot" :class="dotClass" />
    <div class="activity-body">
      <div class="activity-summary">{{ record.summary ?? getActionLabel(record.actionType) }}</div>
      <div class="activity-meta">
        <ActivityActor :actor="record.actor" />
        <span class="separator">·</span>
        <span class="activity-time">{{ formattedTime }}</span>
      </div>
      <div v-if="record.target" class="activity-target-row">
        <ActivityTargetLink :target="record.target" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { ActivityRecord } from '@/entities/activity/model/types'
import { getActionLabel } from '@/entities/activity/model/activityLabels'
import ActivityActor from '@/entities/activity/ui/ActivityActor.vue'
import ActivityTargetLink from '@/entities/activity/ui/ActivityTargetLink.vue'

const props = defineProps<{
  record: ActivityRecord
}>()

const formattedTime = computed(() => {
  if (!props.record.occurredAt) return ''
  const d = new Date(props.record.occurredAt)
  const now = new Date()
  const diffMs = now.getTime() - d.getTime()
  const diffMin = Math.floor(diffMs / 60000)

  if (diffMin < 1) return '刚刚'
  if (diffMin < 60) return `${diffMin} 分钟前`
  const diffHr = Math.floor(diffMin / 60)
  if (diffHr < 24) return `${diffHr} 小时前`
  const diffDay = Math.floor(diffHr / 24)
  if (diffDay < 7) return `${diffDay} 天前`
  return d.toLocaleDateString('zh-CN')
})

const dotClass = computed(() => {
  if (props.record.actionType.startsWith('collection')) return 'dot-collection'
  if (props.record.actionType.startsWith('asset')) return 'dot-asset'
  if (props.record.actionType.startsWith('workspace')) return 'dot-workspace'
  return ''
})
</script>

<style scoped>
.activity-item {
  display: flex;
  padding: 8px 0;
  position: relative;
}
.activity-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #B6BEC9;
  margin-top: 6px;
  flex-shrink: 0;
}
.dot-collection { background: #3B82A0; }
.dot-asset { background: #34A853; }
.dot-workspace { background: #6B8EA4; }
.activity-body {
  margin-left: 10px;
  flex: 1;
  min-width: 0;
}
.activity-summary {
  font-size: 13px;
  color: #1F2933;
  line-height: 1.4;
}
.activity-meta {
  display: flex;
  align-items: center;
  gap: 4px;
  margin-top: 2px;
  font-size: 12px;
  color: #8A94A6;
}
.separator {
  color: #B6BEC9;
}
.activity-target-row {
  margin-top: 2px;
}
</style>
