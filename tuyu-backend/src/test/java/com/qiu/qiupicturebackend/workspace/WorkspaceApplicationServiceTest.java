package com.qiu.qiupicturebackend.workspace;

import com.qiu.qiupicturebackend.model.entity.User;
import com.qiu.qiupicturebackend.workspace.application.WorkspaceApplicationService;
import com.qiu.qiupicturebackend.workspace.domain.model.WorkspaceView;
import com.qiu.qiupicturebackend.workspace.infrastructure.WorkspaceLegacyAdapter;
import com.qiu.qiupicturebackend.workspace.representation.WorkspaceMemberResponse;
import com.qiu.qiupicturebackend.workspace.representation.WorkspaceResponse;
import com.qiu.qiupicturebackend.workspace.representation.WorkspaceUsageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * WorkspaceApplicationService 单元测试 —— 验证输入校验与委托。
 */
@ExtendWith(MockitoExtension.class)
class WorkspaceApplicationServiceTest {

    @Mock
    private WorkspaceLegacyAdapter adapter;

    @InjectMocks
    private WorkspaceApplicationService service;

    private final User loginUser = new User();

    {
        loginUser.setId(1L);
        loginUser.setUserName("test");
    }

    // ==================== listMyWorkspaces ====================

    @Test
    void listMyWorkspaces_shouldReturnEmpty_whenUserNull() {
        List<WorkspaceResponse> result = service.listMyWorkspaces(null);
        assertTrue(result.isEmpty());
        verifyNoInteractions(adapter);
    }

    @Test
    void listMyWorkspaces_shouldDelegate_whenUserValid() {
        when(adapter.listMyWorkspaces(loginUser)).thenReturn(Collections.emptyList());

        List<WorkspaceResponse> result = service.listMyWorkspaces(loginUser);
        assertNotNull(result);
        verify(adapter).listMyWorkspaces(loginUser);
    }

    // ==================== getWorkspaceById ====================

    @Test
    void getWorkspaceById_shouldReturnNull_whenIdNull() {
        WorkspaceResponse result = service.getWorkspaceById(null, loginUser);
        assertNull(result);
        verifyNoInteractions(adapter);
    }

    @Test
    void getWorkspaceById_shouldReturnNull_whenUserNull() {
        WorkspaceResponse result = service.getWorkspaceById(1L, null);
        assertNull(result);
        verifyNoInteractions(adapter);
    }

    @Test
    void getWorkspaceById_shouldDelegate_whenValid() {
        WorkspaceResponse expected = new WorkspaceResponse();
        WorkspaceView view = new WorkspaceView();
        view.setWorkspaceId(1L);
        view.setName("test");
        expected.setWorkspace(view);
        when(adapter.getWorkspaceById(1L, loginUser)).thenReturn(expected);

        WorkspaceResponse result = service.getWorkspaceById(1L, loginUser);
        assertNotNull(result);
        assertEquals("test", result.getWorkspace().getName());
        verify(adapter).getWorkspaceById(1L, loginUser);
    }

    // ==================== getWorkspaceUsage ====================

    @Test
    void getWorkspaceUsage_shouldReturnNull_whenIdNull() {
        WorkspaceUsageResponse result = service.getWorkspaceUsage(null);
        assertNull(result);
        verifyNoInteractions(adapter);
    }

    @Test
    void getWorkspaceUsage_shouldDelegate_whenValid() {
        WorkspaceUsageResponse expected = new WorkspaceUsageResponse();
        expected.setWorkspaceId(1L);
        expected.setMaxCount(100L);
        when(adapter.getWorkspaceUsage(1L)).thenReturn(expected);

        WorkspaceUsageResponse result = service.getWorkspaceUsage(1L);
        assertNotNull(result);
        assertEquals(100L, result.getMaxCount());
        verify(adapter).getWorkspaceUsage(1L);
    }

    // ==================== listWorkspaceMembers ====================

    @Test
    void listWorkspaceMembers_shouldReturnEmpty_whenIdNull() {
        List<WorkspaceMemberResponse> result = service.listWorkspaceMembers(null, loginUser);
        assertTrue(result.isEmpty());
        verifyNoInteractions(adapter);
    }

    @Test
    void listWorkspaceMembers_shouldReturnEmpty_whenUserNull() {
        List<WorkspaceMemberResponse> result = service.listWorkspaceMembers(1L, null);
        assertTrue(result.isEmpty());
        verifyNoInteractions(adapter);
    }

    @Test
    void listWorkspaceMembers_shouldDelegate_whenValid() {
        when(adapter.listWorkspaceMembers(1L, loginUser)).thenReturn(Collections.emptyList());

        List<WorkspaceMemberResponse> result = service.listWorkspaceMembers(1L, loginUser);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(adapter).listWorkspaceMembers(1L, loginUser);
    }
}
