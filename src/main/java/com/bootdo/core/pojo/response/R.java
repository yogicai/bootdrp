package com.bootdo.core.pojo.response;

import lombok.Data;

/**
 * @author L
 */
@Data
public class R<T> {
    public static final Integer SUCCESS = 0;
    public static final Integer ERROR = 1;
    public static final Integer SERVER_ERROR = 500;

    /**
     * 请求是否成功
     */
    private Boolean success;

    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String msg;

    /**
     * 响应对象
     */
    private T data;


    public R() {
    }

    public R(Boolean success, Integer code, String msg, T data) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public static <T> R<T> error() {
        return new R<>(false, ERROR, "操作失败", null);
    }

    public static <T> R<T> error(String msg) {
        return new R<>(false, SERVER_ERROR, msg, null);
    }

    public static <T> R<T> error(int code, String msg) {
        return new R<>(false, code, msg, null);
    }

    public static <T> R<T> ok() {
        return new R<>(true, SUCCESS, "操作成功", null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(true, SUCCESS, "操作成功", data);
    }

    public static <T> R<T> ok(String msg) {
        return new R<>(true, SUCCESS, msg, null);
    }

}
