package com.bossien.train.controller;

import com.bossien.train.domain.ExamStrategy;
import com.bossien.train.domain.ProjectCourse;
import com.bossien.train.domain.User;
import com.bossien.train.exception.Code;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IExamStrategyService;
import com.bossien.train.service.IProjectCourseService;
import com.bossien.train.util.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by huangzhaoyong on 2017/7/28.
 */
@Controller
@RequestMapping("/popup/project_create")
public class ExamStrategyController extends BasicController {
    public static final Logger logger = LoggerFactory.getLogger(StudentExamAnswersController.class);

    @Autowired
    private IExamStrategyService examStrategyService;
    @Autowired
    private IProjectCourseService projectCourseService;


    @RequestMapping("/step6")
    public ModelAndView private_step6(HttpServletRequest request,
                        @RequestParam(value = "projectTypeNo", required = true, defaultValue = "-1") String projectTypeNo,
                        @RequestParam(value = "projectId", required = true, defaultValue = "-1") String projectId,
                        @RequestParam(value = "source", required = false, defaultValue = "") String source,           //项目编号
                        @RequestParam(value = "projectStatus", required = false, defaultValue = "") String projectStatus,           //项目状态
                         @RequestParam(value = "examPermission", required = false, defaultValue = "") String examPermission          //考试状态
                              ){
        Map<String, Object> params = new HashedMap();
        params.put("projectId", projectId);
        List<ExamStrategy> examStrategies = examStrategyService.selectList(params);

        return  new ModelAndView("/admin/project_create/private/step_6")
                .addObject("projectTypeNo",projectTypeNo)
                .addObject("projectId",projectId)
                .addObject("source",source)
                .addObject("count",examStrategies.size())
                .addObject("projectStatus",projectStatus)
                .addObject("examPermission",examPermission)
                .addObject("permissionExercise",hasPermissionQuestion(projectTypeNo))    //是否含有练习
                .addObject("permissionTrain",hasPermissionStudy(projectTypeNo))          //是否含有培训
                .addObject("permissionExam",hasPermissionExam(projectTypeNo));
    }

    /**
     * 根据角色id查询组卷策略
     *
     * @param request
     * @param projectTypeNo
     * @param projectId
     * @return
     */
    @ResponseBody
    @RequestMapping("/step6/info")
    public Object index(HttpServletRequest request,
                        @RequestParam(value = "projectTypeNo", required = true, defaultValue = "-1") String projectTypeNo,
                        @RequestParam(value = "projectId", required = true, defaultValue = "") String projectId) {
        Response<Object> resp = new Response<>();
        resp.setCode(Code.SUCCESS.getCode());

        Map<String, Object> params = new HashedMap();
        params.put("projectId", projectId);
        List<ExamStrategy> examStrategies = examStrategyService.selectList(params);

        List<List<Map<String,Object>>> result = new LinkedList<List<Map<String,Object>>>();
        for(ExamStrategy strategy : examStrategies){
            //必选题量
            params.put("roleId", strategy.getRoleId());
            strategy.setSelectAllCount(getTotalSelectAllCount(params));

            result.add(examStrategyService.assembleExamStrategy(strategy));
        }

        params.clear();
        params.put("examStrategies", examStrategies);
        params.put("list", result);

        resp.setResult(params);
        return resp;
    }

    /**
     * 选择项目中所有角色的组卷策略类型的最小值
     * @param request
     * @param project_id
     * @return
     */
    @ResponseBody
    @RequestMapping("/step6/minInfo")
    public Object minInfo(HttpServletRequest request,
                        @RequestParam(value = "project_id", required = true, defaultValue = "-1") String project_id) {
        Response<Object> resp = new Response<>();
        resp.setCode(Code.SUCCESS.getCode());
        Map<String, Object> params = new HashedMap();
        params.put("projectId", project_id);

        resp.setResult(examStrategyService.selectMinStrategyByProject(project_id));
        return resp;
    }

    @ResponseBody
    @RequestMapping("/step6/save")
    public Object save(HttpServletRequest request, ExamStrategy examStrategy) {
        if(null == examStrategy.getProjectId()){
            return new Response<>(Code.SUCCESS.getCode(), "参数异常");
        }
        User user = this.getCurrUser(request);
        examStrategyService.update(examStrategy, user);

        return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

    @ResponseBody
    @RequestMapping("/getSelectAllCount")
    public Object getSelectAllCount(HttpServletRequest request,
                @RequestParam(value = "projectId", required = true, defaultValue = "") String projectId) {
        if(StringUtil.isEmpty(projectId)){
            return new Response<>(Code.SUCCESS.getCode(), "参数异常");
        }
        Map<String, Object> params = new HashedMap();
        params.put("projectId", projectId);
        List<ExamStrategy> examStrategies = examStrategyService.selectList(params);
        Response response = new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        response.setResult(getMinQuestionCount(examStrategies, getTotalSelectAllCount(params)));
        return response;
    }

    /**
     * 组卷策略约束：获取所有角色类型的最小值
     * @param examStrategies
     * @param totalSelectCount 已经填写的必选题量数值
     * @return
     */
    public int getMinQuestionCount(List<ExamStrategy> examStrategies, int totalSelectCount){
        if(null == examStrategies || examStrategies.size() < 1){
            return 0;
        }
        int selectAllCount = 100000;
        for(ExamStrategy strategy : examStrategies){
            int rs = getSelectAllCount(strategy);
            if(selectAllCount > rs){
                selectAllCount = rs;
            }
        }
        return selectAllCount - totalSelectCount;
    }

    /**
     * 获取一个角色类型的最小值
     * @param examStrategie
     * @return
     */
    public int getSelectAllCount(ExamStrategy examStrategie){
        if(null == examStrategie){
            return 0;
        }
        int selectAllCount = examStrategie.getSingleAllCount();
        if(selectAllCount > examStrategie.getManyAllCount()){
            selectAllCount = examStrategie.getManyAllCount();
        }
        if(selectAllCount > examStrategie.getJudgeAllCount()){
            selectAllCount = examStrategie.getJudgeAllCount();
        }
        return selectAllCount;
    }

    public int getTotalSelectAllCount(Map<String, Object> params){
        List<ProjectCourse> projectCourseList = projectCourseService.selectByProjectIdAndRoleId(params);
        int count = 0;
        for(ProjectCourse projectCourse : projectCourseList){
            count += projectCourse.getSelectCount();
        }
        return count;
    }
}
