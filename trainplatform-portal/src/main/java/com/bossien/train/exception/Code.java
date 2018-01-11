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
    AUTH_ERROR(50502, "授权失败"),
    USER_NOT_LOGIN(50503, "用户未登录"),
    USER_NOT_EXIST(50504, "用户不存在"),
    SIGN_ERROR(50505, "签名错误"),
    REQUEST_EXPIRE(50506, "会话已过期"),
    ROLE_NOT_EXIST(50507, "角色不存在"),
    PERMISSION_NOT_EXIST(50508, "未分配权限"),
    COMPANY_NOT_EXIST(50509, "无归属单位"),
    COMPANY_NOT_VALID(50510, "归属单位被禁用"),
    TO_MANY_REQUESTS(50511, "请求频繁"),
    LOCKED_ACCOUNT(50512, "账户被锁定"),
    INCORRECT_PASSWORD(50513, "密码错误"),
    PAPER_MAKE_FAIL(50514, "生成试卷失败"),
    PAPER_OLD_NUMBER(50515, "返回未考试的试卷"),
    ALREADY_AUTHENTICATED(50516, "重复登录"),
    INCORRECT_CREDENTIALS(50517, "认证失败"),
    PAPER_YET_SUBMIT_NUMBER(50518, "考卷已经提交或该考卷不是你的试卷"),
    ALREADY_LOGIN(50519, "账号已在其他地方登陆"),

    // sso exception code
    SSO_ERROR_C000001(90001, "sso系统异常 errorCode: C000001"),
    SSO_ERROR_C000002(90002, "sign verify 失败 errorCode: C000002"),
    SSO_ERROR_C000003(90003, "数据解密异常 errorCode: C000003"),
    SSO_ERROR_C000004(90004, "获取用户信息异常 errorCode: C000004"),
    SSO_ERROR_TICKET_JSON_CONVERT(90005, "用户信息转换异常"),
    SSO_ERROR_ALREADY_BIND_OPEN_ID(90006, "账户已经被绑定");




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
