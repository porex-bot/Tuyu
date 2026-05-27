<template>
  <div class="ai-parameter-panel">
    <div class="panel-label">处理设置</div>

    <template v-if="capabilityKey === 'outpainting'">
      <div class="param-row">
        <span class="param-name">水平扩展 (xScale)</span>
        <a-slider
          :min="1.0"
          :max="3.0"
          :step="0.1"
          :value="params.xScale ?? 1.5"
          @change="onXScaleChange"
        />
      </div>
      <div class="param-row">
        <span class="param-name">垂直扩展 (yScale)</span>
        <a-slider
          :min="1.0"
          :max="3.0"
          :step="0.1"
          :value="params.yScale ?? 1.5"
          @change="onYScaleChange"
        />
      </div>
      <div class="param-row">
        <span class="param-name">输出比例</span>
        <a-select
          :value="params.outputRatio ?? ''"
          style="width: 160px"
          @change="onOutputRatioChange"
        >
          <a-select-option value="">默认</a-select-option>
          <a-select-option value="1:1">1:1</a-select-option>
          <a-select-option value="3:4">3:4</a-select-option>
          <a-select-option value="4:3">4:3</a-select-option>
          <a-select-option value="9:16">9:16</a-select-option>
          <a-select-option value="16:9">16:9</a-select-option>
        </a-select>
      </div>
    </template>

    <div v-else class="no-params">
      该工具暂无参数设置。
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  capabilityKey: string | null
  params: Record<string, unknown>
}>()

const emit = defineEmits<{
  updateParam: [key: string, value: unknown]
}>()

function updateParam(key: string, value: unknown) {
  emit('updateParam', key, value)
}

function onXScaleChange(v: number) {
  updateParam('xScale', v)
}
function onYScaleChange(v: number) {
  updateParam('yScale', v)
}
function onOutputRatioChange(v: string) {
  updateParam('outputRatio', v)
}
</script>

<style scoped>
.ai-parameter-panel {
  padding: 16px 20px;
  border-top: 1px solid #EEF0F3;
}
.panel-label {
  font-size: 13px;
  font-weight: 600;
  color: #1F2933;
  margin-bottom: 12px;
}
.param-row {
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  gap: 12px;
}
.param-name {
  font-size: 13px;
  color: #4B5563;
  min-width: 130px;
}
.no-params {
  font-size: 13px;
  color: #8A94A6;
}
</style>
