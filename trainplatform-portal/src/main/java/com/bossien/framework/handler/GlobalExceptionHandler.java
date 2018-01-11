package com.bossien.framework.handler;

import com.bossien.train.exception.*;
import com.bossien.train.model.view.Response;
import com.bossien.train.util.RequestUtils;
import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;

/**
 * Created by Administrator on 2017/7/27.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static Logger errorLog = org.slf4j.LoggerFactory.getLogger("errorLog");

    @ExceptionHandler(SsoException.class)
    public Object handlerSsoException(SsoException ex, HttpServletRequest request, HttpServletResponse response) {

        writeLog(ex, request);

        return handlerView(ex, request, response, ex.getExceptionEnums().getCode(), ex.getExceptionEnums().getMessage());
    }

    @ExceptionHandler(IncorrectCredentialsException.class)
    public Object handlerIncorrectCredentialsException(IncorrectCredentialsException ex, HttpServletRequest request, HttpServletResponse response) {

        writeLog(ex, request);

        return handlerView(ex, request, response, Code.INCORRECT_CREDENTIALS.getCode(), Code.INCORRECT_CREDENTIALS.getMessage());
    }

    @ExceptionHandler(AlreadyLoginException.class)
    public Object handlerAlreadyLoginException(AlreadyLoginException ex, HttpServletRequest request, HttpServletResponse response) {

        writeLog(ex, request);

        return handlerView(ex, request, response, Code.ALREADY_LOGIN.getCode(), Code.ALREADY_LOGIN.getMessage());
    }

    @ExceptionHandler(AlreadyAuthenticatedException.class)
    public Object handlerAlreadyAuthenticatedException(AlreadyAuthenticatedException ex, HttpServletRequest request, HttpServletResponse response) {

        writeLog(ex, request);

        return handlerView(ex, request, response, Code.ALREADY_AUTHENTICATED.getCode(), Code.ALREADY_AUTHENTICATED.getMessage());
    }

    @ExceptionHandler(UnknownAccountException.class)
    public Object handlerUnknownAccountException(UnknownAccountException ex, HttpServletRequest request, HttpServletResponse response) {

        writeLog(ex, request);

        return handlerView(ex, request, response, Code.USER_NOT_EXIST.getCode(), Code.USER_NOT_EXIST.getMessage());
    }

    @ExceptionHandler(LockedAccountException.class)
    public Object handlerLockedAccountException(LockedAccountException ex, HttpServletRequest request, HttpServletResponse response) {

        writeLog(ex, request);

        return handlerView(ex, request, response, Code.LOCKED_ACCOUNT.getCode(), Code.LOCKED_ACCOUNT.getMessage());
    }


    @ExceptionHandler(IncorrectPasswordException.class)
    public Object handlerIncorrectPasswordException(IncorrectPasswordException ex, HttpServletRequest request, HttpServletResponse response) {

        writeLog(ex, request);

        return handlerView(ex, request, response, Code.INCORRECT_PASSWORD.getCode(), Code.INCORRECT_PASSWORD.getMessage());
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public Object handlerCompanyNotFoundException(CompanyNotFoundException ex, HttpServletRequest request, HttpServletResponse response) {

        writeLog(ex, request);

        return handlerView(ex, request, response, Code.COMPANY_NOT_EXIST.getCode(), Code.COMPANY_NOT_EXIST.getMessage());
    }

    @ExceptionHandler(CompanyNotValidException.class)
    public Object handlerCompanyNotValidException(CompanyNotValidException ex, HttpServletRequest request, HttpServletResponse response) {

        writeLog(ex, request);

        return handlerView(ex, request, response, Code.COMPANY_NOT_VALID.getCode(), Code.COMPANY_NOT_VALID.getMessage());
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public Object handlerRoleNotFoundException(RoleNotFoundException ex, HttpServletRequest request, HttpServletResponse response) {

        writeLog(ex, request);

        return handlerView(ex, request, response, Code.ROLE_NOT_EXIST.getCode(), Code.ROLE_NOT_EXIST.getMessage());
    }

    @ExceptionHandler(PermissionNotFoundException.class)
    public Object handlerPermissionNotFoundException(PermissionNotFoundException ex, HttpServletRequest request, HttpServletResponse response) {

        writeLog(ex, request);

        return handlerView(ex, request, response, Code.PERMISSION_NOT_EXIST.getCode(), Code.PERMISSION_NOT_EXIST.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public Object handlerServiceException(ServiceException ex, HttpServletRequest request, HttpServletResponse response) {

        writeLog(ex, request);

        return handlerView(ex, request, response, Code.SERVER_ERROR.getCode(), Code.SERVER_ERROR.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public Object handlerExpiredJwtException(ExpiredJwtException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {

        writeLog(ex, request);

        return handlerView(ex, request, response, Code.REQUEST_EXPIRE.getCode(), Code.REQUEST_EXPIRE.getMessage());

    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Object handlerMissingServletRequestParameterException(MissingServletRequestParameterException ex, HttpServletRequest request, HttpServletResponse response) {

        writeLog(ex, request);

        return handlerView(ex, request, response, Code.BAD_REQUEST.getCode(), Code.BAD_REQUEST.getMessage());

    }

    @ExceptionHandler(Exception.class)
    public Object handlerException(Exception ex, HttpServletRequest request, HttpServletResponse response) {

        writeLog(ex, request);

        return handlerView(ex, request, response, Code.SERVER_ERROR.getCode(), Code.SERVER_ERROR.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handlerNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request, HttpServletResponse response) {

        writeLog(ex, request);

        return handlerView(ex, request, response, Code.NO_FOUND.getCode(), Code.NO_FOUND.getMessage());

    }

    @ExceptionHandler(TooManyRequestsException.class)
    public Object handlerTooManyRequestsException(TooManyRequestsException ex, HttpServletRequest request, HttpServletResponse response) {

        writeLog(ex, request);

        return handlerView(ex, request, response, Code.TO_MANY_REQUESTS.getCode(), Code.TO_MANY_REQUESTS.getMessage());

    }

    private void writeLog(Exception ex, HttpServletRequest request) {
        String url = MessageFormat.format("Exception :{0}:{1}", request.getRequestURL(), request.getQueryString());
        errorLog.info(url);
    }

    private void writeMessageUft8(HttpServletResponse response, Response<Object> resp) throws Exception{
        try{
            Gson gson = new Gson();
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(gson.toJson(resp));
        }finally{
            response.getWriter().close();
        }
    }

    private Object handlerView(Exception ex, HttpServletRequest request, HttpServletResponse response, Integer code, String message) {
        if(!StringUtils.isEmpty(request.getHeader("x-requested-with")) && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
            // 异步请求
            try {
                writeMessageUft8(response, new Response<Object>(code, message));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        RequestUtils.addParam(request, "requUrl", request.getRequestURL());
        RequestUtils.addParam(request, "requQueryStr", request.getQueryString());
        RequestUtils.addParam(request, "errorCode", code);
        RequestUtils.addParam(request, "errorMsg", message);
        RequestUtils.addParam(request, "extroMsg", ex.toString());
        return "/error";
    }

}
