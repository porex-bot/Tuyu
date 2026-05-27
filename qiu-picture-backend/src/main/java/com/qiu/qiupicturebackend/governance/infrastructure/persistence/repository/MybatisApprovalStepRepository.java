package com.qiu.qiupicturebackend.governance.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalStepView;
import com.qiu.qiupicturebackend.governance.domain.repository.ApprovalStepRepository;
import com.qiu.qiupicturebackend.governance.infrastructure.persistence.entity.ApprovalStepEntity;
import com.qiu.qiupicturebackend.governance.infrastructure.persistence.mapper.ApprovalStepMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MybatisApprovalStepRepository implements ApprovalStepRepository {

    private final ApprovalStepMapper mapper;

    public MybatisApprovalStepRepository(ApprovalStepMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ApprovalStepView save(ApprovalStepView step) {
        ApprovalStepEntity entity = toEntity(step);
        mapper.insert(entity);
        return toView(entity);
    }

    @Override
    public ApprovalStepView update(ApprovalStepView step) {
        ApprovalStepEntity entity = toEntity(step);
        entity.setUpdatedAt(new java.util.Date());
        mapper.updateById(entity);
        return toView(entity);
    }

    @Override
    public Optional<ApprovalStepView> findById(Long stepId) {
        ApprovalStepEntity entity = mapper.selectById(stepId);
        return Optional.ofNullable(entity).map(this::toView);
    }

    @Override
    public List<ApprovalStepView> findByApprovalId(Long approvalId) {
        List<ApprovalStepEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<ApprovalStepEntity>()
                        .eq(ApprovalStepEntity::getApprovalId, approvalId)
                        .orderByAsc(ApprovalStepEntity::getStepOrder)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    @Override
    public List<ApprovalStepView> findPendingByReviewer(Long reviewerId, int offset, int limit) {
        List<ApprovalStepEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<ApprovalStepEntity>()
                        .eq(ApprovalStepEntity::getReviewerId, reviewerId)
                        .eq(ApprovalStepEntity::getStatus, "pending")
                        .orderByDesc(ApprovalStepEntity::getCreatedAt)
                        .last("LIMIT " + offset + ", " + limit)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    private ApprovalStepEntity toEntity(ApprovalStepView view) {
        ApprovalStepEntity entity = new ApprovalStepEntity();
        entity.setId(view.getStepId());
        entity.setApprovalId(view.getApprovalId());
        entity.setStepOrder(view.getStepOrder());
        entity.setReviewerId(view.getReviewerId());
        entity.setStatus(view.getStatus());
        entity.setCreatedAt(view.getCreatedAt());
        return entity;
    }

    private ApprovalStepView toView(ApprovalStepEntity entity) {
        ApprovalStepView view = new ApprovalStepView();
        view.setStepId(entity.getId());
        view.setApprovalId(entity.getApprovalId());
        view.setStepOrder(entity.getStepOrder());
        view.setReviewerId(entity.getReviewerId());
        view.setStatus(entity.getStatus());
        view.setCreatedAt(entity.getCreatedAt());
        view.setUpdatedAt(entity.getUpdatedAt());
        return view;
    }
}
