import { ref, computed } from 'vue'
import type { AssetCard } from '@/entities/asset/model/types'

/**
 * 工作区资产查询状态 —— 集中管理分页、搜索、筛选状态。
 * 保持独立于 workspace store，避免循环依赖。
 */
export function useAssetQueryState() {
  const current = ref(1)
  const pageSize = ref(20)
  const total = ref(0)
  const searchText = ref('')
  const category = ref<string | null>(null)
  const formatFilter = ref<string | null>(null)
  const dominantColor = ref<string | null>(null)
  const sortField = ref('createTime')
  const sortOrder = ref<'ascend' | 'descend'>('descend')
  const records = ref<AssetCard[]>([])
  const loading = ref(false)

  const hasMore = computed(() => current.value * pageSize.value < total.value)
  const isEmpty = computed(() => !loading.value && records.value.length === 0)

  function setPage(p: number) {
    current.value = p
  }

  function setSearch(text: string) {
    searchText.value = text
    current.value = 1
  }

  function setFilter(filter: {
    category?: string | null
    format?: string | null
    dominantColor?: string | null
  }) {
    if (filter.category !== undefined) category.value = filter.category
    if (filter.format !== undefined) formatFilter.value = filter.format
    if (filter.dominantColor !== undefined) dominantColor.value = filter.dominantColor
    current.value = 1
  }

  function setSort(field: string, order: 'ascend' | 'descend') {
    sortField.value = field
    sortOrder.value = order
    current.value = 1
  }

  function setRecords(data: AssetCard[], t: number) {
    records.value = data
    total.value = t
  }

  function reset() {
    current.value = 1
    searchText.value = ''
    category.value = null
    formatFilter.value = null
    dominantColor.value = null
    sortField.value = 'createTime'
    sortOrder.value = 'descend'
    records.value = []
    total.value = 0
    loading.value = false
  }

  return {
    current,
    pageSize,
    total,
    searchText,
    category,
    formatFilter,
    dominantColor,
    sortField,
    sortOrder,
    records,
    loading,
    hasMore,
    isEmpty,
    setPage,
    setSearch,
    setFilter,
    setSort,
    setRecords,
    reset,
  }
}
