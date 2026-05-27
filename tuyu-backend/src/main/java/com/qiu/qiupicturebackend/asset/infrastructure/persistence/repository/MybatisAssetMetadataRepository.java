package com.qiu.qiupicturebackend.asset.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qiu.qiupicturebackend.asset.domain.model.AssetMetadataSnapshot;
import com.qiu.qiupicturebackend.asset.domain.repository.AssetMetadataRepository;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.entity.AssetMetadataEntity;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.mapper.AssetMetadataMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MybatisAssetMetadataRepository implements AssetMetadataRepository {

    private final AssetMetadataMapper mapper;

    public MybatisAssetMetadataRepository(AssetMetadataMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public AssetMetadataSnapshot save(AssetMetadataSnapshot snapshot) {
        AssetMetadataEntity entity = toEntity(snapshot);
        if (entity.getId() == null) {
            mapper.insert(entity);
        } else {
            mapper.updateById(entity);
        }
        return toView(entity);
    }

    @Override
    public Optional<AssetMetadataSnapshot> findByVersionId(Long versionId) {
        AssetMetadataEntity entity = mapper.selectOne(
                new LambdaQueryWrapper<AssetMetadataEntity>()
                        .eq(AssetMetadataEntity::getVersionId, versionId)
        );
        return Optional.ofNullable(entity).map(this::toView);
    }

    @Override
    public List<AssetMetadataSnapshot> findByAssetId(Long assetId) {
        List<AssetMetadataEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<AssetMetadataEntity>()
                        .eq(AssetMetadataEntity::getAssetId, assetId)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    private AssetMetadataEntity toEntity(AssetMetadataSnapshot view) {
        AssetMetadataEntity entity = new AssetMetadataEntity();
        entity.setId(view.getMetadataId());
        entity.setAssetId(view.getAssetId());
        entity.setVersionId(view.getVersionId());
        entity.setWidth(view.getWidth());
        entity.setHeight(view.getHeight());
        entity.setScale(view.getScale());
        entity.setFormat(view.getFormat());
        entity.setFileSize(view.getFileSize());
        entity.setDominantColor(view.getDominantColor());
        entity.setCategory(view.getCategory());
        entity.setTags(view.getTags());
        entity.setDescription(view.getDescription());
        return entity;
    }

    private AssetMetadataSnapshot toView(AssetMetadataEntity entity) {
        AssetMetadataSnapshot view = new AssetMetadataSnapshot();
        view.setMetadataId(entity.getId());
        view.setAssetId(entity.getAssetId());
        view.setVersionId(entity.getVersionId());
        view.setWidth(entity.getWidth());
        view.setHeight(entity.getHeight());
        view.setScale(entity.getScale());
        view.setFormat(entity.getFormat());
        view.setFileSize(entity.getFileSize());
        view.setDominantColor(entity.getDominantColor());
        view.setCategory(entity.getCategory());
        view.setTags(entity.getTags());
        view.setDescription(entity.getDescription());
        view.setCreatedAt(entity.getCreateTime());
        return view;
    }
}
