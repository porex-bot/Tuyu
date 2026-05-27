package com.qiu.qiupicturebackend.asset;

import com.qiu.qiupicturebackend.asset.application.AssetQueryApplicationService;
import com.qiu.qiupicturebackend.asset.application.query.AssetPageQuery;
import com.qiu.qiupicturebackend.asset.domain.model.AssetVersionType;
import com.qiu.qiupicturebackend.asset.domain.model.AssetVersionView;
import com.qiu.qiupicturebackend.asset.domain.repository.AssetStorageObjectRepository;
import com.qiu.qiupicturebackend.asset.domain.repository.AssetVersionRepository;
import com.qiu.qiupicturebackend.asset.infrastructure.PictureAssetLegacyAdapter;
import com.qiu.qiupicturebackend.asset.representation.AssetDetailResponse;
import com.qiu.qiupicturebackend.asset.representation.AssetPageResponse;
import com.qiu.qiupicturebackend.asset.representation.AssetVersionResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetQueryApplicationServiceTest {

    @Mock
    private PictureAssetLegacyAdapter pictureAssetLegacyAdapter;

    @Mock
    private AssetVersionRepository assetVersionRepository;

    @Mock
    private AssetStorageObjectRepository assetStorageObjectRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AssetQueryApplicationService service;

    // --- searchAssets null-safety ---

    @Test
    void shouldReturnEmptyPageWhenWorkspaceIdIsNull() {
        AssetPageResponse result = service.searchAssets(null, new AssetPageQuery(), request);
        assertNotNull(result);
        assertTrue(result.getRecords().isEmpty());
        assertEquals(0, result.getTotal());
        verify(pictureAssetLegacyAdapter, never()).searchAssets(anyLong(), any(), any());
    }

    @Test
    void shouldReturnEmptyPageWhenQueryIsNull() {
        AssetPageResponse result = service.searchAssets(1L, null, request);
        assertNotNull(result);
        assertTrue(result.getRecords().isEmpty());
        verify(pictureAssetLegacyAdapter, never()).searchAssets(anyLong(), any(), any());
    }

    @Test
    void shouldReturnEmptyPageWhenRequestIsNull() {
        AssetPageResponse result = service.searchAssets(1L, new AssetPageQuery(), null);
        assertNotNull(result);
        assertTrue(result.getRecords().isEmpty());
        verify(pictureAssetLegacyAdapter, never()).searchAssets(anyLong(), any(), any());
    }

    @Test
    void shouldDelegateSearchToAdapter() {
        AssetPageQuery query = new AssetPageQuery();
        AssetPageResponse expected = AssetPageResponse.empty();
        when(pictureAssetLegacyAdapter.searchAssets(1L, query, request)).thenReturn(expected);

        AssetPageResponse result = service.searchAssets(1L, query, request);

        assertSame(expected, result);
        verify(pictureAssetLegacyAdapter).searchAssets(1L, query, request);
    }

    // --- getAssetDetail null-safety ---

    @Test
    void shouldReturnNullDetailWhenWorkspaceIdIsNull() {
        assertNull(service.getAssetDetail(null, 1L, request));
        verify(pictureAssetLegacyAdapter, never()).getAssetDetail(anyLong(), anyLong(), any());
    }

    @Test
    void shouldReturnNullDetailWhenAssetIdIsNull() {
        assertNull(service.getAssetDetail(1L, null, request));
        verify(pictureAssetLegacyAdapter, never()).getAssetDetail(anyLong(), anyLong(), any());
    }

    @Test
    void shouldReturnNullDetailWhenRequestIsNull() {
        assertNull(service.getAssetDetail(1L, 1L, null));
        verify(pictureAssetLegacyAdapter, never()).getAssetDetail(anyLong(), anyLong(), any());
    }

    @Test
    void shouldDelegateDetailToAdapter() {
        AssetDetailResponse expected = new AssetDetailResponse();
        expected.setAssetId(1L);
        when(pictureAssetLegacyAdapter.getAssetDetail(1L, 1L, request)).thenReturn(expected);

        AssetDetailResponse result = service.getAssetDetail(1L, 1L, request);

        assertSame(expected, result);
        verify(pictureAssetLegacyAdapter).getAssetDetail(1L, 1L, request);
    }

    @Test
    void shouldAttachCurrentVersionWhenPresent() {
        AssetDetailResponse detail = new AssetDetailResponse();
        detail.setAssetId(1L);
        when(pictureAssetLegacyAdapter.getAssetDetail(1L, 1L, request)).thenReturn(detail);

        AssetVersionView version = new AssetVersionView();
        version.setVersionId(10L);
        version.setAssetId(1L);
        version.setVersionNo(1);
        version.setVersionType(AssetVersionType.ORIGINAL);
        when(assetVersionRepository.findCurrentByAssetId(1L)).thenReturn(Optional.of(version));

        AssetDetailResponse result = service.getAssetDetail(1L, 1L, request);

        assertNotNull(result.getCurrentVersion());
        assertEquals(10L, result.getCurrentVersion().getVersionId());
    }

    @Test
    void shouldNotAttachCurrentVersionWhenAbsent() {
        AssetDetailResponse detail = new AssetDetailResponse();
        detail.setAssetId(1L);
        when(pictureAssetLegacyAdapter.getAssetDetail(1L, 1L, request)).thenReturn(detail);
        when(assetVersionRepository.findCurrentByAssetId(1L)).thenReturn(Optional.empty());

        AssetDetailResponse result = service.getAssetDetail(1L, 1L, request);

        assertNull(result.getCurrentVersion());
    }

    // --- listAssetVersions ---

    @Test
    void shouldReturnEmptyVersionsWhenWorkspaceIdIsNull() {
        List<AssetVersionResponse> versions = service.listAssetVersions(null, 1L);
        assertTrue(versions.isEmpty());
        verify(pictureAssetLegacyAdapter, never()).assetBelongsToWorkspace(anyLong(), anyLong());
    }

    @Test
    void shouldReturnEmptyVersionsWhenAssetIdIsNull() {
        List<AssetVersionResponse> versions = service.listAssetVersions(1L, null);
        assertTrue(versions.isEmpty());
        verify(pictureAssetLegacyAdapter, never()).assetBelongsToWorkspace(anyLong(), anyLong());
    }

    @Test
    void shouldReturnEmptyVersionsWhenAssetNotInWorkspace() {
        when(pictureAssetLegacyAdapter.assetBelongsToWorkspace(1L, 1L)).thenReturn(false);

        List<AssetVersionResponse> versions = service.listAssetVersions(1L, 1L);

        assertTrue(versions.isEmpty());
        verify(assetVersionRepository, never()).findByAssetId(anyLong());
    }

    @Test
    void shouldReturnEmptyVersionsWhenNoVersionsExist() {
        when(pictureAssetLegacyAdapter.assetBelongsToWorkspace(1L, 1L)).thenReturn(true);
        when(assetVersionRepository.findByAssetId(1L)).thenReturn(Collections.emptyList());

        List<AssetVersionResponse> versions = service.listAssetVersions(1L, 1L);

        assertTrue(versions.isEmpty());
    }

    @Test
    void shouldReturnVersionsFromRepository() {
        when(pictureAssetLegacyAdapter.assetBelongsToWorkspace(1L, 1L)).thenReturn(true);
        AssetVersionView view = new AssetVersionView();
        view.setVersionId(10L);
        view.setAssetId(1L);
        view.setVersionNo(1);
        view.setVersionType(AssetVersionType.ORIGINAL);
        when(assetVersionRepository.findByAssetId(1L)).thenReturn(List.of(view));

        List<AssetVersionResponse> versions = service.listAssetVersions(1L, 1L);

        assertEquals(1, versions.size());
        assertEquals(10L, versions.get(0).getVersionId());
        assertEquals("original", versions.get(0).getVersionType());
    }
}
