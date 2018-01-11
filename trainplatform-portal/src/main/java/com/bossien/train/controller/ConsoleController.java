package com.bossien.train.controller;

import com.alibaba.fastjson.JSONObject;
import com.bossien.framework.interceptor.SignInterceptor;
import com.bossien.framework.mq.sender.QueueCourseTypeSender;
import com.bossien.train.domain.CourseInfo;
import com.bossien.train.domain.CourseQuestion;
import com.bossien.train.domain.ProjectInfo;
import com.bossien.train.domain.eum.ProjectStatusEnum;
import com.bossien.train.exception.Code;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.*;
import com.bossien.train.service.ICourseInfoService;
import com.bossien.train.service.ICourseQuestionService;
import com.bossien.train.service.IProjectCourseService;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("/console")
public class ConsoleController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);
    @Autowired
    private QueueCourseTypeSender queueCourseTypeSender;
    @Autowired
    private ICourseInfoService courseInfoService;
    @Autowired
    private ICourseQuestionService courseQuestionService;
    @Autowired
    private IProjectCourseService projectCourseService;
    @Autowired
    private IProjectExerciseOrderService projectExerciseOrderService;
    @Autowired
    private IProjectStatisticsInfoService projectStatisticsInfoService;
    @Autowired
    private IProjectBasicService projectBasicService;
    @Autowired
    private IProjectInfoService projectInfoService;

    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();


    @RequestMapping("")
    public String index() {
        return "console/index";
    }

    @RequestMapping("menu")
    public String menu() {
        return "console/menu";
    }

    @RequestMapping("feedback")
    public String feedback() {
        return "console/feedback/index";
    }

    /**
     * 发送获取课程类型获取
     */
    @RequestMapping(value = "/getCourseType")
    @ResponseBody
    public Object getCourseType(HttpServletRequest request) {
        Response<Object> response = new Response<Object>();

                 /*发送消息*/
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("platformCode", "001");
        objectMap.put("cmdType", "ok");
        objectMap.put("dataObj", "courseType");
        EXECUTOR.execute(new ConsoleController.CourseType(objectMap));//发送消息
        logger.error("=======TopicCourseListener==message success 发送验证消息成功 ========");
        response.setCode(Code.SUCCESS.getCode());
        return response;
    }

    /**
     * 效验获取题库题库数量
     */
    @RequestMapping(value = "/getQuessionCount")
    @ResponseBody
    public Object Course_info(HttpServletRequest request) {
        Response<Object> response = new Response<Object>();
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        List<CourseInfo> list = courseInfoService.selectAll();
        List<CourseQuestion> courseQuestionList = courseQuestionService.seletcQuestionCount(params);
        for (CourseQuestion courseQuestion : courseQuestionList) {
            map.put(courseQuestion.getIntCourseId().toString(), courseQuestion.getCount());
        }
        if (null != list && list.size() > 0) {
            try {
                for (CourseInfo courseInfo : list) {
                    if (null != map.get(courseInfo.getCourseId().toString())) {
                        int count = Integer.parseInt(map.get(courseInfo.getCourseId().toString()).toString());
                        if (courseInfo.getQuestionCount() != count) {
                            Map<String, Object> par = new HashMap<>();
                            par.put("courseId", courseInfo.getCourseId());
                            par.put("questionCount", count);
                            courseInfoService.updateByPrimaryKeySelective(par);
                        }
                    }
                }
                response.setCode(Code.SUCCESS.getCode());
            } catch (Exception e) {
                logger.error("课程题目数量效验异常" + e.getMessage());
                response.setCode(Code.SERVER_ERROR.getCode());
            }
        } else {
            response.setCode(Code.SERVER_ERROR.getCode());
        }
        return response;
    }

    /**
     * 修改project_course,project_course_info 试题数量
     */
    @RequestMapping(value = "/updateProjectCourse")
    @ResponseBody
    public Object updateProjectCourse(
            @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId                   //项目id
    ) {
        Response<Object> response = new Response<Object>();
            try {
                projectCourseService.updateQuestionCount(projectId);
                response.setCode(Code.SUCCESS.getCode());
            } catch (Exception e) {
                response.setCode(Code.SERVER_ERROR.getCode());
            }
        return response;
    }

    /**
     * 异步处理数据
     */
    private class CourseType extends Thread {

        private Map<String, Object> queueMap;

        private CourseType(Map<String, Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queueCourseTypeSender.sendTextMessage("op.tpAxCourseTypeTableQueue", JSONObject.toJSONString(queueMap));
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    //进入到版本维护页面
    @RequestMapping("maintainWebProperties")
    public String maintainWebProperties(ModelMap modelMap) {
        modelMap.addAttribute("version",PropertiesUtils.getValue("version"));
        modelMap.addAttribute("properties",PropertiesUtils.getValues());
        return "console/webProperties";
    }

    /**
     * 根据key修改value
     * @param version
     * @param versionValue
     * @return
     * @throws Exception
     */
    @RequestMapping("updateWebProperties")
    @ResponseBody
    public Object updateWebProperties(HttpServletResponse res,
            @RequestParam(value = "version", required = false, defaultValue = "") String version,                   //版本键值
            @RequestParam(value = "versionValue", required = false, defaultValue = "") String versionValue,          //版本号值
            @RequestParam(value = "ver", required = false, defaultValue = "ver") String ver                  //版本键值

    )throws Exception {
        Response<Object> response = new Response<Object>();
        Map<String,String> properties = new HashMap<>();

        if(StringUtils.isEmpty(versionValue)){
            response.setResult("值不能为空");
            return response;
        }

        properties.put(version,versionValue);

        //根据key修改value
        PropertiesUtils.setValue(properties);
       /* //删除cookie中版本号，修改成最新的版本号
        CookieGenerator cg = new CookieGenerator();
        cg.setCookieName(ver);
        cg.removeCookie(res);
        cg.addCookie(res,versionValue);*/
        response.setResult(Code.SUCCESS.getCode());
        return response;
    }


    /**
     * 根据projectId修改project_exercise_order、project_statistics_info表中的 total_question字段
     * @param projectId
     * @return
     * @throws Exception
     */
    @RequestMapping("updateTotalQuestion")
    @ResponseBody
    public Object updateTotalQuestion(
            @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId                   //项目id

    )throws Exception {
        Response<Object> response = new Response<Object>();
        Map<String,Object> params = new HashMap<>();
            params.put("projectId",projectId);
            //通过projectId统计总题量
            List<Map<String,Object>> totalQuestions = projectCourseService.selectTotalQuestionByProjectId(projectId);
            if(totalQuestions.size()<=0){
                response.setResult("没有可更新的");
                return response;
           }
            //修改project_exercise_order表中的 total_question字段
            projectExerciseOrderService.updateBatch(totalQuestions);
            //修改project_statistics_info表中的 total_question字段
            projectStatisticsInfoService.updateBatch(totalQuestions);

        response.setResult(Code.SUCCESS.getCode());
        return response;
    }

    /**
     * 根据projectId更新时间到了后project_basic、project_info表中project_status字段
     * @param projectId
     * @return
     * @throws Exception
     */
    @RequestMapping("updateProjectStatus")
    @ResponseBody
    public Object updateProjectStatus(
            @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId                   //项目id

    )throws Exception {
        Response<Object> response = new Response<Object>();
        //查询projectInfo信息
        ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(projectId);
        if(null != projectInfo){
            String projectStatus = projectInfo.getProjectStatus();
            String projectStartTime = projectInfo.getProjectStartTime();
            String projectEndTime = projectInfo.getProjectEndTime();
            if(StringUtils.isNotEmpty(projectStatus)){
                if(ProjectStatusEnum.ProjectStatus_2.getValue().equals(projectStatus) || ProjectStatusEnum.ProjectStatus_3.getValue().equals(projectStatus)){
                    projectStatus = DateUtils.getProjectStatus(projectStartTime,projectEndTime,projectStatus);
                }
            }
            //跟新projectInfo的项目状态
            projectInfoService.updateProjectStatus(projectId,projectStatus);
            //跟新projectBasic的项目状态
            projectBasicService.updateProjectStatus(projectId,projectStatus);
        }
        response.setResult(Code.SUCCESS.getCode());
        return response;
    }
}
