package com.qiu.qiupicturebackend.collection.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qiu.qiupicturebackend.collection.domain.model.CollectionLayout;
import com.qiu.qiupicturebackend.collection.domain.model.CollectionPurpose;
import com.qiu.qiupicturebackend.collection.domain.model.CollectionStatus;
import com.qiu.qiupicturebackend.collection.domain.model.CollectionView;
import com.qiu.qiupicturebackend.collection.domain.repository.CollectionRepository;
import com.qiu.qiupicturebackend.collection.infrastructure.persistence.entity.CollectionEntity;
import com.qiu.qiupicturebackend.collection.infrastructure.persistence.mapper.CollectionMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MybatisCollectionRepository implements CollectionRepository {

    private final CollectionMapper mapper;

    public MybatisCollectionRepository(CollectionMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CollectionView save(CollectionView collection) {
        CollectionEntity entity = toEntity(collection);
        if (entity.getId() == null) {
            mapper.insert(entity);
        } else {
            mapper.updateById(entity);
        }
        return toView(entity);
    }

    @Override
    public Optional<CollectionView> findById(Long collectionId) {
        CollectionEntity entity = mapper.selectById(collectionId);
        return Optional.ofNullable(entity).map(this::toView);
    }

    @Override
    public List<CollectionView> findByWorkspaceId(Long workspaceId) {
        List<CollectionEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<CollectionEntity>()
                        .eq(CollectionEntity::getWorkspaceId, workspaceId)
                        .orderByDesc(CollectionEntity::getUpdateTime)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    private CollectionEntity toEntity(CollectionView view) {
        CollectionEntity entity = new CollectionEntity();
        entity.setId(view.getCollectionId());
        entity.setWorkspaceId(view.getWorkspaceId());
        entity.setName(view.getName());
        entity.setDescription(view.getDescription());
        entity.setPurpose(view.getPurpose() != null ? view.getPurpose().name().toLowerCase() : null);
        entity.setLayout(view.getLayout() != null ? view.getLayout().name().toLowerCase() : null);
        entity.setStatus(view.getStatus() != null ? view.getStatus().name().toLowerCase() : null);
        entity.setCoverAssetId(view.getCoverAssetId());
        entity.setItemCount(view.getItemCount());
        entity.setCreatedBy(view.getCreatedBy());
        entity.setUpdatedBy(view.getUpdatedBy());
        entity.setCreateTime(view.getCreatedAt());
        entity.setUpdateTime(view.getUpdatedAt());
        return entity;
    }

    private CollectionView toView(CollectionEntity entity) {
        CollectionView view = new CollectionView();
        view.setCollectionId(entity.getId());
        view.setWorkspaceId(entity.getWorkspaceId());
        view.setName(entity.getName());
        view.setDescription(entity.getDescription());
        if (entity.getPurpose() != null) {
            try {
                view.setPurpose(CollectionPurpose.valueOf(entity.getPurpose().toUpperCase()));
            } catch (IllegalArgumentException e) {
                view.setPurpose(CollectionPurpose.PROJECT);
            }
        }
        if (entity.getLayout() != null) {
            try {
                view.setLayout(CollectionLayout.valueOf(entity.getLayout().toUpperCase()));
            } catch (IllegalArgumentException e) {
                view.setLayout(CollectionLayout.GRID);
            }
        }
        if (entity.getStatus() != null) {
            try {
                view.setStatus(CollectionStatus.valueOf(entity.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                view.setStatus(CollectionStatus.DRAFT);
            }
        }
        view.setCoverAssetId(entity.getCoverAssetId());
        view.setItemCount(entity.getItemCount());
        view.setCreatedBy(entity.getCreatedBy());
        view.setUpdatedBy(entity.getUpdatedBy());
        view.setCreatedAt(entity.getCreateTime());
        view.setUpdatedAt(entity.getUpdateTime());
        return view;
    }
}
