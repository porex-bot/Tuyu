/**
 * AI 模块中文标签映射。
 */

export const jobStatusLabels: Record<string, string> = {
  created: '已创建',
  queued: '排队中',
  running: '处理中',
  succeeded: '已完成',
  failed: '失败',
  cancelled: '已取消',
  applied: '已应用',
  discarded: '已丢弃',
}

export const resultApplyStatusLabels: Record<string, string> = {
  pending: '待处理',
  applied: '已应用',
  discarded: '已丢弃',
}

export const capabilityLabels: Record<string, string> = {
  outpainting: '智能扩图',
  similar_search: '以图搜图',
  auto_tagging: '自动打标',
  caption: '智能描述',
}

export function getJobStatusLabel(status: string): string {
  return jobStatusLabels[status] ?? status
}

export function getCapabilityLabel(key: string): string {
  return capabilityLabels[key] ?? key
}
