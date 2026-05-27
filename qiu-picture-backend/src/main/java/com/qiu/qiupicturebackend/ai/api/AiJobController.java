package com.qiu.qiupicturebackend.ai.api;

import com.qiu.qiupicturebackend.ai.application.AiJobApplicationService;
import com.qiu.qiupicturebackend.ai.application.AiJobLifecycleApplicationService;
import com.qiu.qiupicturebackend.ai.application.AiResultApplicationService;
import com.qiu.qiupicturebackend.ai.application.command.CreateAiJobCommand;
import com.qiu.qiupicturebackend.ai.application.query.AiJobQuery;
import com.qiu.qiupicturebackend.ai.domain.model.AiJobResultView;
import com.qiu.qiupicturebackend.ai.domain.model.AiJobView;
import com.qiu.qiupicturebackend.ai.representation.AiJobResponse;
import com.qiu.qiupicturebackend.ai.representation.AiJobResultResponse;
import com.qiu.qiupicturebackend.common.BaseResponse;
import com.qiu.qiupicturebackend.common.ResultUtils;
import com.qiu.qiupicturebackend.exception.ErrorCode;
import com.qiu.qiupicturebackend.exception.ThrowUtils;
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
@RequestMapping("/v1/workspaces/{workspaceId}/ai/jobs")
public class AiJobController {

    @Resource
    private AiJobApplicationService aiJobApplicationService;

    @Resource
    private AiJobLifecycleApplicationService aiJobLifecycleApplicationService;

    @Resource
    private AiResultApplicationService aiResultApplicationService;

    @Resource
    private UserService userService;

    @GetMapping
    public BaseResponse<List<AiJobResponse>> listJobs(
            @PathVariable Long workspaceId,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) String status) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        AiJobQuery query = new AiJobQuery();
        query.setOffset(offset);
        query.setLimit(limit);
        query.setStatus(status);
        List<AiJobView> jobs = aiJobApplicationService.listJobs(workspaceId, query);
        List<AiJobResponse> response = jobs.stream().map(this::toResponse).collect(Collectors.toList());
        return ResultUtils.success(response);
    }

    @PostMapping
    public BaseResponse<AiJobResponse> createJob(
            @PathVariable Long workspaceId,
            @RequestBody CreateAiJobCommand command,
            HttpServletRequest request) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        AiJobView job = aiJobApplicationService.createJob(workspaceId, command, loginUser.getId());
        return ResultUtils.success(toResponse(job));
    }

    @GetMapping("/{jobId}")
    public BaseResponse<AiJobResponse> getJob(
            @PathVariable Long workspaceId,
            @PathVariable Long jobId) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(jobId == null || jobId <= 0, ErrorCode.PARAMS_ERROR);
        AiJobView job = aiJobApplicationService.getJob(workspaceId, jobId);
        return ResultUtils.success(toResponse(job));
    }

    @PostMapping("/{jobId}/cancel")
    public BaseResponse<AiJobResponse> cancelJob(
            @PathVariable Long workspaceId,
            @PathVariable Long jobId) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(jobId == null || jobId <= 0, ErrorCode.PARAMS_ERROR);
        AiJobView job = aiJobLifecycleApplicationService.cancelJob(workspaceId, jobId);
        return ResultUtils.success(toResponse(job));
    }

    @PostMapping("/{jobId}/retry")
    public BaseResponse<AiJobResponse> retryJob(
            @PathVariable Long workspaceId,
            @PathVariable Long jobId) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(jobId == null || jobId <= 0, ErrorCode.PARAMS_ERROR);
        AiJobView job = aiJobLifecycleApplicationService.retryJob(workspaceId, jobId);
        return ResultUtils.success(toResponse(job));
    }

    @PostMapping("/{jobId}/results/{resultId}/apply")
    public BaseResponse<AiJobResultResponse> applyResult(
            @PathVariable Long workspaceId,
            @PathVariable Long jobId,
            @PathVariable Long resultId,
            HttpServletRequest request) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(jobId == null || jobId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(resultId == null || resultId <= 0, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        AiJobResultView result = aiResultApplicationService.applyResult(workspaceId, jobId, resultId, loginUser.getId());
        return ResultUtils.success(toResultResponse(result));
    }

    private AiJobResponse toResponse(AiJobView view) {
        AiJobResponse r = new AiJobResponse();
        r.setJobId(view.getJobId());
        r.setWorkspaceId(view.getWorkspaceId());
        r.setCreatorUserId(view.getCreatorUserId());
        r.setCapabilityKey(view.getCapabilityKey());
        r.setStatus(view.getStatus());
        r.setSourceAssetId(view.getSourceAssetId());
        r.setSourceAssetVersionId(view.getSourceAssetVersionId());
        r.setProvider(view.getProvider());
        r.setParametersJson(view.getParametersJson());
        r.setIdempotencyKey(view.getIdempotencyKey());
        r.setErrorCode(view.getErrorCode());
        r.setErrorMessage(view.getErrorMessage());
        r.setCreatedAt(view.getCreatedAt() != null ? view.getCreatedAt().toString() : null);
        r.setStartedAt(view.getStartedAt() != null ? view.getStartedAt().toString() : null);
        r.setFinishedAt(view.getFinishedAt() != null ? view.getFinishedAt().toString() : null);
        if (view.getResults() != null) {
            r.setResults(view.getResults().stream().map(this::toResultResponse).collect(Collectors.toList()));
        } else {
            r.setResults(Collections.emptyList());
        }
        return r;
    }

    private AiJobResultResponse toResultResponse(AiJobResultView view) {
        AiJobResultResponse r = new AiJobResultResponse();
        r.setResultId(view.getResultId());
        r.setJobId(view.getJobId());
        r.setResultType(view.getResultType());
        r.setOutputUrl(view.getOutputUrl());
        r.setApplyStatus(view.getApplyStatus());
        r.setAssetVersionId(view.getAssetVersionId());
        r.setCreatedAt(view.getCreatedAt() != null ? view.getCreatedAt().toString() : null);
        return r;
    }
}
