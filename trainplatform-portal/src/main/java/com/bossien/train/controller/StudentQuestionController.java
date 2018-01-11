package com.bossien.train.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huangzhaoyong on 2017/7/28.
 */
@Controller
@RequestMapping("/student/question")
public class StudentQuestionController extends BasicController{
    public static final Logger logger = LoggerFactory.getLogger(StudentExamAnswersController.class);
    // 操作mongodb的集合名,等同于mysql中的表
    private final String collectionName_question_collection = "question_collection";
    @Autowired
    private MongoOperations mongoTemplate;
    @Autowired
    private IQuestionService questionService;

    /**
     * 专项练习
     * @param request
     * @param project_id
     * @param type
     * @return
     */
    @RequestMapping(value = "/exercise/{type}")
    public String single(HttpServletRequest request, @PathVariable String type,
                       @RequestParam(value="project_id", required = true, defaultValue = "") String project_id){
        List<Map<String, Object>> result = new ArrayList<>();
        if(type.equals("single")){
            request.setAttribute("title", "单选题");
        }else if(type.equals("many")){
            request.setAttribute("title", "多选题");
        }else if(type.equals("judge")){
            request.setAttribute("title", "判断题");
        }else if(type.equals("courseList")){
            request.setAttribute("title", "随机练习");
        }
        request.setAttribute("url", "/student/question/exercise/ajax/" + type + "?project_id=" + project_id);
        return "student/question/student_exercise_ajax";
    }

    @ResponseBody
    @RequestMapping(value = "/exercise/ajax/{type}")
    public Object exercise_ajax(HttpServletRequest request, @PathVariable String type,
                         @RequestParam(value="project_id", required = true, defaultValue = "") String project_id){
        List<Map<String, Object>> result = new ArrayList<>();
        User user = this.getCurrUser(request);
        if (null != project_id && !project_id.equals("") && null != user){
            Map<String, Object> params = new HashedMap();
            params.put("project_id", project_id);
            params.put("user_id", user.getId());

            //mongo里面查询收藏数据
            Query query = new Query(new Criteria().andOperator(
                    Criteria.where("project_id").is(project_id),
                    Criteria.where("user_id").is(user.getId())
            ));
            List<QuestionCollection> questionCollections =
                    mongoTemplate.find(query, QuestionCollection.class, collectionName_question_collection);

            result = questionService.selectListByType(params, type, questionCollections);
        }
        Response<Object> resp = new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        resp.setResult(result);
        return resp;
    }
}
