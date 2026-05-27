<template>
  <span class="activity-actor">
    <a-avatar v-if="actor?.userAvatar" :src="actor.userAvatar" :size="20" style="margin-right: 4px" />
    <span class="actor-name">{{ actor?.userName ?? '未知用户' }}</span>
  </span>
</template>

<script setup lang="ts">
import type { ActivityActor } from '@/entities/activity/model/types'

defineProps<{
  actor: ActivityActor | null
}>()
</script>

<style scoped>
.activity-actor {
  display: inline-flex;
  align-items: center;
}
.actor-name {
  font-weight: 500;
  color: #1F2933;
}
</style>
