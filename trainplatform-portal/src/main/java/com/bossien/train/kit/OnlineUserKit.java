package com.bossien.train.kit;

import com.bossien.train.domain.OnlineUser;
import com.bossien.train.interceptor.LoginInterceptor;
import com.bossien.train.util.PropertiesUtils;
import com.google.gson.Gson;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/8/24.
 */
public class OnlineUserKit {

    private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
    private static final String REDIS_SHIRO_SESSION_ATTR_ONLINE_USER = PropertiesUtils.getValue("REDIS_SHIRO_SESSION_ATTR_ONLINE_USER");

    public static boolean online(HttpServletRequest request) {

        OnlineUser onlineUser = BSTools.parseOnlineUser(request);
        if(null == onlineUser) {
            return false;
        }
        try {
            SecurityUtils.getSubject().getSession().setAttribute(REDIS_SHIRO_SESSION_ATTR_ONLINE_USER, onlineUser);
        } catch (Throwable t) {
            logger.error("OnlineUserKit online 调用失败, value[" + new Gson().toJson(onlineUser) + "]", t);
        }

        return false;
    }

}
