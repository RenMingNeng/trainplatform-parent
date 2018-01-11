package com.bossien.train.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Created by Administrator on 2017/9/6.
 */
public class IncorrectPasswordException extends AuthenticationException {

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

    public IncorrectPasswordException() {
    }

    public IncorrectPasswordException(ExceptionEnums exceptionEnums, String addMsg) {
        this.exceptionEnums = exceptionEnums;
        this.addMsg = addMsg;
    }

    public IncorrectPasswordException(ExceptionEnums exceptionEnums) {
        this.exceptionEnums = exceptionEnums;
    }

    public IncorrectPasswordException create(ExceptionEnums exceptionEnums) {
        return new IncorrectPasswordException(exceptionEnums);
    }

    public IncorrectPasswordException create(ExceptionEnums exceptionEnums, String addMsg) {
        return new IncorrectPasswordException(exceptionEnums, addMsg);
    }
}
