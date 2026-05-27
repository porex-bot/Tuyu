<template>
  <div v-if="visible" class="asset-filter-panel">
    <a-row :gutter="[16, 12]">
      <a-col :span="8">
        <div class="filter-item">
          <label>类型</label>
          <a-select
            v-model:value="localCategory"
            placeholder="全部类型"
            allow-clear
            style="width: 100%"
            @change="emitFilter"
          >
            <a-select-option value="screenshot">截图</a-select-option>
            <a-select-option value="photo">摄影</a-select-option>
            <a-select-option value="illustration">插画</a-select-option>
            <a-select-option value="icon">图标</a-select-option>
          </a-select>
        </div>
      </a-col>
      <a-col :span="8">
        <div class="filter-item">
          <label>格式</label>
          <a-select
            v-model:value="localFormat"
            placeholder="全部格式"
            allow-clear
            style="width: 100%"
            @change="emitFilter"
          >
            <a-select-option value="png">PNG</a-select-option>
            <a-select-option value="jpg">JPG</a-select-option>
            <a-select-option value="webp">WebP</a-select-option>
            <a-select-option value="svg">SVG</a-select-option>
          </a-select>
        </div>
      </a-col>
      <a-col :span="8">
        <div class="filter-item">
          <label>状态</label>
          <a-select
            v-model:value="localStatus"
            placeholder="全部状态"
            allow-clear
            style="width: 100%"
            @change="emitFilter"
          >
            <a-select-option value="APPROVED">已通过</a-select-option>
            <a-select-option value="PENDING_REVIEW">待审核</a-select-option>
            <a-select-option value="REJECTED">未通过</a-select-option>
          </a-select>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'

const props = defineProps<{
  visible: boolean
  category?: string | null
  format?: string | null
  lifecycleStatus?: string | null
}>()

const emit = defineEmits<{
  filter: [filter: { category?: string | null; format?: string | null; lifecycleStatus?: string | null }]
}>()

const localCategory = ref<string | null>(props.category ?? null)
const localFormat = ref<string | null>(props.format ?? null)
const localStatus = ref<string | null>(props.lifecycleStatus ?? null)

watch(
  () => [props.category, props.format, props.lifecycleStatus],
  ([c, f, s]) => {
    localCategory.value = (c as string) ?? null
    localFormat.value = (f as string) ?? null
    localStatus.value = (s as string) ?? null
  }
)

function emitFilter() {
  emit('filter', {
    category: localCategory.value,
    format: localFormat.value,
    lifecycleStatus: localStatus.value,
  })
}
</script>

<style scoped>
.asset-filter-panel {
  padding: 12px 16px;
  background: #F7F8FA;
  border-bottom: 1px solid #EEF0F3;
}
.filter-item label {
  display: block;
  margin-bottom: 4px;
  font-size: 12px;
  color: #8A94A6;
}
</style>
