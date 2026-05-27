package com.qiu.qiupicturebackend.workspace.api;

import com.qiu.qiupicturebackend.common.BaseResponse;
import com.qiu.qiupicturebackend.common.ResultUtils;
import com.qiu.qiupicturebackend.exception.ErrorCode;
import com.qiu.qiupicturebackend.exception.ThrowUtils;
import com.qiu.qiupicturebackend.model.entity.User;
import com.qiu.qiupicturebackend.service.UserService;
import com.qiu.qiupicturebackend.workspace.application.WorkspaceApplicationService;
import com.qiu.qiupicturebackend.workspace.application.WorkspacePermissionApplicationService;
import com.qiu.qiupicturebackend.workspace.domain.model.WorkspacePermissionSet;
import com.qiu.qiupicturebackend.workspace.representation.WorkspaceResponse;
import com.qiu.qiupicturebackend.workspace.representation.WorkspaceUsageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 工作区 v1 只读 API。
 * 与旧 /space 端点共存，不修改 SpaceController。
 */
@Slf4j
@RestController
@RequestMapping("/v1/workspaces")
public class WorkspaceController {

    @Resource
    private WorkspaceApplicationService workspaceApplicationService;

    @Resource
    private WorkspacePermissionApplicationService workspacePermissionApplicationService;

    @Resource
    private UserService userService;

    /**
     * 获取当前用户的工作区列表
     */
    @GetMapping("/my")
    public BaseResponse<List<WorkspaceResponse>> listMyWorkspaces(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<WorkspaceResponse> workspaces = workspaceApplicationService.listMyWorkspaces(loginUser);
        return ResultUtils.success(workspaces);
    }

    /**
     * 按 ID 获取工作区详情
     */
    @GetMapping("/{workspaceId}")
    public BaseResponse<WorkspaceResponse> getWorkspaceById(@PathVariable Long workspaceId,
                                                             HttpServletRequest request) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        WorkspaceResponse workspace = workspaceApplicationService.getWorkspaceById(workspaceId, loginUser);
        ThrowUtils.throwIf(workspace == null, ErrorCode.NOT_FOUND_ERROR, "工作区不存在");
        return ResultUtils.success(workspace);
    }

    /**
     * 获取当前用户在工作区中的权限
     */
    @GetMapping("/{workspaceId}/permissions")
    public BaseResponse<WorkspacePermissionSet> getWorkspacePermissions(@PathVariable Long workspaceId,
                                                                         HttpServletRequest request) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        WorkspacePermissionSet permissions = workspacePermissionApplicationService.getPermissions(workspaceId, loginUser);
        ThrowUtils.throwIf(permissions == null, ErrorCode.NOT_FOUND_ERROR, "工作区不存在");
        return ResultUtils.success(permissions);
    }

    /**
     * 获取工作区用量
     */
    @GetMapping("/{workspaceId}/usage")
    public BaseResponse<WorkspaceUsageResponse> getWorkspaceUsage(@PathVariable Long workspaceId) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        WorkspaceUsageResponse usage = workspaceApplicationService.getWorkspaceUsage(workspaceId);
        ThrowUtils.throwIf(usage == null, ErrorCode.NOT_FOUND_ERROR, "工作区不存在");
        return ResultUtils.success(usage);
    }
}
