package com.bossien.train.controller;

import com.bossien.framework.mq.sender.QueueQuestionSender;
import com.bossien.train.domain.ExamAnswers;
import com.bossien.train.domain.QuestionCollection;
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
 * 我的收藏
 */
@Controller
@RequestMapping("/student/question")
public class StudentQuestionCollectionController extends BasicController {
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    public static final Logger logger = LoggerFactory.getLogger(StudentExamAnswersController.class);
    @Autowired
    private IQuestionService questionService;
    @Autowired
    private MongoOperations mongoTemplate;
    @Autowired
    private QueueQuestionSender queueQuestionSender;

    // 操作mongodb的集合名,等同于mysql中的表
    private final String collectionName_question_collection = "question_collection";

    @RequestMapping("/collection")
    public String index(HttpServletRequest request,
                        @RequestParam(value="project_id", required = true, defaultValue = "") String project_id){

        request.setAttribute("url", "/student/question/collection_ajax?project_id=" + project_id);
        request.setAttribute("title", "我的收藏");
        return "student/question/student_exercise_ajax";
    }

    @ResponseBody
    @RequestMapping("/collection_ajax")
    public Object collection_ajax(HttpServletRequest request,
                        @RequestParam(value="project_id", required = true, defaultValue = "") String project_id){
        User user = this.getCurrUser(request);
        List<Map<String, Object>> result = new ArrayList<>();
        if(null != project_id && !project_id.equals("") && null != user){
            //mongo里面查询收藏数据
            Query query = new Query(new Criteria().andOperator(
                    Criteria.where("project_id").is(project_id),
                    Criteria.where("user_id").is(user.getId())
            ));
            List<QuestionCollection> questionCollections =
                    mongoTemplate.find(query, QuestionCollection.class, collectionName_question_collection);

            Set<String> questionSet = new HashSet<>();
            for(QuestionCollection questionCollection : questionCollections){
                questionSet.add(questionCollection.getQuestion_id());
            }
            Map<String, Object> params = new HashedMap();
            params.put("intIds", questionSet);

            if(questionSet.size() > 0){
                result = questionService.selectList(params, questionCollections, new ArrayList<ExamAnswers>());
            }
        }
        Response<Object> resp = new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        resp.setResult(result);
        return resp;
    }

    /**
     * 收藏
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/save_collect")
    public Object saveCollect(HttpServletRequest request, String question_id, String project_id) {
        Response<Object> resp = new Response<Object>();
        User user = this.getCurrUser(request);
        if(null == user){
            resp.setCode(Code.USER_NOT_EXIST.getCode());
            return resp;
        }
        if (null != question_id && !question_id.equals("") &&
                null != project_id && !project_id.equals("")){
            Map<String, Object> queueMap = new HashMap();
            queueMap.put("type", "save");
            queueMap.put("user_id", user.getId());
            queueMap.put("user_name", user.getUserName());
            queueMap.put("question_id", question_id);
            queueMap.put("project_id", project_id);

            EXECUTOR.execute(new StudentQuestionCollectionController.MakeCollect(queueMap));
        }
        resp.setCode(Code.SUCCESS.getCode());
        return resp;
    }

    /**
     * 删除收藏
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/delete_collect")
    public Object rmCollect(HttpServletRequest request, String question_id, String project_id) {
        Response<Object> resp = new Response<Object>();
        User user = this.getCurrUser(request);
        if(null == user){
            resp.setCode(Code.USER_NOT_EXIST.getCode());
            return resp;
        }
        if (null != question_id && !question_id.equals("") &&
                null != project_id && !project_id.equals("")){
            Map<String, Object> queueMap = new HashMap();
            queueMap.put("type", "delete");
            queueMap.put("user_id", user.getId());
            queueMap.put("question_id", question_id);
            queueMap.put("project_id", project_id);

            EXECUTOR.execute(new StudentQuestionCollectionController.MakeCollect(queueMap));
        }
        resp.setCode(Code.SUCCESS.getCode());
        return resp;
    }

    private class MakeCollect extends Thread {

        private Map<String,Object> queueMap;

        public MakeCollect(Map<String,Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queueQuestionSender.sendMapMessage("tp.collection.queue", queueMap);
            }catch(Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }
}
