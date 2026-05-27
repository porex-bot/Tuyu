export const statusLabels: Record<string, string> = {
  draft: '草稿',
  pending: '待审批',
  approved: '已通过',
  rejected: '已驳回',
  changes_requested: '需修改',
  cancelled: '已取消',
}

export const statusColors: Record<string, string> = {
  draft: 'default',
  pending: 'processing',
  approved: 'success',
  rejected: 'error',
  changes_requested: 'warning',
  cancelled: 'default',
}

export const decisionLabels: Record<string, string> = {
  approve: '通过',
  reject: '驳回',
  request_changes: '要求修改',
}

export const targetTypeLabels: Record<string, string> = {
  asset: '素材',
  collection: '集合',
  ai_result: '处理结果',
}

export function getStatusLabel(status: string): string {
  return statusLabels[status] ?? status
}

export function getStatusColor(status: string): string {
  return statusColors[status] ?? 'default'
}

export function getDecisionLabel(decisionType: string): string {
  return decisionLabels[decisionType] ?? decisionType
}

export function getTargetTypeLabel(targetType: string): string {
  return targetTypeLabels[targetType] ?? targetType
}
