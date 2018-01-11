package com.bossien.train.listener;

import com.bossien.train.interceptor.LoginInterceptor;
import com.bossien.train.kit.OnlineUserKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.*;

/**
 * Created by Administrator on 2017/8/7.
 */
public class SessionListener implements HttpSessionListener {

    private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

//    private RedisTemplate redisTemplate;

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        logger.info("-------------------------sessionCreated-------------------------");

        //this.initService();

        //OnlineUserKit.sessionCreated(httpSessionEvent.getSession(), redisTemplate);

    }

//    private void initService() {
//        redisTemplate = (RedisTemplate) SystemContextLoaderListener.getBean("redisTemplate");
//    }

    /**
     * session 失效事件
     * @param httpSessionEvent
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        logger.info("-------------------------sessionDestroyed-------------------------");

        //OnlineUserKit.sessionDestroyed(httpSessionEvent.getSession(), redisTemplate);
    }

}
