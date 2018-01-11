package com.bossien.train.controller;

import com.bossien.train.domain.*;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.page.SpringDataPageable;
import com.bossien.train.service.*;
import com.bossien.train.util.*;
import com.google.common.collect.Lists;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by A on 2017/7/25.
 */

@Controller
@RequestMapping("/admin/dossier/person")
public class PersonDossierController extends BasicController{
    @Value("${department_id}")
    private String departmentId;

    @Autowired
    private IPersonDossierService personDossierService;

    @Autowired
    private IProjectStatisticsInfoService projectStatisticsInfoService;

    @Autowired
    private ITrainRoleService trainRoleService;

    @Autowired
    private IUserManagerService userManagerService;

    @Autowired
    private IProjectCourseInfoService projectCourseInfoService;

    @Autowired
    private MongoOperations mongoTemplate;

    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private IProjectBasicService projectBasicService;

    /**
     * 人员档案
     * @param request
     * @return
     */
    @RequestMapping
    public String index(HttpServletRequest request){
        request.setAttribute("companyId",getCurrUser(request).getCompanyId());
        return "admin/dossier/person_index";
    }

    /**
     * 人员档案异步
     * @param request
     * @param userName
     * @param roleName
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(HttpServletRequest request,
                       @RequestParam(value="user_name", required = true, defaultValue = "") String userName,
                       @RequestParam(value="role_name", required = true, defaultValue = "") String roleName,
                       @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
                       @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "companyIds",required = true,defaultValue = "") String companyIds,   //公司id集合
                       @RequestParam(value="deptIds", required = false, defaultValue = "") String deptIds
                       ) throws ServiceException {
        Response<Object> resp = new Response<Object>();
        Page<Map<String, Object>> page = new Page<>();

        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }
        User user = this.getCurrUser(request);
        if(null == user){
            resp.setCode(Code.USER_NOT_LOGIN.getCode());
            resp.setMessage(Code.USER_NOT_LOGIN.getMessage());
            return resp;
        }

        List<String> deptlist=new ArrayList<String>();
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(companyIds)) {
            params.put("companyIds", Arrays.asList(companyIds.split(",")));
        }
        if(StringUtils.isNotEmpty(deptIds)) {
            if(StringUtils.equals(departmentId,deptIds)){
                if(StringUtils.isEmpty(companyIds)){
                    params.put("companyId",user.getCompanyId());
                }
              params.put("isValid", User.IsValid.TYPE_2.getValue());
              params.put("deptId", deptIds);
              List<String> userIds = userManagerService.selectUserIds(params);
              if(CollectionUtils.isEmpty(userIds)){
                  resp.setCode(Code.SUCCESS.getCode());
                  resp.setResult(page);
                  return resp;
              }
              params.clear();
              params.put("userIds", userIds);
            }else{
                params.put("departmentIdList", Arrays.asList(deptIds.split(",")));
            }
        }
        params.put("roleName", ParamsUtil.joinLike(roleName));
        params.put("userName", ParamsUtil.joinLike(userName));
        page = personDossierService.queryPersonDossierForPagination(params, pageNum, pageSize, user);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);
        return resp;
    }

    /**
     * 人员档案单个项目记录
     * @param request
     * @return
     */
    @RequestMapping(value = "/project")
    public String project(HttpServletRequest request,
                          @RequestParam(value="user_id", required = true, defaultValue = "") String userId){
        User user = new User();
        user.setId(userId);
        user = userManagerService.queryUserInfo(user);
        request.setAttribute("userName", user.getUserName());
        request.setAttribute("userType", getCurrUser(request).getUserType());
        request.setAttribute("containExamType", PropertiesUtils.getValue("contain_exam_type"));
        request.setAttribute("containTrainType", PropertiesUtils.getValue("contain_train_type"));
        request.setAttribute("containExerciseType", PropertiesUtils.getValue("contain_exercise_type"));
        return "admin/dossier/person_info";
    }

    /**
     * 人员档案单个自学记录
     * @param request
     * @return
     */
    @RequestMapping(value = "/studySelf")
    public String studySelf(HttpServletRequest request,
                          @RequestParam(value="user_id", required = true, defaultValue = "") String userId){
        PersonDossier personDossier = null;
        if(StringUtils.isNotEmpty(userId)){
            Map<String, Object> params = new HashedMap();
            params.put("userId",userId);
            params.put("companyId",getCurrUser(request).getCompanyId());
            personDossier = personDossierService.selectStudySelf(params);
        }
        if(null == personDossier){
            personDossier = new PersonDossier();
        }
        request.setAttribute("userId", userId);
        request.setAttribute("yearStudySelf", MathUtil.getHour(personDossier.getYearStudySelf()));
        request.setAttribute("totalStudySelf", MathUtil.getHour(personDossier.getTotalStudySelf()));
        return "admin/dossier/study_self_info";
    }

    /**
     * 人员档案中 某个项目记录
     * @param request
     * @param userId
     * @param projectName
     * @param trainStatus
     * @param projectStartTime
     * @param projectEndTime
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/project_list")
    @ResponseBody
    public Object project_list(HttpServletRequest request,
                       @RequestParam(value="user_id", required = true, defaultValue = "") String userId,
                       @RequestParam(value="project_name", required = true, defaultValue = "") String projectName,
                       @RequestParam(value="train_status", required = true, defaultValue = "") String trainStatus,
                       @RequestParam(value="project_start_time", required = true, defaultValue = "") String projectStartTime,
                       @RequestParam(value="project_end_time", required = true, defaultValue = "") String projectEndTime,
                       @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
                       @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response<Object> resp = new Response<Object>();

        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }

        //开始时间补充00:00:00
        if(!StringUtils.isEmpty(projectStartTime)){
            projectStartTime = projectStartTime+" 00:00:00";
        }
        //结束时间补充23:59:59
        if(!StringUtils.isEmpty(projectEndTime)){
            projectEndTime = projectEndTime+" 23:59:59";
        }

        Page<Map<String, Object>> page = new Page<Map<String, Object>>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("projectName", ParamsUtil.joinLike(projectName));
        params.put("trainStatus", trainStatus);
        params.put("projectStartTime", projectStartTime);
        params.put("projectEndTime", projectEndTime);
        Integer count = projectStatisticsInfoService.selectCount(params);

        page = new Page(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("endNum", page.getPageSize());
        List<Map<String, Object>> listMap = projectStatisticsInfoService.selectList(params);
        ProjectBasic projectBasic = null;
        for (Map<String, Object> map:listMap) {
            projectBasic = projectBasicService.selectById(map.get("project_id").toString());
            if(null != projectBasic){
                map.put("project_type",projectBasic.getProjectType());
            }
        }
        page.setDataList(listMap);

        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);

        return resp;
    }

    @RequestMapping(value = "/course")
    public String course(HttpServletRequest request,
                         @RequestParam(value="project_id", required = true, defaultValue = "") String projectId){
        ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(projectId);
        if(null != projectInfo){
            request.setAttribute("projectType", projectInfo.getProjectType());
        }

        return "admin/dossier/course_info";
    }

    @RequestMapping("/course_list")
    @ResponseBody
    public Object course_list(
            @RequestParam(value="project_id", required = true, defaultValue = "") String projectId,
            @RequestParam(value="user_id", required = true, defaultValue = "") String userId,
            @RequestParam(value="course_name", required = true, defaultValue = "") String courseName,
            @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
            @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response<Object> resp = new Response<Object>();


        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }

        Page<Map<String, Object>> page = new Page<Map<String, Object>>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId", projectId);
        params.put("userId", userId);
        params.put("courseName", ParamsUtil.joinLike(courseName));
        Integer count = projectCourseInfoService.selectCount(params);

        page = new Page(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("endNum", page.getPageSize());
        List<Map<String, Object>> listMap = projectCourseInfoService.selectList(params);
        page.setDataList(listMap);

        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);

        return resp;
    }

    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value="userIds", required = true, defaultValue = "") String userIds,
                       @RequestParam(value="user_name", required = true, defaultValue = "") String userName,
                       @RequestParam(value = "companyIds",required = true,defaultValue = "") String companyIds,   //公司id集合
                       @RequestParam(value="deptIds", required = false, defaultValue = "") String deptIds) throws Exception{
        String fileName = "人员档案信息" + DateUtils.formatDateTime() + ".zip";
        Map<String, Object> params = new HashMap<String, Object>();
        if(userIds.equals("")){
            //userIds为空时根据条件导出
            if(StringUtils.isNotEmpty(userName)){
                params.put("userName", ParamsUtil.joinLike(userName));
            }
            if(StringUtils.isNotEmpty(companyIds)){
                params.put("companyIds", Arrays.asList(companyIds.split(",")));
            }
            if(StringUtils.isNotEmpty(deptIds)){
                params.put("departmentIdList", Arrays.asList(deptIds.split(",")));
            }
        }else{
            params.put("userIds", Arrays.asList(userIds.split(",")));
        }
        personDossierService.export(params, ExportExcelUtils.setExcelHeardZip(response, fileName));
    }
}
