package com.bossien.train.controller;

import com.bossien.framework.mq.sender.QueueTJSender;
import com.bossien.framework.mq.sender.QueueUdpSender;
import com.bossien.train.domain.CompanyProject;
import com.bossien.train.domain.ExamStrategy;
import com.bossien.train.domain.Page;
import com.bossien.train.domain.ProjectDepartment;
import com.bossien.train.domain.ProjectInfo;
import com.bossien.train.domain.ProjectRole;
import com.bossien.train.domain.User;
import com.bossien.train.domain.eum.ProjectStatusEnum;
import com.bossien.train.domain.eum.ProjectTypeEnum;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.ICompanyProjectService;
import com.bossien.train.service.IExamStrategyService;
import com.bossien.train.service.IProjectDepartmentService;
import com.bossien.train.service.IProjectInfoService;
import com.bossien.train.service.IProjectManagerService;
import com.bossien.train.service.IProjectRoleService;
import com.bossien.train.service.ITrainRoleService;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.ParamsUtil;
import com.bossien.train.util.PropertiesUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 项目管理
 * Created by Administrator on 2017/7/26.
 */
@Controller
@RequestMapping("admin/project")
public class ProjectManagerController extends BasicController {
    private static final Logger logger = LogManager.getLogger(ProjectManagerController.class);

    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    @Autowired
    private QueueUdpSender queueUdpSender;
    @Autowired
    private QueueTJSender queueTJSender;
    @Autowired
    private IProjectManagerService projectManagerService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private IProjectDepartmentService projectDepartmentService;
    @Autowired
    private IExamStrategyService examStrategyService;
    @Autowired
    private IProjectRoleService projectRoleService;
    @Autowired
    private ITrainRoleService trainRoleService;
    @Autowired
    private ICompanyProjectService companyProjectService;
    @Value("${contain_exam_type}")
    private String containExamType;

    @RequestMapping(value = "/projectList")
    public ModelAndView proManagerListPage(HttpServletRequest request)throws Exception {
        logger.info("go to the proManagerListPage method");
        ModelAndView mv = new ModelAndView();
        mv.addObject("project_type", Arrays.asList(ProjectTypeEnum.values()));
        /*mv.addObject("project_status", Arrays.asList(ProjectStatusEnum.values()));*/
        mv.setViewName("admin/proManager/proList");
        logger.info("com.bossien.train.controller.ProjectManagerController stop");
        return mv;
    }

    @RequestMapping("/queryProList")
    @ResponseBody
    public Object list(HttpServletRequest request,String projectName,String projectStartTime,String projectEndTime,
                       String projectStatus,String projectType,
                       @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
                       @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response<Object> resp = new Response<Object>();
        User user=getCurrUser(request);
        //ProjectInfo prjInfo = new ProjectInfo();
        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        // 查询单位下项目
        params.put("company_id", user.getCompanyId());
        List<CompanyProject> companyProjects = companyProjectService.selectList(params);
        List<Map<String, Object>> plist = new ArrayList<>();
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();
        page.setCount(0);
        if(null != companyProjects && companyProjects.size()>0){
            List<String> projectIds = new LinkedList<String>();
            for(CompanyProject companyProject : companyProjects) {
                projectIds.add(companyProject.getProjectId());
            }
            params.put("projectIds", projectIds);
            if(!"".equals(projectName) && projectName!=null){
                params.put("project_name", ParamsUtil.joinLike(projectName ));

            }
            if(!"".equals(projectStartTime) && projectStartTime!=null){
                params.put("project_start_time", projectStartTime + " 00:00:00");

            }
            if(!"".equals(projectEndTime) && projectEndTime!=null){
                params.put("project_end_time", projectEndTime + " 23:59:59");

            }
            if(!"".equals(projectStatus) && projectStatus!=null){
                params.put("project_status", getProjectStatusCode(projectStatus));
            }
            if(!"".equals(projectType) && projectType!=null){
                params.put("projectType", projectType);
            }
            params.put("pageNum", pageNum);
            params.put("pageSize", pageSize);
            params.put("noStartStatus", PropertiesUtils.getValue("unPublish"));
            params.put("projectPrivate", PropertiesUtils.getValue("project_mode"));
            params.put("projectPublic", PropertiesUtils.getValue("project_public"));
            Integer count = projectInfoService.selectCount(params);
            page = new Page(count, pageNum, pageSize);
            params.put("startNum", page.getStartNum());
            params.put("endNum", page.getPageSize());
            plist = projectInfoService.selectList(params);
        }
        page.setDataList(plist);
        resp.setResult(page);
        return resp;
    }

    /**
     * 根据项目状态名称获取项目状态Code
     * @param projectStatusName
     * @return
     */
    public String getProjectStatusCode(String projectStatusName){
        String projectStatus = "-1";
        if(ProjectStatusEnum.ProjectStatus_1.getName().equals(projectStatusName)){
            projectStatus = ProjectStatusEnum.ProjectStatus_1.getValue();
        }else if(ProjectStatusEnum.ProjectStatus_2.getName().equals(projectStatusName)){
            projectStatus = ProjectStatusEnum.ProjectStatus_2.getValue();
        }else if(ProjectStatusEnum.ProjectStatus_3.getName().equals(projectStatusName)){
            projectStatus = ProjectStatusEnum.ProjectStatus_3.getValue();
        }else if(ProjectStatusEnum.ProjectStatus_4.getName().equals(projectStatusName)){
            projectStatus = ProjectStatusEnum.ProjectStatus_4.getValue();
        }
        return projectStatus;
    }
    /**
     * 项目管理（项目详情）
     * @param request
     * @param projectId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/proManagerInfo")
    public ModelAndView proManagerInfo(HttpServletRequest request,String projectId,String projectMode,String type)throws Exception {
        ModelAndView mv = new ModelAndView();
        User user=getCurrUser(request);
        ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(projectId);

        //根据projectId和companyId查询受训单位
        Map<String,Object>  param=new HashMap<String,Object>();
        param.put("projectId" ,projectId);
        List<Map<String, Object>> projectDepartments=projectDepartmentService.selectProDeptInfo(param);
        //受训单位信息
        mv.addObject("projectDepartments",projectDepartments);

        //查询组卷策略
        if(null!=projectInfo){
            if("0".equals(projectInfo.getProjectMode())) {
                String projectType = projectInfo.getProjectType();
                if (StringUtils.contains(PropertiesUtils.getValue("contain_exam_type"),projectType)) {
                    param.put("projectId" ,projectId);
                    ExamStrategy examStrategy = examStrategyService.selectByProjectIdAndRoleId(param);
                    if (null != examStrategy) {
                        //组装组卷策略的题型数据信息
                        List<Map<String, Object>> examStrategyRows = examStrategyService.assembleExamStrategy(examStrategy);
                        //多种题型组合信息
                        mv.addObject("examStrategyRows", examStrategyRows);
                    }
                    //组卷策略信息
                    mv.addObject("examStrategy", examStrategy);
                }
            }
        }

        mv.addObject("projectInfo",projectInfo);
        mv.addObject("type",type);
        mv.addObject("projectMode",projectMode);
        mv.setViewName("admin/proManager/proInfo");
        return mv;
    }


    /**
     * 项目角色
     * @param projectId
     * @return
     */
     @RequestMapping(value = "/projectRole")
     @ResponseBody
    public Object projectRole(String projectId){
         Response<Object> response=new Response <Object>();
        List<ProjectRole> projectRoles=projectRoleService.selectByProjectId(projectId);
         response.setResult(projectRoles);
        return response;
     }
   /* *//**
     * 项目单位
     * @param projectId
     * @return
     *//*
    @RequestMapping(value = "/projectDepartment")
    @ResponseBody
    public Object projectDepartment(String projectId,HttpServletRequest request){
        User user=getCurrUser(request);
        Response<Object> response=new Response <Object>();
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("companyId",user.getCompanyId());
        params.put("projectId",projectId);
        List<ProjectDepartment> projectDepartments=projectDepartmentService.selectByProjectIdAndCompanyId(params);
        response.setResult(projectDepartments);
        return response;
    }
*/
    /**
     * 项目角色
     * @param trainRoles
     * @return
     */
    @RequestMapping(value = "/projectRole/tree")
    @ResponseBody
    public Object projectRoleTree(
            @RequestParam(value = "trainRoles", required = false,defaultValue = "")  String trainRoles){
        Response<Object> response=new Response <Object>();
        //构造角色树
        List<Map<String, Object>> roleTreeNodes= trainRoleService.ConstructRoleTreeNodes(trainRoles);
        Object[]  roleTreeNode=roleTreeNodes.toArray();
        response.setResult(roleTreeNode);
        return  response;
    }


    /**
     * 删除未开始未结束项目
     * @param projectId
     * @return
     */
    @RequestMapping(value ="/proManagerDelete")
    @ResponseBody
    public Object proManagerDelete(HttpServletRequest request,
            @RequestParam(value = "projectId", required = false,defaultValue = "")  String projectId
            ){
        User user=getCurrUser(request);
        Response <Object> response = new Response <>();

        Map<String,Object> params=new HashMap<String,Object>();
        params.put("projectId",projectId);
        params.put("companyId",user.getCompanyId());

        //删除项目(删除projectBasic和projectInfo表的数据)
        projectManagerService.delete(params);
        params.put("operType","delete");
        //发送消息到udp.queue
        EXECUTOR.execute(new AnalysDeal_UD(params));

        response.setCode(Code.SUCCESS.getCode());
        return response;
    }

    /**
     * 修改应修学时和必选题量
     * @param projectId
     * @param id
     * @param requirement
     * @param selectCount
     * @return
     */
    @RequestMapping(value="/updateRequirementAndSelectCount")
    @ResponseBody
    public Object updateRequirementAndSelectCount(HttpServletRequest request,
            @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId,
            @RequestParam(value = "id", required = false, defaultValue = "") String id,
            @RequestParam(value = "roleId", required = false, defaultValue = "-1") String roleId,
            @RequestParam(value = "courseId", required = false, defaultValue = "-1") String courseId,
            @RequestParam(value = "requirement", required = false, defaultValue = "") String requirement,
            @RequestParam(value = "selectCount", required = false, defaultValue = "") String selectCount
          ){
        User user = getCurrUser(request);
        Response <Object> response = new Response <>();
        Map<String,Object> params=new HashMap<String,Object>();
        response.setCode(Code.SUCCESS.getCode());

        //修改必选题量
        if(requirement.equals("")){
//            params.put("projectId",projectId);
//            params.put("roleId", roleId);
//            ExamStrategy examStrategy = examStrategyService.selectByProjectIdAndRoleId(params);
//            if(null == examStrategy){
//                response.setMessage("组卷策略不存在,无法设置必选题量");
//                return response;
//            }
//
//            Integer singleCount = examStrategy.getSingleCount();
//            Integer manyCount = examStrategy.getManyCount();
//            Integer judgeCount = examStrategy.getJudgeCount();
//            Integer selectAllCount = 0;
//            List<ProjectCourse> projectCourseList = projectCourseService.selectByProjectIdAndRoleId(params);
//            for(ProjectCourse projectCourse : projectCourseList){
//                selectAllCount += projectCourse.getSelectCount();
//            }
//
//            if((selectAllCount + Integer.parseInt(selectCount)) > singleCount){
//                response.setMessage("必选题总量大于单选题数量");
//                return response;
//            }
//            if((selectAllCount + Integer.parseInt(selectCount)) > manyCount){
//                response.setMessage("必选题总量大于多选题总数量");
//                return response;
//            }
//            if((selectAllCount + Integer.parseInt(selectCount)) > judgeCount){
//                response.setMessage("必选题总量大于判断题总数量");
//                return response;
//            }

            params.put("selectCount",selectCount);
        }

        if(!requirement.equals("")){
            params.put("requirement",requirement);
        }
        params.put("id",id);
        params.put("projectId",projectId);
        params.put("courseId",courseId);
        params.put("roleId",roleId);
        params.put("operTime", DateUtils.formatDateTime(new Date()));
        projectManagerService.updateRequirementAndSelectCount(params);
        //修改projectCourse表的数据
        //projectCourseService.update(params);

        return response;
    }

    /**
     * 查看进度
     * @param projectId
     * @param projectType
     * @return
     */
    @RequestMapping(value = "/proManagerProcess")
    public ModelAndView proManagerProcess(
        @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId,
        @RequestParam(value = "projectType", required = false, defaultValue = "") String projectType
        ){
        ModelAndView mv = new ModelAndView();
        Map<String, Object> params = new HashedMap();
        params.put("projectId",projectId);
        ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(projectId);
       if(projectType.equals(ProjectTypeEnum.QuestionType_1.getValue()) || projectType.equals(ProjectTypeEnum.QuestionType_4.getValue()) ||
           projectType.equals(ProjectTypeEnum.QuestionType_5.getValue()) || projectType.equals(ProjectTypeEnum.QuestionType_7.getValue())){
           // 计算培训项目总进度
           Map<String, Object> trainProjectProcessMap = projectManagerService.selectTrainProcess(params);
           //查询受训角色集合
           List<ProjectRole> trainRoleLMap = projectRoleService.selectByProjectId(projectId);
           mv.addObject("trainProjectProcessMap", trainProjectProcessMap);
           mv.addObject("trainRoleLMap", trainRoleLMap);
       }
        if(projectType.equals(ProjectTypeEnum.QuestionType_3.getValue()) || projectType.equals(ProjectTypeEnum.QuestionType_5.getValue()) ||
            projectType.equals(ProjectTypeEnum.QuestionType_6.getValue()) || projectType.equals(ProjectTypeEnum.QuestionType_7.getValue())){
            // 查询考试总体情况
            Map<String, Object> examProcessMap = projectManagerService.selectExamInfo(params);
            mv.addObject("examProcessMap", examProcessMap);
        }
        mv.addObject("projectId", projectId);
        mv.addObject("projectType", projectType);
        mv.addObject("projectName", projectInfo.getProjectName());
        mv.addObject("permissionExercise", hasPermissionQuestion(projectType));    //是否含有练习
        mv.addObject("permissionTrain", hasPermissionStudy(projectType));          //是否含有培训
        mv.addObject("permissionExam", hasPermissionExam(projectType));            //是否含有考试
        mv.setViewName("admin/proManager/proProcess");
        return mv;
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

    public  class AnalysDeal_Tj extends Thread {

        private Map<String,Object> queueMap;

        public AnalysDeal_Tj(Map<String,Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queueTJSender.sendMapMessage("tp.tj.queue", queueMap);
            }catch(Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }
}
