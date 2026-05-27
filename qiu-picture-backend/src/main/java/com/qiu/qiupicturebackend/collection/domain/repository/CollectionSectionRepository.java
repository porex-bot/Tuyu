package com.qiu.qiupicturebackend.collection.domain.repository;

import com.qiu.qiupicturebackend.collection.domain.model.CollectionSectionView;

import java.util.List;
import java.util.Optional;

public interface CollectionSectionRepository {

    CollectionSectionView save(CollectionSectionView section);

    Optional<CollectionSectionView> findById(Long sectionId);

    List<CollectionSectionView> findByCollectionId(Long collectionId);
}
