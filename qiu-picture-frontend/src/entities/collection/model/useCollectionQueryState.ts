import { ref } from 'vue'
import type { Collection, CollectionBoard } from '@/entities/collection/model/types'
import {
  listCollections,
  getCollectionBoard,
} from '@/entities/collection/api/collectionApi'

export function useCollectionQueryState() {
  const collections = ref<Collection[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  async function loadCollections(workspaceId: number) {
    loading.value = true
    error.value = null
    try {
      const res = await listCollections(workspaceId)
      if (res.data.code === 0 && res.data.data) {
        collections.value = res.data.data
      } else {
        error.value = res.data.message ?? '加载集合列表失败'
      }
    } catch {
      error.value = '网络错误，请重试'
    } finally {
      loading.value = false
    }
  }

  return { collections, loading, error, loadCollections }
}

export function useCollectionBoardState() {
  const board = ref<CollectionBoard | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)

  async function loadBoard(workspaceId: number, collectionId: number) {
    loading.value = true
    error.value = null
    try {
      const res = await getCollectionBoard(workspaceId, collectionId)
      if (res.data.code === 0 && res.data.data) {
        board.value = res.data.data
      } else {
        error.value = res.data.message ?? '加载看板失败'
      }
    } catch {
      error.value = '网络错误，请重试'
    } finally {
      loading.value = false
    }
  }

  return { board, loading, error, loadBoard }
}
