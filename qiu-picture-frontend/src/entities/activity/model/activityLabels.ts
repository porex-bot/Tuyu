/**
 * 活动类型中文标签映射。
 */
export const actionTypeLabels: Record<string, string> = {
  'workspace.created': '创建工作区',
  'asset.ingested': '上传资产',
  'asset.updated': '更新资产',
  'asset.version.created': '创建资产版本',
  'collection.created': '创建集合',
  'collection.item.added': '添加资产到集合',
  'collection.item.removed': '从集合移除资产',
  'collection.items.reordered': '重新排序集合',
  'ai.job.created': '创建 AI 任务',
  'ai.job.succeeded': 'AI 任务完成',
  'ai.job.failed': 'AI 任务失败',
  'ai.result.applied': '应用 AI 结果',
  'approval.requested': '提交审批',
  'approval.approved': '审批通过',
  'approval.rejected': '审批驳回',
  'approval.changes_requested': '审批要求修改',
  'approval.cancelled': '取消审批',
  'governance.policy.updated': '更新治理策略',
}

export function getActionLabel(actionType: string): string {
  return actionTypeLabels[actionType] ?? actionType
}

export const targetTypeLabels: Record<string, string> = {
  workspace: '工作区',
  asset: '资产',
  collection: '集合',
  collection_item: '集合条目',
}
