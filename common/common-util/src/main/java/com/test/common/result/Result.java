package com.test.common.result;

import lombok.Data;

/**
 * 统一结果类
 */
@Data
public class Result<T> {
    private Integer code;//状态码
    private String message;//返回信息
    private T data;
    private Result(){};

    public static <T> Result<T> build(T data,ResultCodeEnum resultCodeEnum){
        Result<T> result = new Result<>();
        if(data!=null){
            result.setData(data);
        }
        //返回码
        result.setCode(resultCodeEnum.getCode());
        //返回信息
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }
    public static <T> Result<T> success(){
        return build(null,ResultCodeEnum.SUCCESS);
    }
    public static <T> Result<T> success(T data){
        return build(data,ResultCodeEnum.SUCCESS);
    }
    public static <T> Result<T> fail(){
        return build(null,ResultCodeEnum.FAIL);
    }
    public static <T> Result<T> fail(T data){
        return build(data,ResultCodeEnum.FAIL);
    }

    public Result<T> message(String message){
       this.setMessage(message);
       return this;
    }
    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }
}
