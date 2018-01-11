package com.bossien.train.controller;

import com.bossien.train.domain.QuestionCollection;
import com.bossien.train.domain.User;
import com.bossien.train.exception.Code;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.ICourseQuestionService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huangzhaoyong on 2017/8/1.
 * 课程题库
 */
@Controller
@RequestMapping(value="/student/question")
public class StudentCourseQuestionController extends BasicController {
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    public static final Logger logger = LoggerFactory.getLogger(StudentExamAnswersController.class);
    // 操作mongodb的集合名,等同于mysql中的表
    private final String collectionName_question_collection = "question_collection";
    @Autowired
    private MongoOperations mongoTemplate;
    @Autowired
    private ICourseQuestionService courseQuestionService;
    @Autowired
    private IQuestionService questionService;

    /**
     * 课程练习
     * @return
     */
    @RequestMapping("/courseExercise")
    public String courseExercise(HttpServletRequest request,
                                 @RequestParam(value="course_id", required = true, defaultValue = "") String course_id,
                                 @RequestParam(value="project_id", required = true, defaultValue = "") String project_id){
        List<Map<String, Object>> result = new ArrayList<>();
        User user = this.getCurrUser(request);
        request.setAttribute("url", "/student/question/courseExercise_ajax?project_id=" + project_id + "&course_id=" + course_id);
        request.setAttribute("title", "课程练习");
        return "student/question/student_exercise_ajax";
    }

    @ResponseBody
    @RequestMapping("/courseExercise_ajax")
    public Object courseExercise_ajax(HttpServletRequest request,
                                 @RequestParam(value="course_id", required = true, defaultValue = "") String course_id,
                                 @RequestParam(value="project_id", required = true, defaultValue = "") String project_id){
        List<Map<String, Object>> result = new ArrayList<>();
        User user = this.getCurrUser(request);
        if(null != course_id && !course_id.equals("")){
            //查询出题编号集合
            Map<String, Object> params = new HashedMap();
            params.put("intCourseId", course_id);
            List<String> questions = courseQuestionService.selectQuestionIdList(params);
            if(null != questions && questions.size() > 0){
                //mongo里面查询收藏数据
                Query query = new Query(new Criteria().andOperator(
                        Criteria.where("project_id").is(project_id),
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
}
