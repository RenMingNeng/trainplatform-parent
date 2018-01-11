package com.bossien.train.controller;

import com.bossien.train.domain.Page;
import com.bossien.train.domain.User;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IProjectInfoService;
import com.bossien.train.service.IProjectUserService;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

import com.bossien.train.util.ParamsUtil;
import com.bossien.train.util.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student/pu")
public class ProjectUserController extends BasicController{

    protected static Logger LOG= LoggerFactory.getLogger(ProjectUserController.class);
    @Autowired
    private IProjectUserService projectUserService;
    @Autowired
    private IProjectInfoService projectInfoService;

    @RequestMapping("/list")
    @ResponseBody
    public Object list(HttpServletRequest request,
                       @RequestParam(value="startTime", required = false, defaultValue = "") String startTime,
                       @RequestParam(value="endTime", required = false, defaultValue = "") String endTime,
                       @RequestParam(value="projectName", required = false, defaultValue = "") String projectName,
                       @RequestParam(value="projectStatus", required = false, defaultValue = "") String projectStatus,
                       @RequestParam(value="pageSize", required = true, defaultValue = "10000") Integer pageSize,
                       @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException{
        Response<Object> resp = new Response<Object>();
        User user=getCurrUser(request);
        if(pageSize > 1000){
            LOG.error("ProjectUserController.list-------------pageSize 参数过大");
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();
        Map<String, Object> params = new HashMap<String, Object>();
        //查询当前学员的所有项目
        params.put("userId",user.getId());
        List<String> projectIds = projectUserService.selectProjectIdByUserId(params);
        List<Map<String, Object>> listMap = new ArrayList<>();
        if(projectIds != null && projectIds.size() >0){
            params.put("projectIds",projectIds);
            params.put("project_name", ParamsUtil.joinLike(projectName));
            params.put("project_status",projectStatus);
            params.put("unPublish", PropertiesUtils.getValue("unPublish"));
            if(StringUtils.isNotEmpty(startTime)){
                params.put("project_start_time",startTime + " 00:00:00");
            }
            if(StringUtils.isNotEmpty(endTime)){
                params.put("project_end_time",endTime + " 23:59:59");
            }
            Integer count = projectInfoService.selectCount(params);

            page = new Page(count, pageNum, pageSize);
            params.put("startNum", page.getStartNum());
            params.put("endNum", page.getPageSize());
            listMap = projectInfoService.selectList(params);
        }
        page.setDataList(listMap);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);
        return resp;

    }

}
