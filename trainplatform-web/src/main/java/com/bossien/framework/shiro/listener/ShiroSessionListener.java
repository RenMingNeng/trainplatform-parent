package com.bossien.framework.shiro.listener;

import com.bossien.framework.shiro.repository.CachingShiroSessionDao;
import com.bossien.framework.shiro.service.ShiroSessionService;
import com.bossien.train.interceptor.LoginInterceptor;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShiroSessionListener implements SessionListener {

    private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    private ShiroSessionService shiroSessionService;

    private CachingShiroSessionDao sessionDao;

    public void setShiroSessionService(ShiroSessionService shiroSessionService) {
        this.shiroSessionService = shiroSessionService;
    }

    public void setSessionDao(CachingShiroSessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    @Override
    public void onStart(final Session session) {
        // 会话创建时触发
        logger.info("session {} onStart", session.getId());
    }

    @Override
    public void onStop(final Session session) {
        sessionDao.delete(session);
        shiroSessionService.sendUnCacheSessionMessage(session.getId());
        logger.info("session {} onStop", session.getId());
    }

    @Override
    public void onExpiration(final Session session) {
        sessionDao.delete(session);
        shiroSessionService.sendUnCacheSessionMessage(session.getId());
        logger.info("session {} onExpiration", session.getId());
    }

}