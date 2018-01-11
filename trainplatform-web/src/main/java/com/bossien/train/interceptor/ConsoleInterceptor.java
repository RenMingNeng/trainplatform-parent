package com.bossien.train.interceptor;

import com.bossien.train.util.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hubo on 2015/8/10.
 */
public class ConsoleInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    private static final String console_auth_username = PropertiesUtils.getValue("console_auth_username");
    private static final String console_auth_password = PropertiesUtils.getValue("console_auth_password");

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object ohandler
        ) throws Exception {

        if(!checkHeaderAuth(request, response)) {
            response.setStatus(401);
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("WWW-authenticate", "Basic Realm=\"bossien\"");
            response.setDateHeader("Expires", 0);
            response.sendError(401, "Unauthorized");
            return false;
        }
        return true;
    }

    private boolean checkHeaderAuth(HttpServletRequest request, HttpServletResponse response) {
        String auth = request.getHeader("Authorization");

        if(null != auth && auth.length() > 6) {
            auth = auth.substring(6, auth.length());
            String decodedAuth = getFormBase64(auth);
            logger.debug("header auth is "+decodedAuth);
            if(null == decodedAuth) {
                return false;
            }
            if(console_auth_username.equals(decodedAuth.substring(0, decodedAuth.indexOf(":")))
                    && console_auth_password.equals(decodedAuth.substring(decodedAuth.indexOf(":")+1))) {
                return true;
            }
            return false;
        }
        return false;

    }

    private String getFormBase64(final String str) {
        if(null == str) {
            return null;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            return new String(decoder.decodeBuffer(str), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}
