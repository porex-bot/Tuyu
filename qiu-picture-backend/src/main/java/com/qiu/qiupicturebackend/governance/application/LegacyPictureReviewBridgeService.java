package com.qiu.qiupicturebackend.governance.application;

import com.qiu.qiupicturebackend.governance.domain.model.ApprovalRequestView;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalStatus;
import com.qiu.qiupicturebackend.governance.domain.model.GovernanceTargetType;
import com.qiu.qiupicturebackend.governance.domain.repository.ApprovalRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Optional bridge that maps legacy picture.reviewStatus changes into governance approval records.
 * Kept conservative: when disabled (default), old review path works independently.
 * When enabled, creates informational approval history without changing old response behavior.
 */
@Slf4j
@Service
public class LegacyPictureReviewBridgeService {

    @Resource
    private ApprovalRequestRepository approvalRequestRepository;

    /**
     * Optionally records old review action as an approval decision for audit trail.
     * Best-effort only — failures are logged, never re-thrown.
     *
     * @param workspaceId workspace context
     * @param pictureId   legacy picture ID
     * @param oldStatus   previous reviewStatus value
     * @param newStatus   new reviewStatus value
     * @param reviewerId  user who performed the review
     */
    public void bridgeReviewToApproval(Long workspaceId, Long pictureId,
                                       Integer oldStatus, Integer newStatus, Long reviewerId) {
        try {
            String approvalStatus = mapReviewStatusToApprovalStatus(newStatus);
            if (approvalStatus == null) return;

            List<ApprovalRequestView> existing = approvalRequestRepository.findByTarget(
                    workspaceId, GovernanceTargetType.ASSET.getCode(), pictureId);
            boolean alreadyBridged = existing.stream()
                    .anyMatch(r -> "legacy_review".equals(r.getRequestType())
                            && approvalStatus.equals(r.getStatus()));
            if (alreadyBridged) return;

            ApprovalRequestView bridgeRequest = new ApprovalRequestView();
            bridgeRequest.setWorkspaceId(workspaceId);
            bridgeRequest.setTargetType(GovernanceTargetType.ASSET.getCode());
            bridgeRequest.setTargetId(pictureId);
            bridgeRequest.setRequestType("legacy_review");
            bridgeRequest.setStatus(approvalStatus);
            bridgeRequest.setSubmittedBy(reviewerId);
            bridgeRequest.setSubmittedAt(new java.util.Date());
            bridgeRequest.setResolvedBy(reviewerId);
            bridgeRequest.setResolvedAt(new java.util.Date());
            bridgeRequest.setCreatedAt(new java.util.Date());
            bridgeRequest.setUpdatedAt(new java.util.Date());

            approvalRequestRepository.save(bridgeRequest);
            log.debug("Bridged legacy review to approval: pictureId={}, status={}", pictureId, approvalStatus);
        } catch (Exception e) {
            log.warn("Failed to bridge legacy review for picture {}: {}", pictureId, e.getMessage());
        }
    }

    private String mapReviewStatusToApprovalStatus(Integer reviewStatus) {
        if (reviewStatus == null) return null;
        // Legacy mapping: 0=pending, 1=approved, 2=rejected
        switch (reviewStatus) {
            case 0:
                return ApprovalStatus.PENDING.getCode();
            case 1:
                return ApprovalStatus.APPROVED.getCode();
            case 2:
                return ApprovalStatus.REJECTED.getCode();
            default:
                return null;
        }
    }
}
