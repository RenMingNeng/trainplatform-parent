package com.bossien.train.util;


import com.bossien.train.common.AppConstant;

public class FastDFSUtil {
	
	 /**
     * 防盗链
     *
     * @return
     */
    public static String getHttpNginxURLWithToken(String srcFid) {
        long expiresLong = System.currentTimeMillis() / 1000L + AppConstant.ANTI_STEAL_TIMEOUT;
        String expires = String.valueOf(expiresLong);
        String token = MD5Utils.getMD5(AppConstant.ANTI_STEAL_LINK_KEY + srcFid + expires);
        return AppConstant.CONTENT_SERVER_HOST_PORT_WAN + "/play.do?appId=" + AppConstant.MEDIA_APPID + "&srcFid=" + srcFid + "&expires=" + expires + "&token=" + token;
    }

}
