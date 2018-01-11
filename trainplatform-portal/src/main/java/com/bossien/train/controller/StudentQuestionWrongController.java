package com.bossien.train.controller;

import com.bossien.framework.mq.sender.QueueQuestionSender;
import com.bossien.train.domain.QuestionCollection;
import com.bossien.train.domain.QuestionWrongAnswers;
import com.bossien.train.domain.User;
import com.bossien.train.exception.Code;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IQuestionService;
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
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huangzhaoyong on 2017/8/1.
 * 错题
 */
@Controller
@RequestMapping(value="/student/question")
public class StudentQuestionWrongController extends BasicController {
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    public static final Logger logger = LoggerFactory.getLogger(StudentExamAnswersController.class);
    // 操作mongodb的集合名,等同于mysql中的表
    private final String collectionName_question_collection = "question_collection";
    private final String collectionName_question_wrong_answers = "question_wrong_answers";
    @Autowired
    private IQuestionService questionService;
    @Autowired
    private QueueQuestionSender queueQuestionSender;
    @Autowired
    private MongoOperations mongoTemplate;

    /**
     * 我的错题
     * @param request
     * @param project_id
     * @return
     */
    @RequestMapping(value = "/wrongs")
    public String index(HttpServletRequest request,
                        @RequestParam(value="project_id", required = true, defaultValue = "") String project_id){
        List<Map<String, Object>> result = new ArrayList<>();
        request.setAttribute("url", "/student/question/wrongs_ajax?project_id=" + project_id);
        request.setAttribute("title", "我的错题");
        return "student/question/student_exercise_ajax";
    }

    @ResponseBody
    @RequestMapping(value = "/wrongs_ajax")
    public Object wrongs_ajax(HttpServletRequest request,
                        @RequestParam(value="project_id", required = true, defaultValue = "") String project_id){
        List<Map<String, Object>> result = new ArrayList<>();
        User user = this.getCurrUser(request);
        if (null != project_id && !project_id.equals("") && null != user){
            //mongo里面查询我的错题数据
            Query query = new Query(new Criteria().andOperator(
                    Criteria.where("project_id").is(project_id),
                    Criteria.where("user_id").is(user.getId())
            ));
            List<QuestionWrongAnswers> questionWrongAnswerss = mongoTemplate.find(query, QuestionWrongAnswers.class,
                    collectionName_question_wrong_answers);

            if(questionWrongAnswerss.size() > 0){
                //获取所有的题目编号
                Set<String> questionSet = new HashSet<String>();
                for(QuestionWrongAnswers questionWrongAnswers : questionWrongAnswerss){
                    questionSet.add(questionWrongAnswers.getQuestion_id());
                }

                if(questionSet.size() > 0){
                    //mongo里面查询收藏数据
                    query = new Query(new Criteria().andOperator(
                            Criteria.where("project_id").is(project_id),
                            Criteria.where("user_id").is(user.getId())
                    ));
                    List<QuestionCollection> questionCollections = mongoTemplate.find(query, QuestionCollection.class,
                            collectionName_question_collection);

                    //根据题目编号查询所有的题
                    Map<String, Object> params = new HashedMap();
                    params.put("intIds", questionSet);
                    result = questionService.selectList(params, questionCollections);
                }
            }
        }
        Response<Object> resp = new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        resp.setResult(result);
        return resp;
    }

    /**
     * 我的易错题
     * @param request
     * @param project_id
     * @return
     */
    @RequestMapping(value = "/easyWrongs")
    public String easy(HttpServletRequest request,
                        @RequestParam(value="project_id", required = true, defaultValue = "") String project_id){
        List<Map<String, Object>> result = new ArrayList<>();
        request.setAttribute("url", "/student/question/easyWrongs_ajax?project_id=" + project_id);
        request.setAttribute("title", "我的易错题");
        return "student/question/student_exercise_ajax";
    }

    @ResponseBody
    @RequestMapping(value = "/easyWrongs_ajax")
    public Object easyWrongs_ajax(HttpServletRequest request,
                       @RequestParam(value="project_id", required = true, defaultValue = "") String project_id){
        List<Map<String, Object>> result = new ArrayList<>();
        User user = this.getCurrUser(request);
        if (null != project_id && !project_id.equals("") && null != user){
            //mongo里面查询我的错题数据
            Query query = new Query(new Criteria().andOperator(
                    Criteria.where("project_id").is(project_id),
                    Criteria.where("user_id").is(user.getId())
            ));
            List<QuestionWrongAnswers> questionWrongAnswerss = mongoTemplate.find(query, QuestionWrongAnswers.class,
                    collectionName_question_wrong_answers);
            //获取所有的题目编号
            List<String> questionIds = getEasyQuestionIds(questionWrongAnswerss);
            if(questionIds.size() > 0){
                //mongo里面查询收藏数据
                query = new Query(new Criteria().andOperator(
                        Criteria.where("project_id").is(project_id),
                        Criteria.where("user_id").is(user.getId())
                ));
                List<QuestionCollection> questionCollections = mongoTemplate.find(query, QuestionCollection.class,
                        collectionName_question_collection);

                //根据题目编号查询所有的题
                Map<String, Object> params = new HashedMap();
                params.put("intIds", questionIds);
                result = questionService.selectList(params, questionCollections);
            }
        }
        Response<Object> resp = new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        resp.setResult(result);
        return resp;
    }

    /**
     * 删除我的错题
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/delete_wrong")
    public Object delete(HttpServletRequest request,
                         @RequestParam(value="question_id", required = true, defaultValue = "") String question_id,
                         @RequestParam(value="project_id", required = true, defaultValue = "") String project_id) {
        Response<Object> resp = new Response<>();
        User user = this.getCurrUser(request);
        if(null == user){
            resp.setCode(Code.USER_NOT_EXIST.getCode());
            return resp;
        }
        if (null != question_id && !question_id.equals("") &&
                null != project_id && !project_id.equals("")){
            Map<String, Object> queueMap = new HashMap();
            queueMap.put("project_id", project_id);
            queueMap.put("question_id", question_id);
            queueMap.put("user_id", user.getId());
            EXECUTOR.execute(new StudentQuestionWrongController.MakeWrong(queueMap));
        }
        resp.setCode(Code.SUCCESS.getCode());
        return resp;
    }

    /**
     * 查询出错3次的题
     * @param questionWrongAnswers
     * @return
     */
    public List<String> getEasyQuestionIds(List<QuestionWrongAnswers> questionWrongAnswers){
        List<String> questions = new ArrayList<>();
        Map<String, Integer> result = new HashedMap();
        if(questionWrongAnswers.size() < 1){
            return questions;
        }
        for(QuestionWrongAnswers questionWrongAnswer : questionWrongAnswers){
            String question_id = questionWrongAnswer.getQuestion_id();
            int count = 0;
            if(null != result.get(question_id)){
                count = result.get(question_id);
            }
            if(count > 1){//超过三次就就是易错题
                questions.add(question_id);
            }
            result.put(question_id, ++count);
        }
        return questions;
    }

    private class MakeWrong extends Thread {

        private Map<String,Object> queueMap;

        public MakeWrong(Map<String,Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queueQuestionSender.sendMapMessage("tp.wrong.queue", queueMap);
            }catch(Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }
}
