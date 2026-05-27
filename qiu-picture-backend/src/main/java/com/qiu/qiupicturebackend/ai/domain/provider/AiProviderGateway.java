package com.qiu.qiupicturebackend.ai.domain.provider;

import java.util.Map;

public interface AiProviderGateway {

    /**
     * Submit an AI task to the provider.
     *
     * @param imageUrl   source image URL
     * @param parameters provider-neutral parameters (e.g. xScale, yScale, outputRatio for outpainting)
     * @return provider task result with task ID
     */
    AiProviderTaskResult submitTask(String imageUrl, Map<String, Object> parameters);

    /**
     * Query the status of a previously submitted task.
     *
     * @param providerTaskId the provider-side task identifier
     * @return current task result (status, output URL on success, error info on failure)
     */
    AiProviderTaskResult queryTask(String providerTaskId);

    /**
     * The provider type code (e.g., "aliyun").
     */
    String getProviderType();
}
