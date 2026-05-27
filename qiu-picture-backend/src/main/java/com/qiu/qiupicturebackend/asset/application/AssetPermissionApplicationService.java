package com.qiu.qiupicturebackend.asset.application;

import com.qiu.qiupicturebackend.asset.infrastructure.PictureAssetLegacyAdapter;
import com.qiu.qiupicturebackend.model.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 视觉资产权限应用服务。
 * 委托旧 SpaceUserAuthManager 进行权限查询。
 */
@Service
public class AssetPermissionApplicationService {

    @Resource
    private PictureAssetLegacyAdapter pictureAssetLegacyAdapter;

    /**
     * 获取当前用户对指定视觉资产的权限列表。
     */
    public List<String> getAssetPermissions(Long workspaceId, Long assetId, User loginUser) {
        if (workspaceId == null || assetId == null || loginUser == null) {
            return Collections.emptyList();
        }
        return pictureAssetLegacyAdapter.getAssetPermissions(workspaceId, assetId, loginUser);
    }
}
