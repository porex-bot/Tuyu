package com.qiu.qiupicturebackend.governance;

import com.qiu.qiupicturebackend.activity.application.ActivityRecordApplicationService;
import com.qiu.qiupicturebackend.governance.application.GovernancePolicyApplicationService;
import com.qiu.qiupicturebackend.governance.application.command.ConfigureGovernancePolicyCommand;
import com.qiu.qiupicturebackend.governance.domain.model.GovernancePolicyMode;
import com.qiu.qiupicturebackend.governance.domain.model.GovernancePolicyView;
import com.qiu.qiupicturebackend.governance.domain.repository.GovernancePolicyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GovernancePolicyApplicationServiceTest {

    @Mock private GovernancePolicyRepository governancePolicyRepository;
    @Mock private ActivityRecordApplicationService activityRecordApplicationService;

    @InjectMocks
    private GovernancePolicyApplicationService service;

    @Test
    void shouldReturnDefaultPolicyWhenNoneExists() {
        when(governancePolicyRepository.findByWorkspaceId(1L)).thenReturn(Optional.empty());

        GovernancePolicyView policy = service.getPolicy(1L);

        assertNotNull(policy);
        assertEquals(GovernancePolicyMode.OFF.getCode(), policy.getMode());
        assertEquals(0, policy.getRequireApprovalForAssets());
        assertEquals(0, policy.getRequireApprovalForCollections());
    }

    @Test
    void shouldReturnExistingPolicy() {
        GovernancePolicyView existing = new GovernancePolicyView();
        existing.setPolicyId(10L);
        existing.setWorkspaceId(1L);
        existing.setMode(GovernancePolicyMode.MANUAL.getCode());
        existing.setRequireApprovalForAssets(1);

        when(governancePolicyRepository.findByWorkspaceId(1L)).thenReturn(Optional.of(existing));

        GovernancePolicyView policy = service.getPolicy(1L);

        assertNotNull(policy);
        assertEquals(GovernancePolicyMode.MANUAL.getCode(), policy.getMode());
        assertEquals(1, policy.getRequireApprovalForAssets());
    }

    @Test
    void shouldCreatePolicyOnFirstUpdate() {
        when(governancePolicyRepository.findByWorkspaceId(1L)).thenReturn(Optional.empty());
        when(governancePolicyRepository.save(any(GovernancePolicyView.class))).thenAnswer(inv -> {
            GovernancePolicyView p = inv.getArgument(0);
            p.setPolicyId(1L);
            return p;
        });

        ConfigureGovernancePolicyCommand command = new ConfigureGovernancePolicyCommand();
        command.setWorkspaceId(1L);
        command.setMode(GovernancePolicyMode.MANUAL.getCode());
        command.setRequireApprovalForAssets(true);

        GovernancePolicyView policy = service.updatePolicy(command, 100L);

        assertNotNull(policy.getPolicyId());
        assertEquals(GovernancePolicyMode.MANUAL.getCode(), policy.getMode());
        assertEquals(1, policy.getRequireApprovalForAssets());
        verify(governancePolicyRepository).save(any(GovernancePolicyView.class));
    }

    @Test
    void shouldUpdateExistingPolicy() {
        GovernancePolicyView existing = new GovernancePolicyView();
        existing.setPolicyId(10L);
        existing.setWorkspaceId(1L);
        existing.setMode(GovernancePolicyMode.OFF.getCode());

        when(governancePolicyRepository.findByWorkspaceId(1L)).thenReturn(Optional.of(existing));
        when(governancePolicyRepository.update(any(GovernancePolicyView.class))).thenAnswer(inv -> inv.getArgument(0));

        ConfigureGovernancePolicyCommand command = new ConfigureGovernancePolicyCommand();
        command.setWorkspaceId(1L);
        command.setMode(GovernancePolicyMode.STRICT.getCode());

        GovernancePolicyView policy = service.updatePolicy(command, 100L);

        assertEquals(GovernancePolicyMode.STRICT.getCode(), policy.getMode());
        verify(governancePolicyRepository).update(any(GovernancePolicyView.class));
    }
}
