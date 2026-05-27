<template>
  <div class="activity-filter">
    <a-radio-group
      :value="activeFilter"
      size="small"
      @change="(e: any) => $emit('filter', e.target.value)"
    >
      <a-radio-button value="all">全部</a-radio-button>
      <a-radio-button value="collection">集合</a-radio-button>
      <a-radio-button value="asset">资产</a-radio-button>
    </a-radio-group>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  activeFilter: string
}>()

defineEmits<{
  filter: [value: string]
}>()
</script>

<style scoped>
.activity-filter {
  padding: 8px 0;
}
</style>
