package com.qiu.qiupicturebackend.collection.domain.repository;

import com.qiu.qiupicturebackend.collection.domain.model.CollectionItemView;

import java.util.List;
import java.util.Optional;

public interface CollectionItemRepository {

    CollectionItemView save(CollectionItemView item);

    Optional<CollectionItemView> findById(Long itemId);

    List<CollectionItemView> findByCollectionId(Long collectionId);

    List<CollectionItemView> findByCollectionIdAndSectionId(Long collectionId, Long sectionId);

    Optional<CollectionItemView> findActiveByCollectionAndAsset(Long collectionId, Long assetId);

    void deleteById(Long itemId);

    void updateSortOrder(Long itemId, Long sortOrder);
}
