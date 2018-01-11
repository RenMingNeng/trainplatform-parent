package com.bossien.framework.utils;

import com.bossien.train.util.HttpClientUtils;
import com.bossien.train.util.MapUtils;
import com.bossien.train.util.PropertiesUtils;
import com.bossien.train.util.StringUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by A on 2017/12/15.
 */
public class SendJPushContentUtils {
    public static final Logger logger = LoggerFactory.getLogger(SendJPushContentUtils.class);

    /**
     * 注册应用的主密码，API主密码
     */
    private static final String MASTER_SECRET = PropertiesUtils.getValue("MASTER_SECRET");
    /**
     * 注册应用的APP_KEY
     */
    private static final String APP_KEY = PropertiesUtils.getValue("APP_KEY");

    /**
     * jpush接口请求地址
     */
    private static final String JPUSH_URL = PropertiesUtils.getValue("JPUSH_URL");
    /**
     * 是否发送
     */
    private static final String JPUSH_SWITCH = PropertiesUtils.getValue("JPUSH_SWITCH");

    /**
     * 0:管理员名称;1:项目创建日期;2:项目名称;3:培训时间;4:考试时间;
     */
    private static final String project_module = "{0}已于{1}建立了{2}。#{3}#{4} #请及时关注项目学习。#";

    /**
     * title
     */
    private static final String JPUSH_TITLE = "安培空间";

    /**
     * 发送消息
     * @param adminName 创建人
     * @param projectCreateTime 创建时间
     * @param projectName 项目名称
     * @param trainTime 培训时间
     * @param examTime 考试时间
     * @param alias 用户集合（用户id）
     */
    public static void toContent(String adminName, String projectCreateTime, String projectName,
                                 String trainTime, String examTime, List<String> alias){
        if(isSupportJPush(JPUSH_SWITCH)){
            Map<String, Object> data = MapUtils.newHashMap();
            if(!StringUtil.isEmpty(trainTime)){
                trainTime = "培训时间：" + trainTime;
            }
            if(!StringUtil.isEmpty(examTime)){
                examTime = " 考试时间：" + examTime;
            }
            //拼接内容
            String content = MessageFormat.format(project_module, adminName,
                    projectCreateTime, projectName, trainTime, examTime);
            data.put("title", JPUSH_TITLE);
            data.put("alias", alias);
            data.put("content", content);
            data.put("masterSecret", MASTER_SECRET);
            data.put("appKey", APP_KEY);
            String result = HttpClientUtils.doPostByJson(JPUSH_URL, new Gson().toJson(data));
            if(null == result || "".equals(result)){
                logger.error("SendJPushContentUtils-------args:"+new Gson().toJson(data) + "-----result:" + result);
            }
        }
    }

    // 检测是否开启
    private static boolean isSupportJPush(String jpush_switch) {

        return "1".equals(jpush_switch) || "true".equalsIgnoreCase(jpush_switch) || "on".equalsIgnoreCase(jpush_switch);
    }
}
