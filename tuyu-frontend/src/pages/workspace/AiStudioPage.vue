<template>
  <div id="aiStudioPage">
    <AiStudio :workspace-id="workspaceId" />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import AiStudio from '@/widgets/ai-studio/AiStudio.vue'

const route = useRoute()
const workspaceId = computed(() => Number(route.params.workspaceId))
</script>

<style scoped>
#aiStudioPage {
  height: 100%;
}
</style>
