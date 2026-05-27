<template>
  <div id="workspaceSidebar">
    <a-layout-sider
      v-if="loginUserStore.loginUser.id"
      width="200"
      breakpoint="lg"
      collapsed-width="0"
      class="ws-sider"
    >
      <a-menu
        v-model:selectedKeys="current"
        mode="inline"
        :items="menuItems"
        @click="doMenuClick"
      />
    </a-layout-sider>
  </div>
</template>

<script setup lang="ts">
import { computed, h, ref } from 'vue'
import { HomeOutlined, PictureOutlined, FolderOpenOutlined, EditOutlined, SafetyOutlined } from '@ant-design/icons-vue'
import { useRouter, useRoute } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore'

const props = defineProps<{ workspaceId: number }>()

const router = useRouter()
const route = useRoute()
const loginUserStore = useLoginUserStore()

const menuItems = computed(() => [
  {
    key: `/w/${props.workspaceId}/home`,
    icon: () => h(HomeOutlined),
    label: '工作台',
  },
  {
    key: `/w/${props.workspaceId}/assets`,
    icon: () => h(PictureOutlined),
    label: '素材库',
  },
  {
    key: `/w/${props.workspaceId}/collections`,
    icon: () => h(FolderOpenOutlined),
    label: '集合',
  },
  {
    key: `/w/${props.workspaceId}/ai-studio`,
    icon: () => h(EditOutlined),
    label: '编辑工具',
  },
  {
    key: `/w/${props.workspaceId}/approvals`,
    icon: () => h(SafetyOutlined),
    label: '审批',
  },
])

const current = ref<string[]>([route.path])

const doMenuClick = ({ key }: { key: string }) => {
  router.push(key)
}

router.afterEach((to) => {
  current.value = [to.path]
})
</script>

<style scoped>
.ws-sider {
  background: #F3F5F7 !important;
  border-right: 1px solid #E5E7EB;
  padding-top: 12px;
}

.ws-sider :deep(.ant-menu) {
  background: transparent;
}

.ws-sider :deep(.ant-menu-item) {
  margin: 2px 8px !important;
  width: auto !important;
  border-radius: 6px !important;
  color: #4B5563 !important;
  height: 40px !important;
  line-height: 40px !important;
  font-weight: 500;
  font-size: 14px;
}

.ws-sider :deep(.ant-menu-item:hover) {
  background-color: #E2EFF2 !important;
  color: #1F2933 !important;
}

.ws-sider :deep(.ant-menu-item-selected) {
  background-color: #EEF5F7 !important;
  color: #3B82A0 !important;
  font-weight: 600;
  border-radius: 0 6px 6px 0 !important;
}
</style>
