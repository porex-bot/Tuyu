package com.qiu.qiupicturebackend.ai;

import com.qiu.qiupicturebackend.ai.domain.provider.AiProviderTaskResult;
import com.qiu.qiupicturebackend.ai.infrastructure.provider.AliyunOutpaintingProviderGateway;
import com.qiu.qiupicturebackend.api.aliyunai.model.AliYunAiApi;
import com.qiu.qiupicturebackend.api.aliyunai.model.CreateOutPaintingTaskRequest;
import com.qiu.qiupicturebackend.api.aliyunai.model.CreateOutPaintingTaskResponse;
import com.qiu.qiupicturebackend.api.aliyunai.model.GetOutPaintingTaskResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AliyunOutpaintingProviderGatewayTest {

    @Mock
    private AliYunAiApi aliYunAiApi;

    @InjectMocks
    private AliyunOutpaintingProviderGateway gateway;

    @Test
    void shouldSubmitOutpaintingTask() {
        CreateOutPaintingTaskResponse response = new CreateOutPaintingTaskResponse();
        CreateOutPaintingTaskResponse.Output output = new CreateOutPaintingTaskResponse.Output();
        output.setTaskId("task-abc");
        output.setTaskStatus("PENDING");
        response.setOutput(output);

        when(aliYunAiApi.createOutPaintingTask(any(CreateOutPaintingTaskRequest.class))).thenReturn(response);

        Map<String, Object> params = new HashMap<>();
        params.put("xScale", 1.5f);
        params.put("yScale", 1.5f);

        AiProviderTaskResult result = gateway.submitTask("http://example.com/img.png", params);

        assertNotNull(result);
        assertEquals("task-abc", result.getProviderTaskId());
        assertEquals("running", result.getStatus());
    }

    @Test
    void shouldQuerySucceededTask() {
        GetOutPaintingTaskResponse response = new GetOutPaintingTaskResponse();
        GetOutPaintingTaskResponse.Output output = new GetOutPaintingTaskResponse.Output();
        output.setTaskId("task-abc");
        output.setTaskStatus("SUCCEEDED");
        output.setOutputImageUrl("http://output.url/result.png");
        response.setOutput(output);

        when(aliYunAiApi.getOutPaintingTask("task-abc")).thenReturn(response);

        AiProviderTaskResult result = gateway.queryTask("task-abc");

        assertNotNull(result);
        assertEquals("succeeded", result.getStatus());
        assertEquals("http://output.url/result.png", result.getOutputImageUrl());
    }

    @Test
    void shouldQueryFailedTask() {
        GetOutPaintingTaskResponse response = new GetOutPaintingTaskResponse();
        GetOutPaintingTaskResponse.Output output = new GetOutPaintingTaskResponse.Output();
        output.setTaskId("task-abc");
        output.setTaskStatus("FAILED");
        output.setCode("ERROR_001");
        output.setMessage("处理失败");
        response.setOutput(output);

        when(aliYunAiApi.getOutPaintingTask("task-abc")).thenReturn(response);

        AiProviderTaskResult result = gateway.queryTask("task-abc");

        assertNotNull(result);
        assertEquals("failed", result.getStatus());
        assertEquals("ERROR_001", result.getErrorCode());
    }

    @Test
    void shouldQueryRunningTask() {
        GetOutPaintingTaskResponse response = new GetOutPaintingTaskResponse();
        GetOutPaintingTaskResponse.Output output = new GetOutPaintingTaskResponse.Output();
        output.setTaskId("task-abc");
        output.setTaskStatus("RUNNING");
        response.setOutput(output);

        when(aliYunAiApi.getOutPaintingTask("task-abc")).thenReturn(response);

        AiProviderTaskResult result = gateway.queryTask("task-abc");

        assertNotNull(result);
        assertEquals("running", result.getStatus());
    }

    @Test
    void shouldReturnProviderType() {
        assertEquals("aliyun", gateway.getProviderType());
    }
}
