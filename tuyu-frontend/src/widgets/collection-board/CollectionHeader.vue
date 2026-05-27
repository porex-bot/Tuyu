<template>
  <div class="collection-header">
    <div class="header-left">
      <a-page-header :title="name" :sub-title="subtitle" style="padding: 0" />
    </div>
    <div class="header-right">
      <slot name="actions" />
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  name: string
  subtitle?: string
}>()
</script>

<style scoped>
.collection-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 16px;
}
</style>
