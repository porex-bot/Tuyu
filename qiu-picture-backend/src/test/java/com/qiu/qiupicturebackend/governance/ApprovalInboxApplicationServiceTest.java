package com.qiu.qiupicturebackend.governance;

import com.qiu.qiupicturebackend.governance.application.ApprovalInboxApplicationService;
import com.qiu.qiupicturebackend.governance.application.query.ApprovalInboxQuery;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalDecisionView;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalRequestView;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalStatus;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalStepView;
import com.qiu.qiupicturebackend.governance.domain.repository.ApprovalDecisionRepository;
import com.qiu.qiupicturebackend.governance.domain.repository.ApprovalRequestRepository;
import com.qiu.qiupicturebackend.governance.domain.repository.ApprovalStepRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApprovalInboxApplicationServiceTest {

    @Mock private ApprovalRequestRepository approvalRequestRepository;
    @Mock private ApprovalStepRepository approvalStepRepository;
    @Mock private ApprovalDecisionRepository approvalDecisionRepository;

    @InjectMocks
    private ApprovalInboxApplicationService service;

    @Test
    void shouldReturnEmptyInbox() {
        when(approvalRequestRepository.findPendingByWorkspace(1L, 0, 20)).thenReturn(Collections.emptyList());

        ApprovalInboxQuery query = new ApprovalInboxQuery();
        query.setWorkspaceId(1L);
        query.setOffset(0);
        query.setLimit(20);

        List<ApprovalRequestView> inbox = service.getInbox(1L, query);

        assertNotNull(inbox);
        assertTrue(inbox.isEmpty());
    }

    @Test
    void shouldReturnInboxWithEnrichedStepsAndDecisions() {
        ApprovalRequestView request = new ApprovalRequestView();
        request.setApprovalId(1L);
        request.setWorkspaceId(1L);
        request.setTargetType("asset");
        request.setTargetId(100L);
        request.setStatus(ApprovalStatus.PENDING.getCode());

        ApprovalStepView step = new ApprovalStepView();
        step.setStepId(1L);
        step.setApprovalId(1L);
        step.setStepOrder(1);
        step.setStatus(ApprovalStatus.PENDING.getCode());

        ApprovalDecisionView decision = new ApprovalDecisionView();
        decision.setDecisionId(1L);
        decision.setApprovalId(1L);
        decision.setDecisionType("approve");

        when(approvalRequestRepository.findPendingByWorkspace(1L, 0, 20)).thenReturn(List.of(request));
        when(approvalStepRepository.findByApprovalId(1L)).thenReturn(List.of(step));
        when(approvalDecisionRepository.findByApprovalId(1L)).thenReturn(List.of(decision));

        ApprovalInboxQuery query = new ApprovalInboxQuery();
        query.setWorkspaceId(1L);
        query.setOffset(0);
        query.setLimit(20);

        List<ApprovalRequestView> inbox = service.getInbox(1L, query);

        assertEquals(1, inbox.size());
        ApprovalRequestView result = inbox.get(0);
        assertNotNull(result.getSteps());
        assertEquals(1, result.getSteps().size());
        assertNotNull(result.getDecisions());
        assertEquals(1, result.getDecisions().size());
    }
}
