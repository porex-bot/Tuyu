package com.qiu.qiupicturebackend.asset;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiu.qiupicturebackend.asset.application.query.AssetPageQuery;
import com.qiu.qiupicturebackend.asset.domain.model.AssetLifecycleStatus;
import com.qiu.qiupicturebackend.asset.domain.model.AssetView;
import com.qiu.qiupicturebackend.asset.infrastructure.PictureAssetLegacyAdapter;
import com.qiu.qiupicturebackend.asset.representation.AssetCardResponse;
import com.qiu.qiupicturebackend.asset.representation.AssetDetailResponse;
import com.qiu.qiupicturebackend.asset.representation.AssetPageResponse;
import com.qiu.qiupicturebackend.manager.auth.SpaceUserAuthManager;
import com.qiu.qiupicturebackend.model.dto.picture.PictureQueryRequest;
import com.qiu.qiupicturebackend.model.entity.Picture;
import com.qiu.qiupicturebackend.model.entity.Space;
import com.qiu.qiupicturebackend.model.entity.User;
import com.qiu.qiupicturebackend.model.vo.PictureVO;
import com.qiu.qiupicturebackend.model.vo.UserVO;
import com.qiu.qiupicturebackend.service.PictureService;
import com.qiu.qiupicturebackend.service.SpaceService;
import com.qiu.qiupicturebackend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PictureAssetLegacyAdapterTest {

    @Mock
    private PictureService pictureService;

    @Mock
    private SpaceService spaceService;

    @Mock
    private UserService userService;

    @Mock
    private SpaceUserAuthManager spaceUserAuthManager;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private PictureAssetLegacyAdapter adapter;

    private Picture samplePicture;
    private PictureVO samplePictureVO;
    private Space sampleSpace;
    private User sampleUser;

    @BeforeEach
    void setUp() {
        samplePicture = new Picture();
        samplePicture.setId(100L);
        samplePicture.setSpaceId(200L);
        samplePicture.setName("test.png");
        samplePicture.setIntroduction("A test image");
        samplePicture.setCategory("screenshot");
        samplePicture.setTags("[\"tag1\",\"tag2\"]");
        samplePicture.setUrl("https://example.com/test.png");
        samplePicture.setThumbnailUrl("https://example.com/thumb.jpg");
        samplePicture.setPicWidth(1920);
        samplePicture.setPicHeight(1080);
        samplePicture.setPicScale(1.78);
        samplePicture.setPicFormat("png");
        samplePicture.setPicSize(1024000L);
        samplePicture.setPicColor("#ff0000");
        samplePicture.setUserId(300L);
        samplePicture.setReviewStatus(1);
        samplePicture.setCreateTime(new Date());
        samplePicture.setEditTime(new Date());

        UserVO userVO = new UserVO();
        userVO.setId(300L);
        userVO.setUserName("testuser");
        userVO.setUserAvatar("https://example.com/avatar.jpg");

        samplePictureVO = new PictureVO();
        samplePictureVO.setId(100L);
        samplePictureVO.setSpaceId(200L);
        samplePictureVO.setName("test.png");
        samplePictureVO.setIntroduction("A test image");
        samplePictureVO.setCategory("screenshot");
        samplePictureVO.setTags(Arrays.asList("tag1", "tag2"));
        samplePictureVO.setUrl("https://example.com/test.png");
        samplePictureVO.setThumbnailUrl("https://example.com/thumb.jpg");
        samplePictureVO.setPicWidth(1920);
        samplePictureVO.setPicHeight(1080);
        samplePictureVO.setPicScale(1.78);
        samplePictureVO.setPicFormat("png");
        samplePictureVO.setPicSize(1024000L);
        samplePictureVO.setPicColor("#ff0000");
        samplePictureVO.setUserId(300L);
        samplePictureVO.setUser(userVO);
        samplePictureVO.setCreateTime(new Date());
        samplePictureVO.setEditTime(new Date());
        samplePictureVO.setPermissionList(Arrays.asList("picture:view"));

        sampleSpace = new Space();
        sampleSpace.setId(200L);
        sampleSpace.setSpaceName("Test Workspace");
        sampleSpace.setUserId(300L);

        sampleUser = new User();
        sampleUser.setId(300L);
        sampleUser.setUserName("testuser");
    }

    // ---- AssetView mapping ----

    @Test
    void shouldMapPictureToAssetView() {
        AssetView view = adapter.toAssetView(samplePicture);

        assertNotNull(view);
        assertEquals(100L, view.getAssetId());
        assertEquals(100L, view.getLegacyPictureId());
        assertEquals(200L, view.getWorkspaceId());
        assertEquals("test.png", view.getName());
        assertEquals("A test image", view.getDescription());
        assertEquals("screenshot", view.getCategory());
        assertEquals(1920, view.getWidth());
        assertEquals(1080, view.getHeight());
        assertEquals(1.78, view.getScale());
        assertEquals("png", view.getFormat());
        assertEquals(1024000L, view.getSize());
        assertEquals("#ff0000", view.getDominantColor());
        assertEquals(300L, view.getCreatedBy());
        assertEquals(AssetLifecycleStatus.APPROVED, view.getLifecycleStatus());
    }

    @Test
    void shouldReturnNullForNullPicture() {
        assertNull(adapter.toAssetView(null));
    }

    @Test
    void shouldMapPendingReviewStatus() {
        samplePicture.setReviewStatus(0);
        AssetView view = adapter.toAssetView(samplePicture);
        assertEquals(AssetLifecycleStatus.PENDING_REVIEW, view.getLifecycleStatus());
    }

    // ---- AssetCardResponse mapping ----

    @Test
    void shouldMapPictureVOToAssetCardResponse() {
        List<String> permissions = Arrays.asList("picture:view");
        AssetCardResponse card = adapter.toAssetCardResponse(samplePictureVO, permissions);

        assertNotNull(card);
        assertEquals(100L, card.getAssetId());
        assertEquals(100L, card.getLegacyPictureId());
        assertEquals(200L, card.getWorkspaceId());
        assertEquals("test.png", card.getName());
        assertEquals("https://example.com/thumb.jpg", card.getThumbnailUrl());
        assertEquals("https://example.com/test.png", card.getUrl());
        assertEquals("png", card.getFormat());
        assertEquals(1920, card.getWidth());
        assertEquals(1080, card.getHeight());
        assertEquals(1024000L, card.getSize());
        assertNotNull(card.getSizeDisplay());
        assertEquals("#ff0000", card.getDominantColor());
        assertNotNull(card.getLifecycleStatus());
        assertEquals("testuser", card.getCreatedBy());
        assertNotNull(card.getPermissionList());
        assertEquals(1, card.getPermissionList().size());
    }

    @Test
    void shouldReturnNullForNullPictureVO() {
        assertNull(adapter.toAssetCardResponse(null, Collections.emptyList()));
    }

    @Test
    void shouldHandleNullPermissionListInCard() {
        AssetCardResponse card = adapter.toAssetCardResponse(samplePictureVO, null);
        assertNotNull(card);
        assertNotNull(card.getPermissionList());
        assertTrue(card.getPermissionList().isEmpty());
    }

    // ---- AssetDetailResponse mapping ----

    @Test
    void shouldMapPictureVOToAssetDetailResponse() {
        List<String> permissions = Arrays.asList("picture:view", "picture:edit");
        AssetDetailResponse detail = adapter.toAssetDetailResponse(samplePictureVO, permissions);

        assertNotNull(detail);
        assertEquals(100L, detail.getAssetId());
        assertEquals(100L, detail.getLegacyPictureId());
        assertEquals(200L, detail.getWorkspaceId());
        assertEquals("test.png", detail.getName());
        assertEquals("A test image", detail.getDescription());
        assertEquals("screenshot", detail.getCategory());
        assertEquals(1920, detail.getWidth());
        assertEquals(1080, detail.getHeight());
        assertEquals("png", detail.getFormat());
        assertNotNull(detail.getMetadata());
        assertEquals(1920, detail.getMetadata().getWidth());
        assertEquals(1080, detail.getMetadata().getHeight());
        assertEquals(2, detail.getPermissionList().size());
    }

    // ---- Query mapping ----

    @Test
    void shouldMapAssetPageQueryToPictureQueryRequest() {
        AssetPageQuery query = new AssetPageQuery();
        query.setWorkspaceId(200L);
        query.setCurrent(2);
        query.setPageSize(10);
        query.setSearchText("test");
        query.setCategory("screenshot");
        query.setFormat("png");
        query.setSortField("createTime");
        query.setSortOrder("asc");

        PictureQueryRequest req = adapter.toPictureQueryRequest(query);

        assertNotNull(req);
        assertEquals(2, req.getCurrent());
        assertEquals(10, req.getPageSize());
        assertEquals("test", req.getSearchText());
        assertEquals("screenshot", req.getCategory());
        assertEquals("png", req.getPicFormat());
        assertEquals("createTime", req.getSortField());
        assertEquals("asc", req.getSortOrder());
    }

    // ---- Permission delegation ----

    @Test
    void shouldReturnEmptyPermissionsForNullPicture() {
        when(pictureService.getById(999L)).thenReturn(null);
        List<String> perms = adapter.getAssetPermissions(200L, 999L, sampleUser);
        assertTrue(perms.isEmpty());
    }

    @Test
    void shouldReturnEmptyPermissionsForMismatchedWorkspace() {
        when(pictureService.getById(100L)).thenReturn(samplePicture);
        List<String> perms = adapter.getAssetPermissions(999L, 100L, sampleUser);
        assertTrue(perms.isEmpty());
        verify(spaceService, never()).getById(anyLong());
    }

    @Test
    void shouldDelegatePermissionsToAuthManager() {
        when(pictureService.getById(100L)).thenReturn(samplePicture);
        when(spaceService.getById(200L)).thenReturn(sampleSpace);
        when(spaceUserAuthManager.getPermissionList(sampleSpace, sampleUser))
                .thenReturn(Arrays.asList("picture:view", "picture:edit"));

        List<String> perms = adapter.getAssetPermissions(200L, 100L, sampleUser);

        assertEquals(2, perms.size());
        assertTrue(perms.contains("picture:view"));
        assertTrue(perms.contains("picture:edit"));
    }

    // ---- formatSize verification ----

    @Test
    void shouldProduceNonNullSizeDisplay() {
        AssetCardResponse card = adapter.toAssetCardResponse(samplePictureVO, Collections.emptyList());
        assertNotNull(card.getSizeDisplay());
        assertFalse(card.getSizeDisplay().isEmpty());
    }

    // ---- Pagination query defaults ----

    @Test
    void shouldUseDefaultPaginationWhenNotSpecified() {
        AssetPageQuery query = new AssetPageQuery();
        PictureQueryRequest req = adapter.toPictureQueryRequest(query);

        assertEquals(1, req.getCurrent());
        assertEquals(20, req.getPageSize());
    }
}
