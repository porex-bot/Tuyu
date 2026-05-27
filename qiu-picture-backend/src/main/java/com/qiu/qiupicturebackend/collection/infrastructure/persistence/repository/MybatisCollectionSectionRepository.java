package com.qiu.qiupicturebackend.collection.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qiu.qiupicturebackend.collection.domain.model.CollectionSectionView;
import com.qiu.qiupicturebackend.collection.domain.repository.CollectionSectionRepository;
import com.qiu.qiupicturebackend.collection.infrastructure.persistence.entity.CollectionSectionEntity;
import com.qiu.qiupicturebackend.collection.infrastructure.persistence.mapper.CollectionSectionMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MybatisCollectionSectionRepository implements CollectionSectionRepository {

    private final CollectionSectionMapper mapper;

    public MybatisCollectionSectionRepository(CollectionSectionMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CollectionSectionView save(CollectionSectionView section) {
        CollectionSectionEntity entity = toEntity(section);
        if (entity.getId() == null) {
            mapper.insert(entity);
        } else {
            mapper.updateById(entity);
        }
        return toView(entity);
    }

    @Override
    public Optional<CollectionSectionView> findById(Long sectionId) {
        CollectionSectionEntity entity = mapper.selectById(sectionId);
        return Optional.ofNullable(entity).map(this::toView);
    }

    @Override
    public List<CollectionSectionView> findByCollectionId(Long collectionId) {
        List<CollectionSectionEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<CollectionSectionEntity>()
                        .eq(CollectionSectionEntity::getCollectionId, collectionId)
                        .orderByAsc(CollectionSectionEntity::getSortOrder)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    private CollectionSectionEntity toEntity(CollectionSectionView view) {
        CollectionSectionEntity entity = new CollectionSectionEntity();
        entity.setId(view.getSectionId());
        entity.setCollectionId(view.getCollectionId());
        entity.setName(view.getName());
        entity.setSortOrder(view.getSortOrder());
        entity.setCreateTime(view.getCreatedAt());
        return entity;
    }

    private CollectionSectionView toView(CollectionSectionEntity entity) {
        CollectionSectionView view = new CollectionSectionView();
        view.setSectionId(entity.getId());
        view.setCollectionId(entity.getCollectionId());
        view.setName(entity.getName());
        view.setSortOrder(entity.getSortOrder());
        view.setCreatedAt(entity.getCreateTime());
        return view;
    }
}
