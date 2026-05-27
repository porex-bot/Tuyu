<template>
  <div id="workspaceCollectionBoardPage">
    <CollectionBoard
      :board="board"
      :loading="loading"
      :error="error"
      :show-remove="true"
      @retry="loadBoard(workspaceId, collectionId)"
      @remove="handleRemove"
    >
      <template #actions>
        <a-button @click="router.back()">返回集合列表</a-button>
      </template>
    </CollectionBoard>
  </div>
</template>

<script setup lang="ts">
import { computed, watch, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { useCollectionBoardState } from '@/entities/collection/model/useCollectionQueryState'
import { removeItemFromCollection } from '@/entities/collection/api/collectionApi'
import { useActivityRailStore } from '@/entities/activity/model/useActivityRailStore'
import CollectionBoard from '@/widgets/collection-board/CollectionBoard.vue'

const route = useRoute()
const router = useRouter()

const workspaceId = computed(() => Number(route.params.workspaceId))
const collectionId = computed(() => Number(route.params.collectionId))

const { board, loading, error, loadBoard } = useCollectionBoardState()
const activityStore = useActivityRailStore()

async function handleRemove(itemId: number) {
  try {
    const res = await removeItemFromCollection(workspaceId.value, collectionId.value, itemId)
    if (res.data.code === 0) {
      message.success('已移除')
      loadBoard(workspaceId.value, collectionId.value)
    } else {
      message.error(res.data.message ?? '移除失败')
    }
  } catch {
    message.error('网络错误，请重试')
  }
}

watch(() => collectionId.value, (id) => {
  if (id && workspaceId.value) {
    loadBoard(workspaceId.value, id)
  }
}, { immediate: true })

watch(() => board.value?.name, (name) => {
  if (name && collectionId.value) {
    activityStore.setScope('collection', collectionId.value, name)
  }
})

onUnmounted(() => {
  activityStore.clearScope()
})
</script>

<style scoped>
#workspaceCollectionBoardPage {
  max-width: 960px;
}
</style>
