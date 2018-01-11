package com.bossien.train.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bossien.framework.interceptor.SignInterceptor;
import com.bossien.train.domain.Page;
import com.bossien.train.domain.ProjectInfo;
import com.bossien.train.domain.User;
import com.bossien.train.domain.UserTrainRole;
import com.bossien.train.domain.eum.ProblemTypeEnum;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.*;
import com.bossien.train.util.ParamsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/student")
public class StudentController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

    @Autowired
    private IDepartmentService deptService;
    @Autowired
    private IImportExcelUserService importExcelUserService;
    @Autowired
    private IUserManagerService userManagerService;
    @Autowired
    private IProjectUserService projectUserService;
    @Autowired
    private IPersonDossierService personDossierService;
    @Autowired
    private IProjectStatisticsInfoService projectStatisticsInfoService;
    @Autowired
    private IProjectCourseInfoService projectCourseInfoService;
    @Autowired
    private ITrainRoleService trainRoleService;
    @Autowired
    private ICompanyTrainRoleService companyTrainRoleService;
    @Autowired
    private IProjectBasicService projectBasicService;
    @Autowired
    private ICompanyProjectService companyProjectService;
    @Autowired
    private IProjectInfoService projectInfoService;


    @RequestMapping("")
    public String index() {
        return "student/index";
    }

    @RequestMapping("/menu")
    public String menu() {
        return "student/menu";
    }

    @RequestMapping("/message")
    public String index(HttpServletRequest request) {

        request.setAttribute("problemType", ProblemTypeEnum.values());

        return "student/message/index";
    }

    /**
     * 个人档案
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/stu_archive")
    public String stuArchive(HttpServletRequest request) {
        User user = userManagerService.queryUserInfo(getCurrUser(request));
        request.setAttribute("userName", user.getUserName());
        request.setAttribute("userType", user.getUserType());
        request.setAttribute("userId", user.getId());
        return "student/person_archive";
    }

    @RequestMapping(value = "/student_course")
    public String course(HttpServletRequest request,
                         @RequestParam(value="project_id", required = true, defaultValue = "") String projectId) {

        ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(projectId);
        if(null != projectInfo){
            request.setAttribute("projectType", projectInfo.getProjectType());
        }

        return "student/person_course_info";
    }

    /**
     * 学员统计
     *
     * @param request
     * @return
     */
    @RequestMapping("/student_tj")
    @ResponseBody
    public Response<Object> studentTj(HttpServletRequest request) {
        Response<Object> resp = new Response<Object>();
        User user = getCurrUser(request);
        //获取公司排名
        Map<String, Object> result = personDossierService.selectRank(user.getId(), user.getCompanyId());
        resp.setResult(result);
        return resp;
    }

    /**
     * 人员档案中 某个项目记录
     *
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
    @RequestMapping("/stu_project_list")
    @ResponseBody
    public Object project_list(HttpServletRequest request,
                               @RequestParam(value = "user_id", required = true, defaultValue = "") String userId,
                               @RequestParam(value = "project_name", required = true, defaultValue = "") String projectName,
                               @RequestParam(value = "train_status", required = true, defaultValue = "") String trainStatus,
                               @RequestParam(value = "project_start_time", required = true, defaultValue = "") String projectStartTime,
                               @RequestParam(value = "project_end_time", required = true, defaultValue = "") String projectEndTime,
                               @RequestParam(value = "pageSize", required = true, defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        User user = this.getCurrUser(request);

        if (pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }

        //开始时间补充00:00:00
        if (!org.apache.commons.lang.StringUtils.isEmpty(projectStartTime)) {
            projectStartTime = projectStartTime + " 00:00:00";
        }
        //结束时间补充23:59:59
        if (!org.apache.commons.lang.StringUtils.isEmpty(projectEndTime)) {
            projectEndTime = projectEndTime + " 23:59:59";
        }
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("projectName", ParamsUtil.joinLike(projectName));
        params.put("trainStatus", trainStatus);
        params.put("projectStartTime", projectStartTime);
        params.put("projectEndTime", projectEndTime);

        //查询本公司所有项目
        Map<String, Object>  newParams = new HashMap<String, Object>();
        List<String> projectIds = projectUserService.selectProjectIdByUserId(params);
        if(null == projectIds || projectIds.size() < 1){
            page = new Page(0, pageNum, pageSize);
            resp.setResult(page);
            return resp;
        }

        //查询本公司已发布的项目id
        newParams.put("projects", projectIds);
        newParams.put("projectStatusList", Arrays.asList("2,3,4".split(",")));
        List<String> plist = projectBasicService.selectProjectIdsByStatus(newParams);
        if(null == plist || plist.size() < 1){
            page = new Page(0, pageNum, pageSize);
            resp.setResult(page);
            return resp;
        }
        params.put("ids", plist);

        //查询我的项目下所有已发布的项目
        Integer count = projectStatisticsInfoService.selectCount(params);
        page = new Page(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("endNum", page.getPageSize());
        List<Map<String, Object>> listMap = projectStatisticsInfoService.selectList(params);
        page.setDataList(listMap);
        resp.setResult(page);
        return resp;
    }


    @RequestMapping("/student_course_list")
    @ResponseBody
    public Object course_list(
            @RequestParam(value = "project_id", required = true, defaultValue = "") String projectId,
            @RequestParam(value = "user_id", required = true, defaultValue = "") String userId,
            @RequestParam(value = "course_name", required = true, defaultValue = "") String courseName,
            @RequestParam(value = "pageSize", required = true, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response<Object> resp = new Response<Object>();


        if (pageSize > 100) {
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

    //查询该公司下所有受训角色
    @RequestMapping("/allTrainRole")
    @ResponseBody
    public Response<Object> selectAllRole(HttpServletRequest request) {
        Response<Object> resp = new Response<Object>();
        User u = getCurrUser(request);
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("companyId", u.getCompanyId());
        List<String> companyTrainRoles = companyTrainRoleService.selectByCompanyId(u.getCompanyId());
        List<Map<String, Object>> trainRoles = new ArrayList<Map<String, Object>>();

        if (companyTrainRoles != null && companyTrainRoles.size() > 0) {
            para.put("ids", companyTrainRoles);
            //查询公司下所有的的受训色
            trainRoles = trainRoleService.queryTrainRoles(para);
        }

       /* List<Map<String, Object>> rmap = userManagerService.queryAllRoleByParams(para);*/
        JSONArray jsonArray = new JSONArray();
        if (trainRoles.size() > 0) {
            // result

            for (Map<String, Object> map : trainRoles) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", map.get("varId").toString());
                jsonObject.put("text", map.get("varRoleName").toString());
                jsonArray.add(jsonObject);
            }

        }
        resp.setCode(10000);
        resp.setResult(jsonArray);
        return resp;
    }

    //查询该学员的受训角色
    @RequestMapping("/checkedRole")
    @ResponseBody
    public Response<Object> checkedRole(HttpServletRequest request, String userId) {
        Response<Object> resp = new Response<Object>();
        User user = getCurrUser(request);
        // 查询用户角色
        UserTrainRole utr = new UserTrainRole();
        utr.setUserId(userId);
        List<UserTrainRole> userRoles = new ArrayList<>();
        userRoles = userManagerService.queryUserRoles(utr);

       /* if (userRoles.size() > 0) {
            // result
            resp.setCode(10000);
            resp.setResult(userRoles);
        } else {
            resp.setMessage("查询失败");
        }*/
        // result
        resp.setCode(10000);
        resp.setResult(userRoles);
        return resp;
    }
}
