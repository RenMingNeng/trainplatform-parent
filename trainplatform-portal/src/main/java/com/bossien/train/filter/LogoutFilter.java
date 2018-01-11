package com.bossien.train.filter;

import com.bossien.train.domain.User;
import com.bossien.train.kit.BSTools;
import com.bossien.train.kit.BrowserClient;
import com.bossien.train.util.PropertiesUtils;
import com.bossien.train.util.SignUtils;
import com.google.common.collect.Maps;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/29.
 */
public class LogoutFilter extends org.apache.shiro.web.filter.authc.LogoutFilter {

    public static final Logger logger = LoggerFactory.getLogger(LogoutFilter.class);

    private static final String app_key = PropertiesUtils.getValue("app_key");
    private static final String app_code = PropertiesUtils.getValue("app_code");
    private static final String sso_switch = PropertiesUtils.getValue("sso_switch");
    private static final String sso_version = PropertiesUtils.getValue("sso_version");
    private static final String sso_logout_url = PropertiesUtils.getValue("sso_logout_url");
    private static final String session_user = PropertiesUtils.getValue("session_user");



    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    protected boolean preHandle(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response) throws Exception {

        if(isSupportSso(sso_switch)) {
            // 执行退出 sso方式
            handleBySso(request, response);
            return false;
        } else {
            // 执行退出 shiro方式
            handleByShiro(request, response);
            return false;
        }

    }

    private void handleBySso(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response) throws Exception{

        String path = request.getServletContext().getContextPath();;
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

        // 封装请求参数
        Map<String,String> params = Maps.newTreeMap();
        params.put("version", sso_version);
        params.put("appCode", app_code);
        params.put("returnUrl", /*URLEncoder.encode(*/basePath+"sso/login"/*, "UTF-8")*/);
        // SSO登出跳转URL
        StringBuilder logoutUrl = new StringBuilder(sso_logout_url).append("?");
        String key = "";
        String value = "";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            logoutUrl.append(key).append("=").append(null == value ? "" : value).append("&");
        }
        // 计算签名
        String signValue = SignUtils.getSignValue(params, app_key);
        logoutUrl.append("signValue").append("=").append(signValue);

        // 强制子系统退出，防止回调失败
        Subject subject = SecurityUtils.getSubject();
        logoutSubject(subject,request);
        // 调用统一平台的退出地址
        WebUtils.issueRedirect(request, response, logoutUrl.toString());
    }

    private void handleByShiro(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response) throws Exception{
        Subject subject = SecurityUtils.getSubject();
        String redirectUrl = getRedirectUrl(request, response, subject);
        logoutSubject(subject,request);
        // 退出
        issueRedirect(request, response, redirectUrl);
    }

    /**
     * subject 退出
     * @param subject
     */
    private void logoutSubject(Subject subject, javax.servlet.ServletRequest request) {
        try{

            // 获取user_id
            User user = (User) subject.getSession().getAttribute(session_user);
            if(null == user) {
                return;
            }
            String userId = user.getId();
            if(StringUtils.isEmpty(userId)) {
                return;
            }
            // 获取客户端标识
            BrowserClient browserClient = BSTools.parseClient((HttpServletRequest)request);
            String os = browserClient.getOs();
            if("windows".equalsIgnoreCase(os)) {
                redisTemplate.delete("app-online-windows-" + userId);
            } else if ("mac".equalsIgnoreCase(os)) {
                redisTemplate.delete("app-online-mac-" + userId);
            }

            subject.logout(); // 调用logout 从session中删除内容 下一次访问的时候就必须重新登录了
        } catch (SessionException ex) {
            logger.error("LogoutFilter session logout error", ex);
        }
    }

    // 检测是否开启sso
    private boolean isSupportSso(String ssoSwitch) {

        return "1".equals(ssoSwitch) || "true".equalsIgnoreCase(ssoSwitch) || "on".equalsIgnoreCase(ssoSwitch);
    }

}
