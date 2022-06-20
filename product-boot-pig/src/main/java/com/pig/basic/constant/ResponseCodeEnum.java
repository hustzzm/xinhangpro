package com.pig.basic.constant;

/**
 * 返回编码枚举
 */
public enum ResponseCodeEnum {

    SUCCESS(200, "操作成功"),
    FAILURE(400, "业务异常");


    final int code;
    final String message;

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    private ResponseCodeEnum(final int code, final String message) {
        this.code = code;
        this.message = message;
    }
}
