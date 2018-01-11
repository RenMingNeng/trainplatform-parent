package com.bossien.train.listener;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.common.AppConstant;
import com.bossien.train.domain.TrainSubject;
import com.bossien.train.domain.dto.TrainSubjectDTO;
import com.bossien.train.domain.dto.TrainSubjectMessage;
import com.bossien.train.service.ISubjectCourseService;
import com.bossien.train.service.ITrainSubjectService;
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
 * Created by Administrator on 2017/8/10.
 */

/**
 * 培训主题
 */
@Component("topicSubjectListener")
public class TopicSubjectListener extends AbstractListener {

    private static final Logger logger = LogManager.getLogger(TopicSubjectListener.class);
    @Autowired
    private ITrainSubjectService trainSubjectService;
    @Autowired
    private ISubjectCourseService subjectCourseService;
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
            logger.info("======TopicSubjectListener===培训主题监听器========" + json);
            Map<String, Object> maps = JsonUtils.readJson2Map(json);//接受数据将数据转成MAP
            if (!platform_id.equals(maps.get("platformCode"))) {
                return;
            }
            if (maps.get("cmdType").equals("add")) {//添加数据
                List<Map<String, String>> dataObj = (List<Map<String, String>>) maps.get("dataObj");
                TrainSubjectDTO dto = JsonUtils.readValue(json, TrainSubjectDTO.class);//将json数据转换成javabean
                List<TrainSubject> list_add = new ArrayList<>();//新增集合
                List<TrainSubject> list_update = new ArrayList<>();//更新集合

                for (TrainSubjectMessage trainSubjectMessage : dto.getDataObj()) {
                    TrainSubject trainSubjectItem = new TrainSubject();
                    trainSubjectItem.setVarId(trainSubjectMessage.getId());
                    trainSubjectItem.setSubjectName(trainSubjectMessage.getSubjectName());
                    trainSubjectItem.setSubjectDesc(trainSubjectMessage.getSubjectDesc());
                    trainSubjectItem.setSource(trainSubjectMessage.getSource());
                    trainSubjectItem.setIsValid(trainSubjectMessage.getIsValid());
                    trainSubjectItem.setCreateUser(trainSubjectMessage.getCreateUser());
                    trainSubjectItem.setOperUser(trainSubjectMessage.getOperUser());
                    trainSubjectItem.setCreateDate(trainSubjectMessage.getCreateDate());
                    trainSubjectItem.setOperDate(trainSubjectMessage.getOperDate());
                    trainSubjectItem.setLogo(trainSubjectMessage.getfId());
                    trainSubjectItem.setOrder(trainSubjectMessage.getOrder());
                    List<String> stringList = subjectCourseService.selectCourseIds(trainSubjectMessage.getId());
                    trainSubjectItem.setCourseCount(String.valueOf(stringList.size()));
                /*查看是否有数据*/
                    Map<String, Object> param = new HashMap<>();
                    param.put("varId", trainSubjectMessage.getId());
                    TrainSubject trainSubject = trainSubjectService.selectById(param);
                    if (null == trainSubject) {//添加
                        if (list_add.size() >= Integer.parseInt(insertBatchCount)) {
                            trainSubjectService.insertBatch(list_add);
                            list_add = new ArrayList<>();
                            logger.info("======TopicQuestionListener=" + trainSubjectMessage.getId() + "== subject add success==培训主题增加成功========");
                        } else {
                            list_add.add(trainSubjectItem);
                        }
                    } else {//更新
                        if (list_update.size() >= Integer.parseInt(updateBatchCount)) {
                            trainSubjectService.updateBatch(list_update);
                            list_update = new ArrayList<>();
                            logger.info("======TopicSubjectListener==" + trainSubjectMessage.getId() + "=subject update  success==培训主题更新成功========");
                        } else {
                            list_update.add(trainSubjectItem);
                        }
                    }
                }
                if (list_add.size() != 0) {
                    trainSubjectService.insertBatch(list_add);
                    logger.info("======TopicQuestionListener=== subject add success==培训主题增加成功========" + JSONObject.toJSONString(list_add));

                }
                if (list_update.size() != 0) {
                    trainSubjectService.updateBatch(list_update);
                    logger.info("======TopicSubjectListener==subject update  success==培训主题更新成功========" + JSONObject.toJSONString(list_update));

                }
            } else {
                Map<String, String> dataObj = (Map<String, String>) maps.get("dataObj");
                String[] ids = dataObj.get("id").replaceAll(" ", "").split(",");
                Map<String, Object> map = new HashMap<>();
                map.put("ids", ids);
                map.put("chrSource", system_number);
                trainSubjectService.deleteBySourse(map);
                for (String id : ids) {
                    subjectCourseService.deleteByTrainSubjectId(id);
                    logger.info("======TopicSubjectListener==Course_SubjectId:" + id + "=  SubjectCourse  delete success==培训课程关联关系删除成功========");
                }
                logger.info("======TopicSubjectListener==" + JSONObject.toJSONString(ids) + "=subject delete   success==培训主题删除成功========");
            }
        } catch (Exception e) {

            logger.error("======TopicSubjectListener==" +e.getMessage());

        }
    }
}
