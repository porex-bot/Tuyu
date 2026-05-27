package com.qiu.qiupicturebackend.common;

import com.qiu.qiupicturebackend.exception.ErrorCode;

public class ResultUtils {
    /**
     * 成功
     * @param data 数据
     * @param <T> 数据类型
     * @return 响应
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0,data, "ok");
    }

    /**
     * 失败
     * @param errorCode 错误码
     * @return 响应
     */
    public static <T> BaseResponse<?> error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     * @param code 错误码
     * @param message 错误信息
     * @return 响应
     */
    public static <T> BaseResponse<?> error(int code, String message){
        return new BaseResponse<>(code,null,message);
    }

    /**
     * 错误
     * @param errorCode 错误码
     * @return 响应
     */
    public static <T> BaseResponse<?> error(ErrorCode errorCode,String message){
        return new BaseResponse<>(errorCode.getCode(),null,message);
    }
}
