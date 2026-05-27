package com.qiu.qiupicturebackend.activity.representation;

import lombok.Data;

import java.util.List;

@Data
public class ActivityTimelineResponse {

    private List<ActivityRecordResponse> records;
    private long total;
    private int offset;
    private int limit;
}
