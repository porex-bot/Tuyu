package com.qiu.qiupicturebackend.ai.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.qiu.qiupicturebackend.ai.domain.repository.AiProviderTaskRepository;
import com.qiu.qiupicturebackend.ai.infrastructure.persistence.entity.AiProviderTaskEntity;
import com.qiu.qiupicturebackend.ai.infrastructure.persistence.mapper.AiProviderTaskMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public class MybatisAiProviderTaskRepository implements AiProviderTaskRepository {

    private final AiProviderTaskMapper mapper;

    public MybatisAiProviderTaskRepository(AiProviderTaskMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void save(Long jobId, String provider, String providerTaskId, String providerStatus) {
        AiProviderTaskEntity entity = new AiProviderTaskEntity();
        entity.setJobId(jobId);
        entity.setProvider(provider);
        entity.setProviderTaskId(providerTaskId);
        entity.setProviderStatus(providerStatus);
        entity.setCreatedAt(new Date());
        entity.setUpdatedAt(new Date());
        mapper.insert(entity);
    }

    @Override
    public void updateStatus(Long jobId, String providerStatus, String providerResponseJson) {
        mapper.update(null,
                new LambdaUpdateWrapper<AiProviderTaskEntity>()
                        .eq(AiProviderTaskEntity::getJobId, jobId)
                        .set(AiProviderTaskEntity::getProviderStatus, providerStatus)
                        .set(AiProviderTaskEntity::getProviderResponseJson, providerResponseJson)
                        .set(AiProviderTaskEntity::getUpdatedAt, new Date())
        );
    }

    @Override
    public Optional<String> findProviderTaskId(Long jobId) {
        AiProviderTaskEntity entity = mapper.selectOne(
                new LambdaQueryWrapper<AiProviderTaskEntity>()
                        .eq(AiProviderTaskEntity::getJobId, jobId)
        );
        return Optional.ofNullable(entity).map(AiProviderTaskEntity::getProviderTaskId);
    }
}
