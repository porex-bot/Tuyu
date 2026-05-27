package com.qiu.qiupicturebackend.governance.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalDecisionView;
import com.qiu.qiupicturebackend.governance.domain.repository.ApprovalDecisionRepository;
import com.qiu.qiupicturebackend.governance.infrastructure.persistence.entity.ApprovalDecisionEntity;
import com.qiu.qiupicturebackend.governance.infrastructure.persistence.mapper.ApprovalDecisionMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MybatisApprovalDecisionRepository implements ApprovalDecisionRepository {

    private final ApprovalDecisionMapper mapper;

    public MybatisApprovalDecisionRepository(ApprovalDecisionMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ApprovalDecisionView save(ApprovalDecisionView decision) {
        ApprovalDecisionEntity entity = toEntity(decision);
        mapper.insert(entity);
        return toView(entity);
    }

    @Override
    public List<ApprovalDecisionView> findByApprovalId(Long approvalId) {
        List<ApprovalDecisionEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<ApprovalDecisionEntity>()
                        .eq(ApprovalDecisionEntity::getApprovalId, approvalId)
                        .orderByDesc(ApprovalDecisionEntity::getCreatedAt)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    @Override
    public List<ApprovalDecisionView> findByStepId(Long stepId) {
        List<ApprovalDecisionEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<ApprovalDecisionEntity>()
                        .eq(ApprovalDecisionEntity::getStepId, stepId)
                        .orderByDesc(ApprovalDecisionEntity::getCreatedAt)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    private ApprovalDecisionEntity toEntity(ApprovalDecisionView view) {
        ApprovalDecisionEntity entity = new ApprovalDecisionEntity();
        entity.setId(view.getDecisionId());
        entity.setStepId(view.getStepId());
        entity.setApprovalId(view.getApprovalId());
        entity.setDecidedBy(view.getDecidedBy());
        entity.setDecisionType(view.getDecisionType());
        entity.setComment(view.getComment());
        entity.setCreatedAt(view.getCreatedAt());
        return entity;
    }

    private ApprovalDecisionView toView(ApprovalDecisionEntity entity) {
        ApprovalDecisionView view = new ApprovalDecisionView();
        view.setDecisionId(entity.getId());
        view.setStepId(entity.getStepId());
        view.setApprovalId(entity.getApprovalId());
        view.setDecidedBy(entity.getDecidedBy());
        view.setDecisionType(entity.getDecisionType());
        view.setComment(entity.getComment());
        view.setCreatedAt(entity.getCreatedAt());
        return view;
    }
}
