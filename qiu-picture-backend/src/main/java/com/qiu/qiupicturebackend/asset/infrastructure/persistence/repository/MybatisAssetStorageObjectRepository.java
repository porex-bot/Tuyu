package com.qiu.qiupicturebackend.asset.infrastructure.persistence.repository;

import com.qiu.qiupicturebackend.asset.domain.model.AssetStorageObjectView;
import com.qiu.qiupicturebackend.asset.domain.repository.AssetStorageObjectRepository;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.entity.AssetStorageObjectEntity;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.mapper.AssetStorageObjectMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MybatisAssetStorageObjectRepository implements AssetStorageObjectRepository {

    private final AssetStorageObjectMapper mapper;

    public MybatisAssetStorageObjectRepository(AssetStorageObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public AssetStorageObjectView save(AssetStorageObjectView view) {
        AssetStorageObjectEntity entity = toEntity(view);
        if (entity.getId() == null) {
            mapper.insert(entity);
        } else {
            mapper.updateById(entity);
        }
        return toView(entity);
    }

    @Override
    public Optional<AssetStorageObjectView> findById(Long id) {
        AssetStorageObjectEntity entity = mapper.selectById(id);
        return Optional.ofNullable(entity).map(this::toView);
    }

    private AssetStorageObjectEntity toEntity(AssetStorageObjectView view) {
        AssetStorageObjectEntity entity = new AssetStorageObjectEntity();
        entity.setId(view.getStorageObjectId());
        entity.setLegacyUrl(view.getLegacyUrl());
        entity.setStorageKey(view.getStorageKey());
        entity.setBucket(view.getBucket());
        entity.setRegion(view.getRegion());
        entity.setFileSize(view.getFileSize());
        entity.setContentType(view.getContentType());
        entity.setWidth(view.getWidth());
        entity.setHeight(view.getHeight());
        entity.setFormat(view.getFormat());
        entity.setDominantColor(view.getDominantColor());
        return entity;
    }

    private AssetStorageObjectView toView(AssetStorageObjectEntity entity) {
        AssetStorageObjectView view = new AssetStorageObjectView();
        view.setStorageObjectId(entity.getId());
        view.setLegacyUrl(entity.getLegacyUrl());
        view.setStorageKey(entity.getStorageKey());
        view.setBucket(entity.getBucket());
        view.setRegion(entity.getRegion());
        view.setFileSize(entity.getFileSize());
        view.setContentType(entity.getContentType());
        view.setWidth(entity.getWidth());
        view.setHeight(entity.getHeight());
        view.setFormat(entity.getFormat());
        view.setDominantColor(entity.getDominantColor());
        view.setCreatedAt(entity.getCreateTime());
        return view;
    }
}
