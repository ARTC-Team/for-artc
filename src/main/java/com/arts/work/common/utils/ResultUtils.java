package com.arts.work.common.utils;

/**
 * 返回类型工具类
 *
 * @author tgy
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> success(T data) {
        return new RestResponse<>(0, data, "ok");
    }

    /**
     * 成功
     *
     * @param data
     * @return
     */
    public static RestResponse success(int data) {
        return new RestResponse(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static RestResponse error(ErrorCode errorCode) {
        return new RestResponse<>(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static RestResponse error(ErrorCode errorCode, String message, String description) {
        return new RestResponse<>(errorCode.getCode(), description);
    }

    /**
     * 失败
     *
     * @param code
     * @return
     */
    public static RestResponse error(int code, String message, String description) {
        return new RestResponse<>(code, null, message, description);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static RestResponse error(ErrorCode errorCode, String description) {
        return new RestResponse<>(errorCode.getCode(), null, errorCode.getMessage(), description);
    }
}