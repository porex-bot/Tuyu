package com.qiu.qiupicturebackend.asset.domain.repository;

import com.qiu.qiupicturebackend.asset.domain.model.AssetStorageObjectView;

import java.util.Optional;

public interface AssetStorageObjectRepository {

    AssetStorageObjectView save(AssetStorageObjectView storageObject);

    Optional<AssetStorageObjectView> findById(Long id);
}
