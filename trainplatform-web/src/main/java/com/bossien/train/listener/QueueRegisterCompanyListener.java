package com.bossien.train.listener;

import static org.aspectj.bridge.Version.text;

import com.alibaba.fastjson.JSONObject;
import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.common.AppConstant;
import com.bossien.train.sender.QueueVerifySender;
import java.net.URLDecoder;
import java.text.ParseException;
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
@Component("queueRegisterCompanyListener")
public class QueueRegisterCompanyListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(QueueRegisterCompanyListener.class);
    @Autowired
    private QueueVerifySender queueVerifySender;
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    @Override
    @SuppressWarnings("unchecked")
    public <V> void mapMessageHander(Map<String, V> map) throws ParseException {
        try {
            if (StringUtils.isEmpty(text)) {
                return;
            }
            String json = URLDecoder.decode(text, AppConstant.DEFAULT_CHARSET);//首先对数据进行格式转码
            logger.info("=======QueueCompanyListener==公司类型监听器========" + json);
            String companyName = (String) map.get("companyName");
            String regionName = (String) map.get("regionName");
            String industryType = (String) map.get("industryType");
            String socialCreditCode = (String) map.get("socialCreditCode");
            String userId = (String) map.get("userId");
            String userName = (String) map.get("userName");
            String userAccount = (String) map.get("userAccount");
            String mobileNo = (String) map.get("mobileNo");
            String openId = (String) map.get("openId");
            this.SendSuccess((Map<String, Object>) map);
        } catch (Exception e) {
            logger.error("=======QueueRegisterCompanyListener==");
            e.printStackTrace();
        }

    }

    private void SendSuccess(Map<String, Object> map) {
             /*发送消息*/
        map.put("platformCode", "001");
        map.put("cmdType", "ok");
        EXECUTOR.execute(new QueueRegisterCompanyListener.VerifyDeal(map));//发送消息
        logger.info("=======QueueRegisterCompanyListener==message success 发送验证消息成功 ========");
    }


    /**
     * 异步处理数据
     */
    private class VerifyDeal extends Thread {

        private Map<String, Object> queueMap;

        private VerifyDeal(Map<String, Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queueVerifySender.sendTextMessage("op.apTxCompanyRegister", JSONObject.toJSONString(queueMap));
            } catch (Exception ex) {
                logger.info(ex.getMessage(), ex);
            }
        }
    }
}
