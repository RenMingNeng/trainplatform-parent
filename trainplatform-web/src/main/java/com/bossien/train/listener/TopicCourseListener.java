package com.bossien.train.listener;

import com.alibaba.fastjson.JSONObject;
import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.common.AppConstant;
import com.bossien.train.domain.Course;
import com.bossien.train.domain.CourseInfo;
import com.bossien.train.domain.CourseQuestion;
import com.bossien.train.domain.dto.CourseDTO;
import com.bossien.train.domain.dto.CourseMessage;
import com.bossien.train.service.ICourseInfoService;
import com.bossien.train.service.ICourseQuestionService;
import com.bossien.train.service.ICourseService;
import com.bossien.train.service.ISequenceService;
import com.bossien.train.util.JsonUtils;
import com.bossien.train.util.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/16.
 * <p>
 * 消息队列
 * 单位课程监听器
 */
@Component("topicCourseListener")
public class TopicCourseListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(TopicCourseListener.class);

    private String platform_id = PropertiesUtils.getValue("PLATFORM_ID");//获取配置文件
    @Autowired
    private ICourseService courseService;
    @Autowired
    private ICourseInfoService courseInfoService;
    @Autowired
    private ICourseQuestionService courseQuestionService;
    @Autowired
    private ISequenceService sequenceService;
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
            logger.info("=======TopicCourseListener==课程监听器========" + json);
            Map<String, Object> maps = JsonUtils.readJson2Map(json);
            logger.info("=======TopicCourseListener==课程监听器========" + "平台code" + maps.get("platformCode"));
            if (!platform_id.equals(maps.get("platformCode"))) {
                return;
            }
            if (maps.get("cmdType").equals("add")) {
                List<Map<String, String>> dataObj = (List<Map<String, String>>) maps.get("dataObj");
                CourseDTO dto = JsonUtils.readValue(json, CourseDTO.class);//将json数据转换成javabean
                Map<String, Object> params = new HashMap<String, Object>();
            /*Course*/
                List<Course> courseList_add = new ArrayList<>();//新增集合
                List<Course> courseList_update = new ArrayList<>();//更新集合
            /*CourseInfo*/
                List<Map<String, Object>> courseInfoList_add = new ArrayList<>();//新增集合
                List<Map<String, Object>> courseInfoList_update = new ArrayList<>();//更新集合

                for (CourseMessage courseMessage : dto.getDataObj()) {
                    Course courseItem = new Course();
                    courseItem.setIntCompanyId(courseMessage.getCourseCompanyId());
                    courseItem.setChrSource(courseMessage.getCourseSource());
                    courseItem.setIntId(courseMessage.getCourseId());
                    courseItem.setVarCode(courseMessage.getCourseCode());
                    courseItem.setIntTypeId(courseMessage.getCourseTypeId());
                    courseItem.setVarName(courseMessage.getCourseName());
                    courseItem.setVarDesc(courseMessage.getCourseDesc());
                    courseItem.setIntClassHour(courseMessage.getCourseClassHour());
                    courseItem.setVarCoverInfo(courseMessage.getCourseCoverInfo());
                    courseItem.setChrType(courseMessage.getCourse_Type());
                    courseItem.setChrPlatformType(courseMessage.getCoursePlatformType());
                    courseItem.setChrStatus(courseMessage.getCourseStatus());
                    courseItem.setDatCreateDate(courseMessage.getDatCreateDate());
                    courseItem.setDatOperDate(courseMessage.getDatOperDate());
                    courseItem.setVarCreateUser(courseMessage.getCreateUser());
                    courseItem.setVarOperUser(courseMessage.getOperUser());
                    params.put("intId", courseMessage.getCourseId());
                    Course course = courseService.selectId(params);//通过编号查询是否有值
                    if (null == course) {//说明没有重复的数据
                        if (courseList_add.size() >= Integer.parseInt(insertBatchCount)) {
                            courseService.insertBatch(courseList_add);
                            courseList_add = new ArrayList<>();
                            logger
                                .info("=======TopicCourseListener=course_id" + courseMessage.getCourseId() + "= Course  add success==课程增加成功========");

                        } else {
                            courseList_add.add(courseItem);
                        }
                    } else {
                        if (courseList_update.size() >= Integer.parseInt(updateBatchCount)) {
                            courseService.updateBatch(courseList_update);
                            courseList_update = new ArrayList<>();
                            logger.info(
                                "=======TopicCourseListener=course_id" + courseMessage.getCourseId() + "=Course  update success==课程更新成功========");
                        } else {
                            courseList_update.add(courseItem);

                        }
                    }
               /*======================================================================================================*/
                /*CourseInfo*/
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", sequenceService.generator());
                    map.put("courseId", courseMessage.getCourseId());
                    map.put("intCourseId", courseMessage.getCourseId());
                    map.put("courseNo", courseMessage.getCourseCode());
                    map.put("courseName", courseMessage.getCourseName());
                    map.put("courseTypeId", courseMessage.getCourseTypeId());
                    map.put("courseTypeName", courseMessage.getCourse_Type());
                    map.put("questionCount", courseMessage.getCourseQuestionNumber());
        /*        *//*查询试题数量*//*
                    Integer questionCount = courseQuestionService.selectIntQuestionIdCount(map);//查询课程下的多少题目
                    map.put("questionCount", questionCount);*/
                    map.put("classHour", courseMessage.getCourseClassHour());
                    map.put("createUser", courseMessage.getCreateUser());
                    map.put("createTime", courseMessage.getDatCreateDate());
                    map.put("operUser", courseMessage.getOperUser());
                    map.put("operTime", courseMessage.getDatOperDate());
                    CourseInfo courseInfo = courseInfoService.selectCourseInfoByCourseId(courseMessage.getCourseId());
                    if (null == courseInfo) {//没有数据添加数据
                        if (courseInfoList_add.size() >= Integer.parseInt(insertBatchCount)) {
                            courseInfoService.insertBatch(courseInfoList_add);
                            courseInfoList_add = new ArrayList<>();
                            logger.info("=======TopicCourseListener=course_info_course_id:" + courseMessage.getCourseId()
                                + "=CourseInfo  add success========");
                        } else {
                            courseInfoList_add.add(map);
                        }
                    } else {
                        if (courseInfoList_update.size() >= Integer.parseInt(updateBatchCount)) {
                            courseInfoService.updateBatch(courseInfoList_update);
                            courseInfoList_update = new ArrayList<>();
                            logger.info("=======TopicCourseListener=course_info_course_id:" + courseMessage.getCourseId()
                                + "=CourseInfo  update success==========");
                        } else {
                            courseInfoList_update.add(map);
                        }
                    }
                }
                if (courseInfoList_add.size() != 0) {
                    courseInfoService.insertBatch(courseInfoList_add);
                    logger.info("=======TopicCourseListener==== CourseInfo  add success==CourseInfo课程增加成功========" + JSONObject
                        .toJSONString(courseInfoList_add));
                }
                if (courseInfoList_update.size() != 0) {
                    courseInfoService.updateBatch(courseInfoList_update);
                    logger.info("=======TopicCourseListener==== CourseInfo  update success==CourseInfo课程更新成功========" + JSONObject
                        .toJSONString(courseInfoList_update));

                }
                if (courseList_add.size() != 0) {
                    courseService.insertBatch(courseList_add);
                    logger.info("=======TopicCourseListener=== Course  add success==课程增加成功========" + JSONObject.toJSONString(courseList_add));

                }
                if (courseList_update.size() != 0) {
                    courseService.updateBatch(courseList_update);
                    logger.info("=======TopicCourseListener=== Course  add success==课程更新成功========" + JSONObject.toJSONString(courseList_update));

                }
            }
        } catch (Exception e) {

            logger.error("=======TopicCourseListener=== " + e.getMessage());

        }
    }

}
