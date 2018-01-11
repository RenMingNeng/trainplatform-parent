package com.bossien.train.controller;

import com.bossien.framework.interceptor.SignInterceptor;
import com.bossien.framework.mq.sender.QueueRegisterCompanySender;
import com.bossien.train.domain.*;
import com.bossien.train.domain.eum.ExamTypeEnum;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.page.SpringDataPageable;
import com.bossien.train.service.*;
import com.bossien.train.util.*;
import com.google.common.collect.Lists;
import com.mongodb.gridfs.GridFSDBFile;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017-07-31.
 */
@Controller
@RequestMapping("/popup")
public class PopupController extends BasicController{
    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    private final String collectionName_exam_answers = "exam_answers";
    @Value("${search_name}")
    private String searchName;
    @Value("${max_number}")
    private String maxNumber;
    @Value("${department_id}")
    private String leaveDepartmentId;
    @Value("${mongo_db_study_self}")
    private String  mongo_db_study_self;
    @Autowired
    private QueueRegisterCompanySender queueRegisterCompanySender;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private MongoOperations mongoTemplate;
    @Autowired
    private ICourseQuestionService courseQuestionService;
    @Autowired
    private IQuestionService questionService;
    @Autowired
    private IExamQuestionService examQuestionService;
    @Autowired
    private IExamPaperInfoService examPaperInfoService;
    @Autowired
    private ICompanyService companyService;
    @Autowired
    private IFastDFSService fastDFSService;
    @Autowired
    private ICourseService courseService;
    @Autowired
    private IAttachmentService attachmentService;
    @Autowired
    private ICourseAttachmentService courseAttachmentService;
    @Autowired
    private IExamScoreService examScoreService;
    @Autowired
    private IVideoPositionService videoPositionService;
    @Autowired
    private  IUserManagerService userManagerService;
    @Autowired
    private IImportExcelUserService importExcelUserService;
    @Autowired
    private IExamStrategyService examStrategyService;

    /*选择培训课程弹窗(公开型项目)*/
    @RequestMapping("/selectTrainCourse")
    public ModelAndView selectTrainCourse(
            @RequestParam(value = "projectId", required = false,defaultValue = "")  String projectId,
            @RequestParam(value = "type", required = false,defaultValue = "")  String type
            ) {
        return new ModelAndView("super/dialog/select_course")
                .addObject("type",type)
                .addObject("projectId",projectId);
    }


    /*选择培训单位弹窗(公开型项目)*/
    @RequestMapping("/selectTrainCompany")
    public ModelAndView selectTrainCompany(
            @RequestParam(value = "projectId", required = false,defaultValue = "")  String projectId,
            @RequestParam(value = "type", required = false,defaultValue = "")  String type) {
        return new ModelAndView("super/dialog/select_company")
                .addObject("type",type)
                .addObject("projectId",projectId);
    }



    /**
     * 进入到选择课程页面（私有项目）
     * @return
     */
    @RequestMapping(value = "/enterSelectCourse")
    public String enterSelectCourse(){
        return  "admin/project_create/private/select_course";
    }

    /**
     * 进入到选择人员页面（私有项目）
     * @return
     */
    @RequestMapping(value = "/enterSelectUser")
    public String enterSelectUser(HttpServletRequest request){
        User user = getCurrUser(request);
        request.setAttribute("companyId",user.getCompanyId());
        return  "admin/project_create/private/select_user";
    }
    /**
     * 进入到选择人员页面（私有项目）
     * @return
     */
    @RequestMapping(value = "/selectDept")
    public String selectDept(){
        return  "admin/project_create/private/select_dept";
    }

    /**
     * 选择授权课程（私有项目）
     * @param request
     * @param course_name
     * @param course_type
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/private_course_list")
    @ResponseBody
    public Object private_course_list(HttpServletRequest request,
                              @RequestParam(value="course_name", required = false, defaultValue = "") String course_name,
                              @RequestParam(value="course_type", required = false, defaultValue = "") String course_type,
                              @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
                              @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response<Object> resp = new Response<Object>();

        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }

        /*Page<Map<String, Object>> page = new Page<Map<String, Object>>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("course_name", course_name);
        params.put("intTypeId", course_type);
        Integer count = courseService.selectCount(params);

        page = new Page(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("endNum", page.getPageSize());
        List<Map<String, Object>> listMap = courseService.selectList(params);
        page.setDataList(listMap);

        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);*/
        return resp;
    }

    /**
     * 课程题库预览
     * @param request
     * @param course_id
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/course_question_view")
    public String course_question_view(HttpServletRequest request,
                  @RequestParam(value="course_id", required = false, defaultValue = "") String course_id) throws ServiceException {
        request.setAttribute("title", "课程题库预览");
        return "student/question/question_view_ajax";
    }

    @ResponseBody
    @RequestMapping("/course_question_view_ajax")
    public Object course_question_view_ajax(HttpServletRequest request,
                                       @RequestParam(value="course_id", required = false, defaultValue = "") String course_id) throws ServiceException {
        List<Map<String, Object>> result = new ArrayList<>();
        if(null != course_id && !course_id.equals("")){
            //查询出题编号集合
            Map<String, Object> params = new HashedMap();
            params.put("intCourseId", course_id);
            List<String> questions = courseQuestionService.selectQuestionIdList(params);
            if(null != questions && questions.size() > 0){
                //再组装题
                params = new HashedMap();
                params.put("intIds", questions);
                result = questionService.selectList(params);
            }
        }
        Response<Object> resp = new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        resp.setResult(result);
        return resp;
    }
    /**
     * 课程预览
     * @return
     */
    @RequestMapping("course/preview")
    public String index(
            ModelMap modelMap,
            @RequestParam(value="courseId", required = true, defaultValue = "") String courseId
        ) {

        modelMap.put("courseId", courseId);

        return "admin/course/preview";
    }

    /**
     * 答题记录
     * @param request
     * @param exam_no
     * @return
     */
    @RequestMapping("/question/exam_score")
    public String exam_score(HttpServletRequest request,
                             @RequestParam(value="exam_no", required = true, defaultValue = "") String exam_no){
        ExamScore examScore = examScoreService.selectByExamNo(exam_no);
        //判断是否有答题成绩返回
        if(null == examScore){
            request.setAttribute("answer_status", "false");
        }

        //考试详情
        Map<String, Object> params = new HashedMap();
        params.put("examNo", exam_no);
        ExamPaperInfo paperInfo = paperInfo = examPaperInfoService.selectOne(params);
        if(null != paperInfo){
            request.setAttribute("title", ExamTypeEnum.get(paperInfo.getExamType()).getName());
            request.setAttribute("time", paperInfo.getExamDuration());
        }
        return "student/question/exam_score_ajax";
    }

    @ResponseBody
    @RequestMapping("/question/exam_score_ajax")
    public Object exam_score_ajax(HttpServletRequest request,
                             @RequestParam(value="exam_no", required = true, defaultValue = "") String exam_no){
        User user = this.getCurrUser(request);
        List<Map<String, Object>> result = new ArrayList<>();
        if (null != exam_no && !exam_no.equals("") && null != user){
            //查询出题编号集合
            Map<String, Object> params = new HashedMap();
            params.put("examNo", exam_no);
            List<String> questions = examQuestionService.selectList(params);
            if(questions.size() > 0){

                //mongo里面查询考试记录数据
                Query query = new Query(Criteria.where("exam_no").is(exam_no));
                List<ExamAnswers> examAnswersList = mongoTemplate.find(query, ExamAnswers.class,
                        collectionName_exam_answers);

                //再组装题
                params = new HashedMap();
                params.put("intIds", questions);
                result = questionService.selectList(params, new ArrayList<QuestionCollection>(), examAnswersList);
            }
        }
        Response<Object> resp = new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        resp.setResult(result);
        return resp;
    }

    /**
     * 单位树
     * @param request
     * @return
     */
    @RequestMapping("/select_company")
    public String select_company(HttpServletRequest request){

        return "/popup/selectCompany";
    }

    /**
     * 单位树
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/select_company/tree")
    public Object select_company_tree(HttpServletRequest request){
        User user = this.getCurrUser(request);
        if(null == user){
            return new Response<>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }

        Response<Object> res = new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());

        String tree = companyService.getCompanyTree(user.getCompanyId());
        res.setResult(tree);
        return res;
    }

    @RequestMapping("/select_company/search")
    @ResponseBody
    public Object search(
            @RequestParam(value="varName", required = false, defaultValue = "") String varName,
            @RequestParam(value="companyId", required = false, defaultValue = "") String companyId,
            @RequestParam(value="intRegionId", required = false, defaultValue = "") String intRegionId,
            @RequestParam(value="varBusinessId", required = false, defaultValue = "") String varBusinessId,
            @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
            @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum){
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("varName", ParamsUtil.joinLike(varName));
        //根据选择的公司，查询出所有子公司
        if(!StringUtil.isEmpty(companyId)){
            String companyIds = companyService.getChildCompanyIds(companyId);
            List<String> companyIdList = Arrays.asList(companyIds.split(","));
            params.put("companyIds", companyIdList);
        }
        if(!StringUtil.isEmpty(intRegionId)){
            params.put("intRegionIds", Arrays.asList(intRegionId.split(",")));
        }
        if(!StringUtil.isEmpty(varBusinessId)){
            params.put("varBusinessIds", Arrays.asList(varBusinessId.split(",")));
        }

        int count = companyService.selectCount(params);
        Page page = new Page(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("endNum", page.getPageSize());
        List<Map<String, Object>> listMap = companyService.selectList(params);
        page.setDataList(listMap);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);
        return resp;
    }

    @RequestMapping("/getCourse")
    @ResponseBody
    public Object getCourse(HttpServletRequest request,
        @RequestParam(value="courseId", required = true, defaultValue = "") String courseId) throws ServiceException {
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());

        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("intId", courseId);
        Course course = courseService.selectOne(params);

        resp.setResult(course);

        return resp;
    }

    @RequestMapping("/getAttachment")
    @ResponseBody
    public Object getAttachment(HttpServletRequest request,
        @RequestParam(value="courseId", required = true, defaultValue = "") String courseId) throws ServiceException {

        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());

        List<Attachment> attachments = new ArrayList<Attachment>();

        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("intCourseId", courseId);
        List<CourseAttachment> courseAttachments = courseAttachmentService.selectList(params);

        if(null != courseAttachments && !courseAttachments.isEmpty()) {
            List<String> attachmentIds = new ArrayList<String>();
            for(CourseAttachment courseAttachment : courseAttachments) {
                attachmentIds.add(courseAttachment.getIntAttachmentId());
            }
            params.clear();params.put("ids", attachmentIds);
            attachments = attachmentService.selectByIds(params);
        }

        resp.setResult(attachments);

        return resp;
    }

    @RequestMapping("/m3u8")
    @ResponseBody
    public Object m3u8(@RequestParam("fId") String fId,
                       @RequestParam(value = "course_id", required = false, defaultValue = "") String course_id,             //课程id
                       @RequestParam(value = "video_id", required = false, defaultValue = "") String video_id,        //视频或文档id
                        @RequestParam(value = "user_id", required = false, defaultValue = "") String user_id
                       ) throws Exception {
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());

        String urlToken = fastDFSService.getURLToken(fId);

        Map<String, Object> params = new HashMap<String, Object>();
        Integer lastPosition = 0;
        if(StringUtils.isNotEmpty(user_id)){
            params.put("userId",user_id);
            params.put("courseId",course_id);
            params.put("videoId",video_id);
            lastPosition = videoPositionService.selectOne(params);
            if( lastPosition == null ){
                lastPosition = 0;
            }
        }
        params.clear();
        params.put("url",urlToken);
        params.put("lastPosition",lastPosition);
        resp.setResult(params);

        return resp;
    }

    /**
     * 选择报名人员弹窗
     * @param projectId
     * @param projectType
     * @return
     */
    @RequestMapping("/userSignUp")
    public ModelAndView userSignUp(
            @RequestParam(value = "companyId", required = false,defaultValue = "")  String companyId,
            @RequestParam(value = "projectId", required = false,defaultValue = "")  String projectId,
            @RequestParam(value = "projectType", required = false,defaultValue = "")  String projectType) {
        return new ModelAndView("admin/userSignUp/select_user")
                .addObject("companyId",companyId)
                .addObject("projectType",projectType)
                .addObject("projectId",projectId);
    }

    /**
     * 人员管理弹窗
     * @param projectId
     * @return
     */
    @RequestMapping("/userManager")
    public ModelAndView userSignUp(
            @RequestParam(value = "projectId", required = false,defaultValue = "")  String projectId) {
        return new ModelAndView("admin/userSignUp/user_manager")
                .addObject("projectId",projectId);
    }



    @RequestMapping("/menu")
    public String menu() {
        return "popup/menu";
    }


    /**
     * 单位变更
     * @return
     */
    @RequestMapping("/companyChange")
    public ModelAndView companyChange(HttpServletRequest request){
        User user = getCurrUser(request);
        Map<String,Object> param = new HashMap<>();
        param.put("companyId",user.getCompanyId());
        //根据companyId查询公司
        Company company = companyService.selectOne(param);

        String app_code = PropertiesUtils.getValue("app_code");
        String register_url = PropertiesUtils.getValue("register_url");
        return new ModelAndView("popup/company_change")
                 .addObject("userId",user.getId())
                 .addObject("companyId",user.getCompanyId())
                 .addObject("companyName",company!=null?company.getVarName():"")
                 .addObject("app_code",app_code)
                 .addObject("register_url",register_url)
                 .addObject("openId",user!=null?user.getOpenId():"");


    }

    /**
     * 注册新单位
     * @return
     */
    @RequestMapping("/companyRegister")
    public ModelAndView companyRegister(HttpServletRequest request){

        return new ModelAndView("popup/company_register");

    }

    /**
     * 发送注册新单位申请
     * @return
     */
    @RequestMapping("/register")
    @ResponseBody
    public Object register(HttpServletRequest request,
               @RequestParam(value="companyName", required = true, defaultValue = "") String companyName,  //企业名称
               @RequestParam(value="regionName", required = true, defaultValue = "") String regionName,    //行政区划
               @RequestParam(value="industryType", required = true, defaultValue = "") String industryType, //行业类别
               @RequestParam(value="socialCreditCode", required = false) String socialCreditCode  //社会信用代码
                                 ){
        User user = getCurrUser(request);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("companyName", companyName);
        param.put("regionName", regionName);
        param.put("industryType", industryType);
        param.put("socialCreditCode", socialCreditCode);
        param.put("userId", user.getId());
        param.put("userName", user.getUserName());
        param.put("userAccount", user.getUserAccount());
        param.put("mobileNo", user.getMobileNo());
        param.put("openId", user.getOpenId());
        //消息推送（统计）
        EXECUTOR.execute(new AnalysDeal_RegisterCompany(param));
        return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());

    }

    /***
     * 根据集团公司id集合查询人员
     * @param companyIds
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/userList")
    @ResponseBody
    public Object userList(
            @RequestParam(value = "companyIds",required = true,defaultValue = "") String companyIds,   //公司id集合
            @RequestParam(value = "userName",required = false,defaultValue = "") String userName,
            @RequestParam(value = "userAccount",required = false,defaultValue = "") String userAccount,
            @RequestParam(value = "departmentId",required = false,defaultValue = "") String departmentId,
            @RequestParam(value = "searchType",required = false ,defaultValue = "1")String searchType,
            @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
            @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum
         )throws ServiceException{

      Response<Object> resp = new Response<Object>();
      Page<Map<String,Object>> page = new Page<Map<String,Object>>();
      Map<String, Object> params = new HashMap<String, Object>();
      if (StringUtils.isNotEmpty(companyIds)) {
          params.put("companyIds", Arrays.asList(companyIds.split(",")));
      }
      if(pageSize > 1000) {
          throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
      }
      if(StringUtils.isNotEmpty(departmentId) && leaveDepartmentId.equals(departmentId)){
          params.put("deptId", departmentId);
          params.put("isValid", User.IsValid.TYPE_2.getValue());
      }else{
          params.put("isValid", User.IsValid.TYPE_1.getValue());
      }
      if((StringUtils.isNotEmpty(userName)||StringUtils.isNotEmpty(userAccount))&&StringUtils.isNotEmpty(searchType)) {
          //按姓名搜索
          if (searchName.equals(searchType)) {
              params.put("userName", ParamsUtil.joinLike(userName));
          } else {//按账号搜索
              params.put("userAccount", ParamsUtil.joinLike(userAccount));
          }
      }
      Integer count = userManagerService.selectUserListCount(params);

      page = new Page<Map<String,Object>>(count, pageNum, pageSize);
      params.put("startNum", page.getStartNum());
      params.put("endNum", page.getPageSize());
      List<Map<String,Object>> ulist = userManagerService.selectUserList(params);

      page.setDataList(ulist);
      resp.setResult(page);
      return resp;
  }
    /**
     * 根据部门ids查询用户集合
     * @param request
     * @param userName
     * @param searchType
     * @param deptIds
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/deptUserList")
    @ResponseBody
    public Object deptUserList(HttpServletRequest request,
                       @RequestParam(value="userName", required = false, defaultValue = "") String userName,
                       @RequestParam(value="userAccount", required = false, defaultValue = "") String userAccount,
                       @RequestParam(value="searchType", required = false, defaultValue = "1") String searchType,
                       @RequestParam(value="deptIds", required = false, defaultValue = "") String deptIds,
                       @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
                       @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response<Object> resp = new Response<Object>();
        Page<Map<String,Object>> page = new Page<Map<String,Object>>();
        Map<String, Object> params = new HashMap<String, Object>();
        if(pageSize > Integer.parseInt(maxNumber)) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }
        if((StringUtils.isNotEmpty(userName)||StringUtils.isNotEmpty(userAccount))&&StringUtils.isNotEmpty(searchType)){
               //按姓名搜索
            if(searchName.equals(searchType)){
                params.put("userName",ParamsUtil.joinLike(userName));
            }else{
                //按用户账号查询
                params.put("userAccount",ParamsUtil.joinLike(userAccount));
            }
        }
        if(StringUtils.isNotEmpty(deptIds)){
            List<String> deptList = Arrays.asList(deptIds.split(","));
            //搜索离职人员
            if(deptList.size() == 1 && leaveDepartmentId.equals(deptList.get(0))){
                params.put("company_id",getCurrUser(request).getCompanyId());
                params.put("isValid", User.IsValid.TYPE_2.getValue());
            }else{
                //搜索非离职人员
                params.put("isValid", User.IsValid.TYPE_1.getValue());
                params.put("departmentIdList",deptList);
            }
        }
        Integer count = userManagerService.selectUserListCount(params);
        page = new Page<Map<String,Object>>(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("endNum", page.getPageSize());
        List<Map<String,Object>> ulist = userManagerService.selectUserList(params);
        page.setDataList(ulist);
        resp.setResult(page);
        return resp;
    }

    @ResponseBody
    @RequestMapping("/ueditor/imgUpload")
    public Object fileUpload(HttpServletRequest request,
                             @RequestParam(value="action", required = false, defaultValue = "") String action){

        Map<String,Object> rs = new HashMap<String, Object>();
        MultipartHttpServletRequest mReq  =  null;
        MultipartFile multipartFile = null;
        // 原始文件名   UEDITOR创建页面元素时的alt和title属性
        String originalFileName = "";
        String attachment = "";
        try {
            mReq = (MultipartHttpServletRequest)request;
            // 从config.json中取得上传文件的ID
            multipartFile = mReq.getFile("upfile");
            // 取得文件的原始文件名称
            originalFileName = multipartFile.getOriginalFilename();

//            attachment = gridFsTemplate.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename(), multipartFile.getContentType()).getId().toString();
            String suffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1, originalFileName.length());
            attachment = UploadFileUtil.upFile(originalFileName, multipartFile.getInputStream(), suffix);

            /*你的处理图片的代码*/
            rs.put("state", "SUCCESS");// UEDITOR的规则:不为SUCCESS则显示state的内容
            rs.put("url", FastDFSUtil.getHttpNginxURLWithToken(attachment));         //能访问到你现在图片的路径
            rs.put("title", originalFileName);
            rs.put("original", originalFileName);

        } catch (Exception e) {
            rs.put("state", "文件上传失败!"); //在此处写上错误提示信息，这样当错误的时候就会显示此信息
            rs.put("url","");
            rs.put("title", "");
            rs.put("original", "");
        }
        return rs;
    }

    @RequestMapping(value = "attachment/{id}", method = RequestMethod.GET)
    public void getFile(@PathVariable String id, HttpServletResponse response) throws IOException {
        Query query = new Query();
        Criteria criteria = GridFsCriteria.where("_id").is(id);
        query.addCriteria(criteria);
        GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(query);
        gridFSDBFile.writeTo(response.getOutputStream());
    }

    /**
     * 文件上传
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/fastUpload")
    public void uploadByName(HttpServletRequest request,HttpServletResponse response) throws Exception{
        Response<Object> resp = new Response<Object>();
        String fids = "";
        if(request instanceof MultipartHttpServletRequest){
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            Iterator<String> it = multipartHttpServletRequest.getFileNames();
            MultipartFile mfile;
            while(it.hasNext()) {
                String fileName = it.next();
                mfile = multipartHttpServletRequest.getFile(fileName);
                String varFileName = mfile.getOriginalFilename();
                String suffix = varFileName.substring(varFileName.lastIndexOf(".") + 1, varFileName.length());
                String fid = UploadFileUtil.upFile(varFileName, mfile.getInputStream(), suffix);

                fids += fids.equals("")? fid + "," + varFileName: ";" + fid + "," + varFileName;
            }
        }
        resp.setResult(fids);
        writeMessageUft8(response, resp);writeMessageUft8(response, resp);
    }

    /**
     * 文件上传
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/fastDelete")
    public void fastDelete(HttpServletRequest request,HttpServletResponse response,
                           @RequestParam(value="fileName", required = false, defaultValue = "")String fileName) throws Exception{
        if(StringUtils.isEmpty(fileName)){
            writeMessageUft8(response, new Response<>(Code.BAD_REQUEST.getCode(), Code.BAD_REQUEST.getMessage()));
            return;
        }
        if(fileName.split(",").length < 1){
            writeMessageUft8(response, new Response<>(Code.BAD_REQUEST.getCode(), Code.BAD_REQUEST.getMessage()));
            return;
        }

        UploadFileUtil.delete_file(fileName.split(",")[0]);
        writeMessageUft8(response, new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage()));
    }

    /**
     * 培训练习、培训练习考试的信息统计页面
     * @return
     */
    @RequestMapping(value = "/statisticsInfo_1")
    public ModelAndView statisticsInfo_1() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("student/statisticsInfo_1");
        return mv;
    }

    /**
     * 练习、练习考试的信息统计页面
     * @return
     */
    @RequestMapping(value = "/statisticsInfo_2")
    public ModelAndView statisticsInfo_2() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("student/statisticsInfo_2");
        return mv;
    }

    /**
     * 培训、培训考试的信息统计页面
     * @return
     */
    @RequestMapping(value = "/statisticsInfo_3")
    public ModelAndView statisticsInfo_3() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("student/statisticsInfo_3");
        return mv;
    }

    /**
     * 个人信息修改
     *
     * @param request
     * @param user
     * @param roleIds
     * @return
     */
    @RequestMapping("/update_stuInfo")
    @ResponseBody
    public Response<Object> update(HttpServletRequest request, User user, String roleIds) {
        Response<Object> resp = new Response<Object>();
        User usr = getCurrUser(request);
        UserWork uWork = null;
        UserTrainRole usertrole = null;
        // 更新用户信息
        if (user != null) {
            uWork = importExcelUserService.bulidUserWork(usr);
            usertrole = importExcelUserService.bulidUserTrainRole(usr);
            userManagerService.updateUser(user, roleIds, uWork, usertrole);
            resp.setResult(Boolean.TRUE);
            resp.setMessage("修改成功");
        } else {
            // result
            resp.setMessage("修改失败");
            resp.setResult(Boolean.FALSE);

        }
        return resp;
    }

    /**
     * 组卷策略信息
     *
     * @param projectId
     * @return
     */
    @RequestMapping("/strategy/info")
    @ResponseBody
    public Response<Object> strategy(
            @RequestParam(value = "projectId", required = true, defaultValue = "") String projectId) {

        if(StringUtil.isEmpty(projectId)){
            return new Response<>(Code.BAD_REQUEST.getCode(), Code.BAD_REQUEST.getMessage());
        }

        Map<String, Object> params = new HashedMap();
        params.put("projectId", projectId);
        List<ExamStrategy> examStrategies = examStrategyService.selectList(params);
        if(null != examStrategies && examStrategies.size() > 0){
            Response<Object> resp = new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
            resp.setResult(examStrategies.get(0));
            return resp;
        }
        return new Response<>(Code.NO_FOUND.getCode(), Code.NO_FOUND.getMessage());
    }


    /**
     * 异步发送注册新单位的消息
     */
    public  class AnalysDeal_RegisterCompany extends Thread {

        private Map<String,Object> queueMap;

        public AnalysDeal_RegisterCompany(Map<String,Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queueRegisterCompanySender.sendMapMessage("tp.registerCompany.queue", queueMap);
            }catch(Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    @RequestMapping(value = "/studentModify")
    public ModelAndView studentModify(HttpServletRequest request, User user) {
        ModelAndView mv = new ModelAndView();
        Map<String, Object> map = new HashMap<String, Object>();
        user = userManagerService.queryUserInfo(user);
        UserWork uwork = new UserWork();
        uwork.setUserId(user.getId());
        List<UserWork> uwkList = userManagerService.queryUserWorkList(uwork);
        mv.addObject("userInfo", user);
        mv.addObject("workList", uwkList);
        mv.setViewName("student/person_info");
        return mv;
    }

    /**
     * 用户自学学习课程列表
     * @param user_id
     * @param course_name
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/studySelfList")
    @ResponseBody
    public Object studySelfList(
            @RequestParam(value="userId", required = true, defaultValue = "") String user_id,
            @RequestParam(value="course_name", required = true, defaultValue = "") String course_name,
            @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
            @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response<Object> resp = new Response<Object>();
        Page<StudySelf> page = new Page<>();
        // 查询分页条件
        SpringDataPageable pageable = new SpringDataPageable();
        // 查询条件
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("user_id").is(user_id)
        ));
        if(StringUtils.isNotEmpty(course_name)){
            query.addCriteria(Criteria.where("course_name").regex("^.*?" + course_name + ".*$"));
        }
        // 默认按时间降序
        List<Sort.Order> orders = Lists.newArrayList();
        orders.add(new Sort.Order(Sort.Direction.DESC, "create_time"));
        Sort sort = new Sort(orders);
        // 页码
        pageable.setPageNumber(pageNum);
        // 每页条数
        pageable.setPageSize(pageSize);
        // 排序
        pageable.setSort(sort);
        //总条数
        Long count = mongoTemplate.count(query, StudySelf.class,mongo_db_study_self);
        page = new Page(count.intValue(), pageNum, pageSize);
        //学员自学统计信息集合
        List<StudySelf> list = mongoTemplate.find(query.with(pageable),StudySelf.class,mongo_db_study_self);
        page.setDataList(list);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);
        return resp;
    }
}




