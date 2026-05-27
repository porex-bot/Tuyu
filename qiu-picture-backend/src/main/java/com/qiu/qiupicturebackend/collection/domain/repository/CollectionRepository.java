package com.qiu.qiupicturebackend.collection.domain.repository;

import com.qiu.qiupicturebackend.collection.domain.model.CollectionView;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository {

    CollectionView save(CollectionView collection);

    Optional<CollectionView> findById(Long collectionId);

    List<CollectionView> findByWorkspaceId(Long workspaceId);
}
