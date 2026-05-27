package com.qiu.qiupicturebackend.governance.domain.repository;

import com.qiu.qiupicturebackend.governance.domain.model.GovernancePolicyView;

import java.util.Optional;

public interface GovernancePolicyRepository {

    GovernancePolicyView save(GovernancePolicyView policy);

    GovernancePolicyView update(GovernancePolicyView policy);

    Optional<GovernancePolicyView> findByWorkspaceId(Long workspaceId);
}
