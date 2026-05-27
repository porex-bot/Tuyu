<template>
  <div id="basicLayout">
    <a-layout style="min-height: 100vh">
      <a-layout-header class="header">
        <GlobalHeader />
      </a-layout-header>
      <a-layout>
        <GlobalSider class="sider" />
        <a-layout-content class="content">
          <router-view />
        </a-layout-content>
      </a-layout>
      <a-layout-footer class="footer">
        图域 - 图片管理平台
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script setup lang="ts">
import GlobalHeader from '@/components/GlobalHeader.vue'
import GlobalSider from "@/components/GlobalSider.vue";
</script>

<style scoped>
#basicLayout .header {
  padding-inline: 20px;
  background: #fff;
  color: unset;
}

#basicLayout .sider {
  background: var(--color-bg-sidebar, #F3F5F7);
  border-right: 1px solid var(--color-border, #E5E7EB);
  padding-top: 16px;
}

#basicLayout :deep(.ant-menu-root) {
  border-bottom: none !important;
  border-inline-end: none !important;
  background: transparent;
}

#basicLayout .content {
  padding: 24px;
  background: #F7F8FA;
  margin-bottom: 24px;
  min-height: calc(100vh - 56px - 48px);
}

#basicLayout .footer {
  background: #F3F5F7;
  padding: 14px;
  text-align: center;
  font-size: 13px;
  color: var(--color-text-secondary, #8A94A6);
  border-top: 1px solid var(--color-border-light, #EEF0F3);
}
</style>
