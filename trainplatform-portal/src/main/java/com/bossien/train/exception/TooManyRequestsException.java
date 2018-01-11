package com.bossien.train.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Created by Administrator on 2017/9/6.
 */
public class TooManyRequestsException extends AuthenticationException {

    private ExceptionEnums exceptionEnums;

    private String addMsg;

    public ExceptionEnums getExceptionEnums() {
        return exceptionEnums;
    }

    public void setExceptionEnums(ExceptionEnums exceptionEnums) {
        this.exceptionEnums = exceptionEnums;
    }

    public String getAddMsg() {
        return addMsg;
    }

    public void setAddMsg(String addMsg) {
        this.addMsg = addMsg;
    }

    public TooManyRequestsException() {
    }

    public TooManyRequestsException(ExceptionEnums exceptionEnums, String addMsg) {
        this.exceptionEnums = exceptionEnums;
        this.addMsg = addMsg;
    }

    public TooManyRequestsException(ExceptionEnums exceptionEnums) {
        this.exceptionEnums = exceptionEnums;
    }

    public TooManyRequestsException create(ExceptionEnums exceptionEnums) {
        return new TooManyRequestsException(exceptionEnums);
    }

    public TooManyRequestsException create(ExceptionEnums exceptionEnums, String addMsg) {
        return new TooManyRequestsException(exceptionEnums, addMsg);
    }
}
