package com.pig.basic.util;

import com.pig.basic.constant.ResponseCodeEnum;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CommonResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String msg;


    @Getter
    @Setter
    private T data;

    @Getter
    @Setter
    private boolean success;

    public static <T> CommonResult<T> ok() {
        return restResult(null, ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.getMessage(), true);
    }

    public static <T> CommonResult<T> ok(T data) {
        return restResult(data, ResponseCodeEnum.SUCCESS.getCode(), null,true);
    }

    public static <T> CommonResult<T> ok(String msg) {
        return restResult(null, ResponseCodeEnum.SUCCESS.getCode(), msg,true);
    }

    public static <T> CommonResult<T> ok(T data, String msg) {
        return restResult(data, ResponseCodeEnum.SUCCESS.getCode(), msg,true);
    }

    public static <T> CommonResult<T> failed() {
        return restResult(null, ResponseCodeEnum.FAILURE.getCode(), null,false);
    }

    public static <T> CommonResult<T> failed(String msg) {
        return restResult(null, ResponseCodeEnum.FAILURE.getCode(), msg,false);
    }

    public static <T> CommonResult<T> failed(T data) {
        return restResult(data, ResponseCodeEnum.FAILURE.getCode(), null,false);
    }

    public static <T> CommonResult<T> failed(T data, String msg) {
        return restResult(data, ResponseCodeEnum.FAILURE.getCode(), msg,false);
    }

    private static <T> CommonResult<T> restResult(T data, int code, String msg,boolean success) {
        CommonResult<T> apiResult = new CommonResult<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        apiResult.setSuccess(success);
        return apiResult;
    }
}