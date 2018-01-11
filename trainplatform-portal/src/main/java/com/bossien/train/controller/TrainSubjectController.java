package com.bossien.train.controller;

import com.bossien.train.domain.*;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.*;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.FastDFSUtil;
import com.bossien.train.util.ParamsUtil;
import com.bossien.train.util.UploadFileUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.util.*;

/**
 * Created by zhaoli on 2017/8/2.
 * 培训主题业务接口
 */
@Controller
@RequestMapping("admin/trainSubject")
public class TrainSubjectController extends BasicController {
    private static final Logger logger = LogManager.getLogger(TrainSubjectController.class);

    @Autowired
    private ITrainSubjectService trainSubjectService;
    @Autowired
    private ICompanyTrainSubjectService companyTrainSubjectService;
    @Autowired
    private ISubjectCourseService subjectCourseService;
    @Autowired
    private ICompanyCourseService companyCourseService;
    @Autowired
    private ISequenceService sequenceService;
    @Autowired
    private ICourseInfoService courseInfoService;
    @Value("${ENTERPRISE_NUMBER}")
    private String enterprise_number;//企业自制
    @Value("${NO_SYSTEM}")
    private String no_system;
    @Value("${CHRISVAl_ID}")
    private String isVal;

    /**
     * 初始化分页查询
     *
     * @param request
     * @return
     */
    @RequestMapping
    public String index(HttpServletRequest request) {
        return "admin/basicinfo/train_subject_index";
    }

    @RequestMapping("/paging")
    @ResponseBody
    public Object list(HttpServletRequest request, String subjectName,
                       @RequestParam(value = "pageSize", required = true, defaultValue = "10") Integer pageSize,
                       @RequestParam(value = "pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Map<String, Object> params = new HashMap<String, Object>();
        User user = getCurrUser(request);
        if (user == null) {
            logger.error("TrainSubjectController.add-------------用户未登录");
            throw new ServiceException(Code.USER_NOT_LOGIN);
        }
        Response<Object> resp = new Response<Object>();
        params.put("subjectName", ParamsUtil.joinLike(subjectName));
        List<String> list = companyTrainSubjectService.selectByCompanyId(user.getCompanyId());
        Page<TrainSubject> page = new Page<TrainSubject>();
        int count = 0;
        if (list.size() <= 0) {
            list.add(no_system);
        }
        params.put("varId", list);
        count = trainSubjectService.selectCount(params);
        page = new Page(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("pageSize", page.getPageSize());
        List<TrainSubject> trainSubjectList = trainSubjectService.selectList(params);
        for (TrainSubject trainSubject : trainSubjectList) {
            //通过subjectId查询到所有的courseId
            List<String> courseIds = subjectCourseService.selectCourseIds(trainSubject.getVarId());
            Integer courseCount = 0;
            if(courseIds.size()>0){
                params.put("courseIds",courseIds);
                params.put("companyId",user.getCompanyId());
                courseCount = companyCourseService.selectCompanyCourseCount(params);
            }
            //授权课程和主题关联的课程取交集
            trainSubject.setCourseCount(courseCount.toString());

            if (!"".equals(trainSubject.getLogo())) {
                String logo = FastDFSUtil.getHttpNginxURLWithToken(trainSubject.getLogo());
                trainSubject.setLogo(logo);
            }
        }
        page.setDataList(trainSubjectList);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);
        return resp;
    }

    /**
     * 进入新增页面
     *
     * @return
     */
    @RequestMapping(value = "/trainsubjectAdd")
    public ModelAndView addTrainSubject() {
        int randow = new Random().nextInt(2);
        ModelAndView modelAndView = new ModelAndView("admin/basicinfo/train_subject_add");
        modelAndView.addObject("num", randow);
        return modelAndView;
    }


    /**
     * 进入修改页面
     */

    @RequestMapping(value = "/trainsubjectModify")
    public ModelAndView UpdateList(HttpServletRequest request, @Valid TrainSubject trainSubject,
                                   @RequestParam(value = "varId") String varId) throws Exception {
        trainSubject.setVarId(varId);
        TrainSubject trainSubjectOne = trainSubjectService.selectOne(trainSubject);//查询数据
        ModelAndView modelAndView = new ModelAndView("admin/basicinfo/train_subject_modify");
        if (!"".equals(trainSubjectOne.getLogo())) {
            String logo = FastDFSUtil.getHttpNginxURLWithToken(trainSubjectOne.getLogo());
            trainSubjectOne.setLogo(logo);
        }
        int randow = new Random().nextInt(2);
        modelAndView.addObject("num", randow);
        modelAndView.addObject("trainSubject", trainSubjectOne);
        return modelAndView;
    }

    /**
     * 进入详情页面
     */

    @RequestMapping(value = "/trainSubjectInfo")
    public ModelAndView trainsubjectInfo(HttpServletRequest request, @Valid TrainSubject trainSubject,
                                         @RequestParam(value = "varId") String varId) throws Exception {
        trainSubject.setVarId(varId);
        TrainSubject trainSubjectOne = trainSubjectService.selectOne(trainSubject);//查询数据
        ModelAndView modelAndView = new ModelAndView("admin/basicinfo/train_subject_info");
        if (!"".equals(trainSubjectOne.getLogo())) {
            String logo = FastDFSUtil.getHttpNginxURLWithToken(trainSubjectOne.getLogo());
            trainSubjectOne.setLogo(logo);
        }
        int randow = new Random().nextInt(2);
        modelAndView.addObject("num", randow);

        modelAndView.addObject("trainSubject", trainSubjectOne);
        return modelAndView;
    }

    /**
     * 分页展示主题对应的课程
     */
    @RequestMapping("/courseList")
    @ResponseBody
    public Object CourseList(HttpServletRequest request,
                             @RequestParam(value = "varId") String varId,
                             @RequestParam(value = "courseName", required = false) String courseName,
                             @RequestParam(value = "pageSize", required = true, defaultValue = "10") Integer pageSize,
                             @RequestParam(value = "pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {

        if (varId == null) {
            logger.error("TrainSubjectController.courseList-------------没有id");
            throw new ServiceException(Code.USER_NOT_LOGIN);
        }
        User user = getCurrUser(request);
        if (user == null) {
            logger.error("TrainSubjectController.add-------------用户未登录");
            throw new ServiceException(Code.USER_NOT_LOGIN);
        }
        Map<String, Object> params = new HashMap<String, Object>();
        Response<Object> resp = new Response<Object>();
        List<String> courseId = subjectCourseService.selectCourseIds(varId);//通过主题id查询关联关系的课程id*/
        params.put("intCompanyId", user.getCompanyId());
       /*查询出本公司的课程id*/
        List<Map<String, Object>> mapList = companyCourseService.selectByCompanyId(params);
        List<String> companyCourseId = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            String id = map.get("intCourseId").toString();
            companyCourseId.add(id);
        }
        courseId.retainAll(companyCourseId);
        List<Map<String, Object>> courseList = new ArrayList<Map<String, Object>>();
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();
        if (courseId.size() > 0) {
            //params.put("intIds", courseId);
            params.put("courseName", ParamsUtil.joinLike(courseName));
            params.put("courseIds", courseId);
            int count = courseInfoService.selectCourseCount(params);
            /*int count = courseService.selectCount(params);*/
            page = new Page(count, pageNum, pageSize);
            params.put("startNum", page.getStartNum());
            params.put("endNum", page.getPageSize());
            //从courseInfo中查询课程信息
            courseList = courseInfoService.selectCourseList(params);
            //courseList = courseService.selectList(params);//查询出课程数据
        }
        page.setDataList(courseList);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);
        return resp;
    }

    /**
     * 修改主题对应的课程
     *
     * @param request
     * @param
     * @return Response
     * @throws ServiceException
     */
    @RequestMapping(value = "/updateCourse", method = RequestMethod.POST)
    @ResponseBody
    public Object UpdateCourse(HttpServletRequest request,
                               @RequestParam(value = "courseIds", required = false, defaultValue = "") String[] courseIds,
                               @RequestParam(value = "varId", required = false, defaultValue = "") String varId
    ) throws ServiceException, UnsupportedEncodingException {
        Response<Object> resp = new Response<Object>();
        if (courseIds.length > 0) {
            subjectCourseService.deleteByTrainSubjectId(varId);//先删除掉
        }

        /*在保存*/
        //trainSubjectCourse表添加数据
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("trainSubjectId", varId);
        param.put("courseIds", courseIds);
        //批量添加数据
        subjectCourseService.saveTrainSubjectCourse(param);
        /*修改数量*/
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("courseCount", courseIds.length);
        params.put("varId", varId);
        trainSubjectService.update(params);
        return resp;
    }


    /**
     * 删除选择课程
     *
     * @param request
     * @param
     * @return Response
     * @throws ServiceException
     */
    @RequestMapping(value = "/deleteCourse", method = RequestMethod.POST)
    @ResponseBody
    public Object DeleteCourse(HttpServletRequest request,
                               @RequestParam(value = "courseIds", required = false, defaultValue = "") String[] courseIds,
                               @RequestParam(value = "varId", required = false, defaultValue = "") String varId
    ) throws ServiceException, UnsupportedEncodingException {
        Response<Object> resp = new Response<Object>();
        if (varId == null) {
            logger.error("删除选择课程-------------没有主题id");
            throw new ServiceException(Code.USER_NOT_LOGIN);
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("list", courseIds);
        param.put("intTrainSubjectId", varId);
        subjectCourseService.deleteByCourseId(param);
        Map<String, Object> params = new HashMap<String, Object>();
        TrainSubject trainSubject = new TrainSubject();
        trainSubject.setVarId(varId);
        TrainSubject subject = trainSubjectService.selectOne(trainSubject);
        int courseCount = Integer.parseInt(subject.getCourseCount()) - courseIds.length;
        if (courseCount < 0) {
            courseCount = 0;
        }
        params.put("courseCount", String.valueOf(courseCount));
        params.put("varId", varId);
        trainSubjectService.update(params);
        return resp;
    }

    /**
     * 获取主题对应的所有课程
     *
     * @param request
     * @param
     * @return Response
     * @throws ServiceException
     */
    @RequestMapping(value = "/allCourse", method = RequestMethod.POST)
    @ResponseBody
    public Object allCourse(HttpServletRequest request,

                            @RequestParam(value = "varId", required = false, defaultValue = "") String varId
    ) throws ServiceException, UnsupportedEncodingException {
        Response<Object> resp = new Response<Object>();
        List<String> courseId = subjectCourseService.selectCourseIds(varId);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(courseId);
        return resp;
    }

    /**
     * 新增企业自制培训主题
     *
     * @param request
     * @param
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(HttpServletRequest request,
                      @RequestParam(value = "initFileName", required = false, defaultValue = "") String initFileName,
                      @RequestParam(value = "courseIds", required = false) String[] courseIds,
                      @RequestParam(value = "subjectName", required = false, defaultValue = "") String name,
                      @RequestParam(value = "subjectDesc", required = false, defaultValue = "") String desc
    ) throws Exception {
        Response<Object> response = new Response<Object>();
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("fileName");
        //验证非空参数
        if (StringUtils.isEmpty(name)) {
            throw new ServiceException(Code.BAD_REQUEST, "主题名称为空");
        }
        User user = getCurrUser(request);
        if (user == null) {
            logger.error("TrainSubjectController.add-------------用户未登录");
            throw new ServiceException(Code.USER_NOT_LOGIN);
        }
        //封装参数
        TrainSubject trainSubject = new TrainSubject();
        trainSubject.setSubjectName(name.trim());
        trainSubject.setVarId(sequenceService.generator());
        trainSubject.setCreateUser(user.getId());
        trainSubject.setSource(enterprise_number);//来源默认是企业自制
        Date date = new Date();
        trainSubject.setCreateDate(DateUtils.convertDate2StringTime(date));
        trainSubject.setOperDate(DateUtils.convertDate2StringTime(date));
        trainSubject.setCreateUser(user.getCreateUser());
        trainSubject.setOperUser(user.getOperUser());
        trainSubject.setSubjectDesc(desc);
        trainSubject.setIsValid(isVal);
        trainSubject.setCourseCount(String.valueOf(courseIds.length));
        if (null != multipartFile && !multipartFile.getOriginalFilename().equals("")) {
            CommonsMultipartFile cf = (CommonsMultipartFile) multipartFile;
            DiskFileItem diskFileItem = (DiskFileItem) cf.getFileItem();
            File file = diskFileItem.getStoreLocation();
            String file_name = multipartFile.getOriginalFilename();
            String suffix = file_name.substring(file_name.lastIndexOf(".") + 1);
            try {
                String fileId = UploadFileUtil.upFile(file_name, file, suffix);
                trainSubject.setLogo(fileId);
            } catch (Exception e) {
                response.setMessage("fail");
                logger.error("======FastDFS消息服务器出错" + e.getMessage());
            }
        }
        trainSubjectService.insert(trainSubject);
        CompanyTrainSubject record = new CompanyTrainSubject();
        record.setIntCompanyId(user.getCompanyId());
        record.setIntTrainSubjectId(trainSubject.getVarId());
        companyTrainSubjectService.insertSelective(record);
        //trainSubjectCourse表添加数据
        if (courseIds.length != 0) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("trainSubjectId", trainSubject.getVarId());
            param.put("courseIds", courseIds);
            //批量添加数据
            subjectCourseService.saveTrainSubjectCourse(param);
        }
        response.setCode(Code.SUCCESS.getCode());
        return response;
    }

    /**
     * 修改企业自制培训主题
     *
     * @param request
     * @param
     * @return Response
     * @throws ServiceException
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(HttpServletRequest request,
                         @RequestParam(value = "subjectName", required = false, defaultValue = "") String subjectName,
                         @RequestParam(value = "editDesc", required = false, defaultValue = "") String editDesc,
                         @RequestParam(value = "id", required = false, defaultValue = "") String id,
                         @RequestParam(value = "source", required = false, defaultValue = "") String source
    ) throws ServiceException, UnsupportedEncodingException {

        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("fileName");
        User user = getCurrUser(request);
        Response<Object> response = new Response<Object>();
        if (user == null) {
            logger.error("TrainSubjectController.add-------------用户未登录");
            throw new ServiceException(Code.USER_NOT_LOGIN);
        }

        //验证非空参数
        if (StringUtils.isEmpty(id)) {
            throw new ServiceException(Code.BAD_REQUEST, "请选择要修改的主题");
        }
        if (source.equals("1")) {
            throw new ServiceException(Code.BAD_REQUEST, "系统自带的主题不可修改");
        }
        if (StringUtils.isEmpty(subjectName)) {
            throw new ServiceException(Code.BAD_REQUEST, "主题名称为空");
        }
        //封装参数
        Date date = new Date();
        TrainSubject trainSubject = new TrainSubject();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("subjectName", subjectName);
        params.put("subjectDesc", editDesc);
        params.put("operUser", user.getUserName());
        params.put("varId", id);
        params.put("chrSource", enterprise_number);
        if (null != multipartFile && !multipartFile.getOriginalFilename().equals("")) {
            trainSubject.setVarId(id);
            TrainSubject trainSubject1 = trainSubjectService.selectOne(trainSubject);
            CommonsMultipartFile cf = (CommonsMultipartFile) multipartFile;
            String file_name = cf.getOriginalFilename();
            String suffix = file_name.substring(file_name.lastIndexOf(".") + 1);
            try {
                UploadFileUtil.delete_file(trainSubject1.getLogo());
                String fileId = UploadFileUtil.upFile(file_name, multipartFile.getInputStream(), suffix);
                params.put("logo", fileId);
            } catch (Exception e) {
                response.setMessage("fail");
                logger.error("======FastDFS消息服务器出错" + e.getMessage());
            }
        }
        params.put("datOperDate", DateUtils.convertDate2StringTime(date));
        trainSubjectService.update(params);
        return response;
    }

    /**
     * 删除企业自制培训主题
     *
     * @param request
     * @param idStr
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Object delete(HttpServletRequest request,
                         @RequestParam(value = "idStr", required = true) String idStr) throws ServiceException {
        User user = getCurrUser(request);
        if (user == null) {
            logger.error("TrainSubjectController.delete-------------用户未登录");
            throw new ServiceException(Code.USER_NOT_LOGIN);
        }
        //验证非空参数
        if (StringUtils.isEmpty(idStr)) {
            throw new ServiceException(Code.BAD_REQUEST, "请选择要删除的主题");
        }
        String[] ids = idStr.replace(" ", "").split(",");
        if (ids.length == 0) {
            throw new ServiceException(Code.BAD_REQUEST, "请选择要删除的主题");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", ids);
        params.put("chrSource", enterprise_number);//只能删除企业自制
        for (String id : ids) {
            TrainSubject trainSubject = new TrainSubject();
            trainSubject.setVarId(id);
            TrainSubject trainSubject1 = trainSubjectService.selectOne(trainSubject);
            if (null != trainSubject1.getLogo()) {
                UploadFileUtil.delete_file(trainSubject1.getLogo());
            }
        }
        trainSubjectService.deleteBySourse(params);
        params.put("intTrainSubjectId", ids);
        params.put("intCompanyId", user.getCompanyId());
        companyTrainSubjectService.deleteByTrainSubjectId(params);
        return new Response();
    }

    /**
     * 验证企业自制培训主题名称是否重复
     *
     * @param request
     * @param
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "/verify")
    @ResponseBody
    public Object verify(HttpServletRequest request,
                         @RequestParam(value = "subjectName", required = false, defaultValue = "") String name
    ) throws Exception {
        User user = getCurrUser(request);
        if (user == null) {
            logger.error("TrainSubjectController.verify-------------用户未登录");
            throw new ServiceException(Code.USER_NOT_LOGIN);
        }
        Response<Object> resp = new Response<Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        List<String> list = companyTrainSubjectService.selectByCompanyId(user.getCompanyId());
        if(list.size()  == 0 ) {
            resp.setCode(Code.SUCCESS.getCode());
            return resp;
        }
        params.put("varId", list);
        params.put("chrSource", enterprise_number);
        List<TrainSubject> trainSubjectList = trainSubjectService.selectVerify(params);
        for (TrainSubject trainSubject : trainSubjectList) {
            if (trainSubject.getSubjectName().equals(name)) {
                resp.setCode(1);
                return resp;
            } else {
                resp.setCode(Code.SUCCESS.getCode());
            }
        }
        return resp;
    }


    /**
     * 热门主题列表
     *
     * @param request
     * @param company_id
     * @throws ServiceException
     */
    @RequestMapping("/hot_subject_list")
    @ResponseBody
    public Object hot_subject(HttpServletRequest request,
                              @RequestParam(value = "company_id", required = false, defaultValue = "") String company_id
    ) throws ServiceException {
        Response<Object> resp = new Response<Object>();
        Map<String, Object> params = new HashedMap();
        params.put("company_id", company_id);

        List<TrainSubject> subjectList = trainSubjectService.selectHotSubject(params);
        if (subjectList != null && subjectList.size() > 10) {  //截取前10条
            subjectList = subjectList.subList(0, 10);
        }
        resp.setResult(subjectList);
        return resp;
    }


    /**
     * 更多培训主题
     *
     * @param company_id
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/more_subject_list")
    @ResponseBody
    public Object subject_list(
            @RequestParam(value = "company_id", required = false, defaultValue = "") String company_id) throws
            ServiceException {
        Response<Object> resp = new Response<Object>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("companyId", company_id);

        List<TrainSubject> subjectList = trainSubjectService.selectByCompanyId(params);
        resp.setResult(subjectList);
        return resp;
    }


}
