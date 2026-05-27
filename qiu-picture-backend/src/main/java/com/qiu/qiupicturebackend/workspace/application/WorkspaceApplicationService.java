package com.qiu.qiupicturebackend.workspace.application;

import com.qiu.qiupicturebackend.model.entity.User;
import com.qiu.qiupicturebackend.workspace.infrastructure.WorkspaceLegacyAdapter;
import com.qiu.qiupicturebackend.workspace.representation.WorkspaceMemberResponse;
import com.qiu.qiupicturebackend.workspace.representation.WorkspaceResponse;
import com.qiu.qiupicturebackend.workspace.representation.WorkspaceUsageResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 工作区应用服务 —— API 层与 LegacyAdapter 之间的唯一边界。
 * 不直接依赖 SpaceService / SpaceUserService。
 */
@Service
public class WorkspaceApplicationService {

    @Resource
    private WorkspaceLegacyAdapter workspaceLegacyAdapter;

    /**
     * 列出当前用户的所有工作区
     */
    public List<WorkspaceResponse> listMyWorkspaces(User loginUser) {
        if (loginUser == null) {
            return Collections.emptyList();
        }
        return workspaceLegacyAdapter.listMyWorkspaces(loginUser);
    }

    /**
     * 按 ID 获取工作区详情
     */
    public WorkspaceResponse getWorkspaceById(Long workspaceId, User loginUser) {
        if (workspaceId == null || loginUser == null) {
            return null;
        }
        return workspaceLegacyAdapter.getWorkspaceById(workspaceId, loginUser);
    }

    /**
     * 获取工作区用量
     */
    public WorkspaceUsageResponse getWorkspaceUsage(Long workspaceId) {
        if (workspaceId == null) {
            return null;
        }
        return workspaceLegacyAdapter.getWorkspaceUsage(workspaceId);
    }

    /**
     * 获取工作区成员列表
     */
    public List<WorkspaceMemberResponse> listWorkspaceMembers(Long workspaceId, User loginUser) {
        if (workspaceId == null || loginUser == null) {
            return Collections.emptyList();
        }
        return workspaceLegacyAdapter.listWorkspaceMembers(workspaceId, loginUser);
    }
}
