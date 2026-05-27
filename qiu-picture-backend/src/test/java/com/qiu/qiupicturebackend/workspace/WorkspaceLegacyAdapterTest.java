package com.qiu.qiupicturebackend.workspace;

import com.qiu.qiupicturebackend.manager.auth.SpaceUserAuthManager;
import com.qiu.qiupicturebackend.model.entity.Space;
import com.qiu.qiupicturebackend.model.entity.SpaceUser;
import com.qiu.qiupicturebackend.model.entity.User;
import com.qiu.qiupicturebackend.model.enums.SpaceTypeEnum;
import com.qiu.qiupicturebackend.model.vo.UserVO;
import com.qiu.qiupicturebackend.service.SpaceService;
import com.qiu.qiupicturebackend.service.SpaceUserService;
import com.qiu.qiupicturebackend.service.UserService;
import com.qiu.qiupicturebackend.workspace.domain.model.WorkspacePermissionSet;
import com.qiu.qiupicturebackend.workspace.infrastructure.WorkspaceLegacyAdapter;
import com.qiu.qiupicturebackend.workspace.representation.WorkspaceMemberResponse;
import com.qiu.qiupicturebackend.workspace.representation.WorkspaceResponse;
import com.qiu.qiupicturebackend.workspace.representation.WorkspaceUsageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * WorkspaceLegacyAdapter 单元测试 —— 验证 Space → Workspace 映射。
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WorkspaceLegacyAdapterTest {

    @Mock
    private SpaceService spaceService;

    @Mock
    private SpaceUserService spaceUserService;

    @Mock
    private UserService userService;

    @Mock
    private SpaceUserAuthManager spaceUserAuthManager;

    @InjectMocks
    private WorkspaceLegacyAdapter adapter;

    private User loginUser;
    private Space privateSpace;
    private Space teamSpaceBar;
    private Space teamSpaceBaz;
    private SpaceUser teamMemberRecord;

    @BeforeEach
    void setUp() {
        loginUser = new User();
        loginUser.setId(1L);
        loginUser.setUserName("test");

        User owner = new User();
        owner.setId(1L);
        owner.setUserName("test");

        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUserName("other");

        UserVO ownerVO = new UserVO();
        ownerVO.setId(1L);
        ownerVO.setUserName("test");

        UserVO otherVO = new UserVO();
        otherVO.setId(2L);
        otherVO.setUserName("other");

        privateSpace = new Space();
        privateSpace.setId(100L);
        privateSpace.setSpaceName("我的私有空间");
        privateSpace.setSpaceType(SpaceTypeEnum.PRIVATE.getValue());
        privateSpace.setSpaceLevel(0);
        privateSpace.setUserId(1L);
        privateSpace.setMaxSize(100L * 1024 * 1024);
        privateSpace.setTotalSize(50L * 1024 * 1024);
        privateSpace.setMaxCount(100L);
        privateSpace.setTotalCount(30L);
        privateSpace.setCreateTime(new Date());
        privateSpace.setEditTime(new Date());
        privateSpace.setUpdateTime(new Date());

        teamSpaceBar = new Space();
        teamSpaceBar.setId(200L);
        teamSpaceBar.setSpaceName("团队空间Bar");
        teamSpaceBar.setSpaceType(SpaceTypeEnum.TEAM.getValue());
        teamSpaceBar.setSpaceLevel(1);
        teamSpaceBar.setUserId(2L);
        teamSpaceBar.setMaxSize(1000L * 1024 * 1024);
        teamSpaceBar.setTotalSize(200L * 1024 * 1024);
        teamSpaceBar.setMaxCount(1000L);
        teamSpaceBar.setTotalCount(150L);
        teamSpaceBar.setCreateTime(new Date());
        teamSpaceBar.setEditTime(new Date());
        teamSpaceBar.setUpdateTime(new Date());

        teamSpaceBaz = new Space();
        teamSpaceBaz.setId(300L);
        teamSpaceBaz.setSpaceName("团队空间Baz");
        teamSpaceBaz.setSpaceType(SpaceTypeEnum.TEAM.getValue());
        teamSpaceBaz.setSpaceLevel(1);
        teamSpaceBaz.setUserId(3L);
        teamSpaceBaz.setMaxSize(1000L * 1024 * 1024);
        teamSpaceBaz.setTotalSize(0L);
        teamSpaceBaz.setMaxCount(1000L);
        teamSpaceBaz.setTotalCount(0L);
        teamSpaceBaz.setCreateTime(new Date());
        teamSpaceBaz.setEditTime(new Date());
        teamSpaceBaz.setUpdateTime(new Date());

        teamMemberRecord = new SpaceUser();
        teamMemberRecord.setId(10L);
        teamMemberRecord.setSpaceId(200L);
        teamMemberRecord.setUserId(1L);
        teamMemberRecord.setSpaceRole("editor");
        teamMemberRecord.setCreateTime(new Date());

        // ---- default stubs ----
        // userService stubs
        when(userService.getUserVO(any())).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            if (u == null) return null;
            UserVO vo = new UserVO();
            vo.setId(u.getId());
            vo.setUserName(u.getUserName());
            return vo;
        });
        when(userService.getById(eq(1L))).thenReturn(owner);
        when(userService.getById(eq(2L))).thenReturn(otherUser);

        // auth manager stubs
        when(spaceUserAuthManager.getPermissionList(any(), any())).thenReturn(Arrays.asList("view", "edit"));
    }

    // ==================== listMyWorkspaces ====================

    @Test
    void listMyWorkspaces_shouldReturnEmpty_forNullUser() {
        List<WorkspaceResponse> result = adapter.listMyWorkspaces(null);
        assertTrue(result.isEmpty());
    }

    // listMyWorkspaces with non-null user requires lambda chain mocking
    // (MyBatis Plus fluent API). Prefer integration test with real data.

    // ==================== getWorkspaceById ====================

    @Test
    void getWorkspaceById_shouldReturnNull_whenSpaceNotFound() {
        when(spaceService.getById(999L)).thenReturn(null);

        WorkspaceResponse result = adapter.getWorkspaceById(999L, loginUser);
        assertNull(result);
    }

    @Test
    void getWorkspaceById_shouldMapPrivateSpace() {
        when(spaceService.getById(100L)).thenReturn(privateSpace);

        WorkspaceResponse result = adapter.getWorkspaceById(100L, loginUser);
        assertNotNull(result);
        assertNotNull(result.getWorkspace());
        assertEquals(100L, result.getWorkspace().getWorkspaceId());
        assertEquals(100L, result.getWorkspace().getLegacySpaceId());
        assertEquals("我的私有空间", result.getWorkspace().getName());
        assertEquals(SpaceTypeEnum.PRIVATE.getValue(), result.getWorkspace().getType());
        assertEquals(0, result.getWorkspace().getLevel());
        assertEquals(1L, result.getWorkspace().getUserId());
    }

    @Test
    void getWorkspaceById_shouldIncludePermissionList() {
        when(spaceService.getById(100L)).thenReturn(privateSpace);

        WorkspaceResponse result = adapter.getWorkspaceById(100L, loginUser);
        assertNotNull(result.getPermissionList());
        assertTrue(result.getPermissionList().contains("view"));
        assertTrue(result.getPermissionList().contains("edit"));
    }

    // ==================== getWorkspacePermissions ====================

    @Test
    void getWorkspacePermissions_shouldReturnNull_whenSpaceNotFound() {
        when(spaceService.getById(999L)).thenReturn(null);

        WorkspacePermissionSet result = adapter.getWorkspacePermissions(999L, loginUser);
        assertNull(result);
    }

    @Test
    void getWorkspacePermissions_shouldSetAdminRole_forPrivateSpaceOwner() {
        when(spaceService.getById(100L)).thenReturn(privateSpace);

        WorkspacePermissionSet result = adapter.getWorkspacePermissions(100L, loginUser);
        assertNotNull(result);
        assertEquals(100L, result.getWorkspaceId());
        assertEquals("admin", result.getRole());
        assertTrue(result.getPermissions().contains("view"));
    }

    // ==================== getWorkspaceUsage ====================

    @Test
    void getWorkspaceUsage_shouldReturnNull_whenSpaceNotFound() {
        when(spaceService.getById(999L)).thenReturn(null);

        WorkspaceUsageResponse result = adapter.getWorkspaceUsage(999L);
        assertNull(result);
    }

    @Test
    void getWorkspaceUsage_shouldMapFields() {
        when(spaceService.getById(100L)).thenReturn(privateSpace);

        WorkspaceUsageResponse result = adapter.getWorkspaceUsage(100L);
        assertNotNull(result);
        assertEquals(100L, result.getWorkspaceId());
        assertEquals(100L * 1024 * 1024, result.getMaxSize());
        assertEquals(50L * 1024 * 1024, result.getTotalSize());
        assertEquals(100L, result.getMaxCount());
        assertEquals(30L, result.getTotalCount());
    }

    @Test
    void getWorkspaceUsage_shouldCalculateRatios() {
        when(spaceService.getById(100L)).thenReturn(privateSpace);

        WorkspaceUsageResponse result = adapter.getWorkspaceUsage(100L);
        assertEquals(0.5, result.getStorageRatio(), 0.01);
        assertEquals(0.3, result.getCountRatio(), 0.01);
    }

    @Test
    void getWorkspaceUsage_shouldReturnZeroRatio_whenTotalIsZero() {
        when(spaceService.getById(300L)).thenReturn(teamSpaceBaz);

        WorkspaceUsageResponse result = adapter.getWorkspaceUsage(300L);
        assertEquals(0.0, result.getStorageRatio(), 0.01);
        assertEquals(0.0, result.getCountRatio(), 0.01);
    }

    @Test
    void getWorkspaceUsage_shouldReturnZeroRatio_whenMaxIsNull() {
        Space space = new Space();
        space.setId(400L);
        space.setMaxSize(null);
        space.setTotalSize(100L);
        space.setMaxCount(null);
        space.setTotalCount(50L);
        when(spaceService.getById(400L)).thenReturn(space);

        WorkspaceUsageResponse result = adapter.getWorkspaceUsage(400L);
        assertEquals(0.0, result.getStorageRatio(), 0.01);
        assertEquals(0.0, result.getCountRatio(), 0.01);
    }

    // ==================== listWorkspaceMembers ====================

    @Test
    void listWorkspaceMembers_shouldReturnEmpty_whenSpaceNotFound() {
        when(spaceService.getById(999L)).thenReturn(null);

        List<WorkspaceMemberResponse> result = adapter.listWorkspaceMembers(999L, loginUser);
        assertTrue(result.isEmpty());
    }

    @Test
    void listWorkspaceMembers_shouldReturnOwner_forPrivateSpace() {
        when(spaceService.getById(100L)).thenReturn(privateSpace);

        List<WorkspaceMemberResponse> result = adapter.listWorkspaceMembers(100L, loginUser);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getMember().getUserId());
        assertEquals("admin", result.get(0).getMember().getRole());
        assertEquals(100L, result.get(0).getMember().getWorkspaceId());
    }

    @Test
    void listWorkspaceMembers_shouldReturnEmpty_whenOwnerNotFound() {
        when(spaceService.getById(100L)).thenReturn(privateSpace);
        when(userService.getById(1L)).thenReturn(null);

        List<WorkspaceMemberResponse> result = adapter.listWorkspaceMembers(100L, loginUser);
        assertTrue(result.isEmpty());
    }
}
