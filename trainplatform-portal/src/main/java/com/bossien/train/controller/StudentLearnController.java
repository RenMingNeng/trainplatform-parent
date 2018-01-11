package com.bossien.train.controller;

import com.bossien.train.domain.Course;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.ICourseService;
import com.bossien.train.service.IProjectCourseInfoService;
import com.bossien.train.util.ParamsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("student/learn")
public class StudentLearnController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(StudentLearnController.class);

    @Autowired
    private IProjectCourseInfoService projectCourseInfoService;

    @Autowired
    private ICourseService courseService;

    /**
     * 项目-进入学习-直接进入
     * @return
     */
    @RequestMapping("")
    public String index(
            ModelMap modelMap,
            HttpServletRequest request,
            @RequestParam(value="projectId", required = true, defaultValue = "") String projectId,
            @RequestParam(value="source", required = false, defaultValue = "-1") String source // 学时来源：-1：学习、1：练习答题（正确）、2：练习答题（错误）、3：考试答题（正确）、4：考试答题（错误）
        ) {

        modelMap.put("projectId", projectId);
        modelMap.put("userId", getCurrUser(request).getId());
        modelMap.put("source", source);
        return "student/learn/index";
    }

    /**
     * 项目-在线学习-课程列表
     * @param projectId
     * @param userId
     * @return
     */
    @RequestMapping("/getCourses")
    @ResponseBody
    public Object getCourses(
            @RequestParam(value="projectId", required = true, defaultValue = "") String projectId,
            @RequestParam(value="userId", required = true, defaultValue = "") String userId
    ){
        Response<Object> resp = new Response<Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId",projectId);
        params.put("userId", userId);
        List<Map<String, Object>> result = projectCourseInfoService.selectCourseList(params);
        for (Map<String, Object> map: result) {
            params.clear();
            params.put("intId", map.get("courseId"));
            Course course = courseService.selectOne(params);
            if(null != course && null != course.getVarDesc()){
                map.put("varDesc", course.getVarDesc());
            }
        }
        resp.setResult(result);
        return resp;
    }
}
