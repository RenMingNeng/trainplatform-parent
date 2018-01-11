package com.bossien.train.controller;

import com.bossien.framework.interceptor.SignInterceptor;
import com.bossien.framework.mq.sender.QueueCPPubSender;
import com.bossien.train.domain.*;
import com.bossien.train.domain.eum.ProjectStatusEnum;
import com.bossien.train.domain.eum.ProjectTypeEnum;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.kit.PermissionUtil;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.*;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.ParamsUtil;
import com.bossien.train.util.StringUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017-07-31.
 */
@Controller
@RequestMapping("/super/project_create/public")
public class CreatePublicProjectController extends BasicController{

    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    @Autowired
    private QueueCPPubSender queueCPPubSender;
    @Autowired
    private IProjectBasicService projectBasicService;
    @Autowired
    private  ICreatePublicProjectService createPublicProjectService;
    @Autowired
    private IProjectCourseService projectCourseService;
    @Autowired
    private ICompanyService companyService;
    @Autowired
    private ICompanyProjectService companyProjectService;
    @Autowired
    private  IProjectInfoService projectInfoService;
    @Autowired
    private ITrainSubjectService trainSubjectService;
    @Autowired
    private IExamStrategyService examStrategyService;
    @Autowired
    private IProjectManagerService projectManagerService;
    @Value("${contain_exam_type}")
    private String containExamType;

    /**
     * 创建公开项目
     * @param request
     * @param projectTypeNo
     * @param subjectId
     * @param subjectName
     * @return
     * @throws ParseException
     */
    @RequestMapping("/create_project")
    public ModelAndView createPublicProject(HttpServletRequest request,
            @RequestParam(value = "step", required = false,defaultValue = "")  String step,                     //创建项目步骤
            @RequestParam(value = "projectId", required = false,defaultValue = "")  String projectId,           //项目编号
            @RequestParam(value = "projectTypeNo", required = false,defaultValue = "")  String projectTypeNo ,  //项目类型
            @RequestParam(value = "subjectId", required = false,defaultValue = "")  String subjectId,           //主题编号
            @RequestParam(value = "subjectName", required = false,defaultValue = "")  String subjectName) throws ParseException {        //主题名
       ModelAndView mv = new ModelAndView();
       if("0".equals(step)){ //跳到首页
           mv.setViewName("super/index");
       }
       if("1".equals(step)){
           User user = getCurrUser(request);
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           // 项目开始时间－默认第二天开始-yyyy-MM-dd
           String datBeginTime = sdf.format(DateUtils.getNextDay1(new Date()));
           // 项目结束时间-一个月的长度-yyyy-MM-dd
           String datEndTime = sdf.format(DateUtils.getNextMouthDay(new Date()));
           // 考试开始时间－默认第二天开始-yyyy-MM-dd HH:mm:ss
           String examBeginTime = sdf.format(DateUtils.getNextDay1(new Date()));
           // 考试结束时间-一个月的长度-yyyy-MM-dd HH:mm:ss
           String examEndTime = sdf.format(DateUtils.getNextMouthDay(new Date()));
           // 获取系统时间
           String systemTime = sdf.format(new Date());
           // 项目默认名称
           String projectName = sdf.format(new Date()).substring(0, 10);
           ProjectBasic projectBasic = projectBasicService.build(user);
           mv.setViewName("super/createProject/create_project_step1");
           mv.addObject("projectTypeNo", projectTypeNo);
           mv.addObject("subjectId", subjectId);
           mv.addObject("subjectName", subjectName);
           mv.addObject("datBeginTime", datBeginTime);
           mv.addObject("datEndTime", datEndTime);
           mv.addObject("examBeginTime", examBeginTime);
           mv.addObject("examEndTime", examEndTime);
           mv.addObject("systemTime", systemTime);
           mv.addObject("projectName", projectName + " " + subjectName);
           mv.addObject("projectBasic", projectBasic);
           mv.addObject("permissionExercise", PermissionUtil.hasPermissionQuestion(projectTypeNo));    //是否含有练习
           mv.addObject("permissionTrain", PermissionUtil.hasPermissionStudy(projectTypeNo));          //是否含有培训
           mv.addObject("permissionExam", PermissionUtil.hasPermissionExam(projectTypeNo));            //是否含有考试
           mv.addObject("examType", ProjectTypeEnum.QuestionType_3.getValue());             //单独考试
       }
       if("2".equals(step)){
           mv.setViewName("/super/createProject/create_project_step2");
           String projectStatus = projectBasicService.selectProjectStatus(projectId);
           mv.addObject("projectId",projectId);
           mv.addObject("projectTypeNo",projectTypeNo);
           mv.addObject("projectStatus",projectStatus);
       }
        if("3".equals(step)){
            mv.setViewName("/super/createProject/create_project_step3");
            mv.addObject("projectId",projectId);
            mv.addObject("projectTypeNo",projectTypeNo);
        }
        if("4".equals(step)){
            mv.setViewName("super/createProject/create_project_step4");
            String projectStatus = projectInfoService.selectProjectStatus(projectId);
            mv.addObject("projectId",projectId);
            mv.addObject("projectTypeNo",projectTypeNo);
            mv.addObject("projectStatus",projectStatus);
        }
        if("5".equals(step)){
            mv.setViewName("super/createProject/create_project_step5");
            ProjectBasic projectBasic = projectBasicService.selectById(projectId);
            if(null == projectBasic){
                return mv;
            }
            String projectStatus = projectBasic.getProjectStatus();
            mv.addObject("projectId",projectId);
            mv.addObject("projectTypeNo",projectTypeNo);
            mv.addObject("projectStatus",projectStatus);
            mv.addObject("examStatus",checkExamTime(projectBasic));
        }
        return mv;
    }

    /**
     * 检查考试状态
     * @param projectBasic
     * @return
     */
    public boolean checkExamTime(ProjectBasic projectBasic){
        String projectType = projectBasic.getProjectType();
        //项目类型
        if(projectType.equals(ProjectTypeEnum.QuestionType_1.getValue()) ||
                projectType.equals(ProjectTypeEnum.QuestionType_2.getValue()) ||
                projectType.equals(ProjectTypeEnum.QuestionType_4.getValue())){
            return false;
        }

        //如果是未发布、未考试可以修改
        String projectStatus =projectBasic.getProjectStatus();
        if(projectStatus.equals(ProjectStatusEnum.ProjectStatus_1.getValue()) ||
                projectStatus.equals(ProjectStatusEnum.ProjectStatus_2.getValue())){
            return true;
        }

        //考试时间
        String exam_time = projectBasic.getProjectExamInfo();
        if(null == exam_time){
            return false;
        }

        Gson gson = new Gson();
        Map<String, Object> time = gson.fromJson(exam_time, Map.class);
        String beginTime = String.valueOf(time.get("beginTime"));
        if(StringUtil.isEmpty(beginTime)){
            return false;
        }
        //当前时间大于、等于考试时间时
        if(DateUtils.parseDateTime(beginTime).getTime() <= System.currentTimeMillis()){
            return false;
        }
        return true;
    }

    /*公开型项目修改页面*/
    @RequestMapping("/project_update")
    public ModelAndView updatePublicProject(
            @RequestParam(value = "projectType", required = false,defaultValue = "")  String projectTypeNo,
            @RequestParam(value = "projectStatus", required = false,defaultValue = "")  String projectStatus,
            @RequestParam(value = "projectId", required = false,defaultValue = "")  String projectId) throws  Exception{ //项目编号
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取系统时间
        String systemTime = sdf.format(new Date());
        ModelAndView mv = new ModelAndView();
        Map<String,Object>  params=new HashMap<String,Object>();
        params.put("projectId" ,projectId);
        //根据projectId查询项目详情
        ProjectInfo projectInfo=projectInfoService.selectProjectInfoById(projectId);
        ProjectBasic projectBasic=projectBasicService.selectById(projectId);
        if(null!=projectBasic){
            //培训项目开始时间和结束时间处理
            if(!"".equals(projectBasic.getProjectTrainInfo()) && null!=projectBasic.getProjectTrainInfo()){
                Map<String,Object> map1=new Gson().fromJson(projectBasic.getProjectTrainInfo(),Map.class);
                mv.addObject("trainStartTime",map1.get("beginTime").toString());
                mv.addObject("trainEndTime",map1.get("endTime").toString());
            }
            //练习项目开始时间和结束时间处理
            if(!"".equals(projectBasic.getProjectExerciseInfo()) && null!=projectBasic.getProjectExerciseInfo()){
                Map<String,Object> map2=new Gson().fromJson(projectBasic.getProjectExerciseInfo(),Map.class);
                mv.addObject("exerciseStartTime",map2.get("beginTime").toString());
                mv.addObject("exerciseEndTime",map2.get("endTime").toString());
            }
            //考试项目开始时间和结束时间处理
            if(!"".equals(projectBasic.getProjectExamInfo()) && null!=projectBasic.getProjectExamInfo()){
                Map<String,Object> map3=new Gson().fromJson(projectBasic.getProjectExamInfo(),Map.class);
                if(sdf.parse(map3.get("beginTime").toString()).before(new Date())){
                    mv.addObject("examPermission",true);
                }else{
                    mv.addObject("examPermission",false);
                }
                mv.addObject("examStartTime",map3.get("beginTime").toString());
                mv.addObject("examEndTime",map3.get("endTime").toString());
                mv.addObject("intRetestTime",map3.get("count").toString());
            }
            //查询项目主题
            if(!"".equals(projectBasic.getSubjectId()) && null!=projectBasic.getSubjectId() ){
                TrainSubject trainSubject=new TrainSubject();
                trainSubject.setVarId(projectBasic.getSubjectId());
                trainSubject=trainSubjectService.selectOne(trainSubject);
                if(null!=trainSubject) {
                    mv.addObject("subjectName", trainSubjectService.selectOne(trainSubject).getSubjectName());
                }
            }
            mv.addObject("projectTypeNo",projectTypeNo);
            mv.addObject("projectBasic",projectBasic);
            mv.addObject("projectInfo",projectInfo);
            mv.addObject("projectStatus",projectStatus);
            mv.addObject("systemTime",systemTime);
            mv.addObject("permissionExercise",PermissionUtil.hasPermissionQuestion(projectTypeNo));    //是否含有练习
            mv.addObject("permissionTrain",PermissionUtil.hasPermissionStudy(projectTypeNo));          //是否含有培训
            mv.addObject("permissionExam",PermissionUtil.hasPermissionExam(projectTypeNo));            //是否含有考试
        }
        mv.setViewName("super/updateProject/update_project_step1");
        return mv;
    }

    /*公开型项目详情页面*/
    @RequestMapping("/project_info")
    public ModelAndView infoPublicProject(
            @RequestParam(value = "projectId", required = false,defaultValue = "")  String projectId) { //项目编号
        ModelAndView mv  = new ModelAndView();
        ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(projectId);
        Map<String,Object>  param = new HashMap<String,Object>();
        param.put("projectId" ,projectId);
        param.put("roleId" ,"-1");
        //查询组卷策略
        if(null!=projectInfo){
            if("1".equals(projectInfo.getProjectMode())) {
                String projectType = projectInfo.getProjectType();
                if (ProjectTypeEnum.QuestionType_3.getValue().equals(projectType) || ProjectTypeEnum.QuestionType_5.getValue().equals(projectType) ||
                        ProjectTypeEnum.QuestionType_6.getValue().equals(projectType) || ProjectTypeEnum.QuestionType_7.getValue().equals(projectType)) {
                    ExamStrategy examStrategy = examStrategyService.selectByProjectIdAndRoleId(param);
                    if (null != examStrategy) {
                        // 组装组卷策略的题型数据信息
                        List<Map<String, Object>> examStrategyRows = examStrategyService
                                .assembleExamStrategy(examStrategy);  //组装策略信息
                        mv.addObject("examStrategyRows", examStrategyRows);  //多种题型组合信息
                    }
                    //组卷策略信息
                    mv.addObject("examStrategy", examStrategy);
                }
            }
        }
        mv.addObject("projectInfo",projectInfo);
        mv.addObject("projectTypeNo",projectInfo.getProjectType());
        mv.setViewName("super/info_project");
        return mv;
    }

    /*公开型项目保存*/
    @RequestMapping("/save_public_project")
    @ResponseBody
    public Object savePublicProject(HttpServletRequest request, ProjectBasic projectBasic,
            @RequestParam(value = "subjectName", required = false,defaultValue = "")  String subject_name ,
            @RequestParam(value = "intRetestTime", required = false,defaultValue = "0")  String intRetestTime ,
            @RequestParam(value = "datBeginTime", required = false,defaultValue = "")  String datBeginTime ,
            @RequestParam(value = "datEndTime", required = false,defaultValue = "")  String datEndTime,
            @RequestParam(value = "examBeginTime", required = false,defaultValue = "")  String examBeginTime ,
            @RequestParam(value = "examEndTime", required = false,defaultValue = "")  String examEndTime
    ) throws Exception {
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());

        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        if(!StringUtil.isEmpty(datBeginTime) && !StringUtil.isEmpty(datEndTime)){
            datBeginTime = datBeginTime+" 00:00:00";
            datEndTime = datEndTime+" 23:59:59";
        }
        if(!StringUtil.isEmpty(examBeginTime) && !StringUtil.isEmpty(examEndTime)){
            examBeginTime = examBeginTime+":00";
            examEndTime = examEndTime+":00";
        }
        params.put("intRetestTime",intRetestTime);
        params.put("datBeginTime",datBeginTime);
        params.put("datEndTime",datEndTime);
        params.put("examBeginTime",examBeginTime);
        params.put("examEndTime",examEndTime);
        params.put("projectBasic",projectBasic);
        // 保存
        createPublicProjectService.savePublicProject(params, getCurrUser(request));

        // 发送到activemq的cppub.queue(保存项目详情)
        Map<String,Object> queueMap = new ConcurrentHashMap<String, Object>();
        queueMap.put("queueNo","projectInfo");
        queueMap.put("id",projectBasic.getId());
        queueMap.put("project_name",projectBasic.getProjectName());
        queueMap.put("project_type",projectBasic.getProjectType());
        queueMap.put("subject_name",subject_name);
        queueMap.put("datBeginTime",datBeginTime);
        queueMap.put("datEndTime",datEndTime);
        queueMap.put("examBeginTime",examBeginTime);
        queueMap.put("examEndTime",examEndTime);
        queueMap.put("intRetestTime",intRetestTime);
        EXECUTOR.execute(new CreatePublicProjectController.AnalysDeal(queueMap));
        return resp;
    }

    /*公开型项目修改保存*/
    @RequestMapping("/update_public_project")
    @ResponseBody
    public Object updatePublicProject(HttpServletRequest request,
            @RequestParam(value = "projectId", required = false,defaultValue = "")  String projectId ,
            @RequestParam(value = "trainBeginTime", required = false,defaultValue = "")  String trainBeginTime ,
            @RequestParam(value = "trainEndTime", required = false,defaultValue = "")  String trainEndTime ,
            @RequestParam(value = "exerciseBeginTime", required = false,defaultValue = "")  String exerciseBeginTime ,
            @RequestParam(value = "exerciseEndTime", required = false,defaultValue = "")  String exerciseEndTime ,
            @RequestParam(value = "examBeginTime", required = false,defaultValue = "")  String examBeginTime ,
            @RequestParam(value = "examEndTime", required = false,defaultValue = "")  String examEndTime,
            @RequestParam(value = "project_train_Time", required = false,defaultValue = "")  String project_train_Time,
            @RequestParam(value = "project_exercise_Time", required = false,defaultValue = "")  String project_exercise_Time,
            @RequestParam(value = "project_exam_Time", required = false,defaultValue = "")  String project_exam_Time,
            @RequestParam(value = "intRetestTime", required = false,defaultValue = "")  String intRetestTime
    ) throws Exception {
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        User user=getCurrUser(request);
        String beginTime = DateUtils.getMinDate(trainBeginTime, exerciseBeginTime, examBeginTime);      // 项目开始时间
        String endTime = DateUtils.getBigDate(trainEndTime, exerciseEndTime, examEndTime);              //项目结束时间
        Calendar car1 = Calendar.getInstance();car1.setTime(sdf.parse(beginTime));
        Calendar car2 = Calendar.getInstance();car2.setTime(sdf.parse(endTime));
        int days = DateUtils.getDaysBetween(car1,car2);            //开始时间和结束时间的时间间隔
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("projectId",projectId);
        params.put("trainBeginTime",trainBeginTime);
        params.put("trainEndTime",trainEndTime);
        params.put("exerciseBeginTime",exerciseBeginTime);
        params.put("exerciseEndTime",exerciseEndTime);
        params.put("examBeginTime",examBeginTime);
        params.put("examEndTime",examEndTime);
        params.put("intRetestTime",intRetestTime);
        params.put("trainPeriod",days);
        // 保存 (修改projectBasic)
        createPublicProjectService.updatePublicProject(params, user);

        // 发送到activemq的cppub.queue(修改项目详情)
        Map<String,Object> queueMap = new ConcurrentHashMap<String, Object>();
        queueMap.put("queueNo","updateProjectInfo");
        queueMap.put("projectId",projectId);
        queueMap.put("projectStartTime",beginTime);
        queueMap.put("projectEndTime",endTime);
        queueMap.put("project_train_Time",project_train_Time);
        queueMap.put("project_exercise_Time",project_exercise_Time);
        queueMap.put("project_exam_Time",project_exam_Time);
        queueMap.put("intRetestTime",intRetestTime);
        queueMap.put("intTrainPeriod",days);
        queueMap.put("operUser",user.getUserName());
        queueMap.put("operTime",sdf.format(new Date()));
        EXECUTOR.execute(new CreatePublicProjectController.AnalysDeal(queueMap));
        return resp;
    }

    /**
     * 公开型项目课程保存
     * @param request
     * @param project_id
     * @param course_ids
     * @return
     * @throws Exception
     */
    @RequestMapping("/save_public_course")
    @ResponseBody
    public Object savePublicCourse(HttpServletRequest request,
            @RequestParam(value = "course_project_id", required = false,defaultValue = "")  String project_id,     //项目id
            @RequestParam(value = "course_ids", required = false,defaultValue = "")  String course_ids      //课程ids
    ) throws Exception {
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        User user = getCurrUser(request);
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("project_id",project_id);
        params.put("course_ids",course_ids);
        //项目课课程保存
        projectCourseService.saveBatch_Public(params,getCurrUser(request));

        // 发送到activemq的cppri.queue (异步修改组卷策略初始数据)
        Map <String, Object> queueMap = new ConcurrentHashMap <String, Object>();
        queueMap.put("queueNo", "updateExamStrategy");
        queueMap.put("id", project_id);
        queueMap.put("roleId", "-1");
        queueMap.put("userName", user.getUserName());
        EXECUTOR.execute(new CreatePublicProjectController.AnalysDeal(queueMap));
        return resp;
    }

    /**
     * 公开型项目课程删除
     * @param request
     * @param projectId
     * @param courseIds
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete_project_course")
    @ResponseBody
    public Object deletePublicCourse(HttpServletRequest request,
           @RequestParam(value = "projectId", required = false,defaultValue = "")  String projectId,     //项目id
           @RequestParam(value = "courseIds", required = false,defaultValue = "")  String courseIds      //课程ids
    ) throws Exception {
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        User user = getCurrUser(request);
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        List<String> ids = Arrays.asList(courseIds.split(","));
        params.put("projectId",projectId);
        params.put("courseIds",ids);
        //项目课程删除
        projectCourseService.deleteProjectCourse(params);
        // 发送到activemq的cppri.queue (异步修改组卷策略初始数据)
        Map <String, Object> queueMap = new ConcurrentHashMap <String, Object>();
        queueMap.put("queueNo", "updateExamStrategy");
        queueMap.put("id", projectId);
        queueMap.put("roleId", "-1");
        queueMap.put("userName", user.getUserName());
        EXECUTOR.execute(new CreatePublicProjectController.AnalysDeal(queueMap));
        return resp;
    }


    /**
     * 项目课程列表(公开型项目)
     * @param request
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/course_list")
    @ResponseBody
    public Object public_course_list(HttpServletRequest request,
             @RequestParam(value="course_project_id", required = false, defaultValue = "") String course_project_id,
             @RequestParam(value="course_name", required = false, defaultValue = "") String course_name,
             @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
             @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response<Object> resp = new Response<Object>();

        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("courseName", ParamsUtil.joinLike(course_name));
        params.put("projectId", course_project_id);

        Integer count = projectCourseService.selectCount(params);

        page = new Page(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("endNum", page.getPageSize());
        List<Map<String, Object>> listMap = projectCourseService.selectList(params);
        page.setDataList(listMap);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);
        return resp;
    }

    /**
     * 公开型项目单位保存
     * @param request
     * @param project_id
     * @param company_ids
     * @return
     * @throws Exception
     */
    @RequestMapping("/save_public_company")
    @ResponseBody
    public Object savePublicCompany(HttpServletRequest request,
            @RequestParam(value = "company_project_id", required = false,defaultValue = "")  String project_id,  //项目id
            @RequestParam(value = "company_ids", required = false,defaultValue = "")  String company_ids,      //单位ids
            @RequestParam(value = "datBeginTime", required = false,defaultValue = "")  String datBeginTime ,
            @RequestParam(value = "datEndTime", required = false,defaultValue = "")  String datEndTime
    ) throws Exception {
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("project_id",project_id);
        params.put("company_ids",company_ids);
        params.put("datBeginTime",datBeginTime);
        params.put("datEndTime",datEndTime);
        //项目单位保存*
        companyProjectService.saveProjectCompany(params,getCurrUser(request));

        // 发送到activemq的cppub.queue
        Map<String,Object> queueMap = new ConcurrentHashMap<String, Object>();
        queueMap.put("queueNo","projectDossier");
        queueMap.put("project_id",project_id);
        queueMap.put("company_ids",company_ids);
        queueMap.put("datBeginTime",datBeginTime);
        queueMap.put("datEndTime",datEndTime);
        EXECUTOR.execute(new CreatePublicProjectController.AnalysDeal(queueMap));
        return resp;
    }

    /**
     * 公开型项目单位删除
     * @param projectId
     * @param companyIds
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete_project_company")
    @ResponseBody
    public Object deletePublicCompany(
            @RequestParam(value = "projectId", required = false,defaultValue = "")  String projectId,        //项目id
            @RequestParam(value = "companyIds", required = false,defaultValue = "")  String companyIds         //单位ids
    ) throws Exception {
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        List<String> ids = Arrays.asList(companyIds.split(","));
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("projectId",projectId);
        params.put("companyIds",ids);
        //项目单位删除
        companyProjectService.deleteProjectCompany(params);

        // 发送到activemq的cppub.queue
        Map<String,Object> queueMap = new ConcurrentHashMap<String, Object>();
        queueMap.put("queueNo","deleteProjectDossier");
        queueMap.put("projectId",projectId);
        queueMap.put("companyIds",ids);
        EXECUTOR.execute(new CreatePublicProjectController.AnalysDeal(queueMap));
        return resp;
    }

    /**
     * 培训单位列表(创建公开型项目)
     * @param request
     * @param company_project_id
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/company_list")
    @ResponseBody
    public Object company_list(HttpServletRequest request,
            @RequestParam(value="company_project_id", required = false, defaultValue = "") String company_project_id,
            @RequestParam(value="company_name", required = false, defaultValue = "") String company_name,
            @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
            @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response<Object> resp = new Response<Object>();

        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }

        Page<Map<String, Object>> page = new Page<Map<String, Object>>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("project_id", company_project_id);
        List<String> companyIds = companyProjectService.selectCompanyIdsByProjectId(company_project_id);
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        Integer count = 1;
        if(companyIds.size()>0){
            params.put("companyIds", companyIds);
            params.put("varName", ParamsUtil.joinLike(company_name));
            count = companyService.select_count(params);

            page = new Page(count, pageNum, pageSize);
            params.put("startNum", page.getStartNum());
            params.put("endNum", page.getPageSize());
            listMap = companyService.select_list(params);
        }
        page = new Page(count, pageNum, pageSize);
        page.setDataList(listMap);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);
        return resp;
    }

    /**
     * 公开型项目课程必选题量应修学时修改
     * @param id
     * @param projectId
     * @param requirement
     * @return
     * @throws Exception
     */
    @RequestMapping("/edit_ajax")
    @ResponseBody
    public Object update_requirement(
            @RequestParam(value = "id", required = false,defaultValue = "")  String id,                  //主键id
            @RequestParam(value = "projectId", required = false,defaultValue = "")  String projectId,   //项目id
            @RequestParam(value = "requirement", required = false,defaultValue = "")  String requirement, //要修改的学时要求
            @RequestParam(value = "selectCount", required = false,defaultValue = "")  String selectCount  //要修改的必选题量
    ) throws Exception {
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("projectId",projectId);
        params.put("id",id);
        params.put("requirement",requirement);
        params.put("selectCount",selectCount);
        params.put("operTime", DateUtils.formatDateTime(new Date()));
        projectCourseService.update(params);
        return resp;
    }


    /*公开型项目发布*/
    @RequestMapping("/project_publish")
    @ResponseBody
    public Object project_publish(HttpServletRequest request,
              @RequestParam(value = "project_id", required = false,defaultValue = "")  String project_id,
              @RequestParam(value = "project_type", required = false,defaultValue = "")  String project_type,
              @RequestParam(value="startTime", required = false, defaultValue = "") String startTime,
              @RequestParam(value="endTime", required = false, defaultValue = "") String endTime,
              @RequestParam(value="type", required = false) Integer type
    ) throws Exception {
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        User user = getCurrUser(request);
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        if("3567".contains(project_type)){   // 含有考试的项目需要判断组卷策略合法性
            String result = examStrategyService.checkStrategy(project_id);
            if(!result.equals("")){
                resp.setResult(result);
                return resp;
            }
        }
        if(StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)){
            ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(project_id);
            if(null != projectInfo){
                startTime = projectInfo.getProjectStartTime();
                endTime = projectInfo.getProjectEndTime();
            }
        }
        params.put("projectId", project_id);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("type", type);
        params.put("operUser", user.getUserName());
        projectManagerService.updateProjectPublish(params);
        /*// 发布
        projectBasicService.publishProject(params, getCurrUser(request));

        // 发送到activemq的cppub.queue
        Map<String,Object> queueMap = new ConcurrentHashMap<String, Object>();
        queueMap.put("queueNo","projectPublish");
        queueMap.put("project_id",project_id);
        EXECUTOR.execute(new CreatePublicProjectController.AnalysDeal(queueMap));*/
        return resp;
    }

    /*公开型项目删除*/
    @RequestMapping("/project_delete")
    @ResponseBody
    public Object project_delete(HttpServletRequest request,
            @RequestParam(value = "project_id", required = false,defaultValue = "")  String project_id
    ) throws Exception {
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("projectId",project_id);
        // 删除
        projectBasicService.delete(params);

        // 发送到activemq的cppub.queue  (异步删除项目相关数据)
        Map<String,Object> queueMap = new ConcurrentHashMap<String, Object>();
        queueMap.put("queueNo","projectDelete");
        queueMap.put("project_id",project_id);
        EXECUTOR.execute(new CreatePublicProjectController.AnalysDeal(queueMap));
        return resp;
    }


    /**
     * 异步处理数据
     */
    private class AnalysDeal extends Thread {

        private Map<String,Object> queueMap;

        public AnalysDeal(Map<String,Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queueCPPubSender.sendMapMessage("tp.cppub.queue", queueMap);
            }catch(Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }
}
