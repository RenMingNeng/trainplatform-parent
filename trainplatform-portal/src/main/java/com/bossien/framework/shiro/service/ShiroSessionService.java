package com.bossien.framework.shiro.service;

import com.bossien.framework.shiro.repository.CachingShiroSessionDao;
import com.bossien.framework.shiro.session.ShiroSession;
import com.bossien.train.interceptor.LoginInterceptor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.*;

/**
 * 直接操作Session属性
 * 封装Session属性相关操作 Session属性发生改变时保存到Redis中并通知清除本地EhCache缓存
 */
public class ShiroSessionService implements MessageListener {

    private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    private CachingShiroSessionDao sessionDao;

    private RedisTemplate<String, Serializable> redisTemplate;

    private String unCacheChannel = "shiro.session.uncache";

    public void setSessionDao(CachingShiroSessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    public void setRedisTemplate(RedisTemplate<String, Serializable> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setUnCacheChannel(String unCacheChannel) {
        this.unCacheChannel = unCacheChannel;
    }

    /**
     * 发送缓存失效的消息
     */
    public void sendUnCacheSessionMessage(Serializable sessionId) {
        String nodeId = ManagementFactory.getRuntimeMXBean().getName();
        ShiroSessionMessage.MessageBody messageBody = new ShiroSessionMessage.MessageBody(sessionId, nodeId);
        redisTemplate.convertAndSend(unCacheChannel, messageBody);
    }


    public ShiroSession getSession() {
        return (ShiroSession) this.sessionDao.doReadSessionWithoutExpire(
                SecurityUtils.getSubject().getSession().getId()
        );
    }


    public void setId(final Serializable id) {
        ShiroSession session = this.getSession();
        session.setId(id);
        this.sessionDao.update(session);
        // 通过发布消息通知其他节点取消本地对session的缓存
        sendUnCacheSessionMessage(session.getId());
    }

    public void setStopTimestamp(final Date stopTimestamp) {
        ShiroSession session = this.getSession();
        session.setStopTimestamp(stopTimestamp);
        this.sessionDao.update(session);
        // 通过发布消息通知其他节点取消本地对session的缓存
        sendUnCacheSessionMessage(session.getId());
    }

    public void setExpired(final boolean expired) {
        ShiroSession session = this.getSession();
        session.setExpired(expired);
        this.sessionDao.update(session);
        // 通过发布消息通知其他节点取消本地对session的缓存
        sendUnCacheSessionMessage(session.getId());
    }

    public void setTimeout(final long timeout) {
        ShiroSession session = this.getSession();
        session.setTimeout(timeout);
        this.sessionDao.update(session);
        // 通过发布消息通知其他节点取消本地对session的缓存
        sendUnCacheSessionMessage(session.getId());
    }

    public void setHost(final String host) {
        ShiroSession session = this.getSession();
        session.setHost(host);
        this.sessionDao.update(session);
        // 通过发布消息通知其他节点取消本地对session的缓存
        sendUnCacheSessionMessage(session.getId());
    }

    public void setAttributes(final Map<Object, Object> attributes) {
        ShiroSession session = this.getSession();
        session.setAttributes(attributes);
        this.sessionDao.update(session);
        // 通过发布消息通知其他节点取消本地对session的缓存
        sendUnCacheSessionMessage(session.getId());
    }

    public Map<Object, Object> getAttributes() {
        return this.getSession().getAttributes();
    }

    public void setAttribute(final Object key,
                             final Object value) {
        ShiroSession session = this.getSession();
        session.setAttribute(key, value);
        this.sessionDao.update(session);
        // 通过发布消息通知其他节点取消本地对session的缓存
        sendUnCacheSessionMessage(session.getId());
    }

    public Object getAttribute(final Object key) {
        return this.getSession().getAttribute(key);
    }

    public Collection<Object> getAttributeKeys() {
        return this.getSession().getAttributeKeys();
    }

    public Object removeAttribute(final Object key) {
        ShiroSession session = this.getSession();
        Object res = session.removeAttribute(key);
        this.sessionDao.update(session);
        // 通过发布消息通知其他节点取消本地对session的缓存
        sendUnCacheSessionMessage(session.getId());
        return res;
    }

    /**
     * 在线会话的简单实现
     * 后续可以通过 keys统计数量，在通过SCAN 增量迭代取Session
     */
    /*@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getActiveSessions() {
        //List<Map<String, Object>> sessions = Lists.newLinkedList();
        List<Map<String, Object>> sessions = new LinkedList<>();
//        Collection<Session> activeSession = cachingShiroSessionDao.getActiveSessions();
//        for (Session session : activeSession) {
//            Map<String, Object> map = Maps.newHashMap();
//            SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
//
//            if (principalCollection != null) {
//                List<Object> listPrincipals = principalCollection.asList();
//                Map<String, Object> attributes = (Map<String, Object>) listPrincipals.get(1);
//                map.put("loginName", listPrincipals.get(0));
//                map.put("name", attributes.get("name"));
//                map.put("ip", attributes.get("ip"));
//            } else {
//                map.put("loginName", "未登录");
//                map.put("name", "未登录");
//                map.put("ip", "未登录");
//            }
//            map.put("startTimestamp", session.getStartTimestamp());
//            map.put("lastAccessTime", session.getLastAccessTime());
//            sessions.add(map);
//        }
        return sessions;
    }*/


    /**
     * 删除redis中的session同时删除ehCache中的session
     * @see ShiroSessionService#flushAll()
     */
    @Deprecated
    public void flushRedis() {
        Collection<Session> activeSession = sessionDao.getActiveSessions();
        if (activeSession != null) {
            for (Session session : activeSession) {
                try {
                    sessionDao.doDelete(session);
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 只清除EhCache中的session缓存
     */
    public void flushEhCache() {
        //Set<Session> sessions = Sets.newHashSet();
        Set<Session> sessions = new HashSet<>();
        Collection<Session> ehCacheActiveSession = sessionDao.getEhCacheActiveSessions();
        Collection<Session> activeSession = sessionDao.getActiveSessions();
        /*if (CollectionUtils.isNotEmpty(ehCacheActiveSession)) {
            sessions.addAll(ehCacheActiveSession);
        }
        if (CollectionUtils.isNotEmpty(activeSession)) {
            sessions.addAll(activeSession);
        }*/
        if (ehCacheActiveSession.size() > 0) {
            sessions.addAll(ehCacheActiveSession);
        }
        if (activeSession.size() > 0) {
            sessions.addAll(activeSession);
        }
        for (Session session : sessions) {
            try {
                sessionDao.unCache(session.getId());
            } catch (Exception e) {
            }
        }
        logger.info("flushEhCache Project EhCacheActiveSessions {} ", sessionDao.getEhCacheActiveSessions().size());
    }

    /**
     * 同时清除Redis和EhCache中的session
     */
    public void flushAll() {
        Collection<Session> activeSession = sessionDao.getActiveSessions();
        if (activeSession != null) {
            for (Session session : activeSession) {
                try {
                    sessionDao.delete(session);
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 收到缓存过期消息时清除EhCache中的Session缓存
     */
    @Override
    public void onMessage(final Message message, byte[] bytes) {
        ShiroSessionMessage shiroSessionMessage =
                new ShiroSessionMessage(message.getChannel(), message.getBody());
        logger.debug("channel {} , message {} ", shiroSessionMessage.getChannel(), shiroSessionMessage.msgBody);
        sessionDao.unCache(shiroSessionMessage.msgBody.sessionId);
    }
}
