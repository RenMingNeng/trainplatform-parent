package com.bossien.train.shiro;

import com.bossien.train.util.RequestUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2017/9/19.
 */
public class ShiroAccess extends FormAuthenticationFilter {

    private int HTTP_STATUS_SESSION_EXPIRE = 911; // session过期状态码

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);

        if(this.isLoginRequest(request, response)) {
            if(this.isLoginSubmission(request, response)) {
                return this.executeLogin(request, response);
            } else {
                // 放行 allow them to see the login page
                return true;
            }
        } else {
            if(RequestUtils.isAjax(httpRequest)) {
                httpResponse.sendError(HTTP_STATUS_SESSION_EXPIRE);
                return false;
            } else {
                this.saveRequestAndRedirectToLogin(request, response);
            }
        }
        return false;
    }


}
