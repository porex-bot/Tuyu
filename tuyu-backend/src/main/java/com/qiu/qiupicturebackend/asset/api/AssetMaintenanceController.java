package com.qiu.qiupicturebackend.asset.api;

import com.qiu.qiupicturebackend.asset.application.AssetVersionBackfillApplicationService;
import com.qiu.qiupicturebackend.asset.application.command.BackfillAssetVersionsCommand;
import com.qiu.qiupicturebackend.common.BaseResponse;
import com.qiu.qiupicturebackend.common.ResultUtils;
import com.qiu.qiupicturebackend.exception.ErrorCode;
import com.qiu.qiupicturebackend.exception.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/maintenance/assets")
public class AssetMaintenanceController {

    @Resource
    private AssetVersionBackfillApplicationService backfillApplicationService;

    @PostMapping("/backfill-versions")
    public BaseResponse<Map<String, Object>> backfillVersions(@RequestBody BackfillAssetVersionsCommand command) {
        ThrowUtils.throwIf(command == null, ErrorCode.PARAMS_ERROR);
        Map<String, Object> result = backfillApplicationService.backfill(command);
        return ResultUtils.success(result);
    }
}
