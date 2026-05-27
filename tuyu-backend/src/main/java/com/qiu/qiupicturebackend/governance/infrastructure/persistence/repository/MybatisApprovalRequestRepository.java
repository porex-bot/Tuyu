package com.qiu.qiupicturebackend.governance.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalRequestView;
import com.qiu.qiupicturebackend.governance.domain.repository.ApprovalRequestRepository;
import com.qiu.qiupicturebackend.governance.infrastructure.persistence.entity.ApprovalRequestEntity;
import com.qiu.qiupicturebackend.governance.infrastructure.persistence.mapper.ApprovalRequestMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MybatisApprovalRequestRepository implements ApprovalRequestRepository {

    private final ApprovalRequestMapper mapper;

    public MybatisApprovalRequestRepository(ApprovalRequestMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ApprovalRequestView save(ApprovalRequestView request) {
        ApprovalRequestEntity entity = toEntity(request);
        mapper.insert(entity);
        return toView(entity);
    }

    @Override
    public ApprovalRequestView update(ApprovalRequestView request) {
        ApprovalRequestEntity entity = toEntity(request);
        entity.setUpdatedAt(new java.util.Date());
        mapper.updateById(entity);
        return toView(entity);
    }

    @Override
    public Optional<ApprovalRequestView> findById(Long approvalId) {
        ApprovalRequestEntity entity = mapper.selectById(approvalId);
        return Optional.ofNullable(entity).map(this::toView);
    }

    @Override
    public List<ApprovalRequestView> findByWorkspaceId(Long workspaceId, int offset, int limit) {
        List<ApprovalRequestEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<ApprovalRequestEntity>()
                        .eq(ApprovalRequestEntity::getWorkspaceId, workspaceId)
                        .orderByDesc(ApprovalRequestEntity::getCreatedAt)
                        .last("LIMIT " + offset + ", " + limit)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    @Override
    public List<ApprovalRequestView> findByTarget(Long workspaceId, String targetType, Long targetId) {
        List<ApprovalRequestEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<ApprovalRequestEntity>()
                        .eq(ApprovalRequestEntity::getWorkspaceId, workspaceId)
                        .eq(ApprovalRequestEntity::getTargetType, targetType)
                        .eq(ApprovalRequestEntity::getTargetId, targetId)
                        .orderByDesc(ApprovalRequestEntity::getCreatedAt)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    @Override
    public List<ApprovalRequestView> findPendingByWorkspace(Long workspaceId, int offset, int limit) {
        List<ApprovalRequestEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<ApprovalRequestEntity>()
                        .eq(ApprovalRequestEntity::getWorkspaceId, workspaceId)
                        .eq(ApprovalRequestEntity::getStatus, "pending")
                        .orderByDesc(ApprovalRequestEntity::getCreatedAt)
                        .last("LIMIT " + offset + ", " + limit)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    @Override
    public List<ApprovalRequestView> findBySubmitter(Long userId, int offset, int limit) {
        List<ApprovalRequestEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<ApprovalRequestEntity>()
                        .eq(ApprovalRequestEntity::getSubmittedBy, userId)
                        .orderByDesc(ApprovalRequestEntity::getCreatedAt)
                        .last("LIMIT " + offset + ", " + limit)
        );
        return entities.stream().map(this::toView).collect(Collectors.toList());
    }

    private ApprovalRequestEntity toEntity(ApprovalRequestView view) {
        ApprovalRequestEntity entity = new ApprovalRequestEntity();
        entity.setId(view.getApprovalId());
        entity.setWorkspaceId(view.getWorkspaceId());
        entity.setTargetType(view.getTargetType());
        entity.setTargetId(view.getTargetId());
        entity.setTargetVersionId(view.getTargetVersionId());
        entity.setRequestType(view.getRequestType());
        entity.setStatus(view.getStatus());
        entity.setSubmittedBy(view.getSubmittedBy());
        entity.setSubmittedAt(view.getSubmittedAt());
        entity.setResolvedBy(view.getResolvedBy());
        entity.setResolvedAt(view.getResolvedAt());
        entity.setReason(view.getReason());
        entity.setResultMessage(view.getResultMessage());
        entity.setCreatedAt(view.getCreatedAt());
        return entity;
    }

    private ApprovalRequestView toView(ApprovalRequestEntity entity) {
        ApprovalRequestView view = new ApprovalRequestView();
        view.setApprovalId(entity.getId());
        view.setWorkspaceId(entity.getWorkspaceId());
        view.setTargetType(entity.getTargetType());
        view.setTargetId(entity.getTargetId());
        view.setTargetVersionId(entity.getTargetVersionId());
        view.setRequestType(entity.getRequestType());
        view.setStatus(entity.getStatus());
        view.setSubmittedBy(entity.getSubmittedBy());
        view.setSubmittedAt(entity.getSubmittedAt());
        view.setResolvedBy(entity.getResolvedBy());
        view.setResolvedAt(entity.getResolvedAt());
        view.setReason(entity.getReason());
        view.setResultMessage(entity.getResultMessage());
        view.setCreatedAt(entity.getCreatedAt());
        view.setUpdatedAt(entity.getUpdatedAt());
        return view;
    }
}
