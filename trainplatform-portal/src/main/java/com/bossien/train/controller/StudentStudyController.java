package com.bossien.train.controller;

import com.bossien.framework.interceptor.SignInterceptor;
import com.bossien.framework.mq.sender.QueueVideoSender;
import com.bossien.train.domain.User;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("student/study")
public class StudentStudyController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

    @Autowired
    private QueueVideoSender queueVideoSender;

    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    /**
     * 项目-进入学习-单独课程
     * @return
     */
    @RequestMapping("")
    public String index(
            ModelMap modelMap,
            HttpServletRequest request,
            @RequestParam(value="projectId", required = true, defaultValue = "") String projectId,
            @RequestParam(value="courseId", required = true, defaultValue = "") String courseId,
            @RequestParam(value="source", required = false, defaultValue = "-1") String source // 学时来源：-1：学习、1：练习答题（正确）、2：练习答题（错误）、3：考试答题（正确）、4：考试答题（错误）
    ) {

        modelMap.put("projectId", projectId);
        modelMap.put("courseId", courseId);
        modelMap.put("userId", getCurrUser(request).getId());
        modelMap.put("source", source);

        return "student/study/index";
    }



    @RequestMapping("/plusTime")
    @ResponseBody
    public Object plusTime(
            HttpServletRequest request,
            @RequestParam(value="project_id", required = true, defaultValue = "") String project_id,
            @RequestParam(value="course_id", required = true, defaultValue = "") String course_id,
            @RequestParam(value="user_id", required = true, defaultValue = "") String user_id,
            @RequestParam(value="source", required = true, defaultValue = "-1") String source,
            @RequestParam(value="study_time", required = true, defaultValue = "0") Long study_time
    ) throws ServiceException {

        User user = this.getCurrUser(request);

        if(StringUtils.isEmpty(project_id) || StringUtils.isEmpty(course_id) ||
                StringUtils.isEmpty(user_id) || StringUtils.isEmpty(source)) {
            return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        }

        if(null == study_time || study_time <= 0){
            return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        }

        // 发送到activemq的study_time.queue
        Map<String,Object> queueMap = new ConcurrentHashMap<String, Object>();
        queueMap.put("project_id", project_id);
        queueMap.put("course_id", course_id);
        queueMap.put("user_id", user_id);
        queueMap.put("user_name", user.getUserName());
        queueMap.put("source", source);
        queueMap.put("study_time", study_time);
        queueMap.put("companyId", user.getCompanyId());
        EXECUTOR.execute(new StudentStudyController.AnalysDeal(queueMap));

        return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

    private class AnalysDeal extends Thread {

        private Map<String,Object> queueMap;

        public AnalysDeal(Map<String,Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queueVideoSender.sendMapMessage("tp.video.queue", queueMap);
            }catch(Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }


}
