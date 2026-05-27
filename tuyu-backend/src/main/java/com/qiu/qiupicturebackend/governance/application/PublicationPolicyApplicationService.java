package com.qiu.qiupicturebackend.governance.application;

import com.qiu.qiupicturebackend.governance.domain.model.GovernancePolicyMode;
import com.qiu.qiupicturebackend.governance.domain.model.GovernancePolicyView;
import com.qiu.qiupicturebackend.governance.domain.model.GovernanceTargetType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class PublicationPolicyApplicationService {

    @Resource
    private GovernancePolicyApplicationService governancePolicyApplicationService;

    public boolean requiresApproval(Long workspaceId, String targetType) {
        GovernancePolicyView policy = governancePolicyApplicationService.getPolicy(workspaceId);
        GovernancePolicyMode mode = GovernancePolicyMode.fromCode(policy.getMode());
        if (mode == GovernancePolicyMode.OFF) {
            return false;
        }
        if (mode == GovernancePolicyMode.STRICT) {
            return true;
        }
        if (GovernanceTargetType.ASSET.getCode().equals(targetType)) {
            return policy.getRequireApprovalForAssets() != null && policy.getRequireApprovalForAssets() == 1;
        }
        if (GovernanceTargetType.COLLECTION.getCode().equals(targetType)) {
            return policy.getRequireApprovalForCollections() != null && policy.getRequireApprovalForCollections() == 1;
        }
        if (GovernanceTargetType.AI_RESULT.getCode().equals(targetType)) {
            return policy.getRequireApprovalForAiResults() != null && policy.getRequireApprovalForAiResults() == 1;
        }
        return false;
    }

    public GovernancePolicyMode getEffectiveMode(Long workspaceId) {
        GovernancePolicyView policy = governancePolicyApplicationService.getPolicy(workspaceId);
        return GovernancePolicyMode.fromCode(policy.getMode());
    }
}
