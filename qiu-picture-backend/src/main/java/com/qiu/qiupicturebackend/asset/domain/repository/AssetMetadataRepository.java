package com.qiu.qiupicturebackend.asset.domain.repository;

import com.qiu.qiupicturebackend.asset.domain.model.AssetMetadataSnapshot;

import java.util.List;
import java.util.Optional;

public interface AssetMetadataRepository {

    AssetMetadataSnapshot save(AssetMetadataSnapshot metadata);

    Optional<AssetMetadataSnapshot> findByVersionId(Long versionId);

    List<AssetMetadataSnapshot> findByAssetId(Long assetId);
}
