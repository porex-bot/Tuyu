package com.qiu.qiupicturebackend.asset.domain.repository;

import com.qiu.qiupicturebackend.asset.domain.model.AssetVersionView;

import java.util.List;
import java.util.Optional;

public interface AssetVersionRepository {

    AssetVersionView save(AssetVersionView version);

    Optional<AssetVersionView> findByLegacyPictureId(Long legacyPictureId);

    List<AssetVersionView> findByAssetId(Long assetId);

    Optional<AssetVersionView> findCurrentByAssetId(Long assetId);
}
