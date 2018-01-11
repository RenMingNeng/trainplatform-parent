package com.bossien.train.model.view;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/24.
 */
public class Response<T> implements Serializable {
    private int code = 10000; // 状态码
    private String message = "success"; // 异常消息
    private T result;// 数据列表
    private Long serverTime = System.currentTimeMillis();

    public Response() {}

    public Response(T result) {
        this.result = result;
    }

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Long getServerTime() {
        return serverTime;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }
}
