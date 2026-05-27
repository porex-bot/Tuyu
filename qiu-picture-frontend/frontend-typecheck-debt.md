# Frontend TypeScript 类型检查技术债

> 生成日期：2026-05-27
> 分支：`fix/tuyuu-launch-hardening`
> 总错误数：101（全部在遗留文件中，新图域核心模块 0 错误）

## 策略

- **上线门禁**：`npm run build`（Vite 构建），当前通过。
- **类型检查**：`npm run type-check`（vue-tsc --build），标记为遗留技术债，不作为本次上线阻断。
- **铁律**：不允许在新模块（src/widgets/*, src/pages/workspace/*, src/entities/*）中新增类型错误。

---

## 分批复核计划

### 第一批：依赖和配置修复（2 errors）— 预计 0.5h

| 文件 | 错误数 | 问题 | 修复方向 |
|---|---|---|---|
| `node_modules/@tsconfig/node22/tsconfig.json` | 1 | lib 参数值不被 TypeScript 5.6 识别 | 升级 `@tsconfig/node22` 或在 tsconfig 中覆盖 lib |
| `node_modules/vue-cropper/lib/index.ts` | 1 | 缺少 `.vue` 模块声明 | 添加 `src/types/vue-cropper.d.ts` 声明文件 |

### 第二批：新图域模块和全局布局（0 errors）— 已完成

新模块全部干净，无需处理：
`src/widgets/workspace-shell/*`, `src/widgets/asset-grid/*`, `src/widgets/collection-board/*`,
`src/widgets/activity-rail/*`, `src/widgets/ai-studio/*`, `src/widgets/approval-inbox/*`,
`src/pages/workspace/*`, `src/entities/*`, `src/components/GlobalHeader.vue`, `src/components/GlobalSider.vue`

### 第三批：旧 Picture/Space 页面（~44 errors）— 预计 3-4h

| 文件 | 错误数 | 主要问题 |
|---|---|---|
| `src/pages/AddPicturePage.vue` | 9 | route query 类型转换、possibly undefined、select options 类型 |
| `src/pages/SpaceDetailPage.vue` | 7 | spaceId 类型转换、totalSize/maxSize undefined、spaceType 索引 |
| `src/pages/SpaceAnalyzePage.vue` | 6 | route query string→number 类型不匹配 |
| `src/pages/AddSpacePage.vue` | 3 | spaceLevel 属性不存在、route query 类型 |
| `src/pages/MySpacePage.vue` | 3 | API 响应 possibly undefined |
| `src/pages/SearchPicturePage.vue` | 2 | route query null→number |
| `src/pages/PictureDetailPage.vue` | 2 | undefined→string、string→number |

**遗留组件（~38 errors）：**

| 文件 | 错误数 | 主要问题 |
|---|---|---|
| `src/components/ImageOutPainting.vue` | 10 | possibly undefined、null 类型、NodeJS 命名空间、unknown error 类型 |
| `src/components/PictureList.vue` | 9 | implicit any 参数、undefined→string |
| `src/components/PictureSearchForm.vue` | 3 | select options 类型、索引签名 |
| `src/components/PictureUpload.vue` | 2 | 泛型 code/data/message 属性、unknown error |
| `src/components/ImageCropper.vue` | 2 | implicit any、泛型属性 |
| `src/components/BatchEditPictureModal.vue` | 2 | select options 类型不匹配 |
| `src/components/UrlPictureUpload.vue` | 1 | unknown error 类型 |

### 第四批：旧分析/管理页面（~33 errors）— 预计 2-3h

| 文件 | 错误数 | 主要问题 |
|---|---|---|
| `src/components/analyze/SpaceCategoryAnalyze.vue` | 8 | 类型推断 never[]、map 不存在、implicit any |
| `src/components/analyze/SpaceUserAnalyze.vue` | 7 | 同上 + string→number |
| `src/components/analyze/SpaceTagAnalyze.vue` | 4 | 同上 |
| `src/components/analyze/SpaceSizeAnalyze.vue` | 4 | 同上 |
| `src/components/analyze/SpaceRankAnalyze.vue` | 2 | queryAll 属性不存在、possibly undefined |
| `src/pages/admin/SpaceUserManagePage.vue` | 6 | implicit any、string→number |
| `src/pages/admin/PictureManagePage.vue` | 3 | any 索引、implicit any、string→number |
| `src/pages/admin/UserManagePage.vue` | 2 | implicit any、string→number |
| `src/pages/admin/SpaceManagePage.vue` | 2 | implicit any、string→number |

---

## 常见根因归类

| 根因 | 出现次数 | 批量修复方案 |
|---|---|---|
| `string \| number` 不匹配（route query → number prop） | ~15 | 统一 `Number(route.query.xxx)` 或 `useRouteQuery` composable |
| `implicit any` 参数 | ~12 | 补充回调参数类型注解 |
| `possibly undefined` 访问 | ~10 | 添加可选链 `?.` 或 non-null 断言 |
| 泛型 `.code/.data/.message` 属性不存在 | ~8 | 统一 `API.BaseResponse<T>` 类型标注 axios 响应 |
| `{value,label}[]` → `string[]` 类型不匹配 | ~5 | Select options 统一使用正确类型 |
| `never[]` → 分析响应类型 | ~5 | 修复 `ref([])` 初始值的泛型参数 |

---

## 验收门禁

- [ ] `npm run type-check` 错误数从 101 降到 0
- [ ] 新模块持续 0 错误（CI 中可对 `src/widgets/`, `src/pages/workspace/`, `src/entities/` 单独运行严格检查）
- [ ] `npm run build` 持续通过
