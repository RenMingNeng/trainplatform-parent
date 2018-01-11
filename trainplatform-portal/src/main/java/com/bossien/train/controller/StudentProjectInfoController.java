package com.bossien.train.controller;

import com.bossien.train.domain.*;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.*;
import com.bossien.train.util.ParamsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学员项目详情
 * Created by Administrator on 2017/7/25.
 */
@Controller
@RequestMapping("student/ProjectInfo")
public class StudentProjectInfoController extends BasicController {
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private IProjectCourseService projectCourseService;
    @Autowired
    private IProjectCourseInfoService projectCourseInfoService;
    @Autowired
    private IProjectDepartmentService projectDepartmentService;
    @Autowired
    private IExamStrategyService examStrategyService;
    @Autowired
    private IProjectUserService projectUserService;
    /**
     *学员  进入项目详情页面(项目基础信息)
     * @return
     */
    @RequestMapping("/student_info")
    public String template_student_info(HttpServletRequest request,
            @RequestParam(value = "projectMode", required = false,defaultValue = "")  String projectMode , // 项目公开标识
            @RequestParam(value = "projectId", required = false,defaultValue = "")  String projectId , //项目id
             ModelMap modelMap) {
       User user =getCurrUser(request);
        //根据projectId查询项目信息
        ProjectInfo projectInfo=projectInfoService.selectProjectInfoById(projectId);
        modelMap.addAttribute("projectInfo",projectInfo);         // 项目基础信息
        modelMap.addAttribute("projectMode",projectMode);         // 项目公开标识

        //根据projectId和companyId查询受训单位
        Map<String,Object>  param=new HashMap<String,Object>();
        param.put("projectId" ,projectId);
        param.put("companyId", user.getCompanyId());
        List<ProjectDepartment> projectDepartments=projectDepartmentService.selectByProjectIdAndCompanyId(param);
        modelMap.addAttribute("projectDepartments",projectDepartments);         //受训单位信息

        //查询组卷策略
        if(null!=projectInfo){
            if (hasPermissionExam(projectInfo.getProjectType())){
                ExamStrategy examStrategy=examStrategyService.selectByProjectIdAndRoleId(param);
                if(null!=examStrategy){
                    //组装组卷策略的题型数据信息
                  List<Map<String,Object>>  examStrategyRows=examStrategyService.assembleExamStrategy(examStrategy);  //组装策略信息
                  modelMap.addAttribute("examStrategyRows",examStrategyRows);  //多种题型组合信息
                }

               modelMap.addAttribute("examStrategy",examStrategy);             //组卷策略信息
            }
        }

        return "student/projectInfo/project_info";
    }

    /**
     * 学员  项目详情（项目课程列表信息）
     * @param projectId     项目id
     * @param courseName    课程名称
     * @param pageSize      页数据
     * @param pageNum       页码
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/courseInfo_list")
    @ResponseBody
    public Object courseInfo_list(HttpServletRequest request,
               @RequestParam(value="projectId", required = false,defaultValue = "")  String projectId,  //项目id           //项目id
               @RequestParam(value="courseName" , required = false, defaultValue = "")  String courseName,      //角色id
               @RequestParam(value="pageSize", required = true, defaultValue = "1") Integer pageSize,     //每页条数
               @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum        //页码
                 ) throws ServiceException {
        User user=getCurrUser(request);
        Response<Object> response=new Response <Object>();
        List<Map<String, Object>> listMap=new ArrayList <Map<String,Object>>();
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();

        //每页条数过大
        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST,"pageSize 参数过大");
        }

        Map<String,Object> map=new HashMap<String,Object>();
        map.put("projectId",projectId);
        map.put("userId",user.getId());
        map.put("courseName", ParamsUtil.joinLike(courseName));      //通过名字模糊查询
        map.put("startNum", (pageNum-1)*pageSize);
        map.put("endNum", pageSize);
        String roleId=projectUserService.selectRoleId(map);
        map.put("roleId",roleId);
        Integer count=projectCourseService.selectCourseCount(map);
        page = new Page(count, pageNum, pageSize);
        //根据项目id查询课程信息
        //通过projectId查询项目下的课程信息
        List<Map<String, Object>> projectCourses= projectCourseService.selectList(map);

        page.setDataList(projectCourses);
        response.setResult(page);
        return response;
    }

}
