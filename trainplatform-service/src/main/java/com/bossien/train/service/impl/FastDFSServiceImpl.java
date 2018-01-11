package com.bossien.train.service.impl;

import com.bossien.train.service.IFastDFSService;
import com.bossien.train.util.MD5Utils;
import com.bossien.train.util.PropertiesUtils;
import org.springframework.stereotype.Service;

/**
 * Created by huangzhaoyong on 2017/7/25.
 */

@Service
public class FastDFSServiceImpl implements IFastDFSService{
    /**
     * media 服务 域名加端口
     */
    public static String MEDIA_NGINX_DOMAIN_PORT = PropertiesUtils.getValue("media.nginx.domain.port");
    /**
     * media 服务 给应用分配的ID
     */
    public static String MEDIA_APPID = PropertiesUtils.getValue("media.appId");
    /**
     * 防盗链秘钥
     */
    public static String ANTI_STEAL_LINK_KEY = PropertiesUtils.getValue("antiStealLink.key");
    /**
     * 防盗链超时时间 秒
     */
    public static int ANTI_STEAL_TIMEOUT = Integer.valueOf(PropertiesUtils.getValue("antiStealLink.timeOut"));


    @Override
    public String getURLToken(String fid) {
        long expiresLong = System.currentTimeMillis() / 1000L + ANTI_STEAL_TIMEOUT;
        String expires = String.valueOf(expiresLong);
        String token = MD5Utils.encoderByMd5With32Bit(ANTI_STEAL_LINK_KEY + fid + expires);
        return MEDIA_NGINX_DOMAIN_PORT + "/play.do?appId=" + MEDIA_APPID + "&srcFid=" + fid + "&expires=" + expires + "&token=" + token;
    }
}
