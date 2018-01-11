package com.bossien.framework.handler;

import com.bossien.train.exception.Code;
import com.bossien.train.exception.ExceptionEnums;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;

/**
 * Created by Administrator on 2017/7/27.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static Logger errorLog = org.slf4j.LoggerFactory.getLogger("errorLog");

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Object handlerServiceException(ServiceException ex, HttpServletRequest request, HttpServletResponse response) {

        writeLog(ex, request);
        ExceptionEnums enums = ex.getExceptionEnums();
        Response<Object> resp = new Response<Object>();

        StringBuilder errBuilder = new StringBuilder();
        if(null != ex.getAddMsg()){
            errBuilder.append(ex.getAddMsg());
        }else{
            errBuilder.append(enums.getMessage());
        }
        resp.setCode(enums.getCode());
        resp.setMessage(errBuilder.toString());

        return resp;
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    public Object handlerExpiredJwtException(ExpiredJwtException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {

        writeLog(ex, request);

        if(!StringUtils.isEmpty(request.getHeader("x-requested-with")) && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
            // 异步请求
            return new Response<Object>(Code.REQUEST_EXPIRE.getCode(), Code.REQUEST_EXPIRE.getMessage());
        }

        response.sendRedirect(request.getContextPath()+"?msg="+ URLEncoder.encode(Code.REQUEST_EXPIRE.getMessage(), "utf-8"));
        return null;

    }


    /**
     * 参数异常
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Object handlerMissingServletRequestParameterException(MissingServletRequestParameterException ex, HttpServletRequest request, HttpServletResponse response) {

        writeLog(ex, request);
        Response<Object> resp = new Response<Object>(Code.BAD_REQUEST.getCode(), Code.BAD_REQUEST.getMessage());

        return resp;

    }

    /**
     * 服务异常
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handlerException(Exception ex, HttpServletRequest request, HttpServletResponse response) {

        writeLog(ex, request);
       // ex.printStackTrace();
        Response<Object> resp = new Response<Object>(Code.SERVER_ERROR.getCode(), Code.SERVER_ERROR.getMessage());

        return resp;

    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Object handlerNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request, HttpServletResponse response) {

        writeLog(ex, request);
        // ex.printStackTrace();
        Response<Object> resp = new Response<Object>(Code.NO_FOUND.getCode(), Code.NO_FOUND.getMessage());

        return resp;

    }


    private void writeLog(Exception ex, HttpServletRequest request) {
        String url = MessageFormat.format("Exception :{0}:{1}", request.getRequestURL(), request.getQueryString());
        errorLog.info(url);
    }
}
