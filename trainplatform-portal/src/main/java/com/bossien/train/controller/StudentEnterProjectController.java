package com.bossien.train.controller;

import com.bossien.train.domain.*;
import com.bossien.train.domain.eum.ProjectTypeEnum;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.kit.PermissionUtil;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.*;
import com.bossien.train.util.ParamsUtil;
import com.bossien.train.util.StringUtil;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017-07-25.
 */
@Controller
@RequestMapping(value = "/student/enterProject")
public class StudentEnterProjectController extends BasicController {
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    public static final Logger logger = LoggerFactory.getLogger(StudentExamAnswersController.class);
    // 操作mongodb的集合名,等同于mysql中的表
    private final String collectionName_question_collection = "question_collection";
    private final String collectionName_question_wrong_answers = "question_wrong_answers";
    @Autowired
    private MongoOperations mongoTemplate;
    @Autowired
    private IProjectCourseInfoService projectCourseInfoService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private IExamStrategyService examStrategyService;
    @Autowired
    private IProjectUserService projectUserService;
    @Autowired
    private IProjectBasicService projectBasicService;


    /**
     * 学员进入项目（非单独的考试项目）
     * @param request
     * @param project_basicId
     * @param role_id
     * @return
     */
    @RequestMapping("/student_enter")
    public ModelAndView student_enter(HttpServletRequest request,
                                      @RequestParam(value = "projectId", defaultValue = "", required = false) String project_basicId,
                                      @RequestParam(value = "role_id", defaultValue = "", required = false) String role_id
        ) throws ParseException {
        User user = getCurrUser(request);
        ModelAndView mv = new ModelAndView();

        // 项目详情
        ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(project_basicId);
        ProjectBasic projectBasic = projectBasicService.selectById(project_basicId);

        mv.addObject("project_basicId", project_basicId);
        mv.addObject("role_id", role_id);
        mv.addObject("project_userId", user.getId());
        mv.addObject("projectInfo", projectInfo);
        mv.addObject("projectBasic", projectBasic);
        mv.addObject("permissionExercise", PermissionUtil.hasPermissionQuestion(projectInfo.getProjectType()));    //含有练习
        mv.addObject("permissionTrain", PermissionUtil.hasPermissionStudy(projectInfo.getProjectType()));          //含有培训
        mv.addObject("permissionExam", PermissionUtil.hasPermissionExam(projectInfo.getProjectType()));            //含有考试
        mv.addObject("trainType", ProjectTypeEnum.QuestionType_1.getValue());                                      //培训
        mv.addObject("trainExamType", ProjectTypeEnum.QuestionType_5.getValue());                                  //培训考试
        mv.addObject("examType", ProjectTypeEnum.QuestionType_3.getValue());                                       //考试
        mv.addObject("projectTypeName", ProjectTypeEnum.get(projectInfo.getProjectType()).getName());


        if(ProjectTypeEnum.QuestionType_1.getValue().equals(projectBasic.getProjectType())){
            mv.setViewName("student/enterProject/enter_train");return mv;
        }
        if(ProjectTypeEnum.QuestionType_2.getValue().equals(projectBasic.getProjectType())){
            mv.setViewName("student/enterProject/enter_exercise");return mv;
        }
        if(ProjectTypeEnum.QuestionType_4.getValue().equals(projectBasic.getProjectType())){
            mv.setViewName("student/enterProject/enter_train_exercise");return mv;
        }
        if(ProjectTypeEnum.QuestionType_5.getValue().equals(projectBasic.getProjectType())){
            mv.setViewName("student/enterProject/enter_train_exam");return mv;
        }
        if(ProjectTypeEnum.QuestionType_6.getValue().equals(projectBasic.getProjectType())){
            mv.setViewName("student/enterProject/enter_exercise_exam");return mv;
        }
        if(ProjectTypeEnum.QuestionType_7.getValue().equals(projectBasic.getProjectType())){
            mv.setViewName("student/enterProject/enter_train_exercise_exam");return mv;
        }
        mv.setViewName("student/enterProject/enter_train_exercise_exam");return mv;
    }




    /**
     * 学员进入单独的考试项目
     * @param request
     * @param project_basicId
     * @param role_id
     * @return
     */
    @RequestMapping("/student_enter_exam")
    public ModelAndView student_enter_exam(HttpServletRequest request,
        @RequestParam(value = "projectId", defaultValue = "", required = false) String project_basicId,
        @RequestParam(value = "role_id", defaultValue = "", required = false) String role_id) {
        User user = getCurrUser(request);
        ModelAndView mv = new ModelAndView();

        // 项目详情
        ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(project_basicId);

        // 根据projectId和userId查询用户roleId
        ProjectUser projectUser = projectUserService.selectByProjectIdAndUserId(project_basicId,user.getId());

        String roleId = projectUser.getRoleId();
        // 根据projectId和roleId查询组卷策略
        ExamStrategy examStrategy = examStrategyService.selectOneByProjectIdAndRoleId(project_basicId,roleId);

        mv.setViewName("student/enterProject/enter_exam");
        mv.addObject("examStrategy", examStrategy);
        mv.addObject("project_basicId", project_basicId);
        mv.addObject("role_id", role_id);
        mv.addObject("project_userId", user.getId());
        mv.addObject("projectInfo", projectInfo);
        mv.addObject("permissionExercise", PermissionUtil.hasPermissionQuestion(projectInfo.getProjectType()));    //含有练习
        mv.addObject("permissionTrain", PermissionUtil.hasPermissionStudy(projectInfo.getProjectType()));          //含有培训
        mv.addObject("permissionExam", PermissionUtil.hasPermissionExam(projectInfo.getProjectType()));            //含有考试
        mv.addObject("trainType", ProjectTypeEnum.QuestionType_1.getValue());                      //培训
        mv.addObject("trainExamType", ProjectTypeEnum.QuestionType_5.getValue());                 //培训考试
        mv.addObject("examType", ProjectTypeEnum.QuestionType_3.getValue());

        return mv;
    }




    /**
     * 培训练习课程信息列表
     *
     * @param request
     * @param course_name 要搜索的课程名称
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(HttpServletRequest request, @RequestParam(value = "project_basicId", required = true, defaultValue = "") String project_basicId,  //项目序号
                       @RequestParam(value = "project_userId", required = true, defaultValue = "") String project_userId,    //用户序号
                       @RequestParam(value = "course_name", required = false, defaultValue = "") String course_name,         //课程名称
                       @RequestParam(value = "finish_status", required = false, defaultValue = "") String finish_status,     //课程完成状态
                       @RequestParam(value = "pageSize", required = true, defaultValue = "10") Integer pageSize, @RequestParam(value = "pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response <Object> resp = new Response <Object>();

        if (pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }

        Page <Map <String, Object>> page = new Page <Map <String, Object>>();

        Map <String, Object> params = new HashMap <String, Object>();
        params.put("projectId", project_basicId);
        params.put("userId", project_userId);
        params.put("courseName", ParamsUtil.joinLike(course_name));
        params.put("finishStatus", finish_status);
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        Integer count = projectCourseInfoService.selectCount(params);

        page = new Page(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("endNum", page.getPageSize());
        List <Map <String, Object>> listMap = projectCourseInfoService.selectList(params);
        page.setDataList(listMap);
        resp.setResult(page);
        return resp;
    }

    /**
     * 学员进入答题页面
     *
     * @return
     */
    @RequestMapping("/student_exercise")
    public ModelAndView student_exercise(@RequestParam(value = "projectId", defaultValue = "", required = false) String projectId, @RequestParam(value = "userId", defaultValue = "", required = false) String userId, @RequestParam(value = "courseId", defaultValue = "", required = false) String courseId) {

        return new ModelAndView("student/enterProject/student_exercise").addObject("projectId", projectId).addObject("userId", userId).addObject("courseId", courseId);
    }


    /**
     * 学员进入学习页面
     *
     * @return
     */
    @RequestMapping("/student_study")
    public ModelAndView student_study(@RequestParam(value = "projectId", defaultValue = "", required = false) String projectId, @RequestParam(value = "userId", defaultValue = "", required = false) String userId, @RequestParam(value = "courseId", defaultValue = "", required = false) String courseId) {

        return new ModelAndView("student/enterProject/student_study").addObject("projectId", projectId).addObject("userId", userId).addObject("courseId", courseId);
    }

    /**
     * 异步更新我的错题、我的收藏数量
     *
     * @param request
     * @param project_id
     * @param project_userId
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/update_wrong_collect")
    @ResponseBody
    public Object update_wrong_collect(HttpServletRequest request, @RequestParam(value = "project_id", required = true, defaultValue = "") String project_id, @RequestParam(value = "project_userId", required = true, defaultValue = "") String project_userId) throws ServiceException {

        if (StringUtil.isEmpty(project_id)) {
            return new Response <Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        }

        //mongo里面查询我的错题数据
        Query query = new Query(new Criteria().andOperator(Criteria.where("project_id").is(project_id), Criteria.where("user_id").is(project_userId)));
        Map <String, Object> params = new HashedMap();
        params.put("project_id", project_id);
        params.put("user_id", project_userId);
        long wrongCount = mongoTemplate.getCollection(collectionName_question_wrong_answers).
                distinct("question_id", new BasicDBObject(params)).size();
        long collectCount = mongoTemplate.count(query, QuestionCollection.class, collectionName_question_collection);

        Response <Object> resp = new Response <Object>();
        params.clear();
        params.put("wrongCount", wrongCount);
        params.put("collectCount", collectCount);
        resp.setResult(params);
        return resp;
    }

    /**
     * 获取组卷策略
     *
     * @param request
     * @param projectId
     * @return
     */
    @RequestMapping
    @ResponseBody
    public Object getExamStratety(HttpServletRequest request, @RequestParam(value = "projectId", required = true, defaultValue = "-1") String projectId) {
        Response <Object> resp = new Response <Object>();
        Map <String, Object> params = new HashedMap();
        params.put("projectId", projectId);
        ExamStrategy examStrategy = examStrategyService.selectByProjectIdAndRoleId(params);
        resp.setResult(examStrategy);
        return resp;
    }
}
