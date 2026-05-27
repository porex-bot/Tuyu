package com.qiu.qiupicturebackend.ai.domain.repository;

import com.qiu.qiupicturebackend.ai.domain.model.AiCapabilityView;

import java.util.List;
import java.util.Optional;

public interface AiCapabilityRepository {

    List<AiCapabilityView> findAll();

    List<AiCapabilityView> findActive();

    Optional<AiCapabilityView> findByKey(String capabilityKey);
}
