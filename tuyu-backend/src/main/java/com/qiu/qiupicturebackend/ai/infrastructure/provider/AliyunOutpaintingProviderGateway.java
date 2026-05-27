package com.qiu.qiupicturebackend.ai.infrastructure.provider;

import cn.hutool.json.JSONUtil;
import com.qiu.qiupicturebackend.ai.domain.provider.AiProviderGateway;
import com.qiu.qiupicturebackend.ai.domain.provider.AiProviderTaskResult;
import com.qiu.qiupicturebackend.api.aliyunai.model.AliYunAiApi;
import com.qiu.qiupicturebackend.api.aliyunai.model.CreateOutPaintingTaskRequest;
import com.qiu.qiupicturebackend.api.aliyunai.model.CreateOutPaintingTaskResponse;
import com.qiu.qiupicturebackend.api.aliyunai.model.GetOutPaintingTaskResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Component
public class AliyunOutpaintingProviderGateway implements AiProviderGateway {

    @Resource
    private AliYunAiApi aliYunAiApi;

    @Override
    public AiProviderTaskResult submitTask(String imageUrl, Map<String, Object> parameters) {
        try {
            CreateOutPaintingTaskRequest request = new CreateOutPaintingTaskRequest();

            CreateOutPaintingTaskRequest.Input input = new CreateOutPaintingTaskRequest.Input();
            input.setImageUrl(imageUrl);
            request.setInput(input);

            CreateOutPaintingTaskRequest.Parameters params = new CreateOutPaintingTaskRequest.Parameters();
            if (parameters != null) {
                if (parameters.containsKey("xScale")) {
                    params.setXScale(toFloat(parameters.get("xScale")));
                }
                if (parameters.containsKey("yScale")) {
                    params.setYScale(toFloat(parameters.get("yScale")));
                }
                if (parameters.containsKey("outputRatio")) {
                    params.setOutputRatio((String) parameters.get("outputRatio"));
                }
                if (parameters.containsKey("angle")) {
                    params.setAngle(toInt(parameters.get("angle")));
                }
                if (parameters.containsKey("topOffset")) {
                    params.setTopOffset(toInt(parameters.get("topOffset")));
                }
                if (parameters.containsKey("bottomOffset")) {
                    params.setBottomOffset(toInt(parameters.get("bottomOffset")));
                }
                if (parameters.containsKey("leftOffset")) {
                    params.setLeftOffset(toInt(parameters.get("leftOffset")));
                }
                if (parameters.containsKey("rightOffset")) {
                    params.setRightOffset(toInt(parameters.get("rightOffset")));
                }
                if (parameters.containsKey("bestQuality")) {
                    params.setBestQuality(Boolean.TRUE.equals(parameters.get("bestQuality")));
                }
                if (parameters.containsKey("addWatermark")) {
                    params.setAddWatermark(Boolean.TRUE.equals(parameters.get("addWatermark")));
                }
            }
            request.setParameters(params);

            CreateOutPaintingTaskResponse response = aliYunAiApi.createOutPaintingTask(request);
            if (response.getOutput() != null && response.getOutput().getTaskId() != null) {
                String taskId = response.getOutput().getTaskId();
                String status = normalizeStatus(response.getOutput().getTaskStatus());
                return AiProviderTaskResult.running(taskId);
            }
            return AiProviderTaskResult.failed(null, "PROVIDER_ERROR", "Aliyun returned no task ID");
        } catch (Exception e) {
            log.warn("Aliyun outpainting submit failed: {}", e.getMessage());
            return AiProviderTaskResult.failed(null, "SUBMIT_FAILED", e.getMessage());
        }
    }

    @Override
    public AiProviderTaskResult queryTask(String providerTaskId) {
        try {
            GetOutPaintingTaskResponse response = aliYunAiApi.getOutPaintingTask(providerTaskId);
            if (response.getOutput() != null) {
                String status = normalizeStatus(response.getOutput().getTaskStatus());
                if ("succeeded".equals(status)) {
                    return AiProviderTaskResult.succeeded(providerTaskId, response.getOutput().getOutputImageUrl());
                } else if ("failed".equals(status)) {
                    return AiProviderTaskResult.failed(providerTaskId,
                            response.getOutput().getCode(), response.getOutput().getMessage());
                } else if ("running".equals(status) || "pending".equals(status)) {
                    return AiProviderTaskResult.running(providerTaskId);
                }
            }
            return AiProviderTaskResult.running(providerTaskId);
        } catch (Exception e) {
            log.warn("Aliyun outpainting query failed: {}", e.getMessage());
            return AiProviderTaskResult.failed(providerTaskId, "QUERY_FAILED", e.getMessage());
        }
    }

    @Override
    public String getProviderType() {
        return "aliyun";
    }

    private String normalizeStatus(String aliyunStatus) {
        if (aliyunStatus == null) return "unknown";
        switch (aliyunStatus.toUpperCase()) {
            case "PENDING": return "running";
            case "RUNNING": return "running";
            case "SUCCEEDED": return "succeeded";
            case "FAILED": return "failed";
            case "SUSPENDED": return "running";
            default: return "running";
        }
    }

    private Float toFloat(Object value) {
        if (value instanceof Number) return ((Number) value).floatValue();
        if (value instanceof String) return Float.parseFloat((String) value);
        return null;
    }

    private Integer toInt(Object value) {
        if (value instanceof Number) return ((Number) value).intValue();
        if (value instanceof String) return Integer.parseInt((String) value);
        return null;
    }
}
