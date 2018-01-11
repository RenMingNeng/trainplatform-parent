package com.bossien.train.controller;

import com.bossien.train.exception.AlreadyAuthenticatedException;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.shiro.CustomizedToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @ResponseBody
    @RequestMapping("/signin")
    public Object signin(
                HttpServletRequest request,
                @RequestParam(value="userAccount", required = true, defaultValue = "") String userAccount,
                @RequestParam(value="userPasswd", required = true, defaultValue = "") String userPasswd,
                @RequestParam(value="userType", required = true, defaultValue = "") String userType
        ) throws ServiceException {

        Subject subject = SecurityUtils.getSubject();

        // 重复登录判断
        if(subject.isAuthenticated()) {
            throw new AlreadyAuthenticatedException();
        }

        // shiro login
        CustomizedToken token = new CustomizedToken(userAccount.trim(), userPasswd.trim(), isRemeberMe(request), request.getRemoteAddr(), userType, false);
        subject.login(token);

        return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

    private boolean isRemeberMe(HttpServletRequest request) {
//        String remeberMe = request.getParameter("remeberMe");
//        if("true".equalsIgnoreCase(remeberMe))
//            return true;
        return false;
    }

}
