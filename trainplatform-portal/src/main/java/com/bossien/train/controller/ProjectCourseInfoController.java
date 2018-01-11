package com.bossien.train.controller;

import com.bossien.train.domain.Page;
import com.bossien.train.domain.ProjectStatisticsInfo;
import com.bossien.train.exception.Code;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IProjectCourseInfoService;
import com.bossien.train.service.IProjectStatisticsInfoService;
import com.bossien.train.service.IUserService;
import com.bossien.train.util.MathUtil;
import com.bossien.train.util.ParamsUtil;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017-07-25.
 */
@Controller
@RequestMapping(value="/admin/projectCourseInfo")
public class ProjectCourseInfoController extends BasicController{

   @Autowired
   private IProjectCourseInfoService projectCourseInfoService;

    /**
     * 进入培训或者练习记录
     * @param projectId
     * @param userId
     * @param modelMap
     * @return
     */
    @RequestMapping("project_course_entry")
    public String projectCourseEntry(
        @RequestParam(value = "projectId", required = false,defaultValue = "")  String projectId , //项目id
        @RequestParam(value = "userId", required = false,defaultValue = "")  String userId ,      //用户Id
        @RequestParam(value = "type", required = false,defaultValue = "1")  String type ,      //类型
        ModelMap modelMap) {
        modelMap.addAttribute("projectId",projectId);
        modelMap.addAttribute("userId",userId);
        if("1".equals(type)){
            return "admin/proManager/trainRecord";
        }
        return "admin/proManager/exerciseRecord";
    }

    /**
     * 查询项目用户课程信息
     * @param projectId
     * @param userId
     * @param courseName
     * @param pageSize
     * @param pageNum
     * @return
     */
    @RequestMapping("/project_course_list")
    @ResponseBody
    public Object projectCourseList(@RequestParam(value = "projectId", required = false, defaultValue = "") String projectId,  //项目id
        @RequestParam(value = "userId", required = false, defaultValue = "") String userId,              //用户id
        @RequestParam(value = "courseName", required = false, defaultValue = "") String courseName,      //课程名称
        @RequestParam(value = "pageSize", required = true, defaultValue = "1") Integer pageSize,     //每页条数
        @RequestParam(value = "pageNum", required = true, defaultValue = "1") Integer pageNum        //页码
    ) {
        Response <Object> response = new Response <Object>();
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();
        Map <String, Object> params = new HashedMap();
        params.put("projectId", projectId);
        params.put("userId", userId);
        params.put("courseName", ParamsUtil.joinLike(courseName));
        params.put("startNum", (pageNum - 1) * pageSize);
        params.put("endNum", pageSize);
        //项目课程信息
        List<Map <String, Object>> result = projectCourseInfoService.selectList(params);
        //项目课程信息数量
        int count = projectCourseInfoService.selectCount(params);
        page = new Page(count, pageNum, pageSize);
        response.setCode(Code.SUCCESS.getCode());
        page.setDataList(result);
        response.setResult(page);
        return response;
    }
}
