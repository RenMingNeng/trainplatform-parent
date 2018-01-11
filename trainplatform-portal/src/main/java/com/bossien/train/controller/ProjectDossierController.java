package com.bossien.train.controller;

import com.bossien.train.domain.Page;
import com.bossien.train.domain.ProjectInfo;
import com.bossien.train.domain.User;
import com.bossien.train.domain.eum.ProjectTypeEnum;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.*;
import com.bossien.train.util.ExportExcelUtils;
import com.bossien.train.util.ParamsUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by A on 2017/7/25.
 */

@Controller
@RequestMapping("/admin/dossier/project")
public class ProjectDossierController extends BasicController{
    @Autowired
    private IProjectDossierService projectDossierService;

    @Autowired
    private IProjectStatisticsInfoService projectStatisticsInfoService;

    @Autowired
    private ITrainRoleService trainRoleService;

    @Autowired
    private IProjectInfoService projectInfoService;

    /**
     * 项目档案
     * @param request
     * @return
     */
    @RequestMapping
    public String index(HttpServletRequest request){

        request.setAttribute("project_type", Arrays.asList(ProjectTypeEnum.values()));

        return "admin/dossier/project_index";
    }

    /**
     * 项目档案异步
     * @param request
     * @param projectType
     * @param projectStartTime
     * @param projectEndTime
     * @param projectName
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(HttpServletRequest request,
                       @RequestParam(value="project_type", required = true, defaultValue = "") String projectType,
                       @RequestParam(value="project_start_time", required = true, defaultValue = "") String projectStartTime,
                       @RequestParam(value="project_end_time", required = true, defaultValue = "") String projectEndTime,
                       @RequestParam(value="project_name", required = true, defaultValue = "") String projectName,
                       @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
                       @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response<Object> resp = new Response<Object>();

        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }

        User user = this.getCurrUser(request);
        if(null == user){
            resp.setCode(Code.USER_NOT_LOGIN.getCode());
            resp.setMessage(Code.USER_NOT_LOGIN.getMessage());
            return resp;
        }
        //开始时间补充00:00:00
        if(!StringUtils.isEmpty(projectStartTime)){
            projectStartTime = projectStartTime+" 00:00:00";
        }
        //结束时间补充23:59:59
        if(!StringUtils.isEmpty(projectEndTime)){
            projectEndTime = projectEndTime+" 23:59:59";
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectType", projectType);
        params.put("projectName", ParamsUtil.joinLike(projectName));
        params.put("projectStartTime", projectStartTime);
        params.put("projectEndTime", projectEndTime);
        Page<Map<String, Object>> page = projectDossierService.queryForPagination(params, pageNum, pageSize, user);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);
        return resp;
    }

    /**
     * 项目中的人员记录
     * @param request
     * @return
     */
    @RequestMapping(value = "/user")
    public String user(HttpServletRequest request,
                       @RequestParam(value="project_id", required = true, defaultValue = "") String projectId){

//        Map<String, Object> params = new HashedMap();
//        User user = this.getCurrUser(request);
//        params.put("intCompanyId", user.getCompanyId());
//        request.setAttribute("roles", trainRoleService.selectByCompanyId(params));

        ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(projectId);
        if(null != projectInfo){
            request.setAttribute("projectName", projectInfo.getProjectName());
            request.setAttribute("projectType", projectInfo.getProjectType());
        }

        return "admin/dossier/project_info";
    }

    /**
     * 项目中人员记录异步
     * @param request
     * @param projectId
     * @param userName
     * @param deptName
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/user_list")
    @ResponseBody
    public Object user_list(HttpServletRequest request,
                               @RequestParam(value="project_id", required = true, defaultValue = "") String projectId,
                               @RequestParam(value="user_name", required = true, defaultValue = "") String userName,
                               @RequestParam(value="dept_name", required = true, defaultValue = "") String deptName,
                               @RequestParam(value="role_name", required = true, defaultValue = "") String roleName,
                               @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
                               @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response<Object> resp = new Response<Object>();


        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }

        Page<Map<String, Object>> page = new Page<Map<String, Object>>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId", projectId);
        params.put("userName", ParamsUtil.joinLike(userName));
        params.put("deptName", ParamsUtil.joinLike(deptName));
        params.put("roleName", ParamsUtil.joinLike(roleName));
        Integer count = projectStatisticsInfoService.selectCount(params);

        page = new Page(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("endNum", page.getPageSize());
        List<Map<String, Object>> listMap = projectStatisticsInfoService.selectList(params);

        page.setDataList(listMap);

        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);

        return resp;
    }

    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value="project_id", required = true, defaultValue = "") String projectId,
            @RequestParam(value="project_type", required = true, defaultValue = "") String projectType) throws Exception{
        String fileName = "项目档案信息" + System.currentTimeMillis() + ".xlsx";
        User user = this.getCurrUser(request);
        projectDossierService.export(projectId, projectType, user.getId(), ExportExcelUtils.setExcelHeard(response, fileName));
    }
}
