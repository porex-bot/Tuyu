<template>
  <div id="workspaceTopBar">
    <a-row :wrap="false" align="middle">
      <!-- 工作区名称 -->
      <a-col flex="auto">
        <div class="ws-brand">
          <router-link to="/" class="ws-logo-link">
            <span class="ws-logo-text">图域</span>
          </router-link>
          <span class="ws-separator">/</span>
          <span class="ws-name">
            {{ workspaceStore.currentWorkspace?.workspace?.name ?? '加载中…' }}
          </span>
        </div>
      </a-col>

      <!-- 用户区 -->
      <a-col>
        <div class="ws-user">
          <a-button type="text" style="margin-right: 8px" @click="$emit('toggleActivity')">
            <template #icon><ClockCircleOutlined /></template>
          </a-button>
          <a-dropdown>
            <a-space class="ws-user-trigger">
              <a-avatar
                :src="loginUserStore.loginUser.userAvatar"
                :size="32"
              />
              <span>{{ loginUserStore.loginUser.userName ?? '用户' }}</span>
            </a-space>
            <template #overlay>
              <a-menu>
                <a-menu-item>
                  <router-link to="/my_space">
                    <UserOutlined />
                    返回旧版空间
                  </router-link>
                </a-menu-item>
                <a-menu-item @click="doLogout">
                  <LogoutOutlined />
                  退出登录
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { UserOutlined, LogoutOutlined, ClockCircleOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
import { useWorkspaceStore } from '@/entities/workspace/model/useWorkspaceStore'
import { logoutUsingPost } from '@/api/userController'

defineProps<{ workspaceId: number }>()

defineEmits<{
  toggleActivity: []
}>()

const router = useRouter()
const loginUserStore = useLoginUserStore()
const workspaceStore = useWorkspaceStore()

const doLogout = async () => {
  const res = await logoutUsingPost()
  if (res.data.code === 0) {
    loginUserStore.setLoginUser({ userName: '未登录' })
    message.success('退出登录成功')
    await router.push('/user/login')
  } else {
    message.error('退出登录失败，' + res.data.message)
  }
}
</script>

<style scoped>
#workspaceTopBar {
  width: 100%;
}

.ws-brand {
  display: flex;
  align-items: center;
  gap: 8px;
}

.ws-logo-link {
  font-weight: 700;
  font-size: 16px;
  color: #1F2933;
}

.ws-separator {
  color: #B6BEC9;
  font-size: 16px;
}

.ws-name {
  font-size: 15px;
  color: #4B5563;
}

.ws-user-trigger {
  cursor: pointer;
}
</style>
