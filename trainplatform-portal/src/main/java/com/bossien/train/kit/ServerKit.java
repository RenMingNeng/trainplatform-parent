package com.bossien.train.kit;

import com.bossien.train.interceptor.LoginInterceptor;
import com.bossien.train.util.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * Created by Administrator on 2017/8/24.
 */
public class ServerKit {

    private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
    private static final String server_id = PropertiesUtils.getValue("server_id");
    private static final String key_prefix_set_server = PropertiesUtils.getValue("key_prefix_set_server");

    public static boolean contextDestroyed(RedisTemplate redisTemplate) {

        if(StringUtils.isEmpty(server_id)) {
            return false;
        }
        try {
            redisTemplate.opsForSet().remove(key_prefix_set_server, server_id);
        } catch (Throwable t) {
            logger.error("缓存[" + key_prefix_set_server +"]失败,value[" + server_id + "]", t);
        }

        return false;
    }

    public static boolean contextInitialized(RedisTemplate redisTemplate) {

        try {
            redisTemplate.opsForSet().add(key_prefix_set_server, server_id);
            return true;
        } catch (Throwable t) {
            logger.error("移除[" + key_prefix_set_server +"]失败,value[" + server_id + "]", t);
        }

        return false;
    }

    public static Set<String> servers(RedisTemplate redisTemplate) {

        if(StringUtils.isEmpty(server_id)) {
            return null;
        }
        try {
            return redisTemplate.opsForSet().members(key_prefix_set_server);

        } catch (Throwable t) {
            logger.error("获取servers失败", t);
        }

        return null;
    }

}
