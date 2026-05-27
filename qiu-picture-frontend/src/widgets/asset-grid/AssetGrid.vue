<template>
  <div id="assetGrid">
    <AssetToolbar
      :total="queryState.total.value"
      :loading="queryState.loading.value"
      :filter-visible="filterVisible"
      @search="onSearch"
      @toggle-filter="filterVisible = !filterVisible"
    />

    <AssetFilterPanel
      :visible="filterVisible"
      :category="queryState.category.value"
      :format="queryState.formatFilter.value"
      :lifecycle-status="queryState.dominantColor.value"
      @filter="onFilter"
    />

    <!-- 空状态 -->
    <a-empty
      v-if="queryState.isEmpty.value"
      description="暂无素材"
      style="margin: 64px 0"
    >
      <template #image>
        <PictureOutlined :style="{ fontSize: '64px', color: '#d9d9d9' }" />
      </template>
    </a-empty>

    <!-- 加载中 -->
    <a-spin v-else-if="queryState.loading.value && queryState.records.value.length === 0" style="display: block; margin: 64px auto" />

    <!-- 错误 -->
    <a-result
      v-else-if="errorMessage"
      status="error"
      :title="errorMessage"
    >
      <template #extra>
        <a-button @click="loadAssets">重试</a-button>
      </template>
    </a-result>

    <!-- 资产网格 -->
    <div v-else class="asset-grid-container">
      <div class="asset-grid">
        <AssetCardComponent
          v-for="card in queryState.records.value"
          :key="card.assetId"
          :card="card"
          @click="onCardClick"
          @add-to-collection="(c: AssetCard) => emit('addToCollection', c)"
        />
      </div>

      <!-- 分页 -->
      <div v-if="queryState.total.value > queryState.pageSize.value" class="asset-grid-pagination">
        <a-pagination
          v-model:current="queryState.current.value"
          :total="queryState.total.value"
          :page-size="queryState.pageSize.value"
          :show-size-changer="false"
          size="small"
          @change="onPageChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { PictureOutlined } from '@ant-design/icons-vue'
import { searchAssets } from '@/entities/asset/api/assetApi'
import { useAssetQueryState } from '@/entities/asset/model/useAssetQueryState'
import type { AssetCard } from '@/entities/asset/model/types'
import AssetToolbar from './AssetToolbar.vue'
import AssetFilterPanel from './AssetFilterPanel.vue'
import AssetCardComponent from './AssetCard.vue'

const props = defineProps<{
  workspaceId: number
}>()

const emit = defineEmits<{
  cardClick: [card: AssetCard]
  addToCollection: [card: AssetCard]
}>()

const queryState = useAssetQueryState()
const filterVisible = ref(false)
const errorMessage = ref<string | null>(null)

async function loadAssets() {
  queryState.loading.value = true
  errorMessage.value = null
  try {
    const res = await searchAssets(props.workspaceId, {
      current: queryState.current.value,
      pageSize: queryState.pageSize.value,
      searchText: queryState.searchText.value || undefined,
      category: queryState.category.value ?? undefined,
      format: queryState.formatFilter.value ?? undefined,
      dominantColor: queryState.dominantColor.value ?? undefined,
      sortField: queryState.sortField.value,
      sortOrder: queryState.sortOrder.value,
    })
    if (res.data.code === 0 && res.data.data) {
      const page = res.data.data
      queryState.setRecords(page.records, page.total)
    } else {
      errorMessage.value = res.data.message ?? '加载失败'
    }
  } catch {
    errorMessage.value = '网络错误，请重试'
  } finally {
    queryState.loading.value = false
  }
}

function onSearch(text: string) {
  queryState.setSearch(text)
  loadAssets()
}

function onFilter(filter: {
  category?: string | null
  format?: string | null
  lifecycleStatus?: string | null
}) {
  queryState.setFilter({
    category: filter.category,
    format: filter.format,
    dominantColor: filter.lifecycleStatus,
  })
  loadAssets()
}

function onPageChange(_page: number) {
  loadAssets()
}

function onCardClick(card: AssetCard) {
  emit('cardClick', card)
}

onMounted(() => {
  loadAssets()
})
</script>

<style scoped>
#assetGrid {
  background: #fff;
  border-radius: 8px;
  border: 1px solid #E5E7EB;
  overflow: hidden;
}
.asset-grid-container {
  padding: 16px;
}
.asset-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
}
.asset-grid-pagination {
  display: flex;
  justify-content: center;
  margin-top: 24px;
  padding-bottom: 8px;
}
</style>
