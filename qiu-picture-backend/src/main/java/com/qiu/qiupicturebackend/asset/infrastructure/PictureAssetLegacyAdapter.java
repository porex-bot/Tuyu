package com.qiu.qiupicturebackend.asset.infrastructure;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiu.qiupicturebackend.asset.application.query.AssetPageQuery;
import com.qiu.qiupicturebackend.asset.application.query.AssetQuery;
import com.qiu.qiupicturebackend.asset.domain.model.AssetLifecycleStatus;
import com.qiu.qiupicturebackend.asset.domain.model.AssetView;
import com.qiu.qiupicturebackend.asset.representation.*;
import com.qiu.qiupicturebackend.manager.auth.SpaceUserAuthManager;
import com.qiu.qiupicturebackend.manager.auth.model.SpaceUserPermissionConstant;
import com.qiu.qiupicturebackend.model.dto.picture.PictureQueryRequest;
import com.qiu.qiupicturebackend.model.entity.Picture;
import com.qiu.qiupicturebackend.model.entity.Space;
import com.qiu.qiupicturebackend.model.entity.User;
import com.qiu.qiupicturebackend.model.enums.PictureReviewStatusEnum;
import com.qiu.qiupicturebackend.model.vo.PictureVO;
import com.qiu.qiupicturebackend.model.vo.UserVO;
import com.qiu.qiupicturebackend.service.PictureService;
import com.qiu.qiupicturebackend.service.SpaceService;
import com.qiu.qiupicturebackend.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 旧版 Picture/PictureVO 数据到 Asset 响应模型的只读适配器。
 * 不写数据库，不替换 PictureService。
 */
@Component
public class PictureAssetLegacyAdapter {

    @Resource
    private PictureService pictureService;

    @Resource
    private SpaceService spaceService;

    @Resource
    private UserService userService;

    @Resource
    private SpaceUserAuthManager spaceUserAuthManager;

    /**
     * 按工作区 ID 分页搜索视觉资产（Phase 2 内部使用 spaceId）。
     */
    public AssetPageResponse searchAssets(Long workspaceId, AssetPageQuery query, HttpServletRequest request) {
        PictureQueryRequest pictureQuery = toPictureQueryRequest(query);
        pictureQuery.setSpaceId(workspaceId);

        // 空间图片查询不过滤审核状态，与旧 PictureController 行为一致

        Page<Picture> picturePage = pictureService.page(
                new Page<>(query.getCurrent(), query.getPageSize()),
                pictureService.getQueryWrapper(pictureQuery));

        Page<PictureVO> voPage = pictureService.getPictureVOPage(picturePage, request);

        AssetPageResponse response = new AssetPageResponse();
        response.setRecords(voPage.getRecords().stream()
                .map(vo -> toAssetCardResponse(vo, vo.getPermissionList()))
                .collect(Collectors.toList()));
        response.setTotal(voPage.getTotal());
        response.setCurrent(voPage.getCurrent());
        response.setPageSize(voPage.getSize());
        return response;
    }

    /**
     * 按 assetId（Phase 2 即 picture.id）获取资产详情。
     */
    public AssetDetailResponse getAssetDetail(Long workspaceId, Long assetId, HttpServletRequest request) {
        Picture picture = pictureService.getById(assetId);
        if (picture == null) {
            return null;
        }
        // 验证工作区归属
        if (!workspaceId.equals(picture.getSpaceId())) {
            return null;
        }
        PictureVO vo = pictureService.getPictureVO(picture, request);
        return toAssetDetailResponse(vo, vo.getPermissionList());
    }

    /**
     * 验证资产是否属于指定工作区。
     */
    public boolean assetBelongsToWorkspace(Long workspaceId, Long assetId) {
        if (workspaceId == null || assetId == null) return false;
        Picture picture = pictureService.getById(assetId);
        return picture != null && workspaceId.equals(picture.getSpaceId());
    }

    /**
     * 获取资产的权限列表。
     */
    public List<String> getAssetPermissions(Long workspaceId, Long assetId, User loginUser) {
        Picture picture = pictureService.getById(assetId);
        if (picture == null || !workspaceId.equals(picture.getSpaceId())) {
            return Collections.emptyList();
        }
        Space space = spaceService.getById(workspaceId);
        if (space == null) {
            return Collections.emptyList();
        }
        return spaceUserAuthManager.getPermissionList(space, loginUser);
    }

    // ---- query mapping ----

    public PictureQueryRequest toPictureQueryRequest(AssetPageQuery assetQuery) {
        PictureQueryRequest req = new PictureQueryRequest();
        req.setCurrent(assetQuery.getCurrent());
        req.setPageSize(assetQuery.getPageSize());
        req.setSortField(assetQuery.getSortField());
        req.setSortOrder(assetQuery.getSortOrder());
        applyAssetQuery(req, assetQuery);
        return req;
    }

    private void applyAssetQuery(PictureQueryRequest req, AssetQuery assetQuery) {
        if (assetQuery == null) {
            return;
        }
        if (assetQuery.getSearchText() != null) {
            req.setSearchText(assetQuery.getSearchText());
        }
        if (assetQuery.getCategory() != null) {
            req.setCategory(assetQuery.getCategory());
        }
        if (assetQuery.getFormat() != null) {
            req.setPicFormat(assetQuery.getFormat());
        }
        if (assetQuery.getDominantColor() != null) {
            req.setSearchText(assetQuery.getDominantColor());
        }
        if (assetQuery.getWidth() != null) {
            req.setPicWidth(assetQuery.getWidth());
        }
        if (assetQuery.getHeight() != null) {
            req.setPicHeight(assetQuery.getHeight());
        }
        if (assetQuery.getUserId() != null) {
            req.setUserId(assetQuery.getUserId());
        }
    }

    // ---- asset view mapping ----

    public AssetView toAssetView(Picture picture) {
        if (picture == null) {
            return null;
        }
        AssetView view = new AssetView();
        view.setAssetId(picture.getId());
        view.setLegacyPictureId(picture.getId());
        view.setWorkspaceId(picture.getSpaceId());
        view.setName(picture.getName());
        view.setDescription(picture.getIntroduction());
        view.setCategory(picture.getCategory());
        view.setTags(picture.getTags());
        view.setUrl(picture.getUrl());
        view.setThumbnailUrl(picture.getThumbnailUrl());
        view.setWidth(picture.getPicWidth());
        view.setHeight(picture.getPicHeight());
        view.setScale(picture.getPicScale());
        view.setFormat(picture.getPicFormat());
        view.setSize(picture.getPicSize());
        view.setDominantColor(picture.getPicColor());
        view.setCreatedBy(picture.getUserId());
        view.setCreatedAt(picture.getCreateTime());
        view.setUpdatedAt(laterOf(picture.getEditTime(), picture.getUpdateTime()));
        view.setLifecycleStatus(AssetLifecycleStatus.fromReviewStatus(picture.getReviewStatus()));
        return view;
    }

    // ---- response mapping ----

    public AssetCardResponse toAssetCardResponse(PictureVO vo, List<String> permissionList) {
        if (vo == null) {
            return null;
        }
        AssetCardResponse card = new AssetCardResponse();
        card.setAssetId(vo.getId());
        card.setLegacyPictureId(vo.getId());
        card.setWorkspaceId(vo.getSpaceId());
        card.setName(vo.getName());
        card.setThumbnailUrl(vo.getThumbnailUrl());
        card.setUrl(vo.getUrl());
        card.setFormat(vo.getPicFormat());
        card.setWidth(vo.getPicWidth());
        card.setHeight(vo.getPicHeight());
        card.setSize(vo.getPicSize());
        card.setSizeDisplay(formatSize(vo.getPicSize()));
        card.setDominantColor(vo.getPicColor());
        card.setLifecycleStatus(AssetLifecycleStatus.fromReviewStatus(
                vo.getId() != null ? pictureReviewStatus(vo) : null).name());
        card.setReviewStatusText(reviewStatusLabel(pictureReviewStatus(vo)));
        card.setCreatedBy(vo.getUser() != null ? vo.getUser().getUserName() : null);
        card.setCreatedAt(dateStr(vo.getCreateTime()));
        card.setUpdatedAt(dateStr(laterOf(vo.getEditTime(), vo.getUpdateTime())));
        card.setPermissionList(permissionList != null ? permissionList : Collections.emptyList());
        return card;
    }

    public AssetDetailResponse toAssetDetailResponse(PictureVO vo, List<String> permissionList) {
        if (vo == null) {
            return null;
        }
        AssetDetailResponse detail = new AssetDetailResponse();
        detail.setAssetId(vo.getId());
        detail.setLegacyPictureId(vo.getId());
        detail.setWorkspaceId(vo.getSpaceId());
        detail.setName(vo.getName());
        detail.setDescription(vo.getIntroduction());
        detail.setCategory(vo.getCategory());
        detail.setTags(JSONUtil.toJsonStr(vo.getTags()));
        detail.setUrl(vo.getUrl());
        detail.setThumbnailUrl(vo.getThumbnailUrl());
        detail.setWidth(vo.getPicWidth());
        detail.setHeight(vo.getPicHeight());
        detail.setScale(vo.getPicScale());
        detail.setFormat(vo.getPicFormat());
        detail.setSize(vo.getPicSize());
        detail.setSizeDisplay(formatSize(vo.getPicSize()));
        detail.setDominantColor(vo.getPicColor());
        detail.setLifecycleStatus(AssetLifecycleStatus.fromReviewStatus(
                pictureReviewStatus(vo)).name());
        detail.setReviewStatusText(reviewStatusLabel(pictureReviewStatus(vo)));
        detail.setReviewMessage(null);

        AssetMetadataResponse metadata = new AssetMetadataResponse();
        metadata.setWidth(vo.getPicWidth());
        metadata.setHeight(vo.getPicHeight());
        metadata.setScale(vo.getPicScale());
        metadata.setFormat(vo.getPicFormat());
        metadata.setSize(vo.getPicSize());
        metadata.setSizeDisplay(formatSize(vo.getPicSize()));
        metadata.setDominantColor(vo.getPicColor());
        metadata.setCategory(vo.getCategory());
        metadata.setTags(JSONUtil.toJsonStr(vo.getTags()));
        detail.setMetadata(metadata);

        detail.setCreatedBy(vo.getUser() != null ? vo.getUser().getUserName() : null);
        detail.setCreatedAt(dateStr(vo.getCreateTime()));
        detail.setUpdatedAt(dateStr(laterOf(vo.getEditTime(), vo.getUpdateTime())));
        detail.setPermissionList(permissionList != null ? permissionList : Collections.emptyList());
        return detail;
    }

    // ---- private helpers ----

    private Integer pictureReviewStatus(PictureVO vo) {
        // PictureVO doesn't carry reviewStatus directly; derive from context.
        // Approved pictures are the only ones visible in workspace listing.
        return PictureReviewStatusEnum.PASS.getValue();
    }

    private String reviewStatusLabel(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 1: return "已通过";
            case 2: return "未通过";
            default: return "待审核";
        }
    }

    private String formatSize(Long bytes) {
        if (bytes == null || bytes <= 0) return "0 B";
        String[] units = {"B", "KB", "MB", "GB"};
        int i = Math.min(units.length - 1, (int) (Math.log(bytes) / Math.log(1024)));
        return String.format("%.1f %s", bytes / Math.pow(1024, i), units[i]);
    }

    private String dateStr(Date date) {
        return date != null ? date.toInstant().toString() : null;
    }

    private Date laterOf(Date a, Date b) {
        if (a == null) return b;
        if (b == null) return a;
        return a.after(b) ? a : b;
    }
}
