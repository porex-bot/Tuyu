package com.qiu.qiupicturebackend.workspace.application;

import com.qiu.qiupicturebackend.model.entity.User;
import com.qiu.qiupicturebackend.workspace.domain.model.WorkspacePermissionSet;
import com.qiu.qiupicturebackend.workspace.infrastructure.WorkspaceLegacyAdapter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 工作区权限应用服务 —— 集中管理权限查询，供 API 和 Shell 使用。
 */
@Service
public class WorkspacePermissionApplicationService {

    @Resource
    private WorkspaceLegacyAdapter workspaceLegacyAdapter;

    /**
     * 获取当前用户在工作区中的权限集
     */
    public WorkspacePermissionSet getPermissions(Long workspaceId, User loginUser) {
        if (workspaceId == null || loginUser == null) {
            return null;
        }
        return workspaceLegacyAdapter.getWorkspacePermissions(workspaceId, loginUser);
    }
}
