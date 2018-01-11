package com.bossien.train.listener;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.common.AppConstant;
import com.bossien.train.domain.CompanyTrainSubject;
import com.bossien.train.service.ICompanyTrainSubjectService;
import com.bossien.train.util.JsonUtils;
import com.bossien.train.util.PropertiesUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/8/28.
 * 公司和培训主题关联关系
 */

@Component("topicCompanyTrainSubjectListener")
public class TopicCompanyTrainSubjectListener extends AbstractListener {

    private static final Logger logger = LogManager.getLogger(TopicSubjectListener.class);
    @Autowired
    private ICompanyTrainSubjectService companyTrainSubjectService;

    private String platform_id = PropertiesUtils.getValue("PLATFORM_ID");//获取配置文件
    private String system_number = PropertiesUtils.getValue("SYSTEM_NUMBER");//获取配置文件
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
            logger.info("=======topicCompanyTrainSubjectListener==单位和培训主题关联监听器========" + json);
            Map<String, Object> maps = JsonUtils.readJson2Map(json);
            logger.info("======TopicCompanyTrainSubjectListener===单位和培训主题关联监听器========" + maps.get("dataObj"));
            List<Map<String, String>> dataObj = (List<Map<String, String>>) maps.get("dataObj");
            if (!platform_id.equals(maps.get("platformCode"))) {
                return;
            }
            if (maps.get("cmdType").equals("add")) {
                List<CompanyTrainSubject> list_add = new ArrayList<>();//新增集合
                for (Map<String, String> objectMap : dataObj) {//添加数据
                    String companyId = String.valueOf(objectMap.get("companyId"));
                    String[] ids = String.valueOf(objectMap.get("id")).replaceAll(" ", "").split(",");
                    Map<String, Object> map = new HashMap<>();
                    for (String id : ids) {
                        map.put("trainSubjectId", id);
                        map.put("intCompanyId", companyId);
                        CompanyTrainSubject companyTrainSubjects = companyTrainSubjectService.selectById(map);
                        if (null == companyTrainSubjects) {//没有数据 进行添加
                            CompanyTrainSubject record = new CompanyTrainSubject();
                            record.setIntCompanyId(companyId);
                            record.setIntTrainSubjectId(id);
                            if (list_add.size() >= Integer.parseInt(insertBatchCount)) {
                                companyTrainSubjectService.insertBatch(list_add);
                                list_add = new ArrayList<>();
                                logger
                                    .info("======TopicCompanyTrainSubjectListener=CompanyTrainSubject_id:" + id
                                        + "== CompanySubject add success ==培训主题和单位关联关系增加成功========");
                            } else {
                                list_add.add(record);
                            }
                        } else {
                            logger.info("======TopicCompanyTrainSubjectListener==CompanyTrainSubject_id:" + id
                                + "= CompanySubject update success==培训主题和单位关联关系已经存在========");
                        }
                    }
                }
                if (list_add.size() != 0) {
                    companyTrainSubjectService.insertBatch(list_add);
                    logger.info("======TopicCompanyTrainSubjectListener=== CompanySubject add success ==培训主题和单位关联关系增加成功========" + JSONObject
                        .toJSONString(list_add));
                }
            } else {
                for (Map<String, String> objectMap : dataObj) {//删除数据
                    Map<String, Object> params = new HashMap<>();
                    String companyId = String.valueOf(objectMap.get("companyId"));
                    String[] id = String.valueOf(objectMap.get("id")).replace(" ", "").split(",");
                    params.put("intCompanyId", companyId);
                    params.put("intTrainSubjectId", id);
                    companyTrainSubjectService.deleteByTrainSubjectId(params);
                    logger.info("======TopicCompanyTrainSubjectListener==companyId:" + companyId + "TrainSubjectId:=" + JSONObject.toJSONString(id)
                        + " CompanySubject delete success===培训主题和单位关联关系删除成功========");
                }
            }
        } catch (Exception e) {

            logger.error("======TopicCompanyTrainSubjectListener=="+e.getMessage());

        }
    }

}
