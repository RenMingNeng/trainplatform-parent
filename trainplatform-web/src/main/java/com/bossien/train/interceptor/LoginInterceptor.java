package com.bossien.train.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by hubo on 2015/8/8
 */
public class LoginInterceptor  implements HandlerInterceptor{

    private static Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Boolean isLogin =  (Boolean)request.getSession().getAttribute("isLogin");

        if(isLogin == null || !isLogin){
            log.info("没有登录,直接跳转登录页面");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return false;
        }
//        else {
//            boolean isPermission = isPermissionFunc(request.getSession(), request.getServletPath());
//
//            if ( !isPermission ) { // TODO:针对没有权限的url暂时返回到登陆页面，后续应该跳转到错误页面上。
//                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
//            }
//            return isPermission;
//        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
