<template>
  <div class="asset-thumbnail" :style="{ backgroundColor: bgColor }">
    <img
      v-if="src"
      :src="src"
      :alt="alt"
      class="asset-thumbnail-img"
      loading="lazy"
      @error="onError"
    />
    <div v-else class="asset-thumbnail-fallback">
      <FileImageOutlined />
    </div>
    <div class="asset-thumbnail-overlay">
      <span class="asset-thumbnail-format">{{ format?.toUpperCase() }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { FileImageOutlined } from '@ant-design/icons-vue'

const props = defineProps<{
  src: string | null | undefined
  alt?: string
  format?: string | null
  dominantColor?: string | null
}>()

const failed = ref(false)

const bgColor = computed(() => {
  if (failed.value || !props.src) return '#f5f5f5'
  if (props.dominantColor) return props.dominantColor + '20'
  return '#f5f5f5'
})

function onError() {
  failed.value = true
}
</script>

<style scoped>
.asset-thumbnail {
  position: relative;
  width: 100%;
  padding-bottom: 75%;
  overflow: hidden;
  border-radius: 8px;
  background: #f5f5f5;
}
.asset-thumbnail-img {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.asset-thumbnail-fallback {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  color: #d9d9d9;
}
.asset-thumbnail-overlay {
  position: absolute;
  bottom: 4px;
  right: 4px;
}
.asset-thumbnail-format {
  padding: 1px 6px;
  font-size: 11px;
  color: #fff;
  background: rgba(0, 0, 0, 0.55);
  border-radius: 3px;
  text-transform: uppercase;
}
</style>
