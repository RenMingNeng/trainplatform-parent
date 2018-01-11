package com.bossien.train.service.impl;

import com.bossien.train.service.ISequenceService;
import com.bossien.train.service.ISignService;
import com.bossien.train.util.PropertiesUtils;
import com.bossien.train.util.ShortUuid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2017/7/27.
 */
@Service
public class SignServiceImpl implements ISignService {

    private String key = PropertiesUtils.getValue("key");

    public static final Logger logger = LoggerFactory.getLogger(SignServiceImpl.class);

    @Override
    public String getSign(Map<String, String> sortMap) throws Exception {
        if (sortMap != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(key);
            int i = 0;
            for (Map.Entry<String, String> entry : sortMap.entrySet()) {
                builder.append(i).append(entry.getKey()).append("=").append(entry.getValue());
                i++;
            }
            builder.append(key);
            logger.info("加密前：" + builder.toString());
            String signV2 = DigestUtils.md5Hex(builder.toString()).toLowerCase();
            logger.info("签名值:" + signV2);
            return signV2;
        }
        return "";
    }
}

