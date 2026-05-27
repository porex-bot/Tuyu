package com.qiu.qiupicturebackend.workspace.representation;

import com.qiu.qiupicturebackend.model.vo.UserVO;
import com.qiu.qiupicturebackend.workspace.domain.model.WorkspaceMemberView;
import lombok.Data;

import java.io.Serializable;

/**
 * 工作区成员 API 响应
 */
@Data
public class WorkspaceMemberResponse implements Serializable {

    /**
     * 成员信息
     */
    private WorkspaceMemberView member;

    /**
     * 用户信息
     */
    private UserVO user;

    private static final long serialVersionUID = 1L;
}
