package com.qiu.qiupicturebackend.governance.application;

import com.qiu.qiupicturebackend.activity.application.ActivityRecordApplicationService;
import com.qiu.qiupicturebackend.activity.application.command.RecordActivityCommand;
import com.qiu.qiupicturebackend.collection.domain.repository.CollectionRepository;
import com.qiu.qiupicturebackend.exception.BusinessException;
import com.qiu.qiupicturebackend.exception.ErrorCode;
import com.qiu.qiupicturebackend.governance.application.command.CancelApprovalRequestCommand;
import com.qiu.qiupicturebackend.governance.application.command.DecideApprovalCommand;
import com.qiu.qiupicturebackend.governance.application.command.SubmitApprovalRequestCommand;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalDecisionType;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalDecisionView;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalRequestView;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalStatus;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalStepView;
import com.qiu.qiupicturebackend.governance.domain.model.GovernanceTargetType;
import com.qiu.qiupicturebackend.governance.domain.repository.ApprovalDecisionRepository;
import com.qiu.qiupicturebackend.governance.domain.repository.ApprovalRequestRepository;
import com.qiu.qiupicturebackend.governance.domain.repository.ApprovalStepRepository;
import com.qiu.qiupicturebackend.model.entity.Picture;
import com.qiu.qiupicturebackend.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Application service for managing approval requests: submit, decide, cancel, and query.
 */
@Slf4j
@Service
public class ApprovalApplicationService {

    @Resource
    private ApprovalRequestRepository approvalRequestRepository;

    @Resource
    private ApprovalStepRepository approvalStepRepository;

    @Resource
    private ApprovalDecisionRepository approvalDecisionRepository;

    @Resource
    private PublicationPolicyApplicationService publicationPolicyApplicationService;

    @Resource
    private ActivityRecordApplicationService activityRecordApplicationService;

    @Resource
    private PictureService pictureService;

    @Resource
    private CollectionRepository collectionRepository;

    /**
     * Submits a new approval request for a target entity.
     *
     * @param command the submission command containing target details and reason
     * @param userId  the ID of the user submitting the request
     * @return the created ApprovalRequestView with its initial step populated
     */
    public ApprovalRequestView submitApprovalRequest(SubmitApprovalRequestCommand command, Long userId) {
        Long workspaceId = command.getWorkspaceId();
        if (workspaceId == null || workspaceId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid workspace ID");
        }

        String targetType = command.getTargetType();
        GovernanceTargetType governanceTargetType = GovernanceTargetType.fromCode(targetType);
        if (governanceTargetType == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid target type: " + targetType);
        }

        // Validate target belongs to workspace
        validateTargetOwnership(workspaceId, governanceTargetType, command.getTargetId());

        // Check for existing pending approval on the same target
        List<ApprovalRequestView> existingApprovals = approvalRequestRepository.findByTarget(
                workspaceId, targetType, command.getTargetId());
        boolean hasPending = existingApprovals.stream()
                .anyMatch(a -> ApprovalStatus.PENDING.getCode().equals(a.getStatus()));
        if (hasPending) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,
                    "A pending approval request already exists for this target");
        }

        // Create and save the approval request
        ApprovalRequestView requestView = new ApprovalRequestView();
        requestView.setWorkspaceId(workspaceId);
        requestView.setTargetType(targetType);
        requestView.setTargetId(command.getTargetId());
        requestView.setTargetVersionId(command.getTargetVersionId());
        requestView.setRequestType(command.getRequestType());
        requestView.setStatus(ApprovalStatus.PENDING.getCode());
        requestView.setSubmittedBy(userId);
        requestView.setSubmittedAt(new Date());
        requestView.setReason(command.getReason());
        requestView.setCreatedAt(new Date());
        requestView.setUpdatedAt(new Date());

        approvalRequestRepository.save(requestView);

        // Create the initial approval step
        ApprovalStepView stepView = new ApprovalStepView();
        stepView.setApprovalId(requestView.getApprovalId());
        stepView.setStepOrder(1);
        stepView.setStatus(ApprovalStatus.PENDING.getCode());
        stepView.setCreatedAt(new Date());
        stepView.setUpdatedAt(new Date());

        approvalStepRepository.save(stepView);

        log.info("Approval request submitted: approvalId={}, targetType={}, targetId={}, submittedBy={}",
                requestView.getApprovalId(), targetType, command.getTargetId(), userId);

        recordActivity(workspaceId, userId, "approval.requested", targetType, command.getTargetId());

        return requestView;
    }

    /**
     * Makes a decision (approve, reject, or request changes) on a pending approval request.
     *
     * @param workspaceId the workspace context
     * @param command     the decision command containing approvalId, decisionType, and optional comment
     * @param reviewerId  the ID of the reviewer making the decision
     * @return the updated ApprovalRequestView
     */
    public ApprovalRequestView decideApproval(Long workspaceId, DecideApprovalCommand command, Long reviewerId) {
        if (workspaceId == null || workspaceId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid workspace ID");
        }

        // Validate decision type
        String decisionTypeCode = command.getDecisionType();
        ApprovalDecisionType decisionType = ApprovalDecisionType.fromCode(decisionTypeCode);
        if (decisionType == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid decision type: " + decisionTypeCode);
        }

        // Find the approval request
        ApprovalRequestView requestView = approvalRequestRepository.findById(command.getApprovalId())
                .orElse(null);
        if (requestView == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "Approval request not found: " + command.getApprovalId());
        }

        // Validate workspace matches
        if (!workspaceId.equals(requestView.getWorkspaceId())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "Workspace does not match the approval request");
        }

        // Validate request is in PENDING status
        if (!ApprovalStatus.PENDING.getCode().equals(requestView.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,
                    "Approval request is not in pending status, current status: " + requestView.getStatus());
        }

        // Map decision type to approval status
        String targetStatus;
        switch (decisionType) {
            case APPROVE:
                targetStatus = ApprovalStatus.APPROVED.getCode();
                break;
            case REJECT:
                targetStatus = ApprovalStatus.REJECTED.getCode();
                break;
            case REQUEST_CHANGES:
                targetStatus = ApprovalStatus.CHANGES_REQUESTED.getCode();
                break;
            default:
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "Unknown decision type: " + decisionTypeCode);
        }

        // Update all steps for this approval to the mapped status
        List<ApprovalStepView> steps = approvalStepRepository.findByApprovalId(requestView.getApprovalId());
        ApprovalStepView firstStep = null;
        for (ApprovalStepView step : steps) {
            step.setStatus(targetStatus);
            step.setUpdatedAt(new Date());
            approvalStepRepository.update(step);
            if (firstStep == null && step.getStepOrder() == 1) {
                firstStep = step;
            }
        }

        // If no step found with order 1, fall back to first element
        if (firstStep == null && !steps.isEmpty()) {
            firstStep = steps.get(0);
        }

        // Create decision record
        ApprovalDecisionView decisionView = new ApprovalDecisionView();
        decisionView.setApprovalId(requestView.getApprovalId());
        if (firstStep != null) {
            decisionView.setStepId(firstStep.getStepId());
        }
        decisionView.setDecidedBy(reviewerId);
        decisionView.setDecisionType(decisionTypeCode);
        decisionView.setComment(command.getComment());
        decisionView.setCreatedAt(new Date());

        approvalDecisionRepository.save(decisionView);

        // Update the request
        requestView.setStatus(targetStatus);
        requestView.setResolvedBy(reviewerId);
        requestView.setResolvedAt(new Date());
        requestView.setResultMessage(command.getComment());
        requestView.setUpdatedAt(new Date());

        approvalRequestRepository.update(requestView);

        log.info("Approval decision made: approvalId={}, decisionType={}, decidedBy={}, resultingStatus={}",
                requestView.getApprovalId(), decisionTypeCode, reviewerId, targetStatus);

        String activityAction;
        switch (decisionType) {
            case APPROVE:
                activityAction = "approval.approved";
                break;
            case REJECT:
                activityAction = "approval.rejected";
                break;
            case REQUEST_CHANGES:
                activityAction = "approval.changes_requested";
                break;
            default:
                activityAction = null;
        }
        recordActivity(workspaceId, reviewerId, activityAction, requestView.getTargetType(), requestView.getTargetId());

        return requestView;
    }

    /**
     * Cancels a pending approval request. Only the original submitter can cancel.
     *
     * @param command the cancel command containing workspaceId and approvalId
     * @param userId  the ID of the user attempting to cancel
     * @return the updated ApprovalRequestView with status set to CANCELLED
     */
    public ApprovalRequestView cancelApprovalRequest(CancelApprovalRequestCommand command, Long userId) {
        Long workspaceId = command.getWorkspaceId();
        if (workspaceId == null || workspaceId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid workspace ID");
        }

        // Find the approval request
        ApprovalRequestView requestView = approvalRequestRepository.findById(command.getApprovalId())
                .orElse(null);
        if (requestView == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "Approval request not found: " + command.getApprovalId());
        }

        // Validate workspace matches
        if (!workspaceId.equals(requestView.getWorkspaceId())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "Workspace does not match the approval request");
        }

        // Validate request is not in a terminal state
        ApprovalStatus currentStatus = ApprovalStatus.fromCode(requestView.getStatus());
        if (currentStatus.isTerminal()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,
                    "Cannot cancel an approval request that is already in terminal status: " + currentStatus.getCode());
        }

        // Validate that the user is the original submitter
        if (!userId.equals(requestView.getSubmittedBy())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,
                    "Only the original submitter can cancel the approval request");
        }

        // Update request to CANCELLED
        requestView.setStatus(ApprovalStatus.CANCELLED.getCode());
        requestView.setUpdatedAt(new Date());

        approvalRequestRepository.update(requestView);

        log.info("Approval request cancelled: approvalId={}, cancelledBy={}",
                requestView.getApprovalId(), userId);

        recordActivity(workspaceId, userId, "approval.cancelled", requestView.getTargetType(), requestView.getTargetId());

        return requestView;
    }

    /**
     * Retrieves all approval requests for a specific target.
     *
     * @param workspaceId the workspace context
     * @param targetType  the type of target (asset, collection, ai_result)
     * @param targetId    the ID of the target entity
     * @return list of ApprovalRequestView records for the target
     */
    public List<ApprovalRequestView> getTargetApprovals(Long workspaceId, String targetType, Long targetId) {
        if (workspaceId == null || workspaceId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid workspace ID");
        }

        // Validate target type
        GovernanceTargetType govTargetType = GovernanceTargetType.fromCode(targetType);
        if (govTargetType == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid target type: " + targetType);
        }

        return approvalRequestRepository.findByTarget(workspaceId, targetType, targetId);
    }

    private void validateTargetOwnership(Long workspaceId, GovernanceTargetType targetType, Long targetId) {
        switch (targetType) {
            case ASSET:
                Picture picture = pictureService.getById(targetId);
                if (picture == null) {
                    throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "Asset not found: " + targetId);
                }
                if (!workspaceId.equals(picture.getSpaceId())) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR,
                            "Asset does not belong to this workspace");
                }
                break;
            case COLLECTION:
                collectionRepository.findById(targetId).ifPresentOrElse(
                        collection -> {
                            if (!workspaceId.equals(collection.getWorkspaceId())) {
                                throw new BusinessException(ErrorCode.OPERATION_ERROR,
                                        "Collection does not belong to this workspace");
                            }
                        },
                        () -> { throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "Collection not found: " + targetId); }
                );
                break;
            case AI_RESULT:
                break;
            default:
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "Unknown target type: " + targetType);
        }
    }

    private void recordActivity(Long workspaceId, Long userId, String actionType, String targetType, Long targetId) {
        if (actionType == null) return;
        try {
            activityRecordApplicationService.record(RecordActivityCommand.builder()
                    .workspaceId(workspaceId)
                    .actorUserId(userId)
                    .actionType(actionType)
                    .targetType(targetType)
                    .targetId(targetId)
                    .build());
        } catch (Exception e) {
            log.warn("Failed to record governance activity: action={}, workspaceId={}", actionType, workspaceId, e);
        }
    }
}
