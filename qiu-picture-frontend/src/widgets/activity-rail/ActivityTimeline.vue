<template>
  <div class="activity-timeline">
    <template v-if="loading">
      <a-spin style="display: block; margin: 32px auto" />
    </template>

    <template v-else-if="error">
      <a-result status="error" :title="error" size="small">
        <template #extra>
          <a-button size="small" @click="$emit('retry')">重试</a-button>
        </template>
      </a-result>
    </template>

    <template v-else-if="records.length === 0">
      <a-empty description="暂无活动记录" :image="aEmptyImage" style="margin: 24px 0" />
    </template>

    <template v-else>
      <ActivityItem
        v-for="record in records"
        :key="record.activityId"
        :record="record"
      />
    </template>
  </div>
</template>

<script setup lang="ts">
import { Empty } from 'ant-design-vue'
import ActivityItem from './ActivityItem.vue'
import type { ActivityRecord } from '@/entities/activity/model/types'

defineProps<{
  records: ActivityRecord[]
  loading: boolean
  error: string | null
}>()

defineEmits<{
  retry: []
}>()

const aEmptyImage = Empty.PRESENTED_IMAGE_SIMPLE
</script>

<style scoped>
.activity-timeline {
  padding: 4px 0;
}
</style>
