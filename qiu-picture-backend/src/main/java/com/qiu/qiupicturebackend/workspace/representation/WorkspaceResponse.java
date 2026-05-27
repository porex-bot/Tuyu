package com.qiu.qiupicturebackend.workspace.representation;

import com.qiu.qiupicturebackend.model.vo.UserVO;
import com.qiu.qiupicturebackend.workspace.domain.model.WorkspaceView;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 工作区 API 响应
 */
@Data
public class WorkspaceResponse implements Serializable {

    /**
     * 工作区信息
     */
    private WorkspaceView workspace;

    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 当前用户在该工作区的权限列表
     */
    private List<String> permissionList = new ArrayList<>();

    private static final long serialVersionUID = 1L;
}
