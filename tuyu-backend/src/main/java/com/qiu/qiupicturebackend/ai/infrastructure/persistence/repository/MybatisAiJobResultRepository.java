package com.qiu.qiupicturebackend.ai.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qiu.qiupicturebackend.ai.domain.model.AiJobResultView;
import com.qiu.qiupicturebackend.ai.domain.repository.AiJobResultRepository;
import com.qiu.qiupicturebackend.ai.infrastructure.persistence.entity.AiJobResultEntity;
import com.qiu.qiupicturebackend.ai.infrastructure.persistence.mapper.AiJobResultMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MybatisAiJobResultRepository implements AiJobResultRepository {

    private final AiJobResultMapper mapper;

    public MybatisAiJobResultRepository(AiJobResultMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public AiJobResultView save(AiJobResultView result) {
        AiJobResultEntity entity = toEntity(result);
        mapper.insert(entity);
        return toView(entity);
    }

    @Override
    public AiJobResultView update(AiJobResultView result) {
        AiJobResultEntity entity = toEntity(result);
        mapper.updateById(entity);
        return toView(entity);
    }

    @Override
    public Optional<AiJobResultView> findById(Long resultId) {
        AiJobResultEntity entity = mapper.selectById(resultId);
        return Optional.ofNullable(entity).map(this::toView);
    }

    @Override
    public List<AiJobResultView> findByJobId(Long jobId) {
        List<AiJobResultEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<AiJobResultEntity>()
                        .eq(AiJobResultEntity::getJobId, jobId)
                        .orderByDesc(AiJobResultEntity::getCreatedAt)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    private AiJobResultEntity toEntity(AiJobResultView view) {
        AiJobResultEntity entity = new AiJobResultEntity();
        entity.setId(view.getResultId());
        entity.setJobId(view.getJobId());
        entity.setResultType(view.getResultType());
        entity.setOutputUrl(view.getOutputUrl());
        entity.setOutputStorageObjectId(view.getOutputStorageObjectId());
        entity.setOutputMetadataJson(view.getOutputMetadataJson());
        entity.setApplyStatus(view.getApplyStatus());
        entity.setAssetVersionId(view.getAssetVersionId());
        return entity;
    }

    private AiJobResultView toView(AiJobResultEntity entity) {
        AiJobResultView view = new AiJobResultView();
        view.setResultId(entity.getId());
        view.setJobId(entity.getJobId());
        view.setResultType(entity.getResultType());
        view.setOutputUrl(entity.getOutputUrl());
        view.setOutputStorageObjectId(entity.getOutputStorageObjectId());
        view.setOutputMetadataJson(entity.getOutputMetadataJson());
        view.setApplyStatus(entity.getApplyStatus());
        view.setAssetVersionId(entity.getAssetVersionId());
        view.setCreatedAt(entity.getCreatedAt());
        return view;
    }
}
