package com.bossien.train.exception;

/**
 * Created by Administrator on 2017/7/24.
 * 服务异常
 */
public class ServiceException extends Exception {

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

    public ServiceException(ExceptionEnums exceptionEnums, String addMsg) {
        this.exceptionEnums = exceptionEnums;
        this.addMsg = addMsg;
    }

    public ServiceException(ExceptionEnums exceptionEnums) {
        this.exceptionEnums = exceptionEnums;
    }

    public ServiceException create(ExceptionEnums exceptionEnums) {
        return new ServiceException(exceptionEnums);
    }

    public ServiceException create(ExceptionEnums exceptionEnums, String addMsg) {
        return new ServiceException(exceptionEnums, addMsg);
    }
}
