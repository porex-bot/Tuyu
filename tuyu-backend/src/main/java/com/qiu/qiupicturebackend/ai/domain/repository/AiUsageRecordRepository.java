package com.qiu.qiupicturebackend.ai.domain.repository;

import com.qiu.qiupicturebackend.ai.domain.model.AiJobView;

public interface AiUsageRecordRepository {

    void save(AiJobView job, Long userId, String usageType);
}
