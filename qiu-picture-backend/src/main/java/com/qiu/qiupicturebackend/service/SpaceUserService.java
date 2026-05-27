package com.qiu.qiupicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qiu.qiupicturebackend.model.dto.spaceuser.SpaceUserAddRequest;
import com.qiu.qiupicturebackend.model.dto.spaceuser.SpaceUserQueryRequest;
import com.qiu.qiupicturebackend.model.entity.SpaceUser;
import com.qiu.qiupicturebackend.model.vo.SpaceUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 19365
* @description 针对表【space_user(空间用户关联)】的数据库操作Service
* @createDate 2026-05-19 19:05:22
*/
public interface SpaceUserService extends IService<SpaceUser> {
    /**
     * 创建空间成员
     *
     * @param spaceUserAddRequest
     * @return
     */
    long addSpaceUser(SpaceUserAddRequest spaceUserAddRequest);
    /**
     * 获取空间成员包装类
     *
     * @param spaceUser
     * @param request
     * @return
     */
    SpaceUserVO getSpaceUserVO(SpaceUser spaceUser, HttpServletRequest request);

    /**
     * 获取空间成员包装类（多条）
     *
     * @param spaceUserList
     * @return
     */
    List<SpaceUserVO> getSpaceUserVOList(List<SpaceUser> spaceUserList);

    /**
     * 校验空间
     *
     * @param spaceUser
     */
    void validSpaceUser(SpaceUser spaceUser, boolean add);

    /**
     * 获取查询条件
     *
     * @param spaceUserQueryRequest
     * @return
     */
    QueryWrapper<SpaceUser> getQueryWrapper(SpaceUserQueryRequest spaceUserQueryRequest);


}
