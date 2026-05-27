package com.qiu.qiupicturebackend.collection.api;

import com.qiu.qiupicturebackend.collection.application.CollectionApplicationService;
import com.qiu.qiupicturebackend.collection.application.command.CreateCollectionCommand;
import com.qiu.qiupicturebackend.collection.representation.CollectionResponse;
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

@Slf4j
@RestController
@RequestMapping("/v1/workspaces/{workspaceId}/collections")
public class CollectionController {

    @Resource
    private CollectionApplicationService collectionApplicationService;

    @Resource
    private UserService userService;

    @GetMapping
    public BaseResponse<List<CollectionResponse>> listCollections(@PathVariable Long workspaceId) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        List<CollectionResponse> collections = collectionApplicationService.listCollections(workspaceId);
        return ResultUtils.success(collections);
    }

    @PostMapping
    public BaseResponse<CollectionResponse> createCollection(@PathVariable Long workspaceId,
                                                              @RequestBody CreateCollectionCommand command,
                                                              HttpServletRequest request) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(command == null || command.getName() == null || command.getName().isBlank(),
                ErrorCode.PARAMS_ERROR, "集合名称不能为空");
        User loginUser = userService.getLoginUser(request);
        CollectionResponse collection = collectionApplicationService.createCollection(workspaceId, command, loginUser);
        return ResultUtils.success(collection);
    }

    @GetMapping("/{collectionId}")
    public BaseResponse<CollectionResponse> getCollection(@PathVariable Long workspaceId,
                                                           @PathVariable Long collectionId) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(collectionId == null || collectionId <= 0, ErrorCode.PARAMS_ERROR);
        CollectionResponse collection = collectionApplicationService.getCollection(workspaceId, collectionId);
        ThrowUtils.throwIf(collection == null, ErrorCode.NOT_FOUND_ERROR, "集合不存在");
        return ResultUtils.success(collection);
    }
}
