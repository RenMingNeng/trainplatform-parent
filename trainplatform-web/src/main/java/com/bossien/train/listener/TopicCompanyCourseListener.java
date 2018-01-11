package com.bossien.train.listener;

import com.alibaba.fastjson.JSONObject;
import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.common.AppConstant;
import com.bossien.train.domain.CompanyCourse;
import com.bossien.train.domain.CourseAttachment;
import com.bossien.train.domain.CourseQuestion;
import com.bossien.train.domain.dto.CompanyCourseDTO;
import com.bossien.train.domain.dto.CompanyCourseMessage;
import com.bossien.train.sender.QueueVerifySender;
import com.bossien.train.service.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/8/16.
 * <p>
 * 消息队列
 * 单位课程关联表监听器
 */
@Component("topicCompanyCourseListener")
public class TopicCompanyCourseListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(TopicCompanyCourseListener.class);
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    @Autowired
    private ICompanyCourseService companyCourseService;
    @Autowired
    private ICourseService courseService;
    @Autowired
    private ICourseAttachmentService courseAttachmentService;
    @Autowired
    private IAttachmentService attachmentService;
    @Autowired
    private ICourseQuestionService courseQuestionService;
    @Autowired
    private IQuestionService questionService;
    @Autowired
    private QueueVerifySender queueVerifySender;
    @Autowired
    private ICourseInfoService courseInfoService;
    @Autowired
    private ISequenceService sequenceService;
    @Autowired
    private IProjectCourseService projectCourseService;

    private String platform_id = PropertiesUtils.getValue("PLATFORM_ID");//获取配置文件
    private String updateBatchCount = PropertiesUtils.getValue("updateBatchCount");//批量更新数量
    private String insertBatchCount = PropertiesUtils.getValue("insertBatchCount");//批量插入数量

    @Override
    @SuppressWarnings("unchecked")
    public void textMessageHander(String text) throws UnsupportedEncodingException {
          if (StringUtils.isEmpty(text)) {
                return;
            }
            String json = URLDecoder.decode(text, AppConstant.DEFAULT_CHARSET);//首先对数据进行格式转码
            logger.info("=======TopicCompanyCourseListener==单位与课程关联监听器========" + json);
            CompanyCourseDTO dto = JsonUtils.readValue(json, CompanyCourseDTO.class);//将json数据转换成javabean
            Map<String, Object> maps = JsonUtils.readJson2Map(json);
            logger.info("=======TopicCompanyCourseListener==单位与课程关联监听器========" + maps.get("platformCode"));
            if (!platform_id.equals(maps.get("platformCode"))) {
                return;
            }
            if (maps.get("cmdType").equals("add")) {
                Map<String, Object> params = new HashMap<String, Object>();
                List<CompanyCourse> list_add = new ArrayList<>();//新增集合
                List<CompanyCourse> list_update = new ArrayList<>();//更新集合

                Map<String, String> courseRefMap = new HashMap();
                if (dto.getDataObj() != null && dto.getDataObj().size() > 0) {
                    params.put("intCompanyId", dto.getDataObj().get(0).getCompanyId());
                    courseRefMap = companyCourseService.selectRefByCompanyId(params);
                }

                for (CompanyCourseMessage companyCourseMessage : dto.getDataObj()) {
//                params.put("intCourseId", companyCourseMessage.getCourseId());
//                params.put("intCompanyCourseTypeId", companyCourseMessage.getCompanyCourseTypeId());
//                params.put("intCompanyId", companyCourseMessage.getCompanyId());
//                CompanyCourse companyCourse = companyCourseService.selectById(params);//通过编号查询是否有值

                    CompanyCourse companyCourseItem = new CompanyCourse();
                    companyCourseItem.setIntCompanyCourseTypeId(companyCourseMessage.getCompanyCourseTypeId());
                    companyCourseItem.setIntCompanyId(companyCourseMessage.getCompanyId());
                    companyCourseItem.setVarId(sequenceService.generator());
                    companyCourseItem.setIntCourseId(companyCourseMessage.getCourseId());
                    if (!courseRefMap.containsKey(
                        companyCourseMessage.getCompanyCourseTypeId().toString() + "_" + companyCourseMessage.getCourseId().toString())) {//说明没有重复的数据
                        if (list_add.size() >= Integer.parseInt(insertBatchCount)) {
                            companyCourseService.insertBatch(list_add);
                            list_add = new ArrayList<>();
                            logger.info("=======TopicCompanyCourseListener===CompanyCourse_id:" + companyCourseItem.getIntCourseId()
                                + "=  CompanyCourse add success===单位课程类型关联增加成功========");
                        } else {
                            list_add.add(companyCourseItem);
                        }
                    } else {//有重复的数据
                        logger.info("=======TopicCompanyCourseListener===CompanyCourse_id:" + companyCourseMessage.getCourseId()
                            + " CompanyCourse update success===单位课程类型关联关系有数据========");
                    }
                }

                if (list_add.size() != 0) {
                    companyCourseService.insertBatch(list_add);
                    logger.info(
                        "=======TopicAttachmentListener==  CompanyCourse add success===单位课程类型关联增加成功========" + JSONObject.toJSONString(list_add));
                }

/*删除*/
        } else{
            /*删除的话不需要删除基础数据只需要删除公司与课程的关联关系*/
            List<Map<String, String>> dataObj = (List<Map<String, String>>) maps.get("dataObj");
            try {
                for (Map<String, String> param : dataObj) {
                    Map<String, Object> par = new HashMap<>();
                    String companyId = String.valueOf(param.get("companyId"));//公司id
                    String courseId = String.valueOf(param.get("courseId"));//课程id
                    par.put("intCompanyId", companyId);
                    par.put("intCourseId", courseId);
                    int count = projectCourseService.courseCount(par);
                    if (count >= 1) {//说明有项目在使用
                        this.SendFail(dataObj);
                        logger.info("=======课程正在使用无法删除 ========");
                    } else {
                        companyCourseService.deleteByPrimaryKey(par);
                        logger.info("=======TopicCompanyCourseListener=courseId:" + courseId + "companyId:" + companyId
                            + "= CompanyCourse   delete success 单位课程关联删除成功========");
                        this.SendSuccess(dataObj);
                    }

                }


            } catch (Exception e) {
                this.SendFail(dataObj);
                logger.info("=======TopicCourseListener==出现错误========" + e.getMessage());
            }
        }
           /* try {
               *//* 删除单位课程关联关系   删除课程  删除courseInfo表  删除课件 删除课件关联关系  删除题库 删除题库关联*//*
                for (Map<String, String> param : dataObj) {
                    String companyId = String.valueOf(param.get("companyId"));//公司id
                    String courseId = String.valueOf(param.get("courseId"));//课程id
                    Map<String, Object> par = new HashMap<>();
                    par.put("intCompanyId", companyId);
                    par.put("intCourseId", courseId);
                    *//*先查询是否有多个公司在用课程*//*
                    if (companyCourseService.selectIntCourseIdCount(par) == 1) {
                        *//*课程删除*//*
                        courseService.deleteByPrimaryKey(par);
                        *//*删除CourseInfo*//*
                        courseInfoService.deleteByCourseId(par);
                        logger.info("=======TopicCompanyCourseListener=" + courseId + "=Course and CourseInfo delete success==课程删除成功========");
                    } else {
                        logger.info("=======TopicCompanyCourseListener==" + courseId + "Course and CourseInfo have a value ==unable to delete===课程无法删除其他单位在用========");
                    }


                    *//*删除单位课程关联*//*
                    companyCourseService.deleteByPrimaryKey(par);
                    logger.info("=======TopicCompanyCourseListener=" + courseId + "= CompanyCourse   delete success 单位课程关联删除成功========");

                       *//*附件删除*//*
                    List<CourseAttachment> courseAttachments = courseAttachmentService.selectList(par);//通过课程id查询所有的附件id
                    if (null != courseAttachments && courseAttachments.size() > 0) {
                        for (CourseAttachment courseAttachment : courseAttachments) {
                            String attachmentId = courseAttachment.getIntAttachmentId();
                            par.put("intAttachmentId", attachmentId);
                            if (courseAttachmentService.selectIntAttachmentIdCount(par) == 1) {
                                attachmentService.deleteByPrimaryKey(par);
                                logger.info("=======TopicCompanyCourseListener=" + attachmentId + "= Attachment  delete success 附件删除成功========");
                            } else {
                                logger.info("=======TopicCompanyCourseListener=" + attachmentId + "=Attachment have a value ==unable to delete附件无法删除被其他课程使用========");
                            }
                        }
                    }

                    *//*课程附件关联删除*//*
                    courseAttachmentService.deleteByPrimaryKey(par);
                    logger.info("=======TopicCompanyCourseListener== " + courseId + "==AttachmentCourse relation delete success课程附件关联删除成功========");


                      *//*课程题库删除*//*
                    List<CourseQuestion> courseQuestions = courseQuestionService.selectList(par);//通过课程id查询所有题库id
                    if (courseQuestions != null && courseQuestions.size() > 0) {
                        for (CourseQuestion courseQuestion : courseQuestions) {
                            par.put("intQuestionId", courseQuestion.getIntQuestionId());
                            if (courseQuestionService.selectIntQuestionIdCount(par) == 1) {
                                questionService.deleteByPrimaryKey(par);//删除课程id
                                logger.info("=======TopicCourseListener=" + courseQuestion.getIntQuestionId() + "=  Question delete success 课程题库删除成功========");
                            } else {
                                logger.info("=======TopicCourseListener=" + courseQuestion.getIntQuestionId() + "= Question have a value ==unable to delete==题库无法删除，其他课程在用========");
                            }
                        }
                    }
                   *//*删除题库关联关系*//*
                    courseQuestionService.deleteByPrimaryKey(par);
                    logger.info("=======TopicCompanyCourseListener=" + courseId + "= QuestionCourse relation delete success 删除题库关联关系成功========");

                }
                   *//*发送消息*//*
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("platformCode", "001");
                objectMap.put("cmdType", "ok");
                objectMap.put("dataObj", dataObj);
                EXECUTOR.execute(new TopicCompanyCourseListener.VerifyDeal(objectMap));//发送消息
                logger.info("=======TopicCourseListener==message success 发送验证消息成功 ========");

            } catch (Exception e) {
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("platformCode", "001");
                objectMap.put("cmdType", "error");
                objectMap.put("dataObj", dataObj);
                EXECUTOR.execute(new TopicCompanyCourseListener.VerifyDeal(objectMap));//发送消息
                logger.info("=======TopicCourseListener== message success 发送验证消息   fail 任务没有删除完成，发生错误 ========" + e.getMessage());
            }
        }*/

    }

    private void SendSuccess(List<Map<String, String>> dataObj) {
             /*发送消息*/
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("platformCode", "001");
        objectMap.put("cmdType", "ok");
        objectMap.put("dataObj", dataObj);
        EXECUTOR.execute(new TopicCompanyCourseListener.VerifyDeal(objectMap));//发送消息
        logger.info("=======TopicCourseListener==message success 发送验证消息成功 ========");
    }

    private void SendFail(List<Map<String, String>> dataObj) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("platformCode", "001");
        objectMap.put("cmdType", "error");
        objectMap.put("dataObj", dataObj);
        EXECUTOR.execute(new TopicCompanyCourseListener.VerifyDeal(objectMap));//发送消息
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
                queueVerifySender.sendTextMessage("op.courseAuthTpToApQueue", JSONObject.toJSONString(queueMap));
            } catch (Exception ex) {
                logger.info(ex.getMessage(), ex);
            }
        }
    }

}
