package com.bossien.train.listener;

import com.alibaba.druid.util.StringUtils;
import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.common.AppConstant;
import com.bossien.train.domain.SubjectCourse;
import com.bossien.train.domain.TrainSubject;
import com.bossien.train.service.ISequenceService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/10.
 */

/**
 * 培训课程关联关系表
 */
@Component("topicSubjectCourseListener")

public class TopicSubjectCourseListener extends AbstractListener {

    private static final Logger logger = LogManager.getLogger(TopicSubjectCourseListener.class);
    @Autowired
    private ISubjectCourseService subjectCourseService;
    @Autowired
    private ITrainSubjectService trainSubjectService;
    private String system_number = PropertiesUtils.getValue("SYSTEM_NUMBER");//获取配置文件
    private String updateBatchCount = PropertiesUtils.getValue("updateBatchCount");//批量更新数量
    private String insertBatchCount = PropertiesUtils.getValue("insertBatchCount");//批量插入数量
    private String platform_id = PropertiesUtils.getValue("PLATFORM_ID");//获取配置文件
    @Autowired
    private ISequenceService sequenceService;

    @SuppressWarnings("unchecked")
    @Override
    public void textMessageHander(String text) throws UnsupportedEncodingException {
        try {
            if (StringUtils.isEmpty(text)) {
                return;
            }
//
            String json = URLDecoder.decode(text, AppConstant.DEFAULT_CHARSET);//首先对数据进行格式转码
            logger.info("======TopicSubjectCourseListener===培训主题课程主题监听器========" + json);
            Map<String, Object> maps = JsonUtils.readJson2Map(json);//接受数据将数据转成MAP

            if (!platform_id.equals(maps.get("platformCode"))) {
                return;
            }
            if (maps.get("cmdType").equals("add")) {//添加数据
                List<Map<String, String>> dataObj = (List<Map<String, String>>) maps.get("dataObj");
                for (Map map : dataObj) {
                    String subjectId = map.get("subjectId").toString();
                    if (null == subjectId) {
                        return;
                    }
                    subjectCourseService.deleteByTrainSubjectId(subjectId);
                    logger.info("======TopicSubjectCourseListener===主题与课程关联关系表  根据主题id进行删除===delete=subjectId：==" + subjectId);
                    String[] courseIds = map.get("courseIds").toString().replaceAll(" ", "").split(",");
                    SubjectCourse subjectCourse = new SubjectCourse();
                    for (String corseid : courseIds) {
                        if (null != corseid && !corseid.equals("")) {
                            subjectCourse.setIntCourseId(corseid);
                            subjectCourse.setIntTrainSubjectId(subjectId);
                            subjectCourse.setIntId(sequenceService.generator());
                            subjectCourseService.insertSelective(subjectCourse);
                        }
                        logger.info("======TopicSubjectCourseListener==course_id:" + corseid + "==subjectId:=" + subjectId
                            + " SubjectCourse  add success==培训主题课程关联关系添加成功======");
                    }
                   /*修改数量培训主题关联的课程数量*/
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("courseCount", courseIds.length);
                    params.put("varId", subjectId);
                    params.put("chrSource", system_number);
                    trainSubjectService.update(params);
                    logger.info("======TopicSubjectCourseListener==主题关联的课程数量是==SubjectCourse.length====" + courseIds.length);
                }
            }
        } catch (Exception e) {

            logger.error("======TopicSubjectCourseListener=="+e.getMessage());

        }
    }

}
