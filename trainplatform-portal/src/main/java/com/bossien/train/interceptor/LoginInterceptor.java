package com.bossien.train.interceptor;

import com.bossien.framework.event.Event001;
import com.bossien.framework.event.Event003;
import com.bossien.train.domain.User;
import com.bossien.train.exception.Code;
import com.bossien.train.kit.BSTools;
import com.bossien.train.kit.BrowserClient;
import com.bossien.train.service.IUserService;
import com.bossien.train.util.PropertiesUtils;
import com.bossien.train.util.RequestUtils;
import com.google.common.collect.Maps;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by hubo on 2015/8/8
 */
public class LoginInterceptor  implements HandlerInterceptor{

    private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
    @Autowired
    private IUserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 向客户端(浏览器)写入application version
        CookieGenerator cookieGenerator = new CookieGenerator();
        cookieGenerator.setCookieName(URLEncoder.encode("ver"));
        cookieGenerator.setCookiePath("/");
        String version = PropertiesUtils.getValue("version");
        cookieGenerator.addCookie(response,version);

        //向客户端（浏览器）写入准入管理平台url
        CookieGenerator cookieGenerator_url = new CookieGenerator();
        cookieGenerator_url.setCookieName(URLEncoder.encode("stbms_url"));
        cookieGenerator_url.setCookiePath("/");
        String stbms_url = PropertiesUtils.getValue("stbms_url");
        cookieGenerator_url.addCookie(response,stbms_url);

        if(request.getRequestURI().endsWith("/login")&&request.getMethod().equalsIgnoreCase("get") ||
                request.getRequestURI().endsWith("/")&&request.getMethod().equalsIgnoreCase("get")){

            if(SecurityUtils.getSubject().isAuthenticated()) {

                response.sendRedirect(request.getContextPath() + "/redirect");return false;
            }
            return true;
        }


        // 学员登陆
        if(request.getRequestURI().endsWith("/student")&&request.getMethod().equalsIgnoreCase("get")){
            // 触发登陆监听事件
            WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
            Map<String, Object> params = Maps.newHashMap();
            User user = (User)request.getSession().getAttribute(PropertiesUtils.getValue("session_user"));
            if(null != user) {
                params.put("companyId", user.getCompanyId());
                params.put("userId", user.getId());
                params.put("userName", user.getUserName());
                params.put("deptId", user.getDepartmentId());
                params.put("deptName", user.getDepartmentName());
                Event001 event001 = new Event001(params);
                wac.publishEvent(event001);
            }
            return true;
        }

        // 管理员登陆
        if(request.getRequestURI().endsWith("/admin")&&request.getMethod().equalsIgnoreCase("get")){
            // 触发登陆监听事件
            WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
            Map<String, Object> params = Maps.newHashMap();
            User user = (User)request.getSession().getAttribute(PropertiesUtils.getValue("session_user"));
            if(null != user) {
                params.put("companyId", user.getCompanyId());
                params.put("userName", user.getUserName());
                Event003 event003 = new Event003(params);
                wac.publishEvent(event003);
            }
            return true;
        }

        // ajax 会话过期处理
//        if(RequestUtils.isAjax(request)) {
//            response.sendError(Code.REQUEST_EXPIRE.getCode());return false;
//        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        if(-1 != request.getRequestURI().indexOf("/redirect") && request.getMethod().equalsIgnoreCase("get")) {// 登陆成功

            User user = (User)request.getSession().getAttribute(PropertiesUtils.getValue("session_user"));
            String lastLoginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            // user
            if(null == user) {
                return;
            }
            // user_id
            String userId = user.getId();
            if(StringUtils.isEmpty(userId)) {
                return;
            }
            // 更新用户最新登录时间
            userService.updateLastLoginTime(new User(userId, lastLoginTime));
            // 获取客户端标识
            BrowserClient browserClient = BSTools.parseClient(request);
            String os = browserClient.getOs();
            // 记录登陆
            if("windows".equalsIgnoreCase(os)) {
                redisTemplate.opsForValue().set("app-online-windows-" + userId, userId, 120 , TimeUnit.MINUTES);
            } else if ("mac".equalsIgnoreCase(os)) {
                redisTemplate.opsForValue().set("app-online-mac-" + userId, userId, 120, TimeUnit.SECONDS);
            }

        }

    }
}
