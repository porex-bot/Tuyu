package com.qiu.qiupicturebackend.collection.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.qiu.qiupicturebackend.collection.domain.model.CollectionItemView;
import com.qiu.qiupicturebackend.collection.domain.repository.CollectionItemRepository;
import com.qiu.qiupicturebackend.collection.infrastructure.persistence.entity.CollectionItemEntity;
import com.qiu.qiupicturebackend.collection.infrastructure.persistence.mapper.CollectionItemMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MybatisCollectionItemRepository implements CollectionItemRepository {

    private final CollectionItemMapper mapper;

    public MybatisCollectionItemRepository(CollectionItemMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CollectionItemView save(CollectionItemView item) {
        CollectionItemEntity entity = toEntity(item);
        if (entity.getId() == null) {
            mapper.insert(entity);
        } else {
            mapper.updateById(entity);
        }
        return toView(entity);
    }

    @Override
    public Optional<CollectionItemView> findById(Long itemId) {
        CollectionItemEntity entity = mapper.selectById(itemId);
        return Optional.ofNullable(entity).map(this::toView);
    }

    @Override
    public List<CollectionItemView> findByCollectionId(Long collectionId) {
        List<CollectionItemEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<CollectionItemEntity>()
                        .eq(CollectionItemEntity::getCollectionId, collectionId)
                        .orderByAsc(CollectionItemEntity::getSortOrder)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    @Override
    public List<CollectionItemView> findByCollectionIdAndSectionId(Long collectionId, Long sectionId) {
        List<CollectionItemEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<CollectionItemEntity>()
                        .eq(CollectionItemEntity::getCollectionId, collectionId)
                        .eq(sectionId != null, CollectionItemEntity::getSectionId, sectionId)
                        .orderByAsc(CollectionItemEntity::getSortOrder)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    @Override
    public Optional<CollectionItemView> findActiveByCollectionAndAsset(Long collectionId, Long assetId) {
        CollectionItemEntity entity = mapper.selectOne(
                new LambdaQueryWrapper<CollectionItemEntity>()
                        .eq(CollectionItemEntity::getCollectionId, collectionId)
                        .eq(CollectionItemEntity::getAssetId, assetId)
        );
        return Optional.ofNullable(entity).map(this::toView);
    }

    @Override
    public void deleteById(Long itemId) {
        mapper.deleteById(itemId);
    }

    @Override
    public void updateSortOrder(Long itemId, Long sortOrder) {
        mapper.update(null,
                new LambdaUpdateWrapper<CollectionItemEntity>()
                        .eq(CollectionItemEntity::getId, itemId)
                        .set(CollectionItemEntity::getSortOrder, sortOrder)
        );
    }

    private CollectionItemEntity toEntity(CollectionItemView view) {
        CollectionItemEntity entity = new CollectionItemEntity();
        entity.setId(view.getItemId());
        entity.setCollectionId(view.getCollectionId());
        entity.setAssetId(view.getAssetId());
        entity.setAssetVersionId(view.getAssetVersionId());
        entity.setSectionId(view.getSectionId());
        entity.setSortOrder(view.getSortOrder());
        entity.setNote(view.getNote());
        entity.setAddedBy(view.getAddedBy());
        entity.setAddedAt(view.getAddedAt());
        entity.setCreateTime(view.getCreatedAt());
        return entity;
    }

    private CollectionItemView toView(CollectionItemEntity entity) {
        CollectionItemView view = new CollectionItemView();
        view.setItemId(entity.getId());
        view.setCollectionId(entity.getCollectionId());
        view.setAssetId(entity.getAssetId());
        view.setAssetVersionId(entity.getAssetVersionId());
        view.setSectionId(entity.getSectionId());
        view.setSortOrder(entity.getSortOrder());
        view.setNote(entity.getNote());
        view.setAddedBy(entity.getAddedBy());
        view.setAddedAt(entity.getAddedAt());
        view.setCreatedAt(entity.getCreateTime());
        return view;
    }
}
