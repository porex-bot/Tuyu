<template>
  <div id="workspaceApprovalsPage">
    <a-page-header
      :title="workspaceStore.currentWorkspace?.workspace?.name ?? '工作区'"
      sub-title="审批管理"
      style="padding: 0 0 16px"
    />

    <ApprovalInbox
      :workspace-id="workspaceId"
      @decide="onDecisionMade"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, provide, ref } from 'vue'
import { useRoute } from 'vue-router'
import { useWorkspaceStore } from '@/entities/workspace/model/useWorkspaceStore'
import ApprovalInbox from '@/widgets/approval-inbox/ApprovalInbox.vue'

const route = useRoute()
const workspaceStore = useWorkspaceStore()

const workspaceId = computed(() => Number(route.params.workspaceId))
const refreshKey = ref(0)

provide('approvalRefreshKey', refreshKey)

function onDecisionMade() {
  refreshKey.value++
}
</script>
