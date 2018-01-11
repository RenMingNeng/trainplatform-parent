package com.bossien.train.controller;

import com.bossien.framework.interceptor.SignInterceptor;
import com.bossien.train.domain.Page;
import com.bossien.train.domain.TrainSubject;
import com.bossien.train.domain.User;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.*;
import com.bossien.train.util.FastDFSUtil;
import com.bossien.train.util.ParamsUtil;
import com.bossien.train.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/super")
public class SuperController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private ITrainSubjectService trainSubjectService;
    @Autowired
    private ICompanyService companyService;
    @Autowired
    private ICompanyTypeService companyTypeService;
    @Autowired
    private ICourseTypeService courseTypeService;
    @Autowired
    private ICourseInfoService courseInfoService;
    @Autowired
    private IBusinessService businessService;
    @Autowired
    private ICompanyCategoryService companyCategoryService;

    @RequestMapping("/menu")
    public String menu() {
        return "super/menu";
    }

    /**
     * 公开型项目列表
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/project_public/list")
    @ResponseBody
    public Object public_course_list(
             @RequestParam(value="project_name", required = false, defaultValue = "") String project_name,      //项目名称
             @RequestParam(value="project_status", required = false, defaultValue = "") String project_status,  //项目状态
             @RequestParam(value="datBeginTime", required = false, defaultValue = "") String project_start_time,//项目开始时间
             @RequestParam(value="datEndTime", required = false, defaultValue = "") String project_end_time,    //项目结束时间
             @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
             @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response<Object> resp = new Response<Object>();
        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("project_mode", '1');
        params.put("project_name", ParamsUtil.joinLike(project_name));
        params.put("project_status", project_status);
        if(StringUtils.isNotEmpty(project_start_time)){
            params.put("project_start_time",project_start_time + " 00:00:00");
        }
        if(StringUtils.isNotEmpty(project_end_time)){
            params.put("project_end_time",project_end_time + " 23:59:59");
        }
        Integer count = projectInfoService.selectCount(params);
        page = new Page(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("endNum", page.getPageSize());
        List<Map<String, Object>> listMap = projectInfoService.selectList(params);
        page.setDataList(listMap);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);
        return resp;
    }

    /**
     * 加载单位分类树
     * @param
     * @return
     */
    @RequestMapping(value = "/companyType/companyType_tree")
    @ResponseBody
    public Object companyType_tree(HttpServletRequest request){
        Response<Object> response=new Response <Object>();
        User user = getCurrUser(request);
        List<Map <String, Object>> companyTypeZTree = companyTypeService.treeNodes(user);
        //转成 数组
        Object[] companyTypeZTrees = companyTypeZTree.toArray();
        response.setResult(companyTypeZTrees);
        return response;
    }

    /**
     * 加载单位行业树
     * @param
     * @return
     */
    @RequestMapping(value = "/business/companyBusiness_tree")
    @ResponseBody
    public Object companyBusiness_tree(){
        Response<Object> response=new Response <Object>();
        List<Map <String, Object>> businessZTree = businessService.treeNodes();
        //转成 数组
        Object[] companyTypeZTrees = businessZTree.toArray();
        response.setResult(companyTypeZTrees);
        return response;
    }

    /**
     * 加载单位类型树
     * @param
     * @return
     */
    @RequestMapping(value = "/category/companyCategory_tree")
    @ResponseBody
    public Object companyCategory_tree(){
        Response<Object> response=new Response <Object>();
        List<Map <String, Object>> companyCategoryZTree = companyCategoryService.treeNodes();
        //转成 数组
        Object[] companyCategoryZTrees = companyCategoryZTree.toArray();
        response.setResult(companyCategoryZTrees);
        return response;
    }


    /*选择培训单位列表(创建公开型项目)*/
    @RequestMapping("/selectCompany/company_list")
    @ResponseBody
    public Object company_list(HttpServletRequest request,
               @RequestParam(value="company_type", required = false, defaultValue = "") String company_type,
               @RequestParam(value="company_name", required = false, defaultValue = "") String company_name,
               @RequestParam(value="businessId", required = false, defaultValue = "") String businessId,
               @RequestParam(value="categoryId", required = false, defaultValue = "") String categoryId,
               @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
               @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response<Object> resp = new Response<Object>();

        Page<Map<String, Object>> page = new Page<Map<String, Object>>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("intTypeId", company_type);
        params.put("varName", ParamsUtil.joinLike(company_name));
        params.put("varBusinessId", businessId);
        params.put("intCategoryId", categoryId);
        //根据类型id获取所有子节点
        String varCTChiList = companyTypeService.getCTChiList(params);
        List<String> varCTList = Arrays.asList(varCTChiList.split(","));
        params.put("varCTList", varCTList);

        Integer count = companyService.selectCount(params);

        page = new Page(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("endNum", page.getPageSize());
        List<Map<String, Object>> listMap = companyService.selectList(params);

        page.setDataList(listMap);

        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);
        return resp;
    }

    /**
     * 加载课程类别树（全部课程）
     * @param
     * @return
     */
    @RequestMapping(value = "/courseType/ex_courseType_tree")
    @ResponseBody
    public Object ex_courseType_tree(HttpServletRequest request){
        Response<Object> response=new Response <Object>();
        User user = getCurrUser(request);
        List <Map <String, Object>> courseTypeTree= courseTypeService.zTreeNodes(user);
        //转成数组
        Object[] courseTypeZTrees=courseTypeTree.toArray();
        response.setResult(courseTypeZTrees);
        return response;
    }

    /**
     * 选择培训课程列表(创建公开型项目)
     * @param request
     * @param course_name
     * @param course_type
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/selectCourse/public_course_list")
    @ResponseBody
    public Object public_course_list(HttpServletRequest request,
                                     @RequestParam(value="course_project_id", required = false, defaultValue = "") String course_project_id,
                                     @RequestParam(value="course_name", required = false, defaultValue = "") String course_name,
                                     @RequestParam(value="course_type", required = false, defaultValue = "") String course_type,
                                     @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
                                     @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response<Object> resp = new Response<Object>();

        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }

        Page<Map<String, Object>> page = new Page<Map<String, Object>>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("course_name", ParamsUtil.joinLike(course_name));
        params.put("intTypeId", course_type);
        params.put("course_project_id", course_project_id);

        //根据类型id获取所有子节点
        if(StringUtil.isEmpty(course_type)){
            page = new Page(0, pageNum, pageSize);
            page.setDataList(new ArrayList<Map<String, Object>>());
            resp.setCode(Code.SUCCESS.getCode());
            resp.setResult(page);
            return resp;
        }
        String varCSTChiList = courseTypeService.getCSTChiList(params);
        List<String> varCSTList = Arrays.asList(varCSTChiList.split(","));
        params.put("varCSTList", varCSTList);

        Integer count = courseInfoService.selectCount(params);

        page = new Page(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("endNum", page.getPageSize());
        List<Map<String, Object>> listMap = courseInfoService.selectList(params);
        page.setDataList(listMap);

        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);
        return resp;
    }

    /**
     * 首页培训主题列表
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/train_subject/subject_list")
    @ResponseBody
    public Object subject_list(HttpServletRequest request,
        @RequestParam(value="pageSize", required = true, defaultValue = "11") Integer pageSize,
        @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        User user = getCurrUser(request);
        Response<Object> resp = new Response<Object>();
        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }
        Page<TrainSubject> page = new Page<TrainSubject>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("chrSource", '1');
        Integer count = trainSubjectService.selectCount(params);
        page = new Page(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("pageSize", page.getPageSize());
        List<TrainSubject> list = trainSubjectService.selectList(params);
        for (TrainSubject trainSubject : list) {
            String logo = FastDFSUtil.getHttpNginxURLWithToken(trainSubject.getLogo());
            trainSubject.setLogo(logo);
        }
        page.setDataList(list);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);
        return resp;
    }

}
