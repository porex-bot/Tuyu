package com.qiu.qiupicturebackend.activity.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qiu.qiupicturebackend.activity.domain.model.*;
import com.qiu.qiupicturebackend.activity.domain.repository.ActivityRecordRepository;
import com.qiu.qiupicturebackend.activity.infrastructure.persistence.entity.ActivityRecordEntity;
import com.qiu.qiupicturebackend.activity.infrastructure.persistence.mapper.ActivityRecordMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MybatisActivityRecordRepository implements ActivityRecordRepository {

    private final ActivityRecordMapper mapper;

    public MybatisActivityRecordRepository(ActivityRecordMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ActivityRecordView save(ActivityRecordView record) {
        ActivityRecordEntity entity = toEntity(record);
        mapper.insert(entity);
        return toView(entity);
    }

    @Override
    public List<ActivityRecordView> findByWorkspaceId(Long workspaceId, int offset, int limit) {
        List<ActivityRecordEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<ActivityRecordEntity>()
                        .eq(ActivityRecordEntity::getWorkspaceId, workspaceId)
                        .orderByDesc(ActivityRecordEntity::getOccurredAt)
                        .last("LIMIT " + offset + ", " + limit)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    @Override
    public List<ActivityRecordView> findByTarget(String targetType, Long targetId, int offset, int limit) {
        List<ActivityRecordEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<ActivityRecordEntity>()
                        .eq(ActivityRecordEntity::getTargetType, targetType)
                        .eq(ActivityRecordEntity::getTargetId, targetId)
                        .orderByDesc(ActivityRecordEntity::getOccurredAt)
                        .last("LIMIT " + offset + ", " + limit)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    @Override
    public long countByWorkspaceId(Long workspaceId) {
        return mapper.selectCount(
                new LambdaQueryWrapper<ActivityRecordEntity>()
                        .eq(ActivityRecordEntity::getWorkspaceId, workspaceId)
        );
    }

    @Override
    public List<ActivityRecordView> findByWorkspaceAndTarget(Long workspaceId, String targetType, Long targetId, int offset, int limit) {
        List<ActivityRecordEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<ActivityRecordEntity>()
                        .eq(ActivityRecordEntity::getWorkspaceId, workspaceId)
                        .eq(ActivityRecordEntity::getTargetType, targetType)
                        .eq(ActivityRecordEntity::getTargetId, targetId)
                        .orderByDesc(ActivityRecordEntity::getOccurredAt)
                        .last("LIMIT " + offset + ", " + limit)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    @Override
    public long countByWorkspaceAndTarget(Long workspaceId, String targetType, Long targetId) {
        return mapper.selectCount(
                new LambdaQueryWrapper<ActivityRecordEntity>()
                        .eq(ActivityRecordEntity::getWorkspaceId, workspaceId)
                        .eq(ActivityRecordEntity::getTargetType, targetType)
                        .eq(ActivityRecordEntity::getTargetId, targetId)
        );
    }

    @Override
    public long countByTarget(String targetType, Long targetId) {
        return mapper.selectCount(
                new LambdaQueryWrapper<ActivityRecordEntity>()
                        .eq(ActivityRecordEntity::getTargetType, targetType)
                        .eq(ActivityRecordEntity::getTargetId, targetId)
        );
    }

    private ActivityRecordEntity toEntity(ActivityRecordView view) {
        ActivityRecordEntity entity = new ActivityRecordEntity();
        entity.setId(view.getActivityId());
        entity.setWorkspaceId(view.getWorkspaceId());
        entity.setActorUserId(view.getActor() != null ? view.getActor().getUserId() : null);
        entity.setActionType(view.getActionType());
        entity.setTargetType(view.getTarget() != null ? view.getTarget().getTargetType() : null);
        entity.setTargetId(view.getTarget() != null ? view.getTarget().getTargetId() : null);
        entity.setSecondaryTargetType(view.getSecondaryTarget() != null ? view.getSecondaryTarget().getTargetType() : null);
        entity.setSecondaryTargetId(view.getSecondaryTarget() != null ? view.getSecondaryTarget().getTargetId() : null);
        entity.setSummary(view.getSummary());
        entity.setVisibility(view.getVisibility());
        entity.setOccurredAt(view.getOccurredAt());
        entity.setCreateTime(new java.util.Date());
        return entity;
    }

    private ActivityRecordView toView(ActivityRecordEntity entity) {
        ActivityRecordView view = new ActivityRecordView();
        view.setActivityId(entity.getId());
        view.setWorkspaceId(entity.getWorkspaceId());

        ActivityActorView actor = new ActivityActorView();
        actor.setUserId(entity.getActorUserId());
        view.setActor(actor);

        view.setActionType(entity.getActionType());

        if (entity.getTargetType() != null) {
            ActivityTargetView target = new ActivityTargetView();
            target.setTargetType(entity.getTargetType());
            target.setTargetId(entity.getTargetId());
            view.setTarget(target);
        }

        if (entity.getSecondaryTargetType() != null) {
            ActivityTargetView secondaryTarget = new ActivityTargetView();
            secondaryTarget.setTargetType(entity.getSecondaryTargetType());
            secondaryTarget.setTargetId(entity.getSecondaryTargetId());
            view.setSecondaryTarget(secondaryTarget);
        }

        view.setSummary(entity.getSummary());
        view.setVisibility(entity.getVisibility());
        view.setOccurredAt(entity.getOccurredAt());
        return view;
    }
}
