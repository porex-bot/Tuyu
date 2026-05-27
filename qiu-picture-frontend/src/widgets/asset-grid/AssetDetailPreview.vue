<template>
  <a-drawer
    :visible="visible"
    title="资产详情"
    placement="right"
    :width="420"
    @close="$emit('close')"
  >
    <template v-if="loading">
      <a-spin style="display: block; margin: 64px auto" />
    </template>

    <template v-else-if="error">
      <a-result status="error" :title="error">
        <template #extra>
          <a-button @click="loadDetail">重试</a-button>
        </template>
      </a-result>
    </template>

    <template v-else-if="detail">
      <div class="preview-section">
        <AssetThumbnail
          v-if="detail.thumbnailUrl || detail.url"
          :src="detail.thumbnailUrl ?? detail.url"
          :dominant-color="detail.dominantColor"
          :format="detail.format"
          style="width: 100%; max-height: 300px; border-radius: 6px"
        />
      </div>

      <a-descriptions :column="1" size="small" style="margin-top: 16px">
        <a-descriptions-item label="名称">{{ detail.name }}</a-descriptions-item>
        <a-descriptions-item v-if="detail.description" label="描述">{{ detail.description }}</a-descriptions-item>
        <a-descriptions-item label="格式">{{ detail.format?.toUpperCase() }}</a-descriptions-item>
        <a-descriptions-item label="尺寸">{{ detail.width }}x{{ detail.height }}</a-descriptions-item>
        <a-descriptions-item label="大小">{{ detail.sizeDisplay }}</a-descriptions-item>
        <a-descriptions-item label="状态">
          <AssetStatusBadge
            :lifecycle-status="detail.lifecycleStatus"
            :review-status-text="detail.reviewStatusText"
          />
        </a-descriptions-item>
        <a-descriptions-item label="创建时间">{{ detail.createdAt }}</a-descriptions-item>
      </a-descriptions>

      <a-divider />

      <div class="preview-section">
        <h4 class="section-title">版本历史</h4>
        <AssetVersionTimeline :versions="versions" />
      </div>
    </template>
  </a-drawer>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { getAssetDetail, listAssetVersions } from '@/entities/asset/api/assetApi'
import type { AssetDetail, AssetVersion } from '@/entities/asset/model/types'
import AssetThumbnail from '@/entities/asset/ui/AssetThumbnail.vue'
import AssetStatusBadge from '@/entities/asset/ui/AssetStatusBadge.vue'
import AssetVersionTimeline from '@/entities/asset/ui/AssetVersionTimeline.vue'

const props = defineProps<{
  visible: boolean
  workspaceId: number
  assetId: number | null
}>()

defineEmits<{
  close: []
}>()

const detail = ref<AssetDetail | null>(null)
const versions = ref<AssetVersion[]>([])
const loading = ref(false)
const error = ref<string | null>(null)

async function loadDetail() {
  if (!props.assetId) return
  loading.value = true
  error.value = null
  try {
    const [detailRes, versionsRes] = await Promise.all([
      getAssetDetail(props.workspaceId, props.assetId),
      listAssetVersions(props.workspaceId, props.assetId),
    ])
    if (detailRes.data.code === 0 && detailRes.data.data) {
      detail.value = detailRes.data.data
    } else {
      error.value = detailRes.data.message ?? '加载失败'
    }
    if (versionsRes.data.code === 0 && versionsRes.data.data) {
      versions.value = versionsRes.data.data
    }
  } catch {
    error.value = '网络错误，请重试'
  } finally {
    loading.value = false
  }
}

watch(() => props.assetId, (id) => {
  if (id) loadDetail()
})
</script>

<style scoped>
.preview-section {
  margin-bottom: 8px;
}
.section-title {
  font-size: 14px;
  font-weight: 600;
  color: #1F2933;
  margin: 0 0 8px;
}
</style>
