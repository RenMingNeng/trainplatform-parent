package com.bossien.train.controller;

import com.bossien.framework.interceptor.SignInterceptor;
import com.bossien.framework.mq.sender.QueueVideoSender;
import com.bossien.train.domain.*;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.*;
import com.bossien.train.util.ParamsUtil;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("student/studySelf")
public class StudentStudySelfController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

    private final String collectionName_question_collection = "question_collection";
    @Autowired
    private QueueVideoSender queueVideoSender;
    @Autowired
    private ICompanyService companyService;
    @Autowired
    private ICourseService courseService;
    @Autowired
    private ICompanyCourseService companyCourseService;
    @Autowired
    private ICourseInfoService courseInfoService;
    @Autowired
    private ICompanyCourseTypeService companyCourseTypeService;
    @Autowired
    private MongoOperations mongoTemplate;
    @Autowired
    private IQuestionService questionService;
    @Autowired
    private ICourseQuestionService courseQuestionService;
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    // 操作mongodb的集合名,等同于mysql中的表
    private final String collectionName_study_self = "study_self";
    /**
     * 我的课程-进入学习
     * @return
     */
    @RequestMapping("")
    public String index(
            ModelMap modelMap,
            HttpServletRequest request,
            @RequestParam(value="courseId", required = true, defaultValue = "") String courseId,
            @RequestParam(value="source", required = false, defaultValue = "-1") String source // 学时来源：-1：学习、1：练习答题（正确）、2：练习答题（错误）、3：考试答题（正确）、4：考试答题（错误）
    ) {

        //通过courseId查询课程


        modelMap.put("courseId", courseId);
        modelMap.put("userId", getCurrUser(request).getId());
        modelMap.put("source", source);

        return "student/study_self/index";
    }


    /**
     * 加学时
     * @param request
     * @param course_id
     * @param user_id
     * @param source
     * @param study_time
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/plusTime")
    @ResponseBody
    public Object plusTime(
            HttpServletRequest request,
            @RequestParam(value="course_id", required = true, defaultValue = "") String course_id,
            @RequestParam(value="course_no", required = true, defaultValue = "") String course_no,
            @RequestParam(value="course_name", required = true, defaultValue = "") String course_name,
            @RequestParam(value="class_hour", required = true, defaultValue = "") String class_hour,
            @RequestParam(value="user_id", required = true, defaultValue = "") String user_id,
            @RequestParam(value="source", required = true, defaultValue = "-1") String source,
            @RequestParam(value="study_time", required = true, defaultValue = "0") Long study_time
    ) throws ServiceException {

        User user = this.getCurrUser(request);

        if(StringUtils.isEmpty(course_id) || StringUtils.isEmpty(user_id) || StringUtils.isEmpty(source)) {
            return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        }

        if(null == study_time || study_time <= 0){
            return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        }

        // 发送到activemq的study_time.queue
        Map<String,Object> queueMap = new ConcurrentHashMap<String, Object>();
        queueMap.put("course_id", course_id);
        queueMap.put("course_no", course_no);
        queueMap.put("course_name", course_name);
        queueMap.put("class_hour", class_hour);
        queueMap.put("user_id", user_id);
        queueMap.put("user_name", user.getUserName());
        queueMap.put("source", source);
        queueMap.put("study_time", study_time);
        queueMap.put("companyId", user.getCompanyId());

        EXECUTOR.execute(new StudentStudySelfController.AnalysDeal(queueMap));

        return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

    /**
     * 我的课程-进入练习
     * @return
     */
    @RequestMapping("/exercise")
    public String exercise(HttpServletRequest request,
                           @RequestParam(value="course_id", required = true, defaultValue = "") String course_id) {

        request.setAttribute("url", "/student/studySelf/exercise_ajax?project_id=-9&course_id=" + course_id);
        request.setAttribute("title", "课程学习");
        return "student/study_self/exercise_ajax";
    }

    @ResponseBody
    @RequestMapping("/exercise_ajax")
    public Object exercise_ajax(HttpServletRequest request,
                                      @RequestParam(value="course_id", required = true, defaultValue = "") String course_id){
        List<Map<String, Object>> result = new ArrayList<>();
        if(null != course_id && !course_id.equals("")){
            //查询出题编号集合
            Map<String, Object> params = new HashedMap();
            params.put("intCourseId", course_id);
            List<String> questions = courseQuestionService.selectQuestionIdList(params);
            if(null != questions && questions.size() > 0){
                //再组装题
                params = new HashedMap();
                params.put("intIds", questions);
                result = questionService.selectList(params, new ArrayList<QuestionCollection>());
            }
        }
        Response<Object> resp = new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        resp.setResult(result);
        return resp;
    }

    /**
     * 保存练习
     * @param request
     * @param detail
     * @return
     */
    @ResponseBody
    @RequestMapping("/save_exercise")
    public Object save_exercise(HttpServletRequest request, String detail) throws Exception {
        User user = this.getCurrUser(request);
        if(null == user){
            return new Response<>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }
        if (null != detail && !detail.equals("")){
            Map<String,Object> queueMap = new HashedMap();
            queueMap.put("detail", detail);
            queueMap.put("userId", user.getId());
            queueMap.put("userName", user.getUserName());
            queueMap.put("companyId", user.getCompanyId());

            EXECUTOR.execute(new StudentStudySelfController.SaveExerciseThread(queueMap));
        }
        return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

    private class SaveExerciseThread extends Thread {

        private Map<String,Object> queueMap;

        public SaveExerciseThread(Map<String,Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queueVideoSender.sendMapMessage("tp.studySelfExercise.queue", queueMap);
            }catch(Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    private class AnalysDeal extends Thread {

        private Map<String,Object> queueMap;

        public AnalysDeal(Map<String,Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queueVideoSender.sendMapMessage("tp.studySelfVideo.queue", queueMap);
            }catch(Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }


    /**
     * 学员进入我的课程管理页面
     */
    @RequestMapping(value = "/stuCurseList")
    public ModelAndView stuCourseManagerList(){
        ModelAndView mv=new ModelAndView();
        mv.setViewName("student/study_self/stuCourseList");
        return mv;
    }

    /**
     * 我的课程下的课程列表 点击左侧的课程分类查询高分类下的课程
     * @param request
     * @param courseName
     * @param intTypeId
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/showCourseList")
    @ResponseBody
    public Object stuCourseList(HttpServletRequest request,
                                      @RequestParam(value="courseName", required = false, defaultValue = "") String courseName,
                                      @RequestParam(value="intTypeId", required = false, defaultValue = "") String intTypeId,
                                      @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
                                      @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response<Object> resp = new Response<Object>();
        User user=getCurrUser(request);
        String intCompanyId=user.getCompanyId();
        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }

        Page<Map<String, Object>> page = new Page<Map<String, Object>>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("courseName", ParamsUtil.joinLike(courseName));
        params.put("courseTypeIds", intTypeId);
        params.put("intCompanyId",intCompanyId);
        if(!"".equals(intTypeId)) {
            params.put("courseTypeIds", companyCourseTypeService.getAPCTypeChiList(params));
        }
        //根据companyId查询courseIds
        List<Map<String,Object>> companyCourses=companyCourseService.selectByCompanyId(params);
        Integer count = 0;
        List<Map<String, Object>> courses = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> clistMap=new ArrayList<Map<String, Object>>();
        if(null!=companyCourses && companyCourses.size()>0 ){
            List<String>  courseIds=new ArrayList <String>();
            for (Map map:companyCourses) {
                courseIds.add(map.get("intCourseId").toString()) ;
            }
            params.put("courseIds",courseIds);
            count = courseInfoService.selectCourseCount(params);

            page = new Page(count, pageNum, pageSize);
            params.put("startNum", page.getStartNum());
            params.put("endNum", page.getPageSize());
            //从courseInfo中查询课程信息
            courses=courseInfoService.selectCourseList(params);


            Query query = new Query(new Criteria().andOperator(
                    Criteria.where("user_id").is(user.getId()),
                    Criteria.where("course_id").in(courseIds))
            );
            //查询单条记录
            List<StudySelf> studySelfList = mongoTemplate.find(query, StudySelf.class, collectionName_study_self);

            Map<String, Object> stimeMap=new HashMap<String, Object>();

            //循环组合两个集合的数据
            if(courses.size()>0){

                if(studySelfList.size()>0 ) {
                    for (StudySelf sts : studySelfList) {
                        if (sts.getUser_id() != null && sts.getCourse_id() != null) {
                            stimeMap.put(sts.getUser_id() + "-" + sts.getCourse_id(), sts.getStudy_time());
                        }
                    }
                }

                for(Map<String, Object> smap:courses) {
                    if(smap != null && smap.get("course_id") != null) {
                        if(stimeMap.containsKey(user.getId()+"-"+smap.get("course_id").toString())){
                            Integer time = Integer.valueOf(stimeMap.get(user.getId() + "-" + smap.get("course_id")).toString());
                            smap.put("study_time",String.format("%.2f",(float)time/60));
                        }else{
                            smap.put("study_time",0);
                        }
                    }
                    clistMap.add(smap);
                }
            }

        }
        page.setDataList(clistMap);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);
        return resp;
    }

    /**
     * 学员下我的课程查看课程详情
     * @param request
     * @param courseId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/viewCourseInfo")
    public ModelAndView courseInfo(HttpServletRequest request,
                                   @RequestParam(value="courseId", required = false, defaultValue = "") String courseId)throws Exception{
        ModelAndView mv=new ModelAndView();
        User user=getCurrUser(request);
//        Map<String,Object> params=new HashMap<String,Object>();
//        params.put("companyId",user.getCompanyId());
        //根据courseId查询课程信息
        CourseInfo courseInfo=courseInfoService.selectOne(courseId);
        Course course= courseService.selectById(courseId);
        if(null!=course) {
            courseInfo.setCourseDesc(course.getVarDesc());
        }
        String courseTypeName = "";
        //课程类别
        if(!"".equals(courseInfo.getCourseTypeId())&& null!=courseInfo.getCourseTypeId()) {
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("intCourseId",courseInfo.getCourseId());
            param.put("intCompanyId",user.getCompanyId());
            CompanyCourse companyCourse = companyCourseService.selectOne(param);
            courseTypeName = companyCourseTypeService.getParentTypeName(companyCourse.getIntCompanyCourseTypeId(), courseTypeName);
        }
        mv.setViewName("student/study_self/stu_course_info");
        mv.addObject("courseInfo",courseInfo);
        mv.addObject("courseTypeName",courseTypeName);
        return mv;
    }
}
