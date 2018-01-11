package com.bossien.framework.shiro.repository;

import com.bossien.train.interceptor.LoginInterceptor;
import com.bossien.train.util.PropertiesUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 管理Redis中的Session
 */
public class ShiroSessionRepository {

    private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
    private static final String REDIS_SHIRO_SESSION = PropertiesUtils.getValue("REDIS_SHIRO_SESSION");
    private static final int SESSION_VAL_TIME_SPAN = 1800;

    // 保存到Redis中key的前缀 prefix+sessionId
    private String redisShiroSessionPrefix = REDIS_SHIRO_SESSION;

    // 设置会话的过期时间
    private int redisShiroSessionTimeout = SESSION_VAL_TIME_SPAN;

    private RedisTemplate<String, Session> redisTemplate;

    public void setRedisShiroSessionPrefix(String redisShiroSessionPrefix) {
        this.redisShiroSessionPrefix = redisShiroSessionPrefix;
    }

    public void setRedisShiroSessionTimeout(int redisShiroSessionTimeout) {
        this.redisShiroSessionTimeout = redisShiroSessionTimeout;
    }

    public RedisTemplate<String, Session> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Session> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 保存session
     */
    public void saveSession(final Session session) {
        try {
            redisTemplate.opsForValue()
                    .set(
                            buildRedisSessionKey(
                                    session.getId()
                            )
                            , session
                            , redisShiroSessionTimeout
                            , TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("save session to redis error");
        }
    }

    /**
     * 更新session
     */
    public void updateSession(final Session session) {
        try {
            redisTemplate.boundValueOps(
                    buildRedisSessionKey(
                            session.getId()
                    )
            ).set(session
                    , redisShiroSessionTimeout
                    , TimeUnit.SECONDS
            );
        } catch (Exception e) {
            logger.error("update session error");
        }
    }


    /**
     * 刷新session
     */
    public void refreshSession(final Serializable sessionId) {
        redisTemplate.expire(
                buildRedisSessionKey(sessionId)
                , redisShiroSessionTimeout
                , TimeUnit.SECONDS
        );
    }


    /**
     * 删除session
     */
    public void deleteSession(final Serializable id) {
        try {
            redisTemplate.delete(buildRedisSessionKey(id));
        } catch (Exception e) {
            logger.error("delete session error");
        }
    }


    /**
     * 获取session
     */
    public Session getSession(final Serializable id) {
        Session session = null;
        try {
            session = redisTemplate.boundValueOps(buildRedisSessionKey(id)).get();
        } catch (Exception e) {
            logger.error("get session error");
        }
        return session;
    }

    /**
     * 通过sessionId获取sessionKey
     */
    private String buildRedisSessionKey(final Serializable sessionId) {
        return redisShiroSessionPrefix + sessionId;
    }

    private String buildRedisSessionKey2(final String serverId) {
        return redisShiroSessionPrefix + serverId;
    }
}
