package com.qiu.qiupicturebackend.asset.api;

import com.qiu.qiupicturebackend.asset.application.AssetPermissionApplicationService;
import com.qiu.qiupicturebackend.asset.application.AssetQueryApplicationService;
import com.qiu.qiupicturebackend.asset.application.query.AssetPageQuery;
import com.qiu.qiupicturebackend.asset.representation.AssetDetailResponse;
import com.qiu.qiupicturebackend.asset.representation.AssetPageResponse;
import com.qiu.qiupicturebackend.asset.representation.AssetVersionResponse;
import com.qiu.qiupicturebackend.common.BaseResponse;
import com.qiu.qiupicturebackend.common.ResultUtils;
import com.qiu.qiupicturebackend.exception.ErrorCode;
import com.qiu.qiupicturebackend.exception.ThrowUtils;
import com.qiu.qiupicturebackend.model.entity.User;
import com.qiu.qiupicturebackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 视觉资产 V1 只读 API。
 * 与旧 PictureController 共存，不修改旧端点。
 */
@Slf4j
@RestController
@RequestMapping("/v1/workspaces/{workspaceId}/assets")
public class AssetController {

    @Resource
    private AssetQueryApplicationService assetQueryApplicationService;

    @Resource
    private AssetPermissionApplicationService assetPermissionApplicationService;

    @Resource
    private UserService userService;

    /**
     * 工作区范围内的资产卡片搜索。
     */
    @PostMapping("/search")
    public BaseResponse<AssetPageResponse> searchAssets(@PathVariable Long workspaceId,
                                                         @RequestBody AssetPageQuery query,
                                                         HttpServletRequest request) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(query == null, ErrorCode.PARAMS_ERROR);
        AssetPageResponse result = assetQueryApplicationService.searchAssets(workspaceId, query, request);
        return ResultUtils.success(result);
    }

    /**
     * 获取单个资产详情。
     */
    @GetMapping("/{assetId}")
    public BaseResponse<AssetDetailResponse> getAssetDetail(@PathVariable Long workspaceId,
                                                             @PathVariable Long assetId,
                                                             HttpServletRequest request) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(assetId == null || assetId <= 0, ErrorCode.PARAMS_ERROR);
        AssetDetailResponse detail = assetQueryApplicationService.getAssetDetail(workspaceId, assetId, request);
        ThrowUtils.throwIf(detail == null, ErrorCode.NOT_FOUND_ERROR, "资产不存在");
        return ResultUtils.success(detail);
    }

    /**
     * 获取当前用户对指定资产的权限列表。
     */
    @GetMapping("/{assetId}/permissions")
    public BaseResponse<List<String>> getAssetPermissions(@PathVariable Long workspaceId,
                                                          @PathVariable Long assetId,
                                                          HttpServletRequest request) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(assetId == null || assetId <= 0, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        List<String> permissions = assetPermissionApplicationService.getAssetPermissions(workspaceId, assetId, loginUser);
        return ResultUtils.success(permissions);
    }

    @GetMapping("/{assetId}/versions")
    public BaseResponse<List<AssetVersionResponse>> listAssetVersions(@PathVariable Long workspaceId,
                                                                       @PathVariable Long assetId) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(assetId == null || assetId <= 0, ErrorCode.PARAMS_ERROR);
        List<AssetVersionResponse> versions = assetQueryApplicationService.listAssetVersions(workspaceId, assetId);
        return ResultUtils.success(versions);
    }
}
