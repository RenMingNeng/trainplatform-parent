package com.bossien.train.controller;

import com.bossien.framework.db.DataSource;
import com.bossien.framework.mq.sender.QueuePRSender;
import com.bossien.train.domain.*;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.kit.PermissionUtil;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IProjectCourseService;
import com.bossien.train.service.IProjectCustomService;
import com.bossien.train.service.IProjectRoleService;
import com.bossien.train.util.ParamsUtil;
import com.bossien.train.util.PropertiesUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.http.HttpServletRequest;

import com.bossien.train.service.IProjectUserService;
import com.bossien.train.service.ITrainRoleService;
import com.mysql.fabric.xmlrpc.base.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/project/advanceSetting")
public class ProjectCustomController extends BasicController{

    private static Logger logger = LoggerFactory.getLogger(ProjectCustomController.class);

    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    @Autowired
    QueuePRSender queuePRSender;

    @Autowired
    private IProjectCustomService projectCustomService;
    @Autowired
    private ITrainRoleService trainRoleService;
    @Autowired
    private IProjectCourseService projectCourseService;
    @Autowired
    private IProjectUserService projectUserService;
    @Autowired
    private IProjectRoleService projectRoleService;
    /**
     * 第四步操作（高级设置课程）
     * @param projectTypeNo
     * @param projectId
     * @return
     */
    @RequestMapping("/step4")
    public ModelAndView step4(
                              @RequestParam(value = "projectTypeNo", required = false, defaultValue = "") String projectTypeNo,  //项目类型
                              @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId,          //项目编号
                              @RequestParam(value = "projectStatus", required = false, defaultValue = "") String projectStatus,           //项目状态
                              @RequestParam(value = "examPermission", required = false, defaultValue = "") String examPermission          //考试状态
                             ){
        ModelAndView mv=new ModelAndView("admin/project_create/private/step_4");
        Map<String,Object> param=new HashMap<String,Object>();
        param.put("projectId",projectId);
        param.put("roleId", PropertiesUtils.getValue("role_id"));
        List<Map<String,Object>> trainRoles= projectRoleService.selectProjectRole(param);


        mv.addObject("projectTypeNo",projectTypeNo);
        mv.addObject("projectId",projectId);
        mv.addObject("projectStatus",projectStatus);
        mv.addObject("examPermission",examPermission);
        mv.addObject("trainRoles",trainRoles);
        mv.addObject("permissionExercise", PermissionUtil.hasPermissionQuestion(projectTypeNo));    //是否含有练习
        mv.addObject("permissionTrain",PermissionUtil.hasPermissionStudy(projectTypeNo));          //是否含有培训
        mv.addObject("permissionExam",PermissionUtil.hasPermissionExam(projectTypeNo));            //是否含有考试;
        return  mv;
    }
    /**
     * 第四步操作（高级设置课程）
     * @param projectTypeNo
     * @param projectId
     * @return
     */
    @RequestMapping("/getProjectRole")
    @ResponseBody
    public Object getProjectROle(
            @RequestParam(value = "projectTypeNo", required = false, defaultValue = "") String projectTypeNo,  //项目类型
            @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId           //项目编号
    ){

        Map<String,Object> param=new HashMap<String,Object>();
        param.put("projectId",projectId);
        param.put("roleId", PropertiesUtils.getValue("role_id"));
        List<Map<String,Object>> trainRoles= projectRoleService.selectProjectRole(param);

        return  trainRoles;
    }

    /**
     * 通过projectId获取项目课程
     * @param projectTypeNo
     * @param projectId
     * @return
     */
    @RequestMapping("/getProjectCourse")
    @ResponseBody
    public Object getProjectCourse(
            @RequestParam(value = "projectTypeNo", required = false, defaultValue = "") String projectTypeNo,  //项目类型
            @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId           //项目编号
    ){

        Map<String,Object> param=new HashMap<String,Object>();
        param.put("projectId",projectId);
        param.put("roleId",PropertiesUtils.getValue("role_id"));
        List<ProjectCourse> projectCourses= projectCourseService.selectProjectCourses(param);

        return  projectCourses;
    }



    /**
     * 高级设置项目课程、角色查询
     * @param request
     * @param projectId
     * @return
     * @throws ServiceException
     */
    @Transactional(DataSource.BUS)
    @RequestMapping("/course_role")
    @ResponseBody
    public Object courseRole(HttpServletRequest request,
                          @RequestParam(value="projectId", required = false, defaultValue = "") String projectId,
                          @RequestParam(value = "pageSize", required = true, defaultValue = "10") Integer pageSize,     //每页条数
                          @RequestParam(value = "pageNum", required = true, defaultValue = "1") Integer pageNum        //页码
                       ){
        Response<Object> resp = new Response<Object>();
        Page <Map <String, Object>> page = new Page <Map <String, Object>>();
        User user=getCurrUser(request);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId",projectId);
        params.put("companyId",user.getCompanyId());
        params.put("isValid","1");
        params.put("startNum", (pageNum - 1) * pageSize);
        params.put("endNum", pageSize);
        //项目课程数量
        int count = projectCourseService.selectProjectCourseCount(params);
        page = new Page(count, pageNum, pageSize);



        List<Map<String, Object>> roleAndCourse = projectCustomService.selectCourseAndRole(params);
        page.setDataList(roleAndCourse);

        resp.setResult(page);
        resp.setCode(Code.SUCCESS.getCode());
        return resp;
    }

    /**
     * 第五步操作（高级设置用户）
     * @param request
     * @param projectTypeNo
     * @param projectId
     * @return
     */
    @RequestMapping("/step5")
    public ModelAndView step5(HttpServletRequest request,
                              @RequestParam(value = "projectTypeNo", required = false, defaultValue = "") String projectTypeNo,  //项目类型
                              @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId,           //项目编号
                              @RequestParam(value = "projectStatus", required = false, defaultValue = "") String projectStatus,           //项目状态
                              @RequestParam(value = "examPermission", required = false, defaultValue = "") String examPermission          //考试状态
    ){
        User user=getCurrUser(request);
        Map<String,Object> param=new HashMap<String,Object>();
        param.put("projectId",projectId);
        param.put("roleId", PropertiesUtils.getValue("role_id"));
        List<Map<String,Object>> trainRoles= projectRoleService.selectProjectRole(param);
        return  new ModelAndView("admin/project_create/private/step_5")
                .addObject("projectTypeNo",projectTypeNo)
                .addObject("projectId",projectId)
                .addObject("projectStatus",projectStatus)
                .addObject("examPermission",examPermission)
                .addObject("trainRoles",trainRoles)
                .addObject("roleLength",trainRoles.size())
                .addObject("permissionExercise",PermissionUtil.hasPermissionQuestion(projectTypeNo))    //是否含有练习
                .addObject("permissionTrain",PermissionUtil.hasPermissionStudy(projectTypeNo))          //是否含有培训
                .addObject("permissionExam",PermissionUtil.hasPermissionExam(projectTypeNo));            //是否含有考试;
    }

    /**
     * 通过projectId获取项目用户
     * @param projectId
     * @return
     */
    @RequestMapping("/getProjectUser")
    @ResponseBody
    public Object getProjectUser(
            @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId           //项目编号
    ){

        Map<String,Object> param=new HashMap<String,Object>();
        param.put("projectId",projectId);
        List<ProjectUser> projectUsers = projectUserService.selectProjectUsers(param);

        return  projectUsers;
    }
    /**
     * 高级设置项目用户、角色查询
     * @param request
     * @param projectId
     * @return
     * @throws ServiceException
     */
    @Transactional(DataSource.BUS)
    @RequestMapping("/user_role")
    @ResponseBody
    public Object userRole(HttpServletRequest request,
                           @RequestParam(value="projectId", required = false, defaultValue = "") String projectId,
                           @RequestParam(value = "pageSize", required = true, defaultValue = "10") Integer pageSize,     //每页条数
                           @RequestParam(value = "pageNum", required = true, defaultValue = "1") Integer pageNum     //页码
                            ){
        Response<Object> resp = new Response<Object>();
        Page <Map <String, Object>> page = new Page <Map <String, Object>>();
        User user=getCurrUser(request);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId",projectId);
        params.put("companyId",user.getCompanyId());
        params.put("isValid","1");
        params.put("startNum", (pageNum - 1) * pageSize);
        params.put("endNum", pageSize);

        //项目用户信息
        List <Map <String, Object>> result = projectUserService.selectList(params);
        //项目用户数量
        int count = projectUserService.selectCount(params);
        page = new Page(count, pageNum, pageSize);
        resp.setCode(Code.SUCCESS.getCode());
        page.setDataList(result);
        resp.setResult(page);
        return resp;
    }

    /**
     * 高级设置项目课程、角色保存
     * @param request
     * @param projectId
     * @return
     * @throws ServiceException
     */
    @Transactional(DataSource.BUS)
    @RequestMapping("/course_role_save")
    @ResponseBody
    public Object courseRoleSave(HttpServletRequest request,
        @RequestParam(value="projectId", required = false, defaultValue = "") String projectId,
        @RequestParam(value="projectType", required = false, defaultValue = "") String projectType,
        @RequestParam(value = "roles", required = false, defaultValue = "") String roles,
        @RequestParam(value = "courses", required = false, defaultValue = "") String courses
    ){
        User user=getCurrUser(request);
        Response<Object> resp = new Response<Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId",projectId);
        params.put("user",user);
        params.put("roles",roles);
        params.put("courses",courses);
        params.put("projectType",projectType);
        projectCustomService.saveProjectRoleCourse(params);
        //发送消息到pr.queue
        Map<String, Object> param_= new ConcurrentHashMap<String, Object>();
        param_.put("operateType","course");
        param_.put("projectId",projectId);
        param_.put("userName",user.getUserName());
        param_.put("roleCourses",roles);
        param_.put("projectType",projectType);
        EXECUTOR.execute(new AnalysDeal_PR(param_));
        resp.setCode(Code.SUCCESS.getCode());
        return resp;
    }

    /**
     * 高级设置项目用户、角色修改
     * @param request
     * @param projectId
     * @return
     * @throws ServiceException
     */
    @Transactional(DataSource.BUS)
    @RequestMapping("/user_role_update")
    @ResponseBody
    public Object userRoleUpdate(HttpServletRequest request,
        @RequestParam(value="projectId", required = false, defaultValue = "") String projectId,
        @RequestParam(value="projectType", required = false, defaultValue = "") String projectType,
        @RequestParam(value="users", required = false, defaultValue = "") String users,
        @RequestParam(value="roles", required = false, defaultValue = "") String roles
    ){
        User user=getCurrUser(request);
        Response<Object> resp = new Response<Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId",projectId);
        params.put("user",user);
        params.put("roleUsers",users);
        params.put("roles",roles);
        params.put("projectType",projectType);
        projectCustomService.updateProjectRoleUser(params);
        //发送消息到pr.queue
        Map<String, Object> param_= new ConcurrentHashMap<String, Object>();
        param_.put("operateType","user");
        param_.put("projectId",projectId);
        param_.put("userName",user.getUserName());
        param_.put("roleUsers",users);
        param_.put("roles",roles);
        param_.put("projectType",projectType);
        EXECUTOR.execute(new AnalysDeal_PR(param_));
        resp.setCode(Code.SUCCESS.getCode());
        return resp;
    }

    /**
     * 第六步操作（组卷策略）
     * @param request
     * @param projectTypeNo
     * @param projectId
     * @return
     */
    @RequestMapping("/step6")
    public ModelAndView step6(HttpServletRequest request,
                              @RequestParam(value = "projectTypeNo", required = false, defaultValue = "") String projectTypeNo,  //项目类型
                              @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId,           //项目编号
                              @RequestParam(value = "source", required = false, defaultValue = "") String source           //项目编号
    ){
        User user=getCurrUser(request);
        Map<String,Object> param=new HashMap<String,Object>();
        param.put("companyId",user.getCompanyId());
        param.put("IsValid","1");
        return  new ModelAndView("admin/project_create/private/step_6")
                .addObject("projectTypeNo",projectTypeNo)
                .addObject("projectId",projectId)
                .addObject("source",source)
                .addObject("permissionExercise",PermissionUtil.hasPermissionQuestion(projectTypeNo))    //是否含有练习
                .addObject("permissionTrain",PermissionUtil.hasPermissionStudy(projectTypeNo))          //是否含有培训
                .addObject("permissionExam",PermissionUtil.hasPermissionExam(projectTypeNo));            //是否含有考试;
    }

    public  class AnalysDeal_PR extends Thread {

        private Map<String,Object> queueMap;

        public AnalysDeal_PR(Map<String,Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queuePRSender.sendMapMessage("tp.pr.queue", queueMap);
            }catch(Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

}
