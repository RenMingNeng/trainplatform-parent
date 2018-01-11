package com.bossien.train.model.view;

/**
 * 返回结果基类
 * Created by luocc on 2017/7/20.
 */
public final class Result<T> {

    protected  int code;
    protected  String msg;
    protected  T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <K> Result asSuccess(K data){
        Result<K> result = new Result<K>();
        result.code = 0;
        result.msg = "";
        result.data = data;

        return result;
    }

    public static <K> Result fail(K data){
        Result<K> result = new Result<K>();
        result.code = -1;
        result.msg = "参数异常";
        result.data = data;

        return result;
    }

    public static <K> Result asSuccess(){
        Result<Object> result = new Result<Object>();
        result.code = 0;
        result.msg = "";
        result.data = null;

        return result;
    }
}
