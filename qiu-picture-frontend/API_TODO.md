# 未实现接口说明

## 状态
后端部分接口暂未实现，前端已将相关调用注释掉。

## 1. Space 相关接口（空间功能）

### 未实现的接口
- `spaceController.ts` - 所有空间管理接口
- `spaceUserController.ts` - 所有空间成员管理接口

### 已注释的文件
1. `src/router/index.ts` - Space 页面导入和路由
2. `src/components/GlobalHeader.vue` - 空间管理菜单
3. `src/components/GlobalSider.vue` - 我的空间、创建团队菜单
4. `src/pages/MySpacePage.vue` - 我的空间页面
5. `src/pages/AddPicturePage.vue` - 空间信息获取

### 临时方案
- 用户无法访问空间相关页面
- 侧边栏和顶部菜单不显示空间相关选项

---

## 2. AI 扩图接口

### 未实现的接口
- `createPictureOutPaintingTaskUsingPost` - 创建 AI 扩图任务
- `getPictureOutPaintingTaskUsingGet` - 获取 AI 扩图任务结果

### 已注释的文件
1. `src/components/ImageOutPainting.vue` - AI 扩图组件

### 临时方案
- 用户点击"AI 扩图"按钮会提示"AI 扩图功能暂未开放，敬请期待"
- 不会发起 API 请求

---

## 后续操作

当后端实现这些接口后：

1. 运行 `npm run openapi` 生成 API 文件
2. 全局搜索 `TODO: 后端` 找到所有注释
3. 取消注释，恢复功能
4. 删除本说明文件

