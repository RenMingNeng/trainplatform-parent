package com.bossien.train.controller;

import com.bossien.framework.shiro.session.ShiroSession;
import com.bossien.train.domain.OnlineUser;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.util.PropertiesUtils;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/session")
public class SessionController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(SessionController.class);
    private static final String REDIS_SHIRO_SESSION = PropertiesUtils.getValue("REDIS_SHIRO_SESSION");
    private static final String REDIS_SHIRO_SESSION_ATTR_ONLINE_USER = PropertiesUtils.getValue("REDIS_SHIRO_SESSION_ATTR_ONLINE_USER");

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("")
    public String index(
            HttpServletRequest request,
            @RequestParam(value="sessionType", required = false, defaultValue = "") String sessionType
        ) {

        addParam(request, "sessionType", sessionType);
        return "session/index";

    }

    @RequestMapping("/list")
    @ResponseBody
    public Object list() throws ServiceException {

        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());

        ValueOperations<String, ShiroSession> valueOperations = redisTemplate.opsForValue();
        Set<String> sessions = redisTemplate.keys(REDIS_SHIRO_SESSION + "*");
        Set<ShiroSession> sessions_1 = Sets.newConcurrentHashSet(); // 全部
        Set<ShiroSession> sessions_2 = Sets.newConcurrentHashSet(); // 在线
        Set<ShiroSession> sessions_3 = Sets.newConcurrentHashSet(); // 游客
        OnlineUser onlineUser = null;
        ShiroSession shiroSession = null;
        if(null != sessions && !sessions.isEmpty()) {
            for(String redisKey : sessions) {
                shiroSession = valueOperations.get(redisKey);
                if(null == shiroSession) {
                    continue;
                }
                sessions_1.add(shiroSession);
                onlineUser = (OnlineUser) shiroSession.getAttribute(REDIS_SHIRO_SESSION_ATTR_ONLINE_USER);
                if(null != onlineUser) {
                    sessions_2.add(shiroSession); continue;
                } else {

                    sessions_3.add(shiroSession);
                }
            }
        }
        Map<String, Object> params = Maps.newConcurrentMap();
        params.put("sessions_1", sessions_1);
        params.put("sessions_2", sessions_2);
        params.put("sessions_3", sessions_3);
        resp.setResult(params);

        return resp;
    }

}
