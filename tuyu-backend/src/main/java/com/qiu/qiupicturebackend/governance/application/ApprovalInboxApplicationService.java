package com.qiu.qiupicturebackend.governance.application;

import com.qiu.qiupicturebackend.governance.application.query.ApprovalInboxQuery;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalDecisionView;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalRequestView;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalStepView;
import com.qiu.qiupicturebackend.governance.domain.repository.ApprovalDecisionRepository;
import com.qiu.qiupicturebackend.governance.domain.repository.ApprovalRequestRepository;
import com.qiu.qiupicturebackend.governance.domain.repository.ApprovalStepRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Application service for querying the approval inbox, enriching requests with
 * their associated steps and decisions.
 */
@Slf4j
@Service
public class ApprovalInboxApplicationService {

    @Resource
    private ApprovalRequestRepository approvalRequestRepository;

    @Resource
    private ApprovalStepRepository approvalStepRepository;

    @Resource
    private ApprovalDecisionRepository approvalDecisionRepository;

    /**
     * Retrieves pending approval requests within a workspace, enriched with their
     * workflow steps and decisions.
     *
     * @param workspaceId the workspace context
     * @param query       the inbox query with pagination parameters
     * @return list of ApprovalRequestView records, each populated with steps and decisions
     */
    public List<ApprovalRequestView> getInbox(Long workspaceId, ApprovalInboxQuery query) {
        int offset = query.getOffset();
        int limit = query.getLimit();

        List<ApprovalRequestView> requests = approvalRequestRepository.findPendingByWorkspace(
                workspaceId, offset, limit);

        for (ApprovalRequestView request : requests) {
            List<ApprovalStepView> steps = approvalStepRepository.findByApprovalId(request.getApprovalId());
            List<ApprovalDecisionView> decisions = approvalDecisionRepository.findByApprovalId(request.getApprovalId());
            request.setSteps(steps);
            request.setDecisions(decisions);
        }

        log.info("Approval inbox queried: workspaceId={}, offset={}, limit={}, resultCount={}",
                workspaceId, offset, limit, requests.size());

        return requests;
    }
}
