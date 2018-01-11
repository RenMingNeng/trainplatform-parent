package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.common.AppConstant;
import com.bossien.train.domain.CompanyCourseType;
import com.bossien.train.domain.dto.CompanyCourseTypeDTO;
import com.bossien.train.domain.dto.CompanyCourseTypeMessage;
import com.bossien.train.service.ICompanyCourseTypeService;
import com.bossien.train.util.JsonUtils;
import com.bossien.train.util.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 课程类型的监听器
 */
@Component("topicCompanyCourseTypeListener")
public class TopicCompanyCourseTypeListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(TopicCompanyCourseTypeListener.class);

    private String platform_id = PropertiesUtils.getValue("PLATFORM_ID");//获取配置文件
    @Autowired
    private ICompanyCourseTypeService companyCourseTypeService;
    private String updateBatchCount = PropertiesUtils.getValue("updateBatchCount");//批量更新数量
    private String insertBatchCount = PropertiesUtils.getValue("insertBatchCount");//批量插入数量

    @Override
    @SuppressWarnings("unchecked")
    public void textMessageHander(String text) throws UnsupportedEncodingException {
        try {
            if (StringUtils.isEmpty(text)) {
                return;
            }
            String json = URLDecoder.decode(text, AppConstant.DEFAULT_CHARSET);//首先对数据进行格式转码
            logger.info("=======topicCompanyCourseTypeListener==课程类型的监听器========" + json);
            Map<String, Object> maps = JsonUtils.readJson2Map(json);
            logger.info("=======topicCompanyCourseTypeListener==课程类型的监听器========" + maps.get("platformCode"));
            if (!platform_id.equals(maps.get("platformCode"))) {
                return;
            }
            if (maps.get("cmdType").equals("add")) {
                CompanyCourseTypeDTO dto = JsonUtils.readValue(json, CompanyCourseTypeDTO.class);//将json数据转换成javabean
                List<CompanyCourseType> list_add = new ArrayList<>();//新增集合
                List<CompanyCourseType> list_update = new ArrayList<>();//更新集合
                for (CompanyCourseTypeMessage questionMessage : dto.getDataObj()) {
                    CompanyCourseType companyCourseType = companyCourseTypeService.selectById(questionMessage.getCompanyCourseTypeId());//通过编号查询是否有值
                    CompanyCourseType companyCourseTypeItem = new CompanyCourseType();
                    companyCourseTypeItem.setVarId(questionMessage.getCompanyCourseTypeId());
                    companyCourseTypeItem.setChrSource(questionMessage.getCompanyCourseTypeSource());
                    companyCourseTypeItem.setVarName(questionMessage.getCompanyCourseTypeName());
                    companyCourseTypeItem.setVarDesc(questionMessage.getCompanyCourseTypeDesc());
                    companyCourseTypeItem.setIntPid(questionMessage.getCompanyCourseTypePid());
                    companyCourseTypeItem.setIntOrder(questionMessage.getCompanyCourseTypeOrder());
                    companyCourseTypeItem.setIntLevel(questionMessage.getCompanyCourseTypeLevel());
                    companyCourseTypeItem.setChrStatus(questionMessage.getCompanyCourseTypeStatus());
                    companyCourseTypeItem.setIntCompanyId(questionMessage.getCompanyId());
                    companyCourseTypeItem.setDatCreateDate(questionMessage.getDatCreateDate());
                    companyCourseTypeItem.setDatOperDate(questionMessage.getDatOperDate());
                    companyCourseTypeItem.setVarCreateUser(questionMessage.getCreateUser());
                    companyCourseTypeItem.setVarOperUser(questionMessage.getOperUser());
                    if (null == companyCourseType) {//没有重复的数据
                        if (list_add.size() >= Integer.parseInt(insertBatchCount)) {
                            companyCourseTypeService.insertBatch(list_add);
                            list_add = new ArrayList<>();
                            logger.info("=======TopicCompanyCourseTypeListener===CompanyCourseType_id:" + companyCourseTypeItem.getVarId()
                                + "=  CompanyCourseType add success===单位课程类型关联增加成功========");
                        } else {
                            list_add.add(companyCourseTypeItem);
                        }
                    } else {//说明重复的数据
                        if (list_update.size() >= Integer.parseInt(updateBatchCount)) {
                            companyCourseTypeService.updateBatch(list_update);
                            list_update = new ArrayList<>();
                            logger.info("=======TopicCompanyCourseTypeListener===CompanyCourseType_id:" + companyCourseTypeItem.getVarId()
                                + "=  CompanyCourseType update success===单位课程类型关联更新成功========");
                        } else {
                            list_update.add(companyCourseTypeItem);
                        }
                    }
                }

                if (list_add.size() != 0) {
                    companyCourseTypeService.insertBatch(list_add);
                    logger.info("=======TopicCompanyCourseTypeListener==  CompanyCourseType add success==单位课程类型关联增加成功========");
                }
                if (list_update.size() != 0) {
                    companyCourseTypeService.updateBatch(list_update);
                    logger.info("=======TopicCompanyCourseTypeListener==  CompanyCourseType add success=单位课程类型关联增加成功========");
                }
            }
        } catch (Exception e) {
            logger.error("=======TopicCompanyCourseTypeListener=="+e.getMessage());
        }
    }
}
