package com.bossien.train.exception;

/**
 * Created by Administrator on 2017/7/24.
 * 通用异常状态码
 */
public enum Code implements ExceptionEnums {

    SUCCESS(10000, "服务成功"),
    BAD_REQUEST(20000, "参数错误"),
    SERVER_ERROR(50500, "服务器异常"),
    NO_FOUND(50501, "资源找不到"),
    USER_NOT_LOGIN(50503, "用户未登录"),
    USER_NOT_EXIST(50504, "用户不存在"),
    SIGN_ERROR(50505, "签名错误"),
    REQUEST_EXPIRE(50506, "会话已过期"),
    ROLE_NOT_EXIST(50507, "角色不存在");

    private int code;

    private String message;

    private Code(int code, String message) {
        this.code = code;
        this.message =  message;
    }

    /**
     * 获取执行代码
     */
    @Override
    public int getCode() {
        return this.code;
    }

    /**
     * 获取执行消息
     */
    @Override
    public String getMessage() {
        return this.message;
    }
}
