package com.qiu.qiupicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qiu.qiupicturebackend.model.dto.user.UserQueryRequest;
import com.qiu.qiupicturebackend.model.entity.User;
import com.qiu.qiupicturebackend.model.vo.LoginUserVO;
import com.qiu.qiupicturebackend.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 19365
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2026-04-16 20:16:47
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return 注册成功返回用户信息，失败返回null
     */

    long userRegister(String userAccount, String userPassword, String checkPassword, String email);

    String getEncryptPassword(String userPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获得脱敏后的用户信息
     * @param user
     * @return
     */

    LoginUserVO getLoginUserVO(User user);

    /**
     * userList
     * @param user
     * @return
     */

    UserVO getUserVO(User user);

    /**
     * 获得脱敏后的用户信息列表
     * @param userList
     * @return
     */

    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 用户登录态注销
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取查询条件（包装类）
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);

}
