package com.qiu.qiupicturebackend.workspace.infrastructure;

import cn.hutool.core.collection.CollUtil;
import com.qiu.qiupicturebackend.manager.auth.SpaceUserAuthManager;
import com.qiu.qiupicturebackend.model.entity.Space;
import com.qiu.qiupicturebackend.model.entity.SpaceUser;
import com.qiu.qiupicturebackend.model.entity.User;
import com.qiu.qiupicturebackend.model.enums.SpaceTypeEnum;
import com.qiu.qiupicturebackend.model.vo.UserVO;
import com.qiu.qiupicturebackend.service.SpaceService;
import com.qiu.qiupicturebackend.service.SpaceUserService;
import com.qiu.qiupicturebackend.service.UserService;
import com.qiu.qiupicturebackend.workspace.domain.model.WorkspaceMemberView;
import com.qiu.qiupicturebackend.workspace.domain.model.WorkspacePermissionSet;
import com.qiu.qiupicturebackend.workspace.domain.model.WorkspaceView;
import com.qiu.qiupicturebackend.workspace.representation.WorkspaceMemberResponse;
import com.qiu.qiupicturebackend.workspace.representation.WorkspaceResponse;
import com.qiu.qiupicturebackend.workspace.representation.WorkspaceUsageResponse;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 旧版 Space/SpaceUser 数据到 Workspace 读模型的适配器。
 * 只读不写，不直接访问 Mapper。
 */
@Component
public class WorkspaceLegacyAdapter {

    @Resource
    private SpaceService spaceService;

    @Resource
    private SpaceUserService spaceUserService;

    @Resource
    private UserService userService;

    @Resource
    private SpaceUserAuthManager spaceUserAuthManager;

    /**
     * 列出当前用户的所有工作区（私有 + 参与的团队）
     */
    public List<WorkspaceResponse> listMyWorkspaces(User loginUser) {
        if (loginUser == null) {
            return Collections.emptyList();
        }

        // 私有空间
        List<Space> privateSpaces = spaceService.lambdaQuery()
                .eq(Space::getUserId, loginUser.getId())
                .eq(Space::getSpaceType, SpaceTypeEnum.PRIVATE.getValue())
                .list();

        // 团队空间（通过 SpaceUser 关联）
        List<SpaceUser> spaceUsers = spaceUserService.lambdaQuery()
                .eq(SpaceUser::getUserId, loginUser.getId())
                .list();
        Set<Long> teamSpaceIds = spaceUsers.stream()
                .map(SpaceUser::getSpaceId)
                .collect(Collectors.toSet());
        List<Space> teamSpaces = CollUtil.isEmpty(teamSpaceIds)
                ? Collections.emptyList()
                : spaceService.listByIds(teamSpaceIds);

        List<Space> allSpaces = new ArrayList<>();
        allSpaces.addAll(privateSpaces);
        allSpaces.addAll(teamSpaces);

        return allSpaces.stream()
                .map(space -> toWorkspaceResponse(space, loginUser))
                .collect(Collectors.toList());
    }

    /**
     * 按 ID 加载单个工作区
     */
    public WorkspaceResponse getWorkspaceById(Long workspaceId, User loginUser) {
        Space space = spaceService.getById(workspaceId);
        if (space == null) {
            return null;
        }
        return toWorkspaceResponse(space, loginUser);
    }

    /**
     * 获取当前用户在工作区中的权限集
     */
    public WorkspacePermissionSet getWorkspacePermissions(Long workspaceId, User loginUser) {
        Space space = spaceService.getById(workspaceId);
        if (space == null) {
            return null;
        }
        return toPermissionSet(space, loginUser);
    }

    /**
     * 获取工作区用量信息
     */
    public WorkspaceUsageResponse getWorkspaceUsage(Long workspaceId) {
        Space space = spaceService.getById(workspaceId);
        if (space == null) {
            return null;
        }
        return toUsageResponse(space);
    }

    /**
     * 列出工作区成员（团队空间返回 SpaceUser 列表，私有空间返回仅 owner）
     */
    public List<WorkspaceMemberResponse> listWorkspaceMembers(Long workspaceId, User loginUser) {
        Space space = spaceService.getById(workspaceId);
        if (space == null) {
            return Collections.emptyList();
        }

        SpaceTypeEnum typeEnum = SpaceTypeEnum.getEnumByValue(space.getSpaceType());
        if (typeEnum == SpaceTypeEnum.PRIVATE) {
            User owner = userService.getById(space.getUserId());
            if (owner == null) {
                return Collections.emptyList();
            }
            WorkspaceMemberView memberView = new WorkspaceMemberView();
            memberView.setId(space.getId());
            memberView.setWorkspaceId(space.getId());
            memberView.setUserId(owner.getId());
            memberView.setRole("admin");
            memberView.setCreateTime(space.getCreateTime());

            WorkspaceMemberResponse response = new WorkspaceMemberResponse();
            response.setMember(memberView);
            response.setUser(userService.getUserVO(owner));
            return Collections.singletonList(response);
        }

        // 团队空间
        List<SpaceUser> spaceUsers = spaceUserService.lambdaQuery()
                .eq(SpaceUser::getSpaceId, workspaceId)
                .list();
        if (CollUtil.isEmpty(spaceUsers)) {
            return Collections.emptyList();
        }

        List<Long> userIds = spaceUsers.stream()
                .map(SpaceUser::getUserId)
                .collect(Collectors.toList());
        List<User> users = userService.listByIds(userIds);

        return spaceUsers.stream().map(su -> {
            WorkspaceMemberView memberView = toMemberView(su);

            User matchedUser = users.stream()
                    .filter(u -> u.getId().equals(su.getUserId()))
                    .findFirst().orElse(null);

            WorkspaceMemberResponse response = new WorkspaceMemberResponse();
            response.setMember(memberView);
            response.setUser(userService.getUserVO(matchedUser));
            return response;
        }).collect(Collectors.toList());
    }

    // ---- private mapping helpers ----

    private WorkspaceResponse toWorkspaceResponse(Space space, User loginUser) {
        WorkspaceView view = toWorkspaceView(space);

        User owner = userService.getById(space.getUserId());
        UserVO ownerVO = userService.getUserVO(owner);

        List<String> permissionList = spaceUserAuthManager.getPermissionList(space, loginUser);

        WorkspaceResponse response = new WorkspaceResponse();
        response.setWorkspace(view);
        response.setUser(ownerVO);
        response.setPermissionList(permissionList);
        return response;
    }

    private WorkspaceView toWorkspaceView(Space space) {
        WorkspaceView view = new WorkspaceView();
        view.setWorkspaceId(space.getId());
        view.setLegacySpaceId(space.getId());
        view.setName(space.getSpaceName());
        view.setType(space.getSpaceType());
        view.setLevel(space.getSpaceLevel());
        view.setUserId(space.getUserId());
        view.setCreateTime(space.getCreateTime());
        view.setEditTime(space.getEditTime());
        view.setUpdateTime(space.getUpdateTime());
        return view;
    }

    private WorkspaceMemberView toMemberView(SpaceUser spaceUser) {
        WorkspaceMemberView view = new WorkspaceMemberView();
        view.setId(spaceUser.getId());
        view.setWorkspaceId(spaceUser.getSpaceId());
        view.setUserId(spaceUser.getUserId());
        view.setRole(spaceUser.getSpaceRole());
        view.setCreateTime(spaceUser.getCreateTime());
        return view;
    }

    private WorkspacePermissionSet toPermissionSet(Space space, User loginUser) {
        List<String> permissions = spaceUserAuthManager.getPermissionList(space, loginUser);

        String role = null;
        SpaceTypeEnum typeEnum = SpaceTypeEnum.getEnumByValue(space.getSpaceType());
        if (typeEnum == SpaceTypeEnum.PRIVATE) {
            role = space.getUserId().equals(loginUser.getId()) ? "admin" : null;
        } else if (typeEnum == SpaceTypeEnum.TEAM) {
            SpaceUser spaceUser = spaceUserService.lambdaQuery()
                    .eq(SpaceUser::getSpaceId, space.getId())
                    .eq(SpaceUser::getUserId, loginUser.getId())
                    .one();
            role = spaceUser != null ? spaceUser.getSpaceRole() : null;
        }

        WorkspacePermissionSet set = new WorkspacePermissionSet();
        set.setWorkspaceId(space.getId());
        set.setRole(role);
        set.setPermissions(permissions);
        return set;
    }

    private WorkspaceUsageResponse toUsageResponse(Space space) {
        WorkspaceUsageResponse response = new WorkspaceUsageResponse();
        response.setWorkspaceId(space.getId());
        response.setMaxSize(space.getMaxSize());
        response.setTotalSize(space.getTotalSize());
        response.setMaxCount(space.getMaxCount());
        response.setTotalCount(space.getTotalCount());
        response.setStorageRatio(calcRatio(space.getTotalSize(), space.getMaxSize()));
        response.setCountRatio(calcRatio(space.getTotalCount(), space.getMaxCount()));
        return response;
    }

    private double calcRatio(Long used, Long max) {
        if (max == null || max <= 0) {
            return 0.0;
        }
        if (used == null || used <= 0) {
            return 0.0;
        }
        return Math.min(1.0, (double) used / (double) max);
    }
}
