package com.qiu.qiupicturebackend.governance;

import com.qiu.qiupicturebackend.activity.application.ActivityRecordApplicationService;
import com.qiu.qiupicturebackend.collection.domain.model.CollectionView;
import com.qiu.qiupicturebackend.collection.domain.repository.CollectionRepository;
import com.qiu.qiupicturebackend.exception.BusinessException;
import com.qiu.qiupicturebackend.governance.application.ApprovalApplicationService;
import com.qiu.qiupicturebackend.governance.application.PublicationPolicyApplicationService;
import com.qiu.qiupicturebackend.governance.application.command.CancelApprovalRequestCommand;
import com.qiu.qiupicturebackend.governance.application.command.DecideApprovalCommand;
import com.qiu.qiupicturebackend.governance.application.command.SubmitApprovalRequestCommand;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalRequestView;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalStatus;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalStepView;
import com.qiu.qiupicturebackend.governance.domain.repository.ApprovalDecisionRepository;
import com.qiu.qiupicturebackend.governance.domain.repository.ApprovalRequestRepository;
import com.qiu.qiupicturebackend.governance.domain.repository.ApprovalStepRepository;
import com.qiu.qiupicturebackend.model.entity.Picture;
import com.qiu.qiupicturebackend.service.PictureService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ApprovalApplicationServiceTest {

    @Mock private ApprovalRequestRepository approvalRequestRepository;
    @Mock private ApprovalStepRepository approvalStepRepository;
    @Mock private ApprovalDecisionRepository approvalDecisionRepository;
    @Mock private PublicationPolicyApplicationService publicationPolicyApplicationService;
    @Mock private ActivityRecordApplicationService activityRecordApplicationService;
    @Mock private PictureService pictureService;
    @Mock private CollectionRepository collectionRepository;

    @InjectMocks
    private ApprovalApplicationService service;

    @Test
    void shouldSubmitApprovalRequest() {
        Picture picture = new Picture();
        picture.setId(100L);
        picture.setSpaceId(1L);
        when(pictureService.getById(100L)).thenReturn(picture);
        when(approvalRequestRepository.findByTarget(1L, "asset", 100L)).thenReturn(Collections.emptyList());
        when(approvalRequestRepository.save(any(ApprovalRequestView.class))).thenAnswer(inv -> {
            ApprovalRequestView r = inv.getArgument(0);
            r.setApprovalId(1L);
            return r;
        });
        when(approvalStepRepository.save(any(ApprovalStepView.class))).thenAnswer(inv -> {
            ApprovalStepView s = inv.getArgument(0);
            s.setStepId(1L);
            return s;
        });

        SubmitApprovalRequestCommand command = new SubmitApprovalRequestCommand();
        command.setWorkspaceId(1L);
        command.setTargetType("asset");
        command.setTargetId(100L);
        command.setRequestType("publish");
        command.setReason("Ready for review");

        ApprovalRequestView result = service.submitApprovalRequest(command, 10L);

        assertNotNull(result);
        assertEquals(ApprovalStatus.PENDING.getCode(), result.getStatus());
        assertEquals(10L, result.getSubmittedBy());
        verify(approvalRequestRepository).save(any(ApprovalRequestView.class));
        verify(approvalStepRepository).save(any(ApprovalStepView.class));
    }

    @Test
    void shouldRejectDuplicatePendingApproval() {
        ApprovalRequestView existing = new ApprovalRequestView();
        existing.setApprovalId(5L);
        existing.setStatus(ApprovalStatus.PENDING.getCode());

        when(approvalRequestRepository.findByTarget(1L, "asset", 100L)).thenReturn(List.of(existing));

        SubmitApprovalRequestCommand command = new SubmitApprovalRequestCommand();
        command.setWorkspaceId(1L);
        command.setTargetType("asset");
        command.setTargetId(100L);

        assertThrows(BusinessException.class, () -> service.submitApprovalRequest(command, 10L));
    }

    @Test
    void shouldApproveRequest() {
        ApprovalRequestView request = new ApprovalRequestView();
        request.setApprovalId(1L);
        request.setWorkspaceId(1L);
        request.setStatus(ApprovalStatus.PENDING.getCode());

        ApprovalStepView step = new ApprovalStepView();
        step.setStepId(1L);
        step.setApprovalId(1L);
        step.setStepOrder(1);
        step.setStatus(ApprovalStatus.PENDING.getCode());

        when(approvalRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(approvalStepRepository.findByApprovalId(1L)).thenReturn(List.of(step));
        when(approvalStepRepository.update(any())).thenAnswer(inv -> inv.getArgument(0));
        when(approvalDecisionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(approvalRequestRepository.update(any())).thenAnswer(inv -> inv.getArgument(0));

        DecideApprovalCommand command = new DecideApprovalCommand();
        command.setApprovalId(1L);
        command.setDecisionType("approve");
        command.setComment("Looks good");

        ApprovalRequestView result = service.decideApproval(1L, command, 20L);

        assertEquals(ApprovalStatus.APPROVED.getCode(), result.getStatus());
        assertEquals(20L, result.getResolvedBy());
    }

    @Test
    void shouldRejectRequest() {
        ApprovalRequestView request = new ApprovalRequestView();
        request.setApprovalId(1L);
        request.setWorkspaceId(1L);
        request.setStatus(ApprovalStatus.PENDING.getCode());

        when(approvalRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(approvalStepRepository.findByApprovalId(1L)).thenReturn(Collections.emptyList());
        when(approvalDecisionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(approvalRequestRepository.update(any())).thenAnswer(inv -> inv.getArgument(0));

        DecideApprovalCommand command = new DecideApprovalCommand();
        command.setApprovalId(1L);
        command.setDecisionType("reject");
        command.setComment("Not acceptable");

        ApprovalRequestView result = service.decideApproval(1L, command, 20L);

        assertEquals(ApprovalStatus.REJECTED.getCode(), result.getStatus());
    }

    @Test
    void shouldNotDecideNonPendingRequest() {
        ApprovalRequestView request = new ApprovalRequestView();
        request.setApprovalId(1L);
        request.setWorkspaceId(1L);
        request.setStatus(ApprovalStatus.APPROVED.getCode());

        when(approvalRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        DecideApprovalCommand command = new DecideApprovalCommand();
        command.setApprovalId(1L);
        command.setDecisionType("approve");

        assertThrows(BusinessException.class, () -> service.decideApproval(1L, command, 20L));
    }

    @Test
    void shouldCancelOwnRequest() {
        ApprovalRequestView request = new ApprovalRequestView();
        request.setApprovalId(1L);
        request.setWorkspaceId(1L);
        request.setStatus(ApprovalStatus.PENDING.getCode());
        request.setSubmittedBy(10L);

        when(approvalRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(approvalRequestRepository.update(any())).thenAnswer(inv -> inv.getArgument(0));

        CancelApprovalRequestCommand command = new CancelApprovalRequestCommand();
        command.setWorkspaceId(1L);
        command.setApprovalId(1L);

        ApprovalRequestView result = service.cancelApprovalRequest(command, 10L);

        assertEquals(ApprovalStatus.CANCELLED.getCode(), result.getStatus());
    }

    @Test
    void shouldNotCancelTerminalRequest() {
        ApprovalRequestView request = new ApprovalRequestView();
        request.setApprovalId(1L);
        request.setWorkspaceId(1L);
        request.setStatus(ApprovalStatus.APPROVED.getCode());
        request.setSubmittedBy(10L);

        when(approvalRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        CancelApprovalRequestCommand command = new CancelApprovalRequestCommand();
        command.setWorkspaceId(1L);
        command.setApprovalId(1L);

        assertThrows(BusinessException.class, () -> service.cancelApprovalRequest(command, 10L));
    }

    @Test
    void shouldNotCancelOtherUsersRequest() {
        ApprovalRequestView request = new ApprovalRequestView();
        request.setApprovalId(1L);
        request.setWorkspaceId(1L);
        request.setStatus(ApprovalStatus.PENDING.getCode());
        request.setSubmittedBy(10L);

        when(approvalRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        CancelApprovalRequestCommand command = new CancelApprovalRequestCommand();
        command.setWorkspaceId(1L);
        command.setApprovalId(1L);

        assertThrows(BusinessException.class, () -> service.cancelApprovalRequest(command, 30L));
    }

    @Test
    void shouldGetTargetApprovals() {
        ApprovalRequestView request = new ApprovalRequestView();
        request.setApprovalId(1L);
        request.setTargetType("asset");
        request.setTargetId(100L);

        when(approvalRequestRepository.findByTarget(1L, "asset", 100L)).thenReturn(List.of(request));

        List<ApprovalRequestView> results = service.getTargetApprovals(1L, "asset", 100L);

        assertEquals(1, results.size());
        assertEquals(100L, results.get(0).getTargetId());
    }
}
