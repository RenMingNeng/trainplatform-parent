package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.common.AppConstant;
import com.bossien.train.sender.QueueVerifySender;
import com.bossien.train.service.IMessageService;
import com.bossien.train.util.JsonUtils;
import com.bossien.train.util.PropertiesUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 按需复写父类方法，真实业务请直接继承AbstractListener
 * que 监听器
 *
 * @author DF
 */
@Component("queueCompanyMessageListener")
public class QueueMessageListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(QueueMessageListener.class);
    @Autowired
    private IMessageService messageService;
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    private String platform_id = PropertiesUtils.getValue("PLATFORM_ID");//获取配置文件

    @Override
    @SuppressWarnings("unchecked")
    public void textMessageHander(String text) throws UnsupportedEncodingException {
        try {
            if (StringUtils.isEmpty(text)) {
                return;
            }
            String json = URLDecoder.decode(text, AppConstant.DEFAULT_CHARSET);//首先对数据进行格式转码
            Map<String, Object> maps = JsonUtils.readJson2Map(json);
            logger.info("=======QueueMessageListener==========" + maps.get("platformCode"));
            if (!platform_id.equals(maps.get("platformCode"))) {
                return;
            }
            Map<String, Object> map = (Map<String, Object>) maps.get("dataObj");
            map.put("cmdType", maps.get("cmdType"));
            messageService.sendMessageToUser(map);
            logger.info("单位注册审核消息发送成功");
        } catch (Exception e) {
            logger.error("=======QueueRegisterCompanyListener==");
            e.printStackTrace();
        }

    }


}
