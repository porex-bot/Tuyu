<template>
  <div id="workspaceShell">
    <a-layout style="min-height: 100vh">
      <a-layout-header class="ws-header">
        <WorkspaceTopBar :workspace-id="workspaceId" @toggle-activity="activityStore.toggle()" />
      </a-layout-header>

      <a-layout>
        <WorkspaceSidebar :workspace-id="workspaceId" />

        <a-layout-content class="ws-content">
          <router-view />
        </a-layout-content>

        <ActivityRail
          :visible="activityStore.visible.value"
          :records="activityStore.timeline.value?.records ?? []"
          :loading="activityStore.loading.value"
          :error="activityStore.error.value"
          :scope-label="activityStore.scopeLabel.value"
          :active-filter="activeFilter"
          @toggle="activityStore.toggle()"
          @refresh="handleRefresh"
          @clear-scope="activityStore.clearScope()"
        />
      </a-layout>

      <WorkspaceCommandBar />
    </a-layout>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useWorkspaceStore } from '@/entities/workspace/model/useWorkspaceStore'
import { useActivityRailStore } from '@/entities/activity/model/useActivityRailStore'
import WorkspaceTopBar from './WorkspaceTopBar.vue'
import WorkspaceSidebar from './WorkspaceSidebar.vue'
import WorkspaceCommandBar from './WorkspaceCommandBar.vue'
import ActivityRail from '@/widgets/activity-rail/ActivityRail.vue'

const props = defineProps<{
  workspaceId: number
}>()

const workspaceStore = useWorkspaceStore()
const activityStore = useActivityRailStore()
const activeFilter = ref('all')

watch(
  () => props.workspaceId,
  (id) => {
    if (id) {
      workspaceStore.loadWorkspace(id)
      if (activityStore.visible.value) {
        activityStore.clearScope()
        activityStore.loadWorkspaceTimeline(id)
      }
    }
  },
  { immediate: true }
)

watch(() => activityStore.visible.value, (v) => {
  if (v && props.workspaceId) {
    handleRefresh()
  }
})

function handleRefresh() {
  if (!props.workspaceId) return
  if (activityStore.scopeTargetType.value && activityStore.scopeTargetId.value) {
    activityStore.loadTargetTimeline(
      props.workspaceId,
      activityStore.scopeTargetType.value,
      activityStore.scopeTargetId.value
    )
  } else {
    activityStore.loadWorkspaceTimeline(props.workspaceId)
  }
}
</script>

<style scoped>
#workspaceShell .ws-header {
  padding-inline: 20px;
  background: #fff;
  color: unset;
  border-bottom: 1px solid var(--color-border, #E5E7EB);
  height: 56px;
  line-height: 56px;
}

#workspaceShell .ws-content {
  padding: 24px;
  background: #F7F8FA;
  min-height: calc(100vh - 56px - 40px);
}

#workspaceShell :deep(.ant-menu-root) {
  border-bottom: none !important;
  border-inline-end: none !important;
  background: transparent;
}

#workspaceShell :deep(.ant-layout-sider) {
  background: #F3F5F7 !important;
  border-right: 1px solid var(--color-border, #E5E7EB);
}
</style>
