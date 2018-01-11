package com.bossien.train.controller;

import com.bossien.framework.mq.sender.QueueProjectPublishSender;
import com.bossien.framework.mq.sender.QueueTJSender;
import com.bossien.train.domain.*;
import com.bossien.train.domain.eum.ProblemTypeEnum;
import com.bossien.train.domain.eum.ProjectStatusEnum;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.*;
import com.bossien.train.util.FastDFSUtil;
import com.bossien.train.util.ParamsUtil;
import com.bossien.train.util.PropertiesUtils;
import com.bossien.train.util.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("/admin")
public class AdminController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private ICompanyProjectService companyProjectService;
    @Autowired
    private IProjectManagerService projectManagerService;
    @Autowired
    private QueueTJSender queueTJSender;
    @Autowired
    private QueueProjectPublishSender queueProjectPublishSender;
    @Autowired
    private ICompanyTjService companyTjService;
    @Autowired
    private ICompanyService companyService;
    @Autowired
    private ITrainSubjectService trainSubjectService;
    @Autowired
    private ICompanyTrainSubjectService companyTrainSubjectService;
    @Autowired
    private IExamStrategyService examStrategyService;
    @Autowired
    private IProjectCourseService projectCourseService;
    @Autowired
    private ICompanySuperviseService companySuperviseService;
    @Autowired
    private ICompanyCourseService companyCourseService;
    @Autowired
    private ICourseInfoService courseInfoService;

    @RequestMapping("/menu")
    public String menu() {
        return "admin/menu";
    }

    @RequestMapping("/message")
    public String index(HttpServletRequest request) {

        request.setAttribute("problemType", ProblemTypeEnum.values());

        return "admin/message/index";
    }

    @RequestMapping("/dispose_message")
    @ResponseBody
    public Object dispose_message(HttpServletRequest request,
                                  @RequestParam(value="id", required = true, defaultValue = "") String id,
                                  @RequestParam(value="pid", required = true, defaultValue = "") String pid,
                                  @RequestParam(value="isTrue", required = true, defaultValue = "") String isTrue) {

        if(StringUtil.isEmpty(id) || (StringUtil.isEmpty(pid) && !StringUtil.isEmpty(isTrue) )){
            return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        }

        User user = this.getCurrUser(request);
        if(null == user){
            return new Response<>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }

        String companyName_key = PropertiesUtils.getValue("company_name");
        String companyName_value = request.getSession().getAttribute(companyName_key).toString();

        companySuperviseService.addSupervise(id, pid, isTrue, user, companyName_value);

        return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

    @RequestMapping("/tongJi")
    @ResponseBody
    public Object tongJi(HttpServletRequest request)throws Exception{
        User user = getCurrUser(request);
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("companyId",user.getCompanyId());
        //根据companyId查询公司
        Company company = companyService.selectOne(params);

        params.put("companyName",company != null?company.getVarName():"无名");
        params.put("userName",user.getUserName());




        CompanyTj companyTj = companyTjService.selectOne(user.getCompanyId(),user.getUserName());

       /* if(null == companyTj){

            companyTj = companyTjService.initData();
            params.put("operType","add");
            //发送消息统计首页信息
            EXECUTOR.execute(new AnalysDeal_Tj(params));
        }else {

            params.put("operType", "update");
            //发送消息统计首页信息
            EXECUTOR.execute(new AnalysDeal_Tj(params));
        }*/
        return companyTj;
    }
    /**
     * 单位管理员首页项目列表
     * @param company_id
     * @param project_start_time
     * @param project_end_time
     * @param project_name
     * @param project_status
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/project_private/list")
    @ResponseBody
    public Object list(
               @RequestParam(value="company_id", required = false, defaultValue = "") String company_id,            //单位id
               @RequestParam(value="datBeginTime", required = false, defaultValue = "") String project_start_time,  //开始时间
               @RequestParam(value="datEndTime", required = false, defaultValue = "") String project_end_time,      //结束时间
               @RequestParam(value="project_name", required = false, defaultValue = "") String project_name,        //项目名称
               @RequestParam(value="project_status", required = false, defaultValue = "") String project_status,    //项目状态
               @RequestParam(value="project_mode", required = false, defaultValue = "") String project_mode,        //项目公开标识
               @RequestParam(value="pageSize", required = false, defaultValue = "20") Integer pageSize,
               @RequestParam(value="pageNum", required = false, defaultValue = "1") Integer pageNum
        ) throws ServiceException{
        Response<Object> resp = new Response<Object>();
        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();

        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        // 查询单位下项目
        params.put("company_id", company_id);
        List<CompanyProject> companyProjects = companyProjectService.selectList(params);
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        if(companyProjects.size() > 0) {
            List<String> projectIds = new LinkedList<String>();
            for(CompanyProject companyProject : companyProjects) {
                projectIds.add(companyProject.getProjectId());
            }
            params.put("projectIds", projectIds);
            params.put("project_name", ParamsUtil.joinLike(project_name));
            params.put("project_status", project_status);
            params.put("project_mode", project_mode);
            if("1".equals(project_mode) && "".equals(project_status)){
                params.put("unPublish", ProjectStatusEnum.ProjectStatus_1.getValue());
            }
            if(StringUtils.isNotEmpty(project_start_time)){
                params.put("project_start_time", project_start_time + " 00:00:00");
            }
            if(StringUtils.isNotEmpty(project_end_time)){
                params.put("project_end_time", project_end_time + " 23:59:59");
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
        //@RequestParam(value = "company_id", required = false, defaultValue = "") String company_id,
        @RequestParam(value="pageSize", required = true, defaultValue = "11") Integer pageSize,
        @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        User user = getCurrUser(request);
        Response<Object> resp = new Response<Object>();
        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }
        Page<TrainSubject> page = new Page<TrainSubject>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("company_id", user.getCompanyId());
        List<TrainSubject> list = new ArrayList<TrainSubject>();
        // 查询公司下的主题（包括系统自带和企业自制）
        List<String> subjectIds = companyTrainSubjectService.selectCompanySubjectId(user.getCompanyId());
        if(subjectIds.size()>0){
            params.put("varId",subjectIds);
            Integer count = trainSubjectService.selectCount(params);
            page = new Page(count, pageNum, pageSize);
            params.put("startNum", page.getStartNum());
            params.put("pageSize", page.getPageSize());
            params.put("orderBy", "orderBy");
            list = trainSubjectService.selectList(params);
            for (TrainSubject trainSubject : list) {
                if(!"".equals(trainSubject.getLogo())) {
                    String logo = FastDFSUtil.getHttpNginxURLWithToken(trainSubject.getLogo());
                    trainSubject.setLogo(logo);
                }
            }
        }
        page.setDataList(list);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);
        return resp;
    }

    /**
     * 修改项目的项目状态
     * @param request
     * @param id
     * @param type
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/project/proManagerPublish")
    @ResponseBody
    public Object proManagerPublish(HttpServletRequest request,
        @RequestParam(value="id", required = false, defaultValue = "") String id,
        @RequestParam(value="projectType", required = false, defaultValue = "") String projectType,
        @RequestParam(value="startTime", required = false, defaultValue = "") String startTime,
        @RequestParam(value="endTime", required = false, defaultValue = "") String endTime,
        @RequestParam(value="projectStatus", required = false, defaultValue = "") String projectStatus,
        @RequestParam(value="type", required = false) Integer type
    ) throws Exception{
        User user = getCurrUser(request);
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        if(hasPermissionExam(projectType) && 1 == type){
            //判断组卷策略合法性
            String result = examStrategyService.checkStrategy(id);
            if(!result.equals("")){
                resp.setResult(result);
                return resp;
            }
        }
        Map<String, Object> params = new HashedMap();
        params.put("projectId", id);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("type", type);
        params.put("project_status", projectStatus);
        params.put("operUser", user.getUserName());
        projectManagerService.updateProjectPublish(params);

        //消息推送（发布项目）
        if(type == 1){
        EXECUTOR.execute(new AnalysDeal_PP(params));
        }
        return resp;
    }

    public int getTotalSelectAllCount(Map<String, Object> params){
        List<ProjectCourse> projectCourseList = projectCourseService.selectByProjectIdAndRoleId(params);
        int count = 0;
        for(ProjectCourse projectCourse : projectCourseList){
            count += projectCourse.getSelectCount();
        }
        return count;
    }

        public  class AnalysDeal_Tj extends Thread {

            private Map<String,Object> queueMap;

            public AnalysDeal_Tj(Map<String,Object> queueMap) {
                this.queueMap = queueMap;
            }

            @Override
            public void run() {
                try {
                    queueTJSender.sendMapMessage("tp.tj.queue", queueMap);
                }catch(Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }


    /**
     * 发布项目
     */
    private class AnalysDeal_PP extends Thread {

        private Map<String, Object> queueMap;

        public AnalysDeal_PP(Map<String, Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queueProjectPublishSender.sendMapMessage("tp.projectPublish.queue", queueMap);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }
}
