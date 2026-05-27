package com.qiu.qiupicturebackend.ai.api;

import com.qiu.qiupicturebackend.ai.application.AiCapabilityApplicationService;
import com.qiu.qiupicturebackend.ai.domain.model.AiCapabilityView;
import com.qiu.qiupicturebackend.ai.representation.AiCapabilityResponse;
import com.qiu.qiupicturebackend.common.BaseResponse;
import com.qiu.qiupicturebackend.common.ResultUtils;
import com.qiu.qiupicturebackend.exception.ErrorCode;
import com.qiu.qiupicturebackend.exception.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/workspaces/{workspaceId}/ai/capabilities")
public class AiCapabilityController {

    @Resource
    private AiCapabilityApplicationService aiCapabilityApplicationService;

    @GetMapping
    public BaseResponse<List<AiCapabilityResponse>> getCapabilities(@PathVariable Long workspaceId) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        List<AiCapabilityView> capabilities = aiCapabilityApplicationService.getActiveCapabilities(workspaceId);
        List<AiCapabilityResponse> response = capabilities.stream().map(this::toResponse).collect(Collectors.toList());
        return ResultUtils.success(response);
    }

    private AiCapabilityResponse toResponse(AiCapabilityView view) {
        AiCapabilityResponse r = new AiCapabilityResponse();
        r.setCapabilityKey(view.getCapabilityKey());
        r.setDisplayName(view.getDisplayName());
        r.setDescription(view.getDescription());
        r.setProvider(view.getProvider());
        r.setActive(view.isActive());
        return r;
    }
}
