package com.bossien.train.controller;

import com.bossien.framework.mq.sender.QueueQuestionSender;
import com.bossien.train.domain.ExamPaperInfo;
import com.bossien.train.domain.ExamStrategy;
import com.bossien.train.domain.ProjectInfo;
import com.bossien.train.domain.User;
import com.bossien.train.domain.eum.ExamTypeEnum;
import com.bossien.train.exception.Code;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.*;
import com.bossien.train.util.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huangzhaoyong on 2017/8/1.
 * 考试题库表
 */
@Controller
@RequestMapping("/student/question")
public class StudentExamQuestionController extends BasicController {
    public static final Logger logger = LoggerFactory.getLogger(StudentExamAnswersController.class);
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    @Autowired
    private QueueQuestionSender queueQuestionSender;
    @Autowired
    private IExamPaperInfoService examPaperInfoService;
    @Autowired
    private IExamQuestionService examQuestionService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private ISequenceService sequenceService;
    @Autowired
    private IExamStrategyService examStrategyService;

    /**
     * 创建考卷
     * @param request
     * @param project_id
     * @param exam_type
     * @return
     */
    @ResponseBody
    @RequestMapping("/create_paper")
    public Object exam(HttpServletRequest request,
                       @RequestParam(value="project_id", required = true, defaultValue = "") String project_id,
                       @RequestParam(value="exam_no", required = true, defaultValue = "") String exam_no,
                       @RequestParam(value="exam_type", required = true, defaultValue = "") String exam_type){
        Response<Object> rs = null;
        boolean isUpdateNo = false;
        if(StringUtil.isEmpty(exam_no) || exam_no.equals("null")){
            exam_no = sequenceService.generator();
            isUpdateNo = true;
        }

        User user = this.getCurrUser(request);
        Map<String, Object> params = new HashedMap();
        params.put("projectId", project_id);
        params.put("examType", exam_type);
        String result = examQuestionService.createPaper(params, user);
        if(result.equals("")){
            Map<String, Object> queueMap = new HashMap();
            queueMap.put("userName", user.getUserName());
            queueMap.put("userId", user.getId());
            queueMap.put("examNo", exam_no);
            queueMap.put("examType", exam_type);
            queueMap.put("projectId", project_id);
            EXECUTOR.execute(new StudentExamQuestionController.CreatePaper(queueMap));

            rs = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
            if(isUpdateNo){//如果是重新生成考试编号的，就把编号返回
                result = exam_no;
            }
        }else{
            if(result.indexOf("!") != -1){//检查未通过的，生成考卷失败
                rs = new Response<Object>(Code.PAPER_MAKE_FAIL.getCode(), Code.PAPER_MAKE_FAIL.getMessage());
            }else{
                rs = new Response<Object>(Code.PAPER_OLD_NUMBER.getCode(), Code.PAPER_OLD_NUMBER.getMessage());
            }
        }
        rs.setResult(result);
        return rs;
    }

    @ResponseBody
    @RequestMapping("/paper_no")
    public Object paper_no(HttpServletRequest request,
                       @RequestParam(value="project_id", required = true, defaultValue = "") String project_id){
        User user = this.getCurrUser(request);
        Map<String, Object> params = new HashedMap();
        params.put("projectId", project_id);
        params.put("examStatus", user.getId());
        params.put("examType", ExamTypeEnum.ExamType_2.getValue());
        //查询考试次数
        ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(project_id);
        if(null == projectInfo){
            return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        }
        params.put("examStatus", '2');//已经考试的数量
        params.put("userId", user.getId());
        int count = examPaperInfoService.selectCount(params);

        params.remove("examType");
        params.put("examStatus", "1");
        params.put("startNum", 0);
        params.put("endNum", 1);
        List<ExamPaperInfo> examPaperInfos = examPaperInfoService.selectList(params);
        Response<Object> rs = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        params.clear();
        for(ExamPaperInfo examPaperInfo: examPaperInfos){
            String examType = examPaperInfo.getExamType();
            if(examType.equals(ExamTypeEnum.ExamType_1.getValue()) && null == params.get("examNo_mn")){
                params.put("examNo_mn", examPaperInfo.getExamNo());
            }
            if(examType.equals(ExamTypeEnum.ExamType_2.getValue()) && null == params.get("examNo_zs")){
                params.put("examNo_zs", examPaperInfo.getExamNo());
            }
        }
        if(null == params.get("examNo_mn") || params.get("examNo_mn").toString().equals("null")){
            params.put("examNo_mn", sequenceService.generator());
        }
        if(null == params.get("examNo_zs") || params.get("examNo_zs").toString().equals("null")){
            params.put("examNo_zs", sequenceService.generator());
        }

        ExamStrategy examStrategy = examStrategyService.selectProjectIdAndUserId(project_id, user.getId());
        if(null != examStrategy){
            params.put("necessaryHour", examStrategy.getNecessaryHour());
        }

        params.put("las_exam_count", projectInfo.getIntRetestTime() - count);
        rs.setResult(params);
        return rs;
    }

    @ResponseBody
    @RequestMapping("/check_paper")
    public Object check_paper(HttpServletRequest request,
                           @RequestParam(value="examNo", required = true, defaultValue = "") String examNo){
        Response<Object> rs = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        Map<String, Object> params = new HashedMap();
        params.put("examNo", examNo);
        ExamPaperInfo examPaperInfo = examPaperInfoService.selectOne(params);
        if (null != examPaperInfo) {
            rs.setResult("true");
        }
        return rs;
    }


    private class CreatePaper extends Thread {

        private Map<String,Object> queueMap;

        public CreatePaper(Map<String,Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queueQuestionSender.sendMapMessage("tp.paper.queue", queueMap);
            }catch(Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }
}
