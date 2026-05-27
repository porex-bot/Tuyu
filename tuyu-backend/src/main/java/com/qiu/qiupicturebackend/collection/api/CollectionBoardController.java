package com.qiu.qiupicturebackend.collection.api;

import com.qiu.qiupicturebackend.collection.application.CollectionBoardApplicationService;
import com.qiu.qiupicturebackend.collection.application.command.AddAssetToCollectionCommand;
import com.qiu.qiupicturebackend.collection.application.command.ReorderCollectionItemsCommand;
import com.qiu.qiupicturebackend.collection.representation.CollectionBoardResponse;
import com.qiu.qiupicturebackend.collection.representation.CollectionItemResponse;
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

@Slf4j
@RestController
@RequestMapping("/v1/workspaces/{workspaceId}/collections/{collectionId}")
public class CollectionBoardController {

    @Resource
    private CollectionBoardApplicationService collectionBoardApplicationService;

    @Resource
    private UserService userService;

    @GetMapping("/board")
    public BaseResponse<CollectionBoardResponse> getBoard(@PathVariable Long workspaceId,
                                                           @PathVariable Long collectionId) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(collectionId == null || collectionId <= 0, ErrorCode.PARAMS_ERROR);
        CollectionBoardResponse board = collectionBoardApplicationService.getBoard(workspaceId, collectionId);
        return ResultUtils.success(board);
    }

    @PostMapping("/items")
    public BaseResponse<CollectionItemResponse> addItem(@PathVariable Long workspaceId,
                                                         @PathVariable Long collectionId,
                                                         @RequestBody AddAssetToCollectionCommand command,
                                                         HttpServletRequest request) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(collectionId == null || collectionId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(command == null || command.getAssetId() == null || command.getAssetId() <= 0,
                ErrorCode.PARAMS_ERROR, "资产 ID 不能为空");
        User loginUser = userService.getLoginUser(request);
        CollectionItemResponse item = collectionBoardApplicationService.addAsset(workspaceId, collectionId, command, loginUser);
        return ResultUtils.success(item);
    }

    @DeleteMapping("/items/{itemId}")
    public BaseResponse<Boolean> removeItem(@PathVariable Long workspaceId,
                                             @PathVariable Long collectionId,
                                             @PathVariable Long itemId) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(collectionId == null || collectionId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(itemId == null || itemId <= 0, ErrorCode.PARAMS_ERROR);
        collectionBoardApplicationService.removeAsset(workspaceId, collectionId, itemId);
        return ResultUtils.success(true);
    }

    @PostMapping("/items/reorder")
    public BaseResponse<Boolean> reorderItems(@PathVariable Long workspaceId,
                                               @PathVariable Long collectionId,
                                               @RequestBody ReorderCollectionItemsCommand command) {
        ThrowUtils.throwIf(workspaceId == null || workspaceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(collectionId == null || collectionId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(command == null || command.getOrders() == null || command.getOrders().isEmpty(),
                ErrorCode.PARAMS_ERROR);
        collectionBoardApplicationService.reorderItems(workspaceId, collectionId, command);
        return ResultUtils.success(true);
    }
}
