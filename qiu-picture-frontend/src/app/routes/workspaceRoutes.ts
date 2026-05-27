import type { RouteRecordRaw } from 'vue-router'
import WorkspaceShell from '@/widgets/workspace-shell/WorkspaceShell.vue'
import WorkspaceHomePage from '@/pages/workspace/WorkspaceHomePage.vue'
import WorkspaceAssetsPage from '@/pages/workspace/WorkspaceAssetsPage.vue'
import WorkspaceCollectionsPage from '@/pages/workspace/WorkspaceCollectionsPage.vue'
import WorkspaceCollectionBoardPage from '@/pages/workspace/WorkspaceCollectionBoardPage.vue'
import WorkspaceApprovalsPage from '@/pages/workspace/WorkspaceApprovalsPage.vue'
import AiStudioPage from '@/pages/workspace/AiStudioPage.vue'

export const workspaceRoutes: RouteRecordRaw = {
  path: '/w/:workspaceId',
  component: WorkspaceShell,
  props: true,
  children: [
    {
      path: '',
      redirect: (to) => ({ path: `/w/${to.params.workspaceId}/home` }),
    },
    {
      path: 'home',
      name: 'workspace-home',
      component: WorkspaceHomePage,
    },
    {
      path: 'assets',
      name: 'workspace-assets',
      component: WorkspaceAssetsPage,
    },
    {
      path: 'collections',
      name: 'workspace-collections',
      component: WorkspaceCollectionsPage,
    },
    {
      path: 'collections/:collectionId',
      name: 'workspace-collection-board',
      component: WorkspaceCollectionBoardPage,
    },
    {
      path: 'ai-studio',
      name: 'workspace-ai-studio',
      component: AiStudioPage,
    },
    {
      path: 'approvals',
      name: 'workspace-approvals',
      component: WorkspaceApprovalsPage,
    },
  ],
}
