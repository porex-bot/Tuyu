package com.qiu.qiupicturebackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 用户注册请求
 * @Author: qiuqiu
 * @Date: 2023/4/17
 **/
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = -391778663564420470L;
    /**
     * 用户账号
     */
    private String userAccount;
    /**
     * 用户密码
     */
    private String userPassword;



}
