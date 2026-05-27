package com.qiu.qiupicturebackend.ai.domain.repository;

import com.qiu.qiupicturebackend.ai.domain.model.AiJobResultView;

import java.util.List;
import java.util.Optional;

public interface AiJobResultRepository {

    AiJobResultView save(AiJobResultView result);

    AiJobResultView update(AiJobResultView result);

    Optional<AiJobResultView> findById(Long resultId);

    List<AiJobResultView> findByJobId(Long jobId);
}
