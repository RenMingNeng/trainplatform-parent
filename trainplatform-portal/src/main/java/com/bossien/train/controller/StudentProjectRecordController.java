package com.bossien.train.controller;

import com.bossien.train.domain.ExamScore;
import com.bossien.train.domain.Page;
import com.bossien.train.domain.ProjectInfo;
import com.bossien.train.domain.User;
import com.bossien.train.domain.eum.ExamTypeEnum;
import com.bossien.train.domain.eum.ProjectTypeEnum;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IExamScoreService;
import com.bossien.train.service.IProjectInfoService;
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

/**
 * 学生查看项目记录Controller
 * Created by Administrator on 2017/7/27.
 */

@Controller
@RequestMapping("student/studentProjectRecord/")
public class StudentProjectRecordController extends BasicController {
    @Autowired
    private IExamScoreService examScoreService;

    @Autowired
    private IProjectInfoService projectInfoService;

    /**
     * 查看项目记录
     * @param projectId
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping("project_record")
    public String project_record_info(
        @RequestParam(value = "projectId", required = false,defaultValue = "")  String projectId , //项目id
        @RequestParam(value = "projectType", required = false,defaultValue = "")  String projectType , //项目类型
        HttpServletRequest request, ModelMap modelMap) {
        User user = getCurrUser(request);
        // 项目详情
        ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(projectId);
        modelMap.addAttribute("projectId",projectId);
        modelMap.addAttribute("projectName",projectInfo.getProjectName());
        modelMap.addAttribute("userId",user.getId());
        modelMap.addAttribute("projectType",projectType);
        modelMap.addAttribute("projectTypeName", ProjectTypeEnum.get(projectType).getName());
        modelMap.addAttribute("permissionTrain",hasPermissionStudy(projectType));
        modelMap.addAttribute("permissionExercise",hasPermissionQuestion(projectType));
        modelMap.addAttribute("permissionExam",hasPermissionExam(projectType));
        modelMap.addAttribute("examType", ProjectTypeEnum.QuestionType_3.getValue());
        //含培训不含练习
        modelMap.addAttribute("containTrain",ProjectTypeEnum.QuestionType_1.getValue() + ProjectTypeEnum.QuestionType_5.getValue());
        //含练习不含培训
        modelMap.addAttribute("containExercise",ProjectTypeEnum.QuestionType_2.getValue() + ProjectTypeEnum.QuestionType_6.getValue());
        //含培训和练习
        modelMap.addAttribute("containTrainAndExercise",ProjectTypeEnum.QuestionType_4.getValue() + ProjectTypeEnum.QuestionType_7.getValue());
        return "student/projectRecord/project_record";
    }

    /**
     * 查看考试成绩记录
     * @param projectId
     * @param modelMap
     * @return
     */
    @RequestMapping("exam_score")
    public String exam_score_info(HttpServletRequest request,
        @RequestParam(value = "projectId", required = false,defaultValue = "")  String projectId , //项目id
        @RequestParam(value = "projectTypeName", required = false,defaultValue = "")  String projectTypeName , //项目类型名称
        ModelMap modelMap) {
        User user =getCurrUser(request);
        modelMap.addAttribute("userId",user.getId());
        modelMap.addAttribute("projectId",projectId);
        modelMap.addAttribute("projectTypeName",projectTypeName);
        return "student/projectRecord/exam_score";
    }

    /**
     * 显示用户考试成绩记录
     * @param examProjectId
     * @param examType
     * @param isPassed
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/showExamScore")
    @ResponseBody
    public Object showExamScore (HttpServletRequest request,
        @RequestParam(value = "examProjectId", required = false,defaultValue = "")  String examProjectId,    //项目Id
        @RequestParam(value = "examType", required = false,defaultValue = "")  String examType,      //考试类型
        @RequestParam(value = "isPassed", required = false,defaultValue = "")  String isPassed,     //是否合格
        @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,     //每页条数
        @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum        //页码
        ) throws ServiceException{
        User user =getCurrUser(request);
        Response<Object> response=new Response <Object>();
        Page<ExamScore> page = new Page<ExamScore>();

        //每页条数过大
        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST,"pageSize 参数过大");
        }
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("projectId",examProjectId);
        params.put("userId",user.getId());
        params.put("examType",examType);
        params.put("isPassed",isPassed);
        params.put("startNum",(pageNum-1)*pageSize);
        params.put("endNum",pageSize);
        //考试成绩记录
        List<ExamScore> examScoreList = examScoreService.selectExamScoreList(params);
        //考试记录数量
        int count = examScoreService.selectExamScoreCount(params);
        page = new Page(count, pageNum, pageSize);
        response.setCode(Code.SUCCESS.getCode());
        page.setDataList(examScoreList);
        response.setResult(page);
        return response;
    }
}
