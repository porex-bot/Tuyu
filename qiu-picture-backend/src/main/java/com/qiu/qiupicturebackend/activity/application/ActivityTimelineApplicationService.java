package com.qiu.qiupicturebackend.activity.application;

import com.qiu.qiupicturebackend.activity.application.query.ActivityTimelineQuery;
import com.qiu.qiupicturebackend.activity.domain.model.ActivityRecordView;
import com.qiu.qiupicturebackend.activity.domain.repository.ActivityRecordRepository;
import com.qiu.qiupicturebackend.activity.representation.ActivityRecordResponse;
import com.qiu.qiupicturebackend.activity.representation.ActivityTimelineResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityTimelineApplicationService {

    @Resource
    private ActivityRecordRepository activityRecordRepository;

    public ActivityTimelineResponse getWorkspaceTimeline(Long workspaceId, ActivityTimelineQuery query) {
        if (workspaceId == null || workspaceId <= 0) {
            return empty();
        }
        int offset = query != null ? query.getOffset() : 0;
        int limit = query != null ? query.getLimit() : 20;

        List<ActivityRecordView> records = activityRecordRepository.findByWorkspaceId(workspaceId, offset, limit);
        long total = activityRecordRepository.countByWorkspaceId(workspaceId);

        ActivityTimelineResponse response = new ActivityTimelineResponse();
        response.setRecords(toResponseList(records));
        response.setTotal(total);
        response.setOffset(offset);
        response.setLimit(limit);
        return response;
    }

    public ActivityTimelineResponse getTargetTimeline(Long workspaceId, String targetType, Long targetId, ActivityTimelineQuery query) {
        if (workspaceId == null || workspaceId <= 0 || targetType == null || targetId == null) {
            return empty();
        }
        int offset = query != null ? query.getOffset() : 0;
        int limit = query != null ? query.getLimit() : 20;

        List<ActivityRecordView> records = activityRecordRepository.findByWorkspaceAndTarget(workspaceId, targetType, targetId, offset, limit);
        long total = activityRecordRepository.countByWorkspaceAndTarget(workspaceId, targetType, targetId);

        ActivityTimelineResponse response = new ActivityTimelineResponse();
        response.setRecords(toResponseList(records));
        response.setTotal(total);
        response.setOffset(offset);
        response.setLimit(limit);
        return response;
    }

    private List<ActivityRecordResponse> toResponseList(List<ActivityRecordView> views) {
        if (views == null || views.isEmpty()) {
            return Collections.emptyList();
        }
        return views.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private ActivityRecordResponse toResponse(ActivityRecordView view) {
        ActivityRecordResponse response = new ActivityRecordResponse();
        response.setActivityId(view.getActivityId());
        response.setWorkspaceId(view.getWorkspaceId());
        response.setActionType(view.getActionType());
        response.setSummary(view.getSummary());
        response.setVisibility(view.getVisibility());
        response.setOccurredAt(view.getOccurredAt() != null ? view.getOccurredAt().toString() : null);

        if (view.getActor() != null) {
            ActivityRecordResponse.Actor actor = new ActivityRecordResponse.Actor();
            actor.setUserId(view.getActor().getUserId());
            actor.setUserName(view.getActor().getUserName());
            actor.setUserAvatar(view.getActor().getUserAvatar());
            response.setActor(actor);
        }

        if (view.getTarget() != null) {
            ActivityRecordResponse.Target target = new ActivityRecordResponse.Target();
            target.setTargetType(view.getTarget().getTargetType());
            target.setTargetId(view.getTarget().getTargetId());
            target.setTargetName(view.getTarget().getTargetName());
            response.setTarget(target);
        }

        if (view.getSecondaryTarget() != null) {
            ActivityRecordResponse.Target secondaryTarget = new ActivityRecordResponse.Target();
            secondaryTarget.setTargetType(view.getSecondaryTarget().getTargetType());
            secondaryTarget.setTargetId(view.getSecondaryTarget().getTargetId());
            secondaryTarget.setTargetName(view.getSecondaryTarget().getTargetName());
            response.setSecondaryTarget(secondaryTarget);
        }

        return response;
    }

    private ActivityTimelineResponse empty() {
        ActivityTimelineResponse response = new ActivityTimelineResponse();
        response.setRecords(Collections.emptyList());
        response.setTotal(0L);
        response.setOffset(0);
        response.setLimit(20);
        return response;
    }
}
