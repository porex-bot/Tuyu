package com.qiu.qiupicturebackend.governance.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qiu.qiupicturebackend.governance.domain.model.GovernancePolicyView;
import com.qiu.qiupicturebackend.governance.domain.repository.GovernancePolicyRepository;
import com.qiu.qiupicturebackend.governance.infrastructure.persistence.entity.GovernancePolicyEntity;
import com.qiu.qiupicturebackend.governance.infrastructure.persistence.mapper.GovernancePolicyMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MybatisGovernancePolicyRepository implements GovernancePolicyRepository {

    private final GovernancePolicyMapper mapper;

    public MybatisGovernancePolicyRepository(GovernancePolicyMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public GovernancePolicyView save(GovernancePolicyView policy) {
        GovernancePolicyEntity entity = toEntity(policy);
        mapper.insert(entity);
        return toView(entity);
    }

    @Override
    public GovernancePolicyView update(GovernancePolicyView policy) {
        GovernancePolicyEntity entity = toEntity(policy);
        entity.setUpdatedAt(new java.util.Date());
        mapper.updateById(entity);
        return toView(entity);
    }

    @Override
    public Optional<GovernancePolicyView> findByWorkspaceId(Long workspaceId) {
        GovernancePolicyEntity entity = mapper.selectOne(
                new LambdaQueryWrapper<GovernancePolicyEntity>()
                        .eq(GovernancePolicyEntity::getWorkspaceId, workspaceId)
        );
        return Optional.ofNullable(entity).map(this::toView);
    }

    private GovernancePolicyEntity toEntity(GovernancePolicyView view) {
        GovernancePolicyEntity entity = new GovernancePolicyEntity();
        entity.setId(view.getPolicyId());
        entity.setWorkspaceId(view.getWorkspaceId());
        entity.setMode(view.getMode());
        entity.setRequireApprovalForAssets(view.getRequireApprovalForAssets());
        entity.setRequireApprovalForCollections(view.getRequireApprovalForCollections());
        entity.setRequireApprovalForAiResults(view.getRequireApprovalForAiResults());
        entity.setAutoApproveTrustedUsers(view.getAutoApproveTrustedUsers());
        entity.setUpdatedBy(view.getUpdatedBy());
        entity.setCreatedAt(view.getCreatedAt());
        return entity;
    }

    private GovernancePolicyView toView(GovernancePolicyEntity entity) {
        GovernancePolicyView view = new GovernancePolicyView();
        view.setPolicyId(entity.getId());
        view.setWorkspaceId(entity.getWorkspaceId());
        view.setMode(entity.getMode());
        view.setRequireApprovalForAssets(entity.getRequireApprovalForAssets());
        view.setRequireApprovalForCollections(entity.getRequireApprovalForCollections());
        view.setRequireApprovalForAiResults(entity.getRequireApprovalForAiResults());
        view.setAutoApproveTrustedUsers(entity.getAutoApproveTrustedUsers());
        view.setUpdatedBy(entity.getUpdatedBy());
        view.setCreatedAt(entity.getCreatedAt());
        view.setUpdatedAt(entity.getUpdatedAt());
        return view;
    }
}
