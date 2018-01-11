package com.bossien.train.controller;


import com.bossien.framework.db.DataSource;
import com.bossien.framework.interceptor.SignInterceptor;
import com.bossien.framework.mq.sender.*;
import com.bossien.train.domain.*;
import com.bossien.train.domain.eum.ProjectTypeEnum;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.kit.PermissionUtil;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.*;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.JsonUtils;
import com.bossien.train.util.ParamsUtil;
import com.bossien.train.util.PropertiesUtils;
import com.google.gson.Gson;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
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


@Controller
@RequestMapping("/admin/project_create/private")
public class CreatePrivateProjectController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    @Autowired
    private QueueCPPriSender queueCPPriSender;
    @Autowired
    private QueueTJSender queueTJSender;
    @Autowired
    private QueueProjectCourseSender queueProjectCourseSender;
    @Autowired
    private QueueProjectUserSender queueProjectUserSender;
    @Autowired
    private QueueUdpSender queueUdpSender;
    @Autowired
    private QueueProjectPublishSender queueProjectPublishSender;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private ITrainRoleService trainRoleService;
    @Autowired
    private ICreatePrivateProjectService createPrivateProjectService;
    @Autowired
    private IProjectBasicService projectBasicService;
    @Autowired
    private IProjectCourseService projectCourseService;
    @Autowired
    private IProjectUserService projectUserService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private ICompanyTrainRoleService companyTrainRoleService;
    @Autowired
    private ITrainSubjectService trainSubjectService;
    @Autowired
    private IProjectDepartmentService projectDepartmentService;
    @Autowired
    private IProjectRoleService projectRoleService;
    @Autowired
    private IExamStrategyService examStrategyService;
    @Autowired
    private ICompanyService companyService;

    // 创建私有项目页面
    @RequestMapping("/create_private_project")
    public ModelAndView create_project(@RequestParam(value = "projectTypeNo", required = false, defaultValue = "") String projectTypeNo, //项目类型
                                       @RequestParam(value = "subjectId", required = false, defaultValue = "") String subjectId,           //主题编号
                                       @RequestParam(value = "subjectName", required = false, defaultValue = "") String subjectName,       //主题名
                                       HttpServletRequest request) throws ParseException {
        User user = getCurrUser(request);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 项目开始时间－默认第二天开始-yyyy-MM-dd
        String projectBeginTime = sdf.format(DateUtils.getNextDay1(new Date()));
        // 项目结束时间-一个月的长度-yyyy-MM-dd
        String projectEndTime = sdf.format(DateUtils.getNextMouthDay(new Date()));
        // 考试开始时间－默认第二天开始-yyyy-MM-dd HH:mm:ss
        String examBeginTime = sdf.format(DateUtils.getNextDay1(new Date()));
        // 考试结束时间-一个月的长度-yyyy-MM-dd HH:mm:ss
        String examEndTime = sdf.format(DateUtils.getNextMouthDay(new Date()));
        //项目默认名称
        String projectName = sdf.format(new Date()).substring(0, 10);
        //创建项目基础信息
        ProjectBasic projectBasic = projectBasicService.build(user);
        //获取系统时间
        String systemTime = sdf.format(new Date());
        return new ModelAndView("admin/project_create/private/step_1")
                .addObject("projectTypeNo", projectTypeNo)
                .addObject("subjectId", subjectId)
                .addObject("subjectName", subjectName)
                .addObject("projectBeginTime", projectBeginTime)
                .addObject("projectEndTime", projectEndTime)
                .addObject("examBeginTime", examBeginTime)
                .addObject("examEndTime", examEndTime)
                .addObject("systemTime", systemTime)
                .addObject("intRetestTime", "1")
                .addObject("projectName", subjectName + " " + projectName )
                .addObject("projectBasic", projectBasic)
                .addObject("permissionExercise", PermissionUtil.hasPermissionQuestion(projectTypeNo))    //是否含有练习
                .addObject("permissionTrain", PermissionUtil.hasPermissionStudy(projectTypeNo))          //是否含有培训
                .addObject("permissionExam", PermissionUtil.hasPermissionExam(projectTypeNo))            //是否含有考试
                .addObject("examType", ProjectTypeEnum.QuestionType_3.getValue());      //单独考试

    }

    /**
     * 初始化受训角色
     *
     * @return
     */
    @RequestMapping(value = "/trainRole")
    @ResponseBody
    public Object InitTrainRole(HttpServletRequest request,
                                @RequestParam(value = "pageSize", required = true, defaultValue = "10") Integer pageSize,     //每页条数
                                @RequestParam(value = "pageNum", required = true, defaultValue = "1") Integer pageNum,       //页码
                                @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId        //项目Id
    ) {
        User user = getCurrUser(request);
        Response<Object> response = new Response<Object>();
        Object object = null;
        String companyId = user.getCompanyId();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("companyId", companyId);
        params.put("IsValid", "1");

        List<String> companyTrainRoles=companyTrainRoleService.selectByCompanyId(companyId);
        List<Map<String,Object>> trainRoles=null;
        int count=0;
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();

        if(companyTrainRoles != null && companyTrainRoles.size()>0){
            params.put("ids", companyTrainRoles);
            count=trainRoleService.selectCount(params);
            page = new Page(count, pageNum, pageSize);
            params.put("startNum", page.getStartNum());
            params.put("endNum", page.getPageSize());
            //查询公司下所有的的受训色
            trainRoles = trainRoleService.selectTrainRoles(params);
            //查询该公司在么一项目的受训角色
            if(StringUtils.isNotEmpty(projectId)){
                params.put("projectId", projectId);
                params.put("roleId", PropertiesUtils.getValue("role_id"));
                List<Map<String, Object>> projectRoles = projectRoleService.selectProjectRole(params);
                if(projectRoles != null && projectRoles.size()>0){
                    for (Map<String, Object> trainRole: trainRoles) {
                        String trainRoleId = trainRole.get("varId").toString();
                        trainRole.put("selected","");
                        for (Map<String, Object> projectRole: projectRoles) {
                            String projectRoleId = projectRole.get("roleId").toString();
                            if(trainRoleId.equals(projectRoleId)){
                                trainRole.put("selected","selected");
                            }
                        }
                    }
                }
            }
        }

        page.setDataList(trainRoles);

        response.setResult(page);
        return response;
    }

    /**
     * 初始化受训单位
     *
     * @return
     */
    @RequestMapping(value = "/trainDepartment")
    @ResponseBody
    public Object InitTrainDepartment(HttpServletRequest request, @RequestParam(value = "deptName", required = false, defaultValue = "") String deptName     //  部门名称
    ) {
        User user = getCurrUser(request);
        Response<Object> response = new Response<Object>();

        String companyId = user.getCompanyId();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("companyId", companyId);
        params.put("deptName", deptName);
        Object object = null;


        //根据查询公司的部门
       /* List<Department> departments = departmentService.selectDepartmentByCompanyId(params);
        if (null != departments && departments.size() > 0) {
            object = departmentService.assembleDepartmentData(departments);

        }*/
        List<String> clist=new ArrayList<String>();
        String cids = companyService.getChildCompanyIds(user.getCompanyId());
        if(cids!=null){
            String[] ids=cids.split(",");
            if(ids.length>0){
                for(int n=0;n<ids.length;n++){
                    clist.add(ids[n]);
                }
            }
        }
        List<Company> companylist = companyService.getChildCompanyList(clist);
        object = companyService.getDepts(companylist);
        response.setResult(object);
        return response;
    }

    /**
     * 保存项目基础信息
     *
     * @param projectBasic
     * @param request
     * @param trainPeriod
     * @param departments
     * @return
     */
    @Transactional(DataSource.BUS)   //培训库的事物
    @RequestMapping(value = "/save")
    @ResponseBody
    public Object saveProjectBasic(ProjectBasic projectBasic, HttpServletRequest request,
                                   @RequestParam(value = "subjectName", required = false, defaultValue = "") String subjectName,
                                   @RequestParam(value = "trainBeginTime", required = false, defaultValue = "") String trainBeginTime,
                                   @RequestParam(value = "trainEndTime", required = false, defaultValue = "") String trainEndTime,
                                   @RequestParam(value = "exerciseBeginTime", required = false, defaultValue = "") String exerciseBeginTime,
                                   @RequestParam(value = "exerciseEndTime", required = false, defaultValue = "") String exerciseEndTime,
                                   @RequestParam(value = "examBeginTime", required = false, defaultValue = "") String examBeginTime,
                                   @RequestParam(value = "examEndTime", required = false, defaultValue = "") String examEndTime,
                                   @RequestParam(value = "trainPeriod", required = false, defaultValue = "") String trainPeriod,
                                   @RequestParam(value = "intRetestTime", required = false, defaultValue = "") String intRetestTime,
                                   // @RequestParam(value = "roles", required = false, defaultValue = "") String roles,
                                   @RequestParam(value = "departments", required = false, defaultValue = "") String departments) throws Exception {
        User user = getCurrUser(request);

        Response<Object> response = new Response<Object>();

        projectBasic.setTrainPeriod(new Integer(trainPeriod));
        Map<String, Object> params = new HashedMap();
        params.put("projectBasic", projectBasic);
        params.put("departments", departments);
        // params.put("roles", roles);

        createPrivateProjectService.saveProjectBasicInfo(params, user);

        // 发送到activemq的cppri.queue
        Map<String, Object> queueMap = new ConcurrentHashMap<String, Object>();
        queueMap.put("operateType", "saveProject");
        queueMap.put("id", projectBasic.getId());
        queueMap.put("companyId", user.getCompanyId());
        queueMap.put("intRetestTime", intRetestTime);
        queueMap.put("subject_name", subjectName);
        queueMap.put("trainBeginTime", trainBeginTime);
        queueMap.put("trainEndTime", trainEndTime);
        queueMap.put("exerciseBeginTime", exerciseBeginTime);
        queueMap.put("exerciseEndTime", exerciseEndTime);
        queueMap.put("examBeginTime", examBeginTime);
        queueMap.put("examEndTime", examEndTime);
        queueMap.put("departments", departments);
        queueMap.put("user", JsonUtils.writeValueAsString(user));
        queueMap.put("projectType", projectBasic.getProjectType());
        EXECUTOR.execute(new CreatePrivateProjectController.AnalysDeal(queueMap));

       /* Map<String, Object> param = new HashMap<String, Object>();
        param.put("companyId", user.getCompanyId());
        param.put("userName", user.getUserName());
        param.put("operType", "update");
        //消息推送（统计）
        EXECUTOR.execute(new AnalysDeal_Tj(param));*/

        /*发送项目预发布的数据*/




        response.setCode(Code.SUCCESS.getCode());
        //response.setCode(10000);
        // response.setResult(roles);
        return response;
    }


    /**
     * 上一步修改基本信息
     * @param request
     * @param projectId
     * @param projectTypeNo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/basicInfoModify")
    public ModelAndView basicInfoModify(HttpServletRequest request,String projectId,String projectTypeNo,String projectStatus)throws  Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ModelAndView mv = new ModelAndView();
        //根据projectId和companyId查询受训单位
        Map<String,Object>  params=new HashMap<String,Object>();
        params.put("projectId" ,projectId);
        //根据projectId查询项目详情
        ProjectInfo projectInfo=projectInfoService.selectProjectInfoById(projectId);
        ProjectBasic projectBasic=projectBasicService.selectById(projectId);
        //获取系统时间
        String systemTime = sdf.format(new Date());

        boolean  trainPermission = false;
        boolean  exercisePermission = false;
        boolean  examPermission = false ;

        if(null!=projectBasic){
            //培训项目开始时间和结束时间处理
            if(!"".equals(projectBasic.getProjectTrainInfo()) && null!=projectBasic.getProjectTrainInfo()){
                Map<String,Object> map1=new Gson().fromJson(projectBasic.getProjectTrainInfo(),Map.class);
                if(sdf.parse(map1.get("beginTime").toString()).before(new Date())){
                    trainPermission = true;
                }
                mv.addObject("trainStartTime",map1.get("beginTime").toString());
                mv.addObject("trainEndTime",map1.get("endTime").toString());
            }

            //练习项目开始时间和结束时间处理
            if(!"".equals(projectBasic.getProjectExerciseInfo()) && null!=projectBasic.getProjectExerciseInfo()){
                Map<String,Object> map2=new Gson().fromJson(projectBasic.getProjectExerciseInfo(),Map.class);
                if(sdf.parse(map2.get("beginTime").toString()).before(new Date())){
                    exercisePermission = true;
                }
                mv.addObject("exerciseStartTime",map2.get("beginTime").toString());
                mv.addObject("exerciseEndTime",map2.get("endTime").toString());

            }

            //考试项目开始时间和结束时间处理
            if(!"".equals(projectBasic.getProjectExamInfo()) && null!=projectBasic.getProjectExamInfo()){
                Map<String,Object> map3=new Gson().fromJson(projectBasic.getProjectExamInfo(),Map.class);
                if(sdf.parse(map3.get("beginTime").toString()).before(new Date())){
                    examPermission = true;
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

        }

        mv.setViewName("admin/project_create/private/basicInfoModify");
        mv.addObject("projectTypeNo",projectTypeNo);
        mv.addObject("projectStatus",projectStatus);
        mv.addObject("projectBasic",projectBasic);
        mv.addObject("projectInfo",projectInfo);
        mv.addObject("systemTime",systemTime);
        mv.addObject("permissionExercise",PermissionUtil.hasPermissionQuestion(projectTypeNo));    //是否含有练习
        mv.addObject("permissionTrain",PermissionUtil.hasPermissionStudy(projectTypeNo));          //是否含有培训
        mv.addObject("permissionExam",PermissionUtil.hasPermissionExam(projectTypeNo));            //是否含有考试
        mv.addObject("trainPermission",trainPermission);                                           //培训项目是否开始
        mv.addObject("exercisePermission",exercisePermission);                                     //练习项目是否开始
        mv.addObject("examPermission",examPermission);                                             //考试项目是否开始
        return mv;
    }
    /**
     * 项目单位
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/projectDepartment")
    @ResponseBody
    public Object projectDepartment(String projectId,HttpServletRequest request){
        User user=getCurrUser(request);
        Response<Object> response=new Response <Object>();
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("projectId",projectId);
        List<ProjectDepartment> projectDepartments=projectDepartmentService.selectByProjectIdAndCompanyId(params);
        response.setResult(projectDepartments);
        return response;
    }
    /**
     *保存修改信息
     * @return
     */
    @Transactional(DataSource.BUS)    //培训库事物
    @RequestMapping(value = "/updateProject")
    @ResponseBody
    public Object updateProject(HttpServletRequest request,
                                //@RequestParam(value = "roleNames", required = false,defaultValue = "")  String roleNames,                           //受训角色名称
                                //@RequestParam(value = "roles", required = false,defaultValue = "")  String roles,                                    //受训角色
                                @RequestParam(value = "departments", required = false,defaultValue = "")  String departments,                        //受训部门
                                @RequestParam(value = "projectId", required = false,defaultValue = "")  String projectId,                            //项目id
                                @RequestParam(value = "projectType", required = false,defaultValue = "")  String projectType,                        //项目类型
                                @RequestParam(value = "projectName", required = false,defaultValue = "")  String projectName,                        //项目名称
                                @RequestParam(value = "projectStatus", required = false,defaultValue = "")  String projectStatus,                    //项目名称
                                @RequestParam(value = "project_train_info", required = false,defaultValue = "")  String project_train_info,          //培训项目时间json
                                @RequestParam(value = "project_exercise_info", required = false,defaultValue = "") String  project_exercise_info,    //练习项目时间json
                                @RequestParam(value = "project_exam_info", required = false,defaultValue = "")  String project_exam_info,            //考试项目时间json
                                @RequestParam(value = "project_train_Time", required = false,defaultValue = "")  String project_train_Time,
                                @RequestParam(value = "project_exercise_Time", required = false,defaultValue = "")  String project_exercise_Time,
                                @RequestParam(value = "project_exam_Time", required = false,defaultValue = "")  String project_exam_Time,
                                @RequestParam(value = "trainBeginTime", required = false,defaultValue = "")  String trainBeginTime,
                                @RequestParam(value = "trainEndTime", required = false,defaultValue = "")  String trainEndTime,
                                @RequestParam(value = "exerciseBeginTime", required = false,defaultValue = "")  String exerciseBeginTime,
                                @RequestParam(value = "exerciseEndTime", required = false,defaultValue = "")  String exerciseEndTime,
                                @RequestParam(value = "examBeginTime", required = false,defaultValue = "")  String examBeginTime,
                                @RequestParam(value = "examEndTime", required = false,defaultValue = "")  String examEndTime,
                                @RequestParam(value = "intRetestTime", required = false, defaultValue = "") String intRetestTime
    )throws Exception{
        User user=getCurrUser(request);
        String beginTime = DateUtils.getMinDate(trainBeginTime, exerciseBeginTime, examBeginTime);
        String endTime = DateUtils.getBigDate(trainEndTime, exerciseEndTime, examEndTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String projectTrainTime = "";
        String projectExerciseTime = "";
        String projectExamTime = "";
        Map<String,Object> param_=new HashMap<String,Object>();
        param_.put("projectId",projectId);
        //获取项目部门信息
        List<ProjectDepartment> pList = projectDepartmentService.selectByProjectIdAndCompanyId(param_);
        //修改projectBasic表的数据
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("projectId",projectId);
        params.put("projectType",projectType);
        params.put("projectName",projectName);
        params.put("projectTrainInfo",project_train_info);
        params.put("projectExerciseInfo",project_exercise_info);
        params.put("projectExamInfo",project_exam_info);
        if(StringUtils.isNotEmpty(trainBeginTime) && StringUtils.isNotEmpty(trainEndTime)){
            projectTrainTime = trainBeginTime.substring(0,10) + " 至 " + trainEndTime.substring(0,10);
        }
        if(StringUtils.isNotEmpty(exerciseBeginTime) && StringUtils.isNotEmpty(exerciseEndTime)){
            projectExerciseTime = exerciseBeginTime.substring(0,10) + " 至 " + exerciseEndTime.substring(0,10);
        }
        if(StringUtils.isNotEmpty(examBeginTime) && StringUtils.isNotEmpty(examEndTime)){
            projectExamTime = examBeginTime.substring(0,16) + " 至 " + examEndTime.substring(0,16);
        }
        params.put("projectTrainTime",projectTrainTime);
        params.put("projectExerciseTime",projectExerciseTime);
        params.put("projectExamTime",projectExamTime);
        params.put("operUser",user.getUserName());
        params.put("operTime",sdf.format(new Date()));
       // params.put("roles",roles);
       // params.put("roleName",roleNames );
        params.put("departments",departments);
        params.put("projectStartTime",beginTime);
        params.put("projectEndTime",endTime);
        params.put("intRetestTime",intRetestTime);
        params.put("personCount",projectUserService.selectCount(params));
        if("2".equals(projectStatus)){
            projectStatus = DateUtils.getProjectStatus(beginTime,endTime,projectStatus);
        }
        params.put("projectStatus",projectStatus);
        params.put("pList",new Gson().toJson(pList));
        params.put("companyId", user.getCompanyId());
        createPrivateProjectService.updateProject(user,params);

        //消息推送（修改）
        params.put("operType","update");
        params.put("user", JsonUtils.writeValueAsString(user));
        EXECUTOR.execute(new AnalysDeal_UD(params));

       /* Map<String,Object> param=new HashMap<String,Object>();
        param.put("companyId",getCurrUser(request).getCompanyId());
        param.put("userName",getCurrUser(request).getUserName());
        param.put("operType","update");
        //消息推送（统计）到tj.queue
        EXECUTOR.execute(new AnalysDeal_Tj(param));*/

        Response<Object> response=new Response <Object>();
        response.setCode(Code.SUCCESS.getCode());
        return response;
    }


    /**
     * 第二步操作（选择课程）
     *
     * @param request
     * @param projectTypeNo
     * @param projectId
     * @return
     */
    @RequestMapping("/step2")
    public ModelAndView step2(HttpServletRequest request,
                              @RequestParam(value = "projectTypeNo", required = false, defaultValue = "") String projectTypeNo,  //项目类型
                              @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId,           //项目编号
                              @RequestParam(value = "projectStatus", required = false, defaultValue = "") String projectStatus           //项目状态

    )throws  Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ModelAndView mv = new ModelAndView("admin/project_create/private/step_2");
        ProjectBasic projectBasic=projectBasicService.selectById(projectId);


        if(null!=projectBasic){
            //考试项目开始时间和结束时间处理
            if(!"".equals(projectBasic.getProjectExamInfo()) && null!=projectBasic.getProjectExamInfo()){
                Map<String,Object> map=new Gson().fromJson(projectBasic.getProjectExamInfo(),Map.class);
                if(sdf.parse(map.get("beginTime").toString()).before(new Date())){
                    mv.addObject("examPermission",true);
                }else{
                    mv.addObject("examPermission",false);
                }
            }


        }
        Map<String, Object> params = new HashedMap();
        params.put("roleId", PropertiesUtils.getValue("role_id"));
        params.put("projectId", projectId);
        boolean flag = true;
        //查询该项目下是否有不是默认角色的角色课程信息
        List<ProjectCourse> list = projectCourseService.selectProjectCourses(params);
        if (list != null && list.size()>0) {
            flag = false;
        }
        mv.addObject("flag", flag);
        mv.addObject("projectTypeNo", projectTypeNo);
        mv.addObject("projectId", projectId);
        mv.addObject("projectStatus", projectStatus);
        mv.addObject("permissionExercise", PermissionUtil.hasPermissionQuestion(projectTypeNo));    //是否含有练习
        mv.addObject("permissionTrain", PermissionUtil.hasPermissionStudy(projectTypeNo));          //是否含有培训
        mv.addObject("permissionExam", PermissionUtil.hasPermissionExam(projectTypeNo));            //是否含有考试;
        return mv;
    }



    /**
     * 保存项目课程
     *
     * @param request
     * @param courseIds
     * @return
     * @throws Exception
     */
    @Transactional(DataSource.BUS)
    @RequestMapping(value = "/save_project_course")
    @ResponseBody
    public Object saveProjectCourse(HttpServletRequest request,
                                    @RequestParam(value = "projectTypeNo", required = false, defaultValue = "") String projectTypeNo,
                                    @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId,
                                    @RequestParam(value = "roles", required = false, defaultValue = "") String roles,
                                    @RequestParam(value = "courseIds", required = false, defaultValue = "") String[] courseIds
    ) throws Exception {
        User user = getCurrUser(request);
        Map<String, Object> param = new HashedMap();
        param.put("projectId", projectId);
        param.put("roles", roles);
        param.put("courseIds", courseIds);
        param.put("projectType", projectTypeNo);
        List<String> courseList = createPrivateProjectService.saveProjectCourseInfo(param, user);
        if(!CollectionUtils.isEmpty(courseList)){
            // 发送到activemq的cppri.queue
            Class<Map> clazz = Map.class;
            //获取角色集合
            List<Map> roleList = JsonUtils.joinToList(roles, clazz);
            Map<String, Object> queueMap = new ConcurrentHashMap<String, Object>();
            queueMap.put("operateType", "saveCourse");
            queueMap.put("courseList", courseList);
            queueMap.put("id", projectId);
            queueMap.put("roleList", roleList);
            queueMap.put("userName", user.getUserName());
            queueMap.put("projectType", projectTypeNo);
            EXECUTOR.execute(new CreatePrivateProjectController.AnalysDeal_PC(queueMap));
        }
        Response<Object> response = new Response<>();
        response.setCode(Code.SUCCESS.getCode());
        return response;
    }

    /**
     * 根据属性查询项目课程信信息
     *
     * @param projectId
     * @param roleId
     * @param courseName
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/project_course_list")
    @ResponseBody
    public Object projectCourseList(@RequestParam(value = "projectId", required = false, defaultValue = "") String projectId,  //项目id
                                    @RequestParam(value = "roleId", required = false, defaultValue = "") String roleId,      //角色id
                                    @RequestParam(value = "courseName", required = false, defaultValue = "") String courseName,      //角色id
                                    @RequestParam(value = "pageSize", required = true, defaultValue = "1") Integer pageSize,     //每页条数
                                    @RequestParam(value = "pageNum", required = true, defaultValue = "1") Integer pageNum        //页码
    ) throws ServiceException {
        Response<Object> response = new Response<Object>();
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();
        Map<String, Object> params = new HashedMap();
        params.put("projectId", projectId);
        params.put("roleId", roleId);
        params.put("courseName", ParamsUtil.joinLike(courseName));
        params.put("startNum", (pageNum - 1) * pageSize);
        params.put("endNum", pageSize);
        //项目课程信息
        List<Map<String, Object>> result = projectCourseService.selectList(params);
        //项目课程数量
        int count = projectCourseService.selectCount(params);
        page = new Page(count, pageNum, pageSize);
        response.setCode(Code.SUCCESS.getCode());
        page.setDataList(result);
        response.setResult(page);
        return response;
    }

    /**
     * 删除项目课程
     *
     * @return
     * @throws Exception
     */
    @Transactional(DataSource.BUS)
    @RequestMapping(value = "/delete_project_course")
    @ResponseBody
    public Object deleteProjectCourse(HttpServletRequest request,
                                      @RequestParam(value = "projectType", required = false, defaultValue = "") String projectType,  //项目类型
                                      @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId,  //项目id
                                      @RequestParam(value = "courseIds", required = false, defaultValue = "") String[] courseIds
                                      ) throws Exception {
        User user = getCurrUser(request);
        //获取课程Id集合
        List<String> projectCourseIds = Arrays.asList(courseIds);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("project_id", projectId);
        param.put("courseIds", projectCourseIds);
        createPrivateProjectService.deleteProjectCourse(param);
        // 发送到activemq的cppri.queue
        Map<String, Object> queueMap = new ConcurrentHashMap<String, Object>();
        queueMap.put("operateType", "deleteCourse");
        queueMap.put("courseIds", projectCourseIds);
        queueMap.put("id", projectId);
        queueMap.put("projectType", projectType);
        queueMap.put("userName", user.getUserName());
        EXECUTOR.execute(new CreatePrivateProjectController.AnalysDeal_PC(queueMap));
        Response<Object> response = new Response<>();
        response.setCode(Code.SUCCESS.getCode());
        return response;
    }

    /**
     * 选择课程的上一步操作（基本信息操作）
     *
     * @param projectTypeNo
     * @param projectId
     * @param request
     * @return
     * @throws ParseException
     */
    // 创建私有项目页面
    @RequestMapping("/prevOper_step2")
    public ModelAndView prevOperStep2(@RequestParam(value = "projectTypeNo", required = false, defaultValue = "") String projectTypeNo, //项目类型
                                      @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId,  //项目id
                                      HttpServletRequest request) throws ParseException {
        User user = getCurrUser(request);
        ProjectBasic projectBasic = projectBasicService.selectById(projectId);
        ModelAndView mv = new ModelAndView();
        if (null != projectBasic) {
            //培训项目开始时间和结束时间处理
            if (!"".equals(projectBasic.getProjectTrainInfo()) && null != projectBasic.getProjectTrainInfo()) {
                Map<String, Object> map1 = new Gson().fromJson(projectBasic.getProjectTrainInfo(), Map.class);
                mv.addObject("projectBeginTime", map1.get("beginTime").toString());
                mv.addObject("projectEndTime", map1.get("endTime").toString());
            }

            //考试项目开始时间和结束时间处理
            if (!"".equals(projectBasic.getProjectExamInfo()) && null != projectBasic.getProjectExamInfo()) {
                Map<String, Object> map3 = new Gson().fromJson(projectBasic.getProjectExamInfo(), Map.class);
                mv.addObject("examBeginTime", map3.get("beginTime").toString());
                mv.addObject("examEndTime", map3.get("endTime").toString());
                mv.addObject("intRetestTime", map3.get("count").toString());
            }
            //查询项目主题
            if (!"".equals(projectBasic.getSubjectId()) && null != projectBasic.getSubjectId()) {
                TrainSubject trainSubject = new TrainSubject();
                trainSubject.setVarId(projectBasic.getSubjectId());
                trainSubject = trainSubjectService.selectOne(trainSubject);
                if (null != trainSubject) {
                    mv.addObject("subjectName", trainSubjectService.selectOne(trainSubject).getSubjectName());
                }
            }

            mv.addObject("subjectId", projectBasic.getSubjectId());
            mv.addObject("projectName", projectBasic.getProjectName());

        }


        mv.setViewName("admin/project_create/private/step_1");
        mv.addObject("projectTypeNo", projectTypeNo);
        mv.addObject("projectBasic", projectBasic);
        mv.addObject("permissionExercise", PermissionUtil.hasPermissionQuestion(projectTypeNo));    //是否含有练习
        mv.addObject("permissionTrain", PermissionUtil.hasPermissionStudy(projectTypeNo));          //是否含有培训
        mv.addObject("permissionExam", PermissionUtil.hasPermissionExam(projectTypeNo));            //是否含有考试
        mv.addObject("examType", ProjectTypeEnum.QuestionType_3.getValue());      //单独考试

        return mv;

    }

    /**
     * 第三步操作（选择用户）
     *
     * @param request
     * @param projectTypeNo
     * @param projectId
     * @return
     */
    @RequestMapping("/step3")
    public ModelAndView step3(HttpServletRequest request,
                              @RequestParam(value = "projectTypeNo", required = false, defaultValue = "") String projectTypeNo,  //项目类型
                              @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId,           //项目编号
                              @RequestParam(value = "projectStatus", required = false, defaultValue = "") String projectStatus,           //项目状态
                              @RequestParam(value = "examPermission", required = false, defaultValue = "") String examPermission        //考试状态
    ) {
        Map<String, Object> params = new HashedMap();
        params.put("roleId", PropertiesUtils.getValue("role_id"));
        params.put("projectId", projectId);
        boolean flag = false;
        //查询该项目下是否有不是默认角色的角色课程信息
        List<Map<String, Object>> list = projectRoleService.selectProjectRole(params);
        if (list != null && list.size()>0) {
            flag = true;
        }
        return new ModelAndView("admin/project_create/private/step_3")
                .addObject("projectTypeNo", projectTypeNo)
                .addObject("projectId", projectId)
                .addObject("flag", flag)
                .addObject("projectStatus", projectStatus)
                .addObject("examPermission", examPermission)
                .addObject("permissionExercise", PermissionUtil.hasPermissionQuestion(projectTypeNo))    //是否含有练习
                .addObject("permissionTrain", PermissionUtil.hasPermissionStudy(projectTypeNo))          //是否含有培训
                .addObject("permissionExam", PermissionUtil.hasPermissionExam(projectTypeNo));            //是否含有考试;
    }

    /**
     * 保存项目用户
     *
     * @param projectBasic
     * @param request
     * @param roleId
     * @param roleName
     * @param users
     * @return
     * @throws Exception
     */
    @Transactional(DataSource.BUS)
    @RequestMapping(value = "/save_project_user")
    @ResponseBody
    public Object saveProjectUser(ProjectBasic projectBasic, HttpServletRequest request,
                                  @RequestParam(value = "roleId", required = false, defaultValue = "") String roleId,
                                  @RequestParam(value = "roleName", required = false, defaultValue = "") String roleName,
                                  @RequestParam(value = "projectType", required = false, defaultValue = "") String projectType,
                                  @RequestParam(value = "users", required = false, defaultValue = "") String users) throws Exception {
        User user = getCurrUser(request);
        Map<String, Object> param = new HashedMap();
        param.put("projectBasic", projectBasic);
        param.put("roleId", roleId);
        param.put("roleName", roleName);
        param.put("users", users);
        List<Map<String, Object>> userList = projectUserService.saveBatch(param,user);
        if(!CollectionUtils.isEmpty(userList)){
            // 发送到activemq的cppri.queue
            Map<String, Object> queueMap = new ConcurrentHashMap<String, Object>();
            queueMap.put("operateType", "saveUser");
            queueMap.put("id", projectBasic.getId());
            queueMap.put("userList", userList);
            queueMap.put("roleId", roleId);
            queueMap.put("roleName", roleName);
            queueMap.put("projectType", projectType);
            EXECUTOR.execute(new CreatePrivateProjectController.AnalysDeal_PU(queueMap));
        }
        Response<Object> response = new Response<>();
        response.setCode(Code.SUCCESS.getCode());
        return response;
    }

    /**
     * 根据属相查询项目用户信息
     *
     * @param projectId
     * @param userName
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/project_user_list")
    @ResponseBody
    public Object projectUserList(@RequestParam(value = "projectId", required = false, defaultValue = "") String projectId,    //项目id
                                  @RequestParam(value = "userName", required = false, defaultValue = "") String userName,    //用户名称
                                  @RequestParam(value = "pageSize", required = true, defaultValue = "1") Integer pageSize,     //每页条数
                                  @RequestParam(value = "pageNum", required = true, defaultValue = "1") Integer pageNum        //页码
    ) throws ServiceException {
        Response<Object> response = new Response<Object>();
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();
        Map<String, Object> params = new HashedMap();
        params.put("projectId", projectId);
        params.put("userName", ParamsUtil.joinLike(userName));
        params.put("startNum", (pageNum - 1) * pageSize);
        params.put("endNum", pageSize);
        //项目用户信息
        List<Map<String, Object>> result = projectUserService.selectList(params);
        //项目用户数量
        int count = projectUserService.selectCount(params);
        page = new Page(count, pageNum, pageSize);
        response.setCode(Code.SUCCESS.getCode());
        page.setDataList(result);
        response.setResult(page);
        return response;
    }

    /**
     * 删除项目用户
     *
     * @param projectId
     * @param ids
     * @return
     * @throws Exception
     */
    @Transactional(DataSource.BUS)
    @RequestMapping(value = "/delete_project_user")
    @ResponseBody
    public Object deleteProjectUser(@RequestParam(value = "projectType", required = false, defaultValue = "") String projectType,  //项目类型
                                    @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId,  //项目id
                                    @RequestParam(value = "projectStatus", required = false, defaultValue = "") String projectStatus,  //项目状态
                                    @RequestParam(value = "ids", required = false, defaultValue = "") String[] ids) throws Exception {
        //获取用户Id集合
        List<String> userIds = Arrays.asList(ids);
        Map<String, Object> param = new HashedMap();
        param.put("projectId", projectId);
        param.put("userIds", userIds);
        param.put("projectType", projectType);
        param.put("projectStatus", projectStatus);
        createPrivateProjectService.deleteProjectUser(param);
        // 发送到activemq的cppri.queue
        Class<Map> clazz = Map.class;
        Map<String, Object> queueMap = new ConcurrentHashMap<String, Object>();
        queueMap.put("operateType", "deleteUser");
        queueMap.put("id", projectId);
        queueMap.put("userIds", userIds);
        queueMap.put("projectType", projectType);
        queueMap.put("projectStatus", projectStatus);
        EXECUTOR.execute(new CreatePrivateProjectController.AnalysDeal_PU(queueMap));
        Response<Object> response = new Response<>();
        response.setCode(Code.SUCCESS.getCode());
        return response;
    }
  /**
     * 第二步操作（选择课程）
     * @param request
     * @param projectTypeNo
     * @param projectId
     * @return
     */
    @RequestMapping("/selectRole")
    public ModelAndView selectRole(HttpServletRequest request,
                              @RequestParam(value = "projectTypeNo", required = false, defaultValue = "") String projectTypeNo,  //项目类型
                              @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId,           //项目编号
                              @RequestParam(value = "projectStatus", required = false, defaultValue = "") String projectStatus,           //项目状态
                              @RequestParam(value = "examPermission", required = false, defaultValue = "") String examPermission          //考试状态
    ){
        return  new ModelAndView("admin/project_create/private/selectRole")
                .addObject("projectTypeNo",projectTypeNo)
                .addObject("projectId",projectId)
                .addObject("projectStatus",projectStatus)
                .addObject("examPermission",examPermission)
                .addObject("permissionExercise",PermissionUtil.hasPermissionQuestion(projectTypeNo))    //是否含有练习
                .addObject("permissionTrain",PermissionUtil.hasPermissionStudy(projectTypeNo))          //是否含有培训
                .addObject("permissionExam",PermissionUtil.hasPermissionExam(projectTypeNo));            //是否含有考试;
    }


    /**
     * 发布项目
     *
     * @param projectId
     * @param projectStatus
     * @return
     * @throws Exception
     */
    @Transactional(DataSource.BUS)
    @RequestMapping(value = "/publish_project")
    @ResponseBody
    public Object publish(HttpServletRequest request,
                          @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId,
                          @RequestParam(value = "permissionExam", required = false, defaultValue = "") String permissionExam,
                          @RequestParam(value = "projectStatus", required = false, defaultValue = "") String projectStatus
    ) throws Exception {
        Response<Object> response = new Response<>();
        if(!permissionExam.equals("") && permissionExam.equals("true")){
            //判断组卷策略合法性
            String result = examStrategyService.checkStrategy(projectId);
            if(!result.equals("")){
                response.setResult(result);
                return response;
            }
        }
        Map<String, Object> param = new HashedMap();
        param.put("projectId", projectId);
        param.put("projectStatus", projectStatus);
        param.put("operTime", DateUtils.formatDateTime(new Date()));
        createPrivateProjectService.updateInfo(param);


        //消息推送（发布项目）
        EXECUTOR.execute(new AnalysDeal_PP(param));

        response.setCode(Code.SUCCESS.getCode());
        return response;
    }

    private class AnalysDeal extends Thread {

        private Map<String, Object> queueMap;

        public AnalysDeal(Map<String, Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queueCPPriSender.sendMapMessage("tp.cppri.queue", queueMap);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    private class AnalysDeal_PC extends Thread {

        private Map<String, Object> queueMap;

        public AnalysDeal_PC(Map<String, Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queueProjectCourseSender.sendMapMessage("tp.projectCourse.queue", queueMap);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    private class AnalysDeal_PU extends Thread {

        private Map<String, Object> queueMap;

        public AnalysDeal_PU(Map<String, Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queueProjectUserSender.sendMapMessage("tp.projectUser.queue", queueMap);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    public class AnalysDeal_Tj extends Thread {

        private Map<String, Object> queueMap;

        public AnalysDeal_Tj(Map<String, Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queueTJSender.sendMapMessage("tp.tj.queue", queueMap);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }
    public  class AnalysDeal_UD extends Thread {

        private Map<String,Object> queueMap;

        public AnalysDeal_UD(Map<String,Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queueUdpSender.sendMapMessage("tp.udp.queue", queueMap);
            }catch(Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    /**
     * 发布项目
     */
    private class AnalysDeal_PP extends Thread {

        private Map<String, Object> queueMap;

        public AnalysDeal_PP(Map<String, Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queueProjectPublishSender.sendMapMessage("tp.projectPublish.queue", queueMap);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    /**
     * 修改应修学时
     *
     * @param projectId
     * @param id
     * @param requirement
     * @return
     */
    @Transactional(DataSource.BUS)
    @RequestMapping(value = "/updateRequirement")
    @ResponseBody
    public Object updateRequirement(
            @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId,
            @RequestParam(value = "id", required = false, defaultValue = "") String id,
            @RequestParam(value = "courseId", required = false, defaultValue = "") String courseId,
            @RequestParam(value = "requirement", required = false, defaultValue = "") String requirement
    ) {
        Response<Object> response = new Response<>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId", projectId);
        params.put("id", id);
        params.put("requirement", requirement);
        params.put("operTime", DateUtils.formatDateTime(new Date()));
        projectCourseService.update(params);
        response.setCode(Code.SUCCESS.getCode());
        return response;
    }

    /**
     * 批量保存项目角色
     * @param request
     * @param projectId
     * @param roles
     * @return
     * @throws Exception
     */
    @Transactional(DataSource.BUS)
    @RequestMapping(value = "/saveProjectRole")
    @ResponseBody
    public Object saveProjectRole(HttpServletRequest request,
        @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId,
        @RequestParam(value = "projectType", required = false, defaultValue = "") String projectType,
        @RequestParam(value = "roles", required = false, defaultValue = "") String roles
    ) throws Exception{
        User user = getCurrUser(request);
        Response<Object> response = new Response<>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId", projectId);
        params.put("projectType", projectType);
        params.put("roles", roles);
        createPrivateProjectService.saveByProjectRole(params,user);
        response.setCode(Code.SUCCESS.getCode());
        return response;
    }
}
