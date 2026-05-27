package com.qiu.qiupicturebackend.activity.application;

import com.qiu.qiupicturebackend.activity.application.command.RecordActivityCommand;
import com.qiu.qiupicturebackend.activity.domain.model.*;
import com.qiu.qiupicturebackend.activity.domain.repository.ActivityRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class ActivityRecordApplicationService {

    @Resource
    private ActivityRecordRepository activityRecordRepository;

    /**
     * Record an activity event. Best-effort: failures are logged but not re-thrown
     * to avoid breaking core business operations.
     */
    public void record(RecordActivityCommand command) {
        if (command == null || command.getWorkspaceId() == null || command.getActionType() == null) {
            return;
        }
        try {
            ActivityRecordView view = new ActivityRecordView();
            view.setWorkspaceId(command.getWorkspaceId());

            ActivityActorView actor = new ActivityActorView();
            actor.setUserId(command.getActorUserId());
            view.setActor(actor);

            view.setActionType(command.getActionType());

            if (command.getTargetType() != null) {
                ActivityTargetView target = new ActivityTargetView();
                target.setTargetType(command.getTargetType());
                target.setTargetId(command.getTargetId());
                target.setTargetName(command.getTargetName());
                view.setTarget(target);
            }

            if (command.getSecondaryTargetType() != null) {
                ActivityTargetView secondaryTarget = new ActivityTargetView();
                secondaryTarget.setTargetType(command.getSecondaryTargetType());
                secondaryTarget.setTargetId(command.getSecondaryTargetId());
                view.setSecondaryTarget(secondaryTarget);
            }

            view.setSummary(command.getSummary());
            view.setVisibility(command.getVisibility() != null ? command.getVisibility() : "members");
            view.setOccurredAt(command.getOccurredAt() != null ? command.getOccurredAt() : new Date());

            activityRecordRepository.save(view);
        } catch (Exception e) {
            log.warn("Failed to record activity: actionType={}, workspaceId={}",
                    command.getActionType(), command.getWorkspaceId(), e);
        }
    }
}
