package com.bossien.train.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Created by Administrator on 2017/9/6.
 */
public class CompanyNotExistException extends AuthenticationException {

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

    public CompanyNotExistException(ExceptionEnums exceptionEnums, String addMsg) {
        this.exceptionEnums = exceptionEnums;
        this.addMsg = addMsg;
    }

    public CompanyNotExistException(ExceptionEnums exceptionEnums) {
        this.exceptionEnums = exceptionEnums;
    }

    public CompanyNotExistException create(ExceptionEnums exceptionEnums) {
        return new CompanyNotExistException(exceptionEnums);
    }

    public CompanyNotExistException create(ExceptionEnums exceptionEnums, String addMsg) {
        return new CompanyNotExistException(exceptionEnums, addMsg);
    }
}
