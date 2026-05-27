package com.qiu.qiupicturebackend.activity;

import com.qiu.qiupicturebackend.activity.application.ActivityRecordApplicationService;
import com.qiu.qiupicturebackend.activity.application.command.RecordActivityCommand;
import com.qiu.qiupicturebackend.activity.domain.model.ActivityRecordView;
import com.qiu.qiupicturebackend.activity.domain.repository.ActivityRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityRecordApplicationServiceTest {

    @Mock
    private ActivityRecordRepository activityRecordRepository;

    @InjectMocks
    private ActivityRecordApplicationService service;

    @Test
    void shouldRecordActivity() {
        RecordActivityCommand command = RecordActivityCommand.builder()
                .workspaceId(1L)
                .actorUserId(10L)
                .actionType("collection.created")
                .targetType("collection")
                .targetId(100L)
                .targetName("测试集合")
                .summary("创建了集合「测试集合」")
                .occurredAt(new Date())
                .build();

        when(activityRecordRepository.save(any(ActivityRecordView.class))).thenAnswer(inv -> {
            ActivityRecordView view = inv.getArgument(0);
            view.setActivityId(1L);
            return view;
        });

        assertDoesNotThrow(() -> service.record(command));
        verify(activityRecordRepository).save(any(ActivityRecordView.class));
    }

    @Test
    void shouldNotThrowWhenRepositoryFails() {
        RecordActivityCommand command = RecordActivityCommand.builder()
                .workspaceId(1L)
                .actionType("collection.created")
                .summary("test")
                .build();

        when(activityRecordRepository.save(any())).thenThrow(new RuntimeException("DB error"));

        assertDoesNotThrow(() -> service.record(command));
    }

    @Test
    void shouldSkipNullCommand() {
        assertDoesNotThrow(() -> service.record(null));
        verify(activityRecordRepository, never()).save(any());
    }

    @Test
    void shouldSkipCommandWithoutWorkspaceId() {
        RecordActivityCommand command = RecordActivityCommand.builder()
                .actionType("collection.created")
                .build();

        assertDoesNotThrow(() -> service.record(command));
        verify(activityRecordRepository, never()).save(any());
    }

    @Test
    void shouldSkipCommandWithoutActionType() {
        RecordActivityCommand command = RecordActivityCommand.builder()
                .workspaceId(1L)
                .build();

        assertDoesNotThrow(() -> service.record(command));
        verify(activityRecordRepository, never()).save(any());
    }
}
