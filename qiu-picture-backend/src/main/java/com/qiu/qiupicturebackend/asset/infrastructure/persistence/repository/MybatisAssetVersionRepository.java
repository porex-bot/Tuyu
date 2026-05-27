package com.qiu.qiupicturebackend.asset.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qiu.qiupicturebackend.asset.domain.model.AssetVersionType;
import com.qiu.qiupicturebackend.asset.domain.model.AssetVersionView;
import com.qiu.qiupicturebackend.asset.domain.repository.AssetVersionRepository;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.entity.AssetVersionEntity;
import com.qiu.qiupicturebackend.asset.infrastructure.persistence.mapper.AssetVersionMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MybatisAssetVersionRepository implements AssetVersionRepository {

    private final AssetVersionMapper mapper;

    public MybatisAssetVersionRepository(AssetVersionMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public AssetVersionView save(AssetVersionView version) {
        AssetVersionEntity entity = toEntity(version);
        if (entity.getId() == null) {
            mapper.insert(entity);
        } else {
            mapper.updateById(entity);
        }
        return toView(entity);
    }

    @Override
    public Optional<AssetVersionView> findByLegacyPictureId(Long legacyPictureId) {
        AssetVersionEntity entity = mapper.selectOne(
                new LambdaQueryWrapper<AssetVersionEntity>()
                        .eq(AssetVersionEntity::getLegacyPictureId, legacyPictureId)
        );
        return Optional.ofNullable(entity).map(this::toView);
    }

    @Override
    public List<AssetVersionView> findByAssetId(Long assetId) {
        List<AssetVersionEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<AssetVersionEntity>()
                        .eq(AssetVersionEntity::getAssetId, assetId)
                        .orderByDesc(AssetVersionEntity::getVersionNo)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    @Override
    public Optional<AssetVersionView> findCurrentByAssetId(Long assetId) {
        AssetVersionEntity entity = mapper.selectOne(
                new LambdaQueryWrapper<AssetVersionEntity>()
                        .eq(AssetVersionEntity::getAssetId, assetId)
                        .eq(AssetVersionEntity::getIsCurrent, 1)
        );
        return Optional.ofNullable(entity).map(this::toView);
    }

    private AssetVersionEntity toEntity(AssetVersionView view) {
        AssetVersionEntity entity = new AssetVersionEntity();
        entity.setId(view.getVersionId());
        entity.setAssetId(view.getAssetId());
        entity.setLegacyPictureId(view.getLegacyPictureId());
        entity.setVersionNo(view.getVersionNo());
        entity.setVersionType(view.getVersionType() != null ? view.getVersionType().name().toLowerCase() : null);
        entity.setStorageObjectId(null); // set separately after storage object save
        entity.setThumbnailStorageObjectId(null);
        entity.setMetadataId(null);
        entity.setCreatedBy(view.getCreatedBy());
        entity.setIsCurrent(view.getIsCurrent() != null && view.getIsCurrent() ? 1 : 0);
        entity.setCreateTime(view.getCreatedAt());
        return entity;
    }

    private AssetVersionView toView(AssetVersionEntity entity) {
        AssetVersionView view = new AssetVersionView();
        view.setVersionId(entity.getId());
        view.setAssetId(entity.getAssetId());
        view.setLegacyPictureId(entity.getLegacyPictureId());
        view.setVersionNo(entity.getVersionNo());
        if (entity.getVersionType() != null) {
            try {
                view.setVersionType(AssetVersionType.valueOf(entity.getVersionType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                view.setVersionType(AssetVersionType.ORIGINAL);
            }
        }
        view.setCreatedBy(entity.getCreatedBy());
        view.setCreatedAt(entity.getCreateTime());
        view.setIsCurrent(entity.getIsCurrent() != null && entity.getIsCurrent() == 1);
        return view;
    }
}
