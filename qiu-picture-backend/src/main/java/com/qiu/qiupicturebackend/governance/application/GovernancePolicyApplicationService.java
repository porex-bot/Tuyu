package com.qiu.qiupicturebackend.governance.application;

import com.qiu.qiupicturebackend.activity.application.ActivityRecordApplicationService;
import com.qiu.qiupicturebackend.activity.application.command.RecordActivityCommand;
import com.qiu.qiupicturebackend.governance.application.command.ConfigureGovernancePolicyCommand;
import com.qiu.qiupicturebackend.governance.domain.model.GovernancePolicyMode;
import com.qiu.qiupicturebackend.governance.domain.model.GovernancePolicyView;
import com.qiu.qiupicturebackend.governance.domain.repository.GovernancePolicyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class GovernancePolicyApplicationService {

    @Resource
    private GovernancePolicyRepository governancePolicyRepository;

    @Resource
    private ActivityRecordApplicationService activityRecordApplicationService;

    public GovernancePolicyView getPolicy(Long workspaceId) {
        return governancePolicyRepository.findByWorkspaceId(workspaceId)
                .orElseGet(() -> buildDefaultPolicy(workspaceId));
    }

    public GovernancePolicyView updatePolicy(ConfigureGovernancePolicyCommand command, Long updatedBy) {
        GovernancePolicyView existing = governancePolicyRepository.findByWorkspaceId(command.getWorkspaceId())
                .orElseGet(() -> buildDefaultPolicy(command.getWorkspaceId()));

        if (command.getMode() != null) {
            if (GovernancePolicyMode.fromCode(command.getMode()) == null) {
                throw new IllegalArgumentException("无效的治理模式: " + command.getMode());
            }
            existing.setMode(command.getMode());
        }
        if (command.getRequireApprovalForAssets() != null) {
            existing.setRequireApprovalForAssets(command.getRequireApprovalForAssets() ? 1 : 0);
        }
        if (command.getRequireApprovalForCollections() != null) {
            existing.setRequireApprovalForCollections(command.getRequireApprovalForCollections() ? 1 : 0);
        }
        if (command.getRequireApprovalForAiResults() != null) {
            existing.setRequireApprovalForAiResults(command.getRequireApprovalForAiResults() ? 1 : 0);
        }
        if (command.getAutoApproveTrustedUsers() != null) {
            existing.setAutoApproveTrustedUsers(command.getAutoApproveTrustedUsers() ? 1 : 0);
        }
        existing.setUpdatedBy(updatedBy);

        GovernancePolicyView result;
        if (existing.getPolicyId() != null) {
            result = governancePolicyRepository.update(existing);
        } else {
            result = governancePolicyRepository.save(existing);
        }

        recordActivity(command.getWorkspaceId(), updatedBy, "governance.policy.updated");

        return result;
    }

    private void recordActivity(Long workspaceId, Long userId, String actionType) {
        try {
            activityRecordApplicationService.record(RecordActivityCommand.builder()
                    .workspaceId(workspaceId)
                    .actorUserId(userId)
                    .actionType(actionType)
                    .targetType("governance_policy")
                    .targetId(workspaceId)
                    .build());
        } catch (Exception e) {
            log.warn("Failed to record governance activity: action={}, workspaceId={}", actionType, workspaceId, e);
        }
    }

    private GovernancePolicyView buildDefaultPolicy(Long workspaceId) {
        GovernancePolicyView policy = new GovernancePolicyView();
        policy.setWorkspaceId(workspaceId);
        policy.setMode(GovernancePolicyMode.OFF.getCode());
        policy.setRequireApprovalForAssets(0);
        policy.setRequireApprovalForCollections(0);
        policy.setRequireApprovalForAiResults(0);
        policy.setAutoApproveTrustedUsers(0);
        policy.setCreatedAt(new Date());
        policy.setUpdatedAt(new Date());
        return policy;
    }
}
