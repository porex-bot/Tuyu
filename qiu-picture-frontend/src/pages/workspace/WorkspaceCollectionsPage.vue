<template>
  <div id="workspaceCollectionsPage">
    <div class="page-header">
      <a-page-header
        :title="workspaceStore.currentWorkspace?.workspace?.name ?? '工作区'"
        sub-title="集合"
        style="padding: 0 0 16px"
      />
      <a-button type="primary" @click="createVisible = true">
        <template #icon><PlusOutlined /></template>
        新建集合
      </a-button>
    </div>

    <CollectionList
      :collections="collections"
      :loading="loading"
      :error="error"
      @card-click="onCardClick"
      @retry="loadCollections(workspaceId)"
    />

    <CreateCollectionModal
      :visible="createVisible"
      :workspace-id="workspaceId"
      @close="createVisible = false"
      @created="onCollectionCreated"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { PlusOutlined } from '@ant-design/icons-vue'
import { useWorkspaceStore } from '@/entities/workspace/model/useWorkspaceStore'
import { useCollectionQueryState } from '@/entities/collection/model/useCollectionQueryState'
import CollectionList from '@/widgets/collection-list/CollectionList.vue'
import CreateCollectionModal from '@/features/collection-create/CreateCollectionModal.vue'
import type { Collection } from '@/entities/collection/model/types'

const route = useRoute()
const router = useRouter()
const workspaceStore = useWorkspaceStore()

const workspaceId = computed(() => Number(route.params.workspaceId))
const createVisible = ref(false)

const { collections, loading, error, loadCollections } = useCollectionQueryState()

function onCardClick(collection: Collection) {
  router.push(`/w/${workspaceId.value}/collections/${collection.collectionId}`)
}

function onCollectionCreated() {
  createVisible.value = false
  loadCollections(workspaceId.value)
}

watch(() => workspaceId.value, (id) => {
  if (id) loadCollections(id)
}, { immediate: true })
</script>

<style scoped>
#workspaceCollectionsPage {
  max-width: 1200px;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}
</style>
