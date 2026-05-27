package com.qiu.qiupicturebackend.governance.api;

import com.qiu.qiupicturebackend.common.BaseResponse;
import com.qiu.qiupicturebackend.common.ResultUtils;
import com.qiu.qiupicturebackend.exception.ErrorCode;
import com.qiu.qiupicturebackend.exception.ThrowUtils;
import com.qiu.qiupicturebackend.governance.application.ApprovalApplicationService;
import com.qiu.qiupicturebackend.governance.application.ApprovalInboxApplicationService;
import com.qiu.qiupicturebackend.governance.application.command.CancelApprovalRequestCommand;
import com.qiu.qiupicturebackend.governance.application.command.DecideApprovalCommand;
import com.qiu.qiupicturebackend.governance.application.command.SubmitApprovalRequestCommand;
import com.qiu.qiupicturebackend.governance.application.query.ApprovalInboxQuery;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalDecisionView;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalRequestView;
import com.qiu.qiupicturebackend.governance.domain.model.ApprovalStepView;
import com.qiu.qiupicturebackend.governance.representation.ApprovalDecisionResponse;
import com.qiu.qiupicturebackend.governance.representation.ApprovalRequestResponse;
import com.qiu.qiupicturebackend.governance.representation.ApprovalStepResponse;
import com.qiu.qiupicturebackend.model.entity.User;
import com.qiu.qiupicturebackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/workspaces/{workspaceId}/approvals")
public class ApprovalController {

    @Resource
    private ApprovalApplicationService approvalApplicationService;

    @Resource
    private ApprovalInboxApplicationService approvalInboxApplicationService;

    @Resource
    private UserService userService;

    @GetMapping("/inbox")
    public BaseResponse<List<ApprovalRequestResponse>> getInbox(
            @PathVariable Long workspaceId,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int limit) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ApprovalInboxQuery query = new ApprovalInboxQuery();
        query.setWorkspaceId(workspaceId);
        query.setOffset(offset);
        query.setLimit(limit);
        List<ApprovalRequestView> inbox = approvalInboxApplicationService.getInbox(workspaceId, query);
        List<ApprovalRequestResponse> response = inbox.stream().map(this::toResponse).collect(Collectors.toList());
        return ResultUtils.success(response);
    }

    @GetMapping("/targets/{targetType}/{targetId}")
    public BaseResponse<List<ApprovalRequestResponse>> getTargetApprovals(
            @PathVariable Long workspaceId,
            @PathVariable String targetType,
            @PathVariable Long targetId) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(targetId == null || targetId <= 0, ErrorCode.PARAMS_ERROR);
        List<ApprovalRequestView> approvals = approvalApplicationService.getTargetApprovals(workspaceId, targetType, targetId);
        List<ApprovalRequestResponse> response = approvals.stream().map(this::toResponse).collect(Collectors.toList());
        return ResultUtils.success(response);
    }

    @PostMapping
    public BaseResponse<ApprovalRequestResponse> submitApproval(
            @PathVariable Long workspaceId,
            @RequestBody SubmitApprovalRequestCommand command,
            HttpServletRequest request) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        command.setWorkspaceId(workspaceId);
        ApprovalRequestView approval = approvalApplicationService.submitApprovalRequest(command, loginUser.getId());
        return ResultUtils.success(toResponse(approval));
    }

    @PostMapping("/{approvalId}/approve")
    public BaseResponse<ApprovalRequestResponse> approve(
            @PathVariable Long workspaceId,
            @PathVariable Long approvalId,
            @RequestBody DecideApprovalCommand command,
            HttpServletRequest request) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(approvalId == null || approvalId <= 0, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        command.setWorkspaceId(workspaceId);
        command.setApprovalId(approvalId);
        command.setDecisionType("approve");
        ApprovalRequestView approval = approvalApplicationService.decideApproval(workspaceId, command, loginUser.getId());
        return ResultUtils.success(toResponse(approval));
    }

    @PostMapping("/{approvalId}/reject")
    public BaseResponse<ApprovalRequestResponse> reject(
            @PathVariable Long workspaceId,
            @PathVariable Long approvalId,
            @RequestBody DecideApprovalCommand command,
            HttpServletRequest request) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(approvalId == null || approvalId <= 0, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        command.setWorkspaceId(workspaceId);
        command.setApprovalId(approvalId);
        command.setDecisionType("reject");
        ApprovalRequestView approval = approvalApplicationService.decideApproval(workspaceId, command, loginUser.getId());
        return ResultUtils.success(toResponse(approval));
    }

    @PostMapping("/{approvalId}/request-changes")
    public BaseResponse<ApprovalRequestResponse> requestChanges(
            @PathVariable Long workspaceId,
            @PathVariable Long approvalId,
            @RequestBody DecideApprovalCommand command,
            HttpServletRequest request) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(approvalId == null || approvalId <= 0, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        command.setWorkspaceId(workspaceId);
        command.setApprovalId(approvalId);
        command.setDecisionType("request_changes");
        ApprovalRequestView approval = approvalApplicationService.decideApproval(workspaceId, command, loginUser.getId());
        return ResultUtils.success(toResponse(approval));
    }

    @PostMapping("/{approvalId}/cancel")
    public BaseResponse<ApprovalRequestResponse> cancel(
            @PathVariable Long workspaceId,
            @PathVariable Long approvalId,
            HttpServletRequest request) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(approvalId == null || approvalId <= 0, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        CancelApprovalRequestCommand command = new CancelApprovalRequestCommand();
        command.setWorkspaceId(workspaceId);
        command.setApprovalId(approvalId);
        ApprovalRequestView approval = approvalApplicationService.cancelApprovalRequest(command, loginUser.getId());
        return ResultUtils.success(toResponse(approval));
    }

    private ApprovalRequestResponse toResponse(ApprovalRequestView view) {
        ApprovalRequestResponse r = new ApprovalRequestResponse();
        r.setApprovalId(view.getApprovalId());
        r.setWorkspaceId(view.getWorkspaceId());
        r.setTargetType(view.getTargetType());
        r.setTargetId(view.getTargetId());
        r.setTargetVersionId(view.getTargetVersionId());
        r.setRequestType(view.getRequestType());
        r.setStatus(view.getStatus());
        r.setSubmittedBy(view.getSubmittedBy());
        r.setSubmittedAt(view.getSubmittedAt() != null ? view.getSubmittedAt().toString() : null);
        r.setResolvedBy(view.getResolvedBy());
        r.setResolvedAt(view.getResolvedAt() != null ? view.getResolvedAt().toString() : null);
        r.setReason(view.getReason());
        r.setResultMessage(view.getResultMessage());
        r.setCreatedAt(view.getCreatedAt() != null ? view.getCreatedAt().toString() : null);
        if (view.getSteps() != null) {
            r.setSteps(view.getSteps().stream().map(this::toStepResponse).collect(Collectors.toList()));
        } else {
            r.setSteps(Collections.emptyList());
        }
        if (view.getDecisions() != null) {
            r.setDecisions(view.getDecisions().stream().map(this::toDecisionResponse).collect(Collectors.toList()));
        } else {
            r.setDecisions(Collections.emptyList());
        }
        return r;
    }

    private ApprovalStepResponse toStepResponse(ApprovalStepView view) {
        ApprovalStepResponse r = new ApprovalStepResponse();
        r.setStepId(view.getStepId());
        r.setApprovalId(view.getApprovalId());
        r.setStepOrder(view.getStepOrder());
        r.setReviewerId(view.getReviewerId());
        r.setStatus(view.getStatus());
        r.setCreatedAt(view.getCreatedAt() != null ? view.getCreatedAt().toString() : null);
        return r;
    }

    private ApprovalDecisionResponse toDecisionResponse(ApprovalDecisionView view) {
        ApprovalDecisionResponse r = new ApprovalDecisionResponse();
        r.setDecisionId(view.getDecisionId());
        r.setStepId(view.getStepId());
        r.setApprovalId(view.getApprovalId());
        r.setDecidedBy(view.getDecidedBy());
        r.setDecisionType(view.getDecisionType());
        r.setComment(view.getComment());
        r.setCreatedAt(view.getCreatedAt() != null ? view.getCreatedAt().toString() : null);
        return r;
    }
}
