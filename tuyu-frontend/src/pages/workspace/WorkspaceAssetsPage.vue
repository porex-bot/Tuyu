<template>
  <div id="workspaceAssetsPage">
    <a-page-header
      :title="workspaceStore.currentWorkspace?.workspace?.name ?? '工作区'"
      sub-title="素材库"
      style="padding: 0 0 16px"
    />

    <AssetGrid
      :workspace-id="workspaceId"
      @card-click="onCardClick"
      @add-to-collection="onAddToCollection"
    />

    <AssetDetailPreview
      :visible="previewVisible"
      :workspace-id="workspaceId"
      :asset-id="selectedAssetId"
      @close="previewVisible = false"
    />

    <AddAssetsToCollectionModal
      :visible="addToCollectionVisible"
      :workspace-id="workspaceId"
      :asset-id="selectedAssetForCollection"
      @close="addToCollectionVisible = false"
      @added="addToCollectionVisible = false"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { useWorkspaceStore } from '@/entities/workspace/model/useWorkspaceStore'
import AssetGrid from '@/widgets/asset-grid/AssetGrid.vue'
import AssetDetailPreview from '@/widgets/asset-grid/AssetDetailPreview.vue'
import AddAssetsToCollectionModal from '@/features/add-assets-to-collection/AddAssetsToCollectionModal.vue'
import type { AssetCard } from '@/entities/asset/model/types'

const route = useRoute()
const workspaceStore = useWorkspaceStore()

const workspaceId = computed(() => Number(route.params.workspaceId))

const previewVisible = ref(false)
const selectedAssetId = ref<number | null>(null)

const addToCollectionVisible = ref(false)
const selectedAssetForCollection = ref<number | null>(null)

function onCardClick(card: AssetCard) {
  selectedAssetId.value = card.assetId
  previewVisible.value = true
}

function onAddToCollection(card: AssetCard) {
  selectedAssetForCollection.value = card.assetId
  addToCollectionVisible.value = true
}
</script>

<style scoped>
#workspaceAssetsPage {
  max-width: 1200px;
}
</style>
