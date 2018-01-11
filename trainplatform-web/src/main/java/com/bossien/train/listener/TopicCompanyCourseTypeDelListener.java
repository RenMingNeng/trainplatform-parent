package com.bossien.train.listener;

import com.alibaba.fastjson.JSONObject;
import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.common.AppConstant;
import com.bossien.train.domain.CompanyCourseType;
import com.bossien.train.domain.dto.CompanyCourseTypeDTO;
import com.bossien.train.domain.dto.CompanyCourseTypeMessage;
import com.bossien.train.service.ICompanyCourseTypeService;
import com.bossien.train.util.JsonUtils;
import com.bossien.train.util.PropertiesUtils;
import netscape.javascript.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/16.
 * <p>
 * 消息队列
 * 公司课程分类删除的监听器
 */
@Component("topicCompanyCourseTypeDelListener")
public class TopicCompanyCourseTypeDelListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(TopicCompanyCourseTypeDelListener.class);

    private String platform_id = PropertiesUtils.getValue("PLATFORM_ID");//获取配置文件
    @Autowired
    private ICompanyCourseTypeService companyCourseTypeService;
    private String updateBatchCount = PropertiesUtils.getValue("updateBatchCount");//批量更新数量
    private String insertBatchCount = PropertiesUtils.getValue("insertBatchCount");//批量插入数量

    @Override
    @SuppressWarnings("unchecked")
    public void textMessageHander(String text) throws UnsupportedEncodingException, JmsException {
        try {
            if (StringUtils.isEmpty(text)) {
                return;
            }
            String json = URLDecoder.decode(text, AppConstant.DEFAULT_CHARSET);//首先对数据进行格式转码
            logger.info("=======topicCompanyCourseTypeDelListener==公司课程分类删除的监听器========" + json);
            Map<String, Object> maps = JsonUtils.readJson2Map(json);
            logger.info("=======topicCompanyCourseTypeDelListener==公司课程分类删除的监听器========" + maps.get("platformCode"));
            if (!platform_id.equals(maps.get("platformCode"))) {
                return;
            }
            if (maps.get("cmdType").equals("delete")) {
                List<Map<String, Object>> dataObj = (List<Map<String, Object>>) maps.get("dataObj");
                companyCourseTypeService.deleteBatch(dataObj);
                logger.info("公司课程分类删除成功" + JSONObject.toJSONString(dataObj));
            }
        } catch (Exception e) {

            logger.error("=======topicCompanyCourseTypeDelListener"+e.getMessage());

        }
    }

}
