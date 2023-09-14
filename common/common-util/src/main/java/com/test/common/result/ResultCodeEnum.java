package com.test.common.result;

import lombok.Getter;

/**
 * 枚举类，用于枚举所有的返回结果码
 *
 */
@Getter
public enum ResultCodeEnum {
    SUCCESS(200,"成功"),
    FAIL(201,"失败"),
    SERVICE_ERROR(2012,"服务异常"),
    DATA_ERROR(204,"数据异常"),
    LOGIN_ERROR(208,"登录错误" ) ,
    PERMISSION(209,"没有权限"),
    LOGIN_AUTH(210,"未登录");
    private final Integer code;
    private final String message;

    private ResultCodeEnum(Integer code,String message){
        this.code=code;
        this.message=message;
    }
}
