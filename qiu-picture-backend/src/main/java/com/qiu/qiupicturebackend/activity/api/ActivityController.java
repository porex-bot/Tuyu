package com.qiu.qiupicturebackend.activity.api;

import com.qiu.qiupicturebackend.activity.application.ActivityTimelineApplicationService;
import com.qiu.qiupicturebackend.activity.application.query.ActivityTimelineQuery;
import com.qiu.qiupicturebackend.activity.representation.ActivityTimelineResponse;
import com.qiu.qiupicturebackend.common.BaseResponse;
import com.qiu.qiupicturebackend.common.ResultUtils;
import com.qiu.qiupicturebackend.exception.ErrorCode;
import com.qiu.qiupicturebackend.exception.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/v1/workspaces/{workspaceId}/activities")
public class ActivityController {

    @Resource
    private ActivityTimelineApplicationService activityTimelineApplicationService;

    @GetMapping
    public BaseResponse<ActivityTimelineResponse> getWorkspaceTimeline(
            @PathVariable Long workspaceId,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int limit) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ActivityTimelineQuery query = new ActivityTimelineQuery();
        query.setOffset(offset);
        query.setLimit(limit);
        ActivityTimelineResponse result = activityTimelineApplicationService.getWorkspaceTimeline(workspaceId, query);
        return ResultUtils.success(result);
    }

    @GetMapping("/targets/{targetType}/{targetId}")
    public BaseResponse<ActivityTimelineResponse> getTargetTimeline(
            @PathVariable Long workspaceId,
            @PathVariable String targetType,
            @PathVariable Long targetId,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int limit) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(targetType == null || targetType.isBlank(), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(targetId == null || targetId <= 0, ErrorCode.PARAMS_ERROR);
        ActivityTimelineQuery query = new ActivityTimelineQuery();
        query.setOffset(offset);
        query.setLimit(limit);
        ActivityTimelineResponse result = activityTimelineApplicationService.getTargetTimeline(
                workspaceId, targetType, targetId, query);
        return ResultUtils.success(result);
    }
}
