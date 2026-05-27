package com.qiu.qiupicturebackend.ai.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qiu.qiupicturebackend.ai.domain.model.AiJobView;
import com.qiu.qiupicturebackend.ai.domain.repository.AiJobRepository;
import com.qiu.qiupicturebackend.ai.infrastructure.persistence.entity.AiJobEntity;
import com.qiu.qiupicturebackend.ai.infrastructure.persistence.mapper.AiJobMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MybatisAiJobRepository implements AiJobRepository {

    private final AiJobMapper mapper;

    public MybatisAiJobRepository(AiJobMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public AiJobView save(AiJobView job) {
        AiJobEntity entity = toEntity(job);
        mapper.insert(entity);
        return toView(entity);
    }

    @Override
    public AiJobView update(AiJobView job) {
        AiJobEntity entity = toEntity(job);
        entity.setUpdatedAt(new java.util.Date());
        mapper.updateById(entity);
        return toView(entity);
    }

    @Override
    public Optional<AiJobView> findById(Long jobId) {
        AiJobEntity entity = mapper.selectById(jobId);
        return Optional.ofNullable(entity).map(this::toView);
    }

    @Override
    public List<AiJobView> findByWorkspaceId(Long workspaceId, int offset, int limit) {
        List<AiJobEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<AiJobEntity>()
                        .eq(AiJobEntity::getWorkspaceId, workspaceId)
                        .orderByDesc(AiJobEntity::getCreatedAt)
                        .last("LIMIT " + offset + ", " + limit)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    @Override
    public List<AiJobView> findByWorkspaceAndStatus(Long workspaceId, String status, int offset, int limit) {
        List<AiJobEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<AiJobEntity>()
                        .eq(AiJobEntity::getWorkspaceId, workspaceId)
                        .eq(AiJobEntity::getStatus, status)
                        .orderByDesc(AiJobEntity::getCreatedAt)
                        .last("LIMIT " + offset + ", " + limit)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    @Override
    public List<AiJobView> findByCreatorUserId(Long userId, int offset, int limit) {
        List<AiJobEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<AiJobEntity>()
                        .eq(AiJobEntity::getCreatorUserId, userId)
                        .orderByDesc(AiJobEntity::getCreatedAt)
                        .last("LIMIT " + offset + ", " + limit)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    @Override
    public Optional<AiJobView> findByIdempotencyKey(String idempotencyKey) {
        if (idempotencyKey == null) return Optional.empty();
        AiJobEntity entity = mapper.selectOne(
                new LambdaQueryWrapper<AiJobEntity>()
                        .eq(AiJobEntity::getIdempotencyKey, idempotencyKey)
        );
        return Optional.ofNullable(entity).map(this::toView);
    }

    @Override
    public List<AiJobView> findRunningJobs(int limit) {
        List<AiJobEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<AiJobEntity>()
                        .eq(AiJobEntity::getStatus, "running")
                        .orderByAsc(AiJobEntity::getCreatedAt)
                        .last("LIMIT " + limit)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    @Override
    public long countByWorkspaceId(Long workspaceId) {
        return mapper.selectCount(
                new LambdaQueryWrapper<AiJobEntity>()
                        .eq(AiJobEntity::getWorkspaceId, workspaceId)
        );
    }

    private AiJobEntity toEntity(AiJobView view) {
        AiJobEntity entity = new AiJobEntity();
        entity.setId(view.getJobId());
        entity.setWorkspaceId(view.getWorkspaceId());
        entity.setCreatorUserId(view.getCreatorUserId());
        entity.setCapabilityKey(view.getCapabilityKey());
        entity.setStatus(view.getStatus());
        entity.setSourceAssetId(view.getSourceAssetId());
        entity.setSourceAssetVersionId(view.getSourceAssetVersionId());
        entity.setProvider(view.getProvider());
        entity.setParametersJson(view.getParametersJson());
        entity.setIdempotencyKey(view.getIdempotencyKey());
        entity.setErrorCode(view.getErrorCode());
        entity.setErrorMessage(view.getErrorMessage());
        entity.setCreatedAt(view.getCreatedAt());
        entity.setStartedAt(view.getStartedAt());
        entity.setFinishedAt(view.getFinishedAt());
        return entity;
    }

    private AiJobView toView(AiJobEntity entity) {
        AiJobView view = new AiJobView();
        view.setJobId(entity.getId());
        view.setWorkspaceId(entity.getWorkspaceId());
        view.setCreatorUserId(entity.getCreatorUserId());
        view.setCapabilityKey(entity.getCapabilityKey());
        view.setStatus(entity.getStatus());
        view.setSourceAssetId(entity.getSourceAssetId());
        view.setSourceAssetVersionId(entity.getSourceAssetVersionId());
        view.setProvider(entity.getProvider());
        view.setParametersJson(entity.getParametersJson());
        view.setIdempotencyKey(entity.getIdempotencyKey());
        view.setErrorCode(entity.getErrorCode());
        view.setErrorMessage(entity.getErrorMessage());
        view.setCreatedAt(entity.getCreatedAt());
        view.setStartedAt(entity.getStartedAt());
        view.setFinishedAt(entity.getFinishedAt());
        return view;
    }
}
