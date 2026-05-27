package com.qiu.qiupicturebackend.governance.api;

import com.qiu.qiupicturebackend.common.BaseResponse;
import com.qiu.qiupicturebackend.common.ResultUtils;
import com.qiu.qiupicturebackend.exception.ErrorCode;
import com.qiu.qiupicturebackend.exception.ThrowUtils;
import com.qiu.qiupicturebackend.governance.application.GovernancePolicyApplicationService;
import com.qiu.qiupicturebackend.governance.application.command.ConfigureGovernancePolicyCommand;
import com.qiu.qiupicturebackend.governance.domain.model.GovernancePolicyView;
import com.qiu.qiupicturebackend.governance.representation.GovernancePolicyResponse;
import com.qiu.qiupicturebackend.model.entity.User;
import com.qiu.qiupicturebackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/v1/workspaces/{workspaceId}/governance")
public class GovernancePolicyController {

    @Resource
    private GovernancePolicyApplicationService governancePolicyApplicationService;

    @Resource
    private UserService userService;

    @GetMapping("/policy")
    public BaseResponse<GovernancePolicyResponse> getPolicy(@PathVariable Long workspaceId) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        GovernancePolicyView policy = governancePolicyApplicationService.getPolicy(workspaceId);
        return ResultUtils.success(toResponse(policy));
    }

    @PutMapping("/policy")
    public BaseResponse<GovernancePolicyResponse> updatePolicy(
            @PathVariable Long workspaceId,
            @RequestBody ConfigureGovernancePolicyCommand command,
            HttpServletRequest request) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        command.setWorkspaceId(workspaceId);
        GovernancePolicyView policy = governancePolicyApplicationService.updatePolicy(command, loginUser.getId());
        return ResultUtils.success(toResponse(policy));
    }

    private GovernancePolicyResponse toResponse(GovernancePolicyView view) {
        GovernancePolicyResponse r = new GovernancePolicyResponse();
        r.setPolicyId(view.getPolicyId());
        r.setWorkspaceId(view.getWorkspaceId());
        r.setMode(view.getMode());
        r.setRequireApprovalForAssets(view.getRequireApprovalForAssets() != null && view.getRequireApprovalForAssets() == 1);
        r.setRequireApprovalForCollections(view.getRequireApprovalForCollections() != null && view.getRequireApprovalForCollections() == 1);
        r.setRequireApprovalForAiResults(view.getRequireApprovalForAiResults() != null && view.getRequireApprovalForAiResults() == 1);
        r.setAutoApproveTrustedUsers(view.getAutoApproveTrustedUsers() != null && view.getAutoApproveTrustedUsers() == 1);
        r.setUpdatedBy(view.getUpdatedBy());
        r.setCreatedAt(view.getCreatedAt() != null ? view.getCreatedAt().toString() : null);
        r.setUpdatedAt(view.getUpdatedAt() != null ? view.getUpdatedAt().toString() : null);
        return r;
    }
}
