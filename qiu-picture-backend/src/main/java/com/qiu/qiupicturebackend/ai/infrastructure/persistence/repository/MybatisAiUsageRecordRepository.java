package com.qiu.qiupicturebackend.ai.infrastructure.persistence.repository;

import com.qiu.qiupicturebackend.ai.domain.model.AiJobView;
import com.qiu.qiupicturebackend.ai.domain.repository.AiUsageRecordRepository;
import com.qiu.qiupicturebackend.ai.infrastructure.persistence.entity.AiUsageRecordEntity;
import com.qiu.qiupicturebackend.ai.infrastructure.persistence.mapper.AiUsageRecordMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

@Repository
public class MybatisAiUsageRecordRepository implements AiUsageRecordRepository {

    private final AiUsageRecordMapper mapper;

    public MybatisAiUsageRecordRepository(AiUsageRecordMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void save(AiJobView job, Long userId, String usageType) {
        AiUsageRecordEntity entity = new AiUsageRecordEntity();
        entity.setWorkspaceId(job.getWorkspaceId());
        entity.setJobId(job.getJobId());
        entity.setUserId(userId);
        entity.setCapabilityKey(job.getCapabilityKey());
        entity.setProvider(job.getProvider() != null ? job.getProvider() : "aliyun");
        entity.setUsageType(usageType);
        entity.setUsageAmount(new BigDecimal("1.00"));
        entity.setUsageUnit("call");
        entity.setRecordedAt(new Date());
        entity.setCreatedAt(new Date());
        mapper.insert(entity);
    }
}
