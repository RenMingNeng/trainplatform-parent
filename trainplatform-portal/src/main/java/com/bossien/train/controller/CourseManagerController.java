package com.bossien.train.controller;

import com.bossien.train.domain.*;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.*;
import com.bossien.train.util.ParamsUtil;
import org.apache.commons.collections.map.HashedMap;
import com.bossien.train.service.ICompanyCourseService;
import com.bossien.train.service.ICourseInfoService;
import com.bossien.train.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 管理员界面的课程管理
 * Created by Administrator on 2017/7/28.
 */
@Controller
@RequestMapping("admin/course")
public class CourseManagerController extends BasicController {


    @Autowired
    private ICompanyCourseService companyCourseService;
    @Autowired
    private ICourseService courseService;
    @Autowired
    private ICourseInfoService courseInfoService;
    @Autowired
    private ICompanyCourseTypeService companyCourseTypeService;


    /**
     * 进入课程管理页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/courseList")
    public String courseManagerList(ModelMap modelMap){
        return "admin/courseManager/courseManagerList";
    }


    /**
     * 选择授权课程（私有项目）
     * @param request
     * @param courseName
     * @param intTypeId
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/course_list")
    @ResponseBody
    public Object private_course_list(HttpServletRequest request,
                                      @RequestParam(value="courseName", required = false, defaultValue = "") String courseName,
                                      @RequestParam(value="intTypeId", required = false, defaultValue = "") String intTypeId,
                                      @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
                                      @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response<Object> resp = new Response<Object>();
        User user=getCurrUser(request);
        String intCompanyId=user.getCompanyId();
        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }

        Page<Map<String, Object>> page = new Page<Map<String, Object>>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("courseName", ParamsUtil.joinLike(courseName));
        params.put("courseTypeIds", intTypeId);
        params.put("intCompanyId",intCompanyId);
        if(!"".equals(intTypeId)) {
            params.put("courseTypeIds", companyCourseTypeService.getAPCTypeChiList(params));
        }
        //根据companyId查询courseIds
        List<Map<String,Object>> companyCourses=companyCourseService.selectByCompanyId(params);
        Integer count = 0;
        List<Map<String, Object>> courses = new ArrayList <Map<String, Object>>();
        if(null!=companyCourses && companyCourses.size()>0 ){
            List<String>  courseIds=new ArrayList <String>();
            for (Map map:companyCourses) {
                courseIds.add(map.get("intCourseId").toString()) ;
            }
            params.put("courseIds",courseIds);
            count = courseInfoService.selectCourseCount(params);

            page = new Page(count, pageNum, pageSize);
            params.put("startNum", page.getStartNum());
            params.put("endNum", page.getPageSize());
            //从courseInfo中查询课程信息
            courses=courseInfoService.selectCourseList(params);

        }
        page.setDataList(courses);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);
        return resp;
    }

    /**
     * 课程管理（课程详情）
     * @param request
     * @param courseId
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "/course_info")
    public ModelAndView courseInfo(HttpServletRequest request,
            @RequestParam(value="courseId", required = false, defaultValue = "") String courseId
           )throws Exception{
        User user=getCurrUser(request);
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("companyId",user.getCompanyId());
        //根据courseId查询课程信息
        CourseInfo courseInfo=courseInfoService.selectOne(courseId);
        Course course= courseService.selectById(courseId);
        if(null!=course) {
            courseInfo.setCourseDesc(course.getVarDesc());
        }
        String courseTypeName = "";
       //课程类别
        if(!"".equals(courseInfo.getCourseTypeId())&& null!=courseInfo.getCourseTypeId()) {
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("intCourseId",courseInfo.getCourseId());
            param.put("intCompanyId",user.getCompanyId());
            CompanyCourse companyCourse = companyCourseService.selectOne(param);
            courseTypeName = companyCourseTypeService.getParentTypeName(companyCourse.getIntCompanyCourseTypeId(), courseTypeName);
        }
        return  new ModelAndView("admin/courseManager/course_info")
                                .addObject("courseInfo",courseInfo)
                                .addObject("courseTypeName",courseTypeName);
    }

    /**
     *
     * @param courseIds
     * @param courseTypeId
     * @return
     */
    @RequestMapping(value = "/course_move")
    @ResponseBody
    public Object courseMove(HttpServletRequest request,
            @RequestParam(value="courseIds", required = false, defaultValue = "") String[] courseIds,
            @RequestParam(value="courseTypeId", required = false, defaultValue = "") String courseTypeId
              ){
        Response<Object> resp = new Response<Object>();
        User user=getCurrUser(request);
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("courseIds",courseIds);
        params.put("courseTypeId",courseTypeId);
        params.put("operUser",user.getUserName());
        params.put("operTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        courseInfoService.courseMove(params);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(Code.SUCCESS.getCode());
        return resp;
    }
}
