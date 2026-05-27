package com.qiu.qiupicturebackend.activity.domain.repository;

import com.qiu.qiupicturebackend.activity.domain.model.ActivityRecordView;

import java.util.List;

public interface ActivityRecordRepository {

    ActivityRecordView save(ActivityRecordView record);

    List<ActivityRecordView> findByWorkspaceId(Long workspaceId, int offset, int limit);

    List<ActivityRecordView> findByTarget(String targetType, Long targetId, int offset, int limit);

    List<ActivityRecordView> findByWorkspaceAndTarget(Long workspaceId, String targetType, Long targetId, int offset, int limit);

    long countByWorkspaceId(Long workspaceId);

    long countByTarget(String targetType, Long targetId);

    long countByWorkspaceAndTarget(Long workspaceId, String targetType, Long targetId);
}
