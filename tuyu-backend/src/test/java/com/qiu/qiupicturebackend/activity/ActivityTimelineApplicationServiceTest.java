package com.qiu.qiupicturebackend.activity;

import com.qiu.qiupicturebackend.activity.application.ActivityTimelineApplicationService;
import com.qiu.qiupicturebackend.activity.application.query.ActivityTimelineQuery;
import com.qiu.qiupicturebackend.activity.domain.model.ActivityRecordView;
import com.qiu.qiupicturebackend.activity.domain.repository.ActivityRecordRepository;
import com.qiu.qiupicturebackend.activity.representation.ActivityTimelineResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityTimelineApplicationServiceTest {

    @Mock
    private ActivityRecordRepository activityRecordRepository;

    @InjectMocks
    private ActivityTimelineApplicationService service;

    @Test
    void shouldReturnWorkspaceTimeline() {
        ActivityRecordView record = new ActivityRecordView();
        record.setActivityId(1L);
        record.setWorkspaceId(1L);
        record.setActionType("collection.created");
        record.setSummary("test");

        when(activityRecordRepository.findByWorkspaceId(1L, 0, 20)).thenReturn(List.of(record));
        when(activityRecordRepository.countByWorkspaceId(1L)).thenReturn(1L);

        ActivityTimelineQuery query = new ActivityTimelineQuery();
        query.setOffset(0);
        query.setLimit(20);

        ActivityTimelineResponse result = service.getWorkspaceTimeline(1L, query);

        assertNotNull(result);
        assertEquals(1, result.getRecords().size());
        assertEquals(1L, result.getTotal());
        assertEquals("collection.created", result.getRecords().get(0).getActionType());
    }

    @Test
    void shouldReturnEmptyTimelineForNullWorkspace() {
        ActivityTimelineResponse result = service.getWorkspaceTimeline(null, null);

        assertNotNull(result);
        assertEquals(0, result.getRecords().size());
        assertEquals(0L, result.getTotal());
    }

    @Test
    void shouldReturnEmptyTimelineForInvalidWorkspace() {
        ActivityTimelineResponse result = service.getWorkspaceTimeline(0L, null);

        assertNotNull(result);
        assertEquals(0, result.getRecords().size());
    }

    @Test
    void shouldReturnTargetTimeline() {
        ActivityRecordView record = new ActivityRecordView();
        record.setActivityId(1L);
        record.setWorkspaceId(1L);
        record.setActionType("collection.item.added");

        when(activityRecordRepository.findByWorkspaceAndTarget(1L, "collection", 100L, 0, 20))
                .thenReturn(List.of(record));
        when(activityRecordRepository.countByWorkspaceAndTarget(1L, "collection", 100L)).thenReturn(1L);

        ActivityTimelineResponse result = service.getTargetTimeline(1L, "collection", 100L, null);

        assertNotNull(result);
        assertEquals(1, result.getRecords().size());
    }

    @Test
    void shouldReturnEmptyTargetTimelineForNullParams() {
        ActivityTimelineResponse result = service.getTargetTimeline(null, null, null, null);

        assertNotNull(result);
        assertEquals(0, result.getRecords().size());
    }

    @Test
    void shouldHandleEmptyRepository() {
        when(activityRecordRepository.findByWorkspaceId(1L, 0, 20))
                .thenReturn(Collections.emptyList());
        when(activityRecordRepository.countByWorkspaceId(1L)).thenReturn(0L);

        ActivityTimelineResponse result = service.getWorkspaceTimeline(1L, new ActivityTimelineQuery());

        assertNotNull(result);
        assertEquals(0, result.getRecords().size());
    }
}
