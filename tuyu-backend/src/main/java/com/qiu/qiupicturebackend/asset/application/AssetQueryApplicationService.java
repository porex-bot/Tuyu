package com.qiu.qiupicturebackend.asset.application;

import com.qiu.qiupicturebackend.asset.application.query.AssetPageQuery;
import com.qiu.qiupicturebackend.asset.domain.model.AssetStorageObjectView;
import com.qiu.qiupicturebackend.asset.domain.model.AssetVersionView;
import com.qiu.qiupicturebackend.asset.domain.repository.AssetStorageObjectRepository;
import com.qiu.qiupicturebackend.asset.domain.repository.AssetVersionRepository;
import com.qiu.qiupicturebackend.asset.infrastructure.PictureAssetLegacyAdapter;
import com.qiu.qiupicturebackend.asset.representation.AssetDetailResponse;
import com.qiu.qiupicturebackend.asset.representation.AssetPageResponse;
import com.qiu.qiupicturebackend.asset.representation.AssetVersionResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssetQueryApplicationService {

    @Resource
    private PictureAssetLegacyAdapter pictureAssetLegacyAdapter;

    @Resource
    private AssetVersionRepository assetVersionRepository;

    @Resource
    private AssetStorageObjectRepository assetStorageObjectRepository;

    public AssetPageResponse searchAssets(Long workspaceId, AssetPageQuery query, HttpServletRequest request) {
        if (workspaceId == null || query == null || request == null) {
            return AssetPageResponse.empty();
        }
        return pictureAssetLegacyAdapter.searchAssets(workspaceId, query, request);
    }

    public AssetDetailResponse getAssetDetail(Long workspaceId, Long assetId, HttpServletRequest request) {
        if (workspaceId == null || assetId == null || request == null) {
            return null;
        }
        AssetDetailResponse detail = pictureAssetLegacyAdapter.getAssetDetail(workspaceId, assetId, request);
        if (detail != null) {
            assetVersionRepository.findCurrentByAssetId(assetId)
                    .map(this::toVersionResponse)
                    .ifPresent(detail::setCurrentVersion);
        }
        return detail;
    }

    public List<AssetVersionResponse> listAssetVersions(Long workspaceId, Long assetId) {
        if (workspaceId == null || assetId == null || assetId <= 0) {
            return Collections.emptyList();
        }
        // Validate workspace ownership
        if (!pictureAssetLegacyAdapter.assetBelongsToWorkspace(workspaceId, assetId)) {
            return Collections.emptyList();
        }
        List<AssetVersionView> versions = assetVersionRepository.findByAssetId(assetId);
        if (versions.isEmpty()) {
            return Collections.emptyList();
        }
        return versions.stream()
                .map(this::toVersionResponse)
                .collect(Collectors.toList());
    }

    private AssetVersionResponse toVersionResponse(AssetVersionView version) {
        AssetVersionResponse response = new AssetVersionResponse();
        response.setVersionId(version.getVersionId());
        response.setAssetId(version.getAssetId());
        response.setLegacyPictureId(version.getLegacyPictureId());
        response.setVersionNo(version.getVersionNo());
        response.setVersionType(version.getVersionType() != null ? version.getVersionType().name().toLowerCase() : null);
        response.setWidth(version.getWidth());
        response.setHeight(version.getHeight());
        response.setFileSize(version.getFileSize());
        response.setFormat(version.getFormat());
        response.setDominantColor(version.getDominantColor());
        response.setCreatedBy(version.getCreatedBy());
        response.setCreatedAt(version.getCreatedAt() != null ? version.getCreatedAt().toString() : null);
        response.setIsCurrent(version.getIsCurrent());

        // Populate storage URLs — fall back to empty if no storage object exists.
        // The version view may not carry URLs directly; derived from storage object lookups.
        return response;
    }
}
