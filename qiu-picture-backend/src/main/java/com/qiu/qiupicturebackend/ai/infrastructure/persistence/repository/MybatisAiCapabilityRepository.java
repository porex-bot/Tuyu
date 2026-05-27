package com.qiu.qiupicturebackend.ai.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qiu.qiupicturebackend.ai.domain.model.AiCapabilityView;
import com.qiu.qiupicturebackend.ai.domain.repository.AiCapabilityRepository;
import com.qiu.qiupicturebackend.ai.infrastructure.persistence.entity.AiCapabilityEntity;
import com.qiu.qiupicturebackend.ai.infrastructure.persistence.mapper.AiCapabilityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MybatisAiCapabilityRepository implements AiCapabilityRepository {

    private final AiCapabilityMapper mapper;

    public MybatisAiCapabilityRepository(AiCapabilityMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<AiCapabilityView> findAll() {
        List<AiCapabilityEntity> entities = mapper.selectList(null);
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    @Override
    public List<AiCapabilityView> findActive() {
        List<AiCapabilityEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<AiCapabilityEntity>()
                        .eq(AiCapabilityEntity::getIsActive, 1)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    @Override
    public Optional<AiCapabilityView> findByKey(String capabilityKey) {
        AiCapabilityEntity entity = mapper.selectOne(
                new LambdaQueryWrapper<AiCapabilityEntity>()
                        .eq(AiCapabilityEntity::getCapabilityKey, capabilityKey)
        );
        return Optional.ofNullable(entity).map(this::toView);
    }

    private AiCapabilityView toView(AiCapabilityEntity entity) {
        AiCapabilityView view = new AiCapabilityView();
        view.setId(entity.getId());
        view.setCapabilityKey(entity.getCapabilityKey());
        view.setDisplayName(entity.getDisplayName());
        view.setDescription(entity.getDescription());
        view.setProvider(entity.getProvider());
        view.setActive(entity.getIsActive() != null && entity.getIsActive() == 1);
        return view;
    }
}
