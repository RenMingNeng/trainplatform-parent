package com.bossien.train.controller;

import com.bossien.framework.mq.sender.QueueQuestionSender;
import com.bossien.train.domain.ExamPaperInfo;
import com.bossien.train.domain.QuestionCollection;
import com.bossien.train.domain.User;
import com.bossien.train.domain.eum.ExamTypeEnum;
import com.bossien.train.exception.Code;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.*;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huangzhaoyong on 2017/8/1.
 * 练习答题
 */
@Controller
@RequestMapping("/student/question")
public class StudentExamAnswersController extends BasicController {
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    public static final Logger logger = LoggerFactory.getLogger(StudentExamAnswersController.class);
    // 操作mongodb的集合名,等同于mysql中的表
    private final String collectionName_question_collection = "question_collection";
    private final String collectionName_exam_answers = "exam_answers";
    @Autowired
    private MongoOperations mongoTemplate;
    @Autowired
    private QueueQuestionSender queueQuestionSender;
    @Autowired
    private IExamAnswersService examAnswersService;
    @Autowired
    private ICourseQuestionService courseQuestionService;
    @Autowired
    private IExamPaperInfoService examPaperInfoService;
    @Autowired
    private IExamQuestionService examQuestionService;
    @Autowired
    private IQuestionService questionService;


    @RequestMapping("/exam")
    public String exam(HttpServletRequest request,
                       @RequestParam(value="exam_no", required = true, defaultValue = "") String exam_no){
        User user = this.getCurrUser(request);
        List<Map<String, Object>> result = new ArrayList<>();
        ExamPaperInfo paperInfo = new ExamPaperInfo();
        if (null != exam_no && !exam_no.equals("") && null != user){
            Map<String, Object> params = new HashedMap();
            params.put("examNo", exam_no);
            //考试详情
            paperInfo = examPaperInfoService.selectOne(params);
            if(null != paperInfo){
                //title，考试时间time
                request.setAttribute("time", paperInfo.getExamDuration());
                request.setAttribute("project_id", paperInfo.getProjectId());
                request.setAttribute("title", ExamTypeEnum.get(paperInfo.getExamType()).getName());
            }
        }
        request.setAttribute("questions", result);
        return "student/question/student_exam_ajax";
    }

    @ResponseBody
    @RequestMapping("/exam_ajax")
    public Object exam_ajax(HttpServletRequest request,
                       @RequestParam(value="exam_no", required = true, defaultValue = "") String exam_no){
        User user = this.getCurrUser(request);
        List<Map<String, Object>> result = new ArrayList<>();
        ExamPaperInfo paperInfo = new ExamPaperInfo();
        if (null != exam_no && !exam_no.equals("") && null != user){
            //查询出题编号集合
            Map<String, Object> params = new HashedMap();
            params.put("examNo", exam_no);
            List<String> questions = examQuestionService.selectList(params);
            //考试详情
            paperInfo = examPaperInfoService.selectOne(params);
            if(questions.size() > 0 && null != paperInfo){
                //mongo里面查询收藏数据
                Query query = new Query(new Criteria().andOperator(
                        Criteria.where("project_id").is(paperInfo.getProjectId()),
                        Criteria.where("user_id").is(user.getId())
                ));
                List<QuestionCollection> questionCollections =
                        mongoTemplate.find(query, QuestionCollection.class, collectionName_question_collection);
                //再组装题
                params = new HashedMap();
                params.put("intIds", questions);
                result = questionService.selectList(params, questionCollections);
            }
        }
        Response<Object> resp = new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        resp.setResult(result);
        return resp;
    }

    /**
     * 保存考试
     * @param request
     * @param detail
     * @return
     */
    @ResponseBody
    @RequestMapping("/save_exam")
    public Object save_exam(HttpServletRequest request, String exam_no,
                            @RequestParam(value="detail", required = false, defaultValue = "") String detail,
                            @RequestParam(value="examTime", required = false, defaultValue = "0") String examTime) throws Exception {
        User user = this.getCurrUser(request);
        if(null == user){
            return new Response<>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }

        //清楚空的字符串
        detail = StringUtil.trim(detail);

        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("examNo", exam_no);
        ExamPaperInfo examPaperInfo = examPaperInfoService.selectOne(params);
        if(null != examPaperInfo
                //2代表该考卷已经有成绩
                && !examPaperInfo.getExamStatus().equals("2")
                //比较试卷用户id是否跟提交用户一直
                && examPaperInfo.getUserId().equals(user.getId())){

            Map<String,Object> result = examAnswersService.insert(detail, exam_no, user.getUserName(), examPaperInfo);

            //更新考卷中考试状态
            examPaperInfoService.update(exam_no);
            //发送消息队列
            Map<String,Object> queueMap = new HashedMap();
            queueMap.put("detail", detail);
            queueMap.put("userId", user.getId());
            queueMap.put("examNo", exam_no);
            queueMap.put("userName", user.getUserName());
            queueMap.put("examTime", examTime);
            queueMap.put("companyId", user.getCompanyId());
            queueMap.put("examEndTime", DateUtils.formatDateTime(new Date()));
            EXECUTOR.execute(new StudentExamAnswersController.SaveExam(queueMap));

            Response<Object> resp = new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
            resp.setResult(result.get("result"));
            return resp;
        }
        return new Response<>(Code.PAPER_YET_SUBMIT_NUMBER.getCode(), Code.PAPER_YET_SUBMIT_NUMBER.getMessage());
    }

    private class SaveExam extends Thread {

        private Map<String,Object> queueMap;

        public SaveExam(Map<String,Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queueQuestionSender.sendMapMessage("tp.exam.queue", queueMap);
            }catch(Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }
}
