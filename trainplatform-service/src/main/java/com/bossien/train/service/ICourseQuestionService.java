package com.bossien.train.service;

import com.bossien.train.domain.CourseQuestion;
import com.bossien.train.domain.Question;
import com.bossien.train.domain.dto.CourseQuestionDTO;
import com.bossien.train.domain.dto.CourseQuestionMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by huangzhaoyong on 2017/7/25.
 */
public interface ICourseQuestionService {

    /**
     * 根据参数查询对象集合
     */
    List<CourseQuestion> selectList(Map<String, Object> params);

    /**
     * 根据参数查询列表
     */
    List<String> selectQuestionIdList(Map<String, Object> params);

    /**
     * 根据项目id+用户id查询  map<question_id, course_id>
     */
    Map<String, String> selectCourseIdByQuestionId(String project_id, String user_id);

    /**
     * 查询题库集合
     */
    List<Question> selectQuestionByProjectAndUserId(String project_id, String user_id);

    int insertMessage(CourseQuestionMessage courseQuestionMessage);

    int insertBatch(List<CourseQuestion> list);

    int updateByQuestionId(CourseQuestionMessage courseQuestionMessage);

    int deleteByPrimaryKey(Map<String, Object> params);

    int selectIntQuestionIdCount(Map<String, Object> params);

    CourseQuestion selectOne(Map<String, Object> params);

    CourseQuestion selectById(Map<String, Object> params);

    List<CourseQuestion> seletcQuestionCount(Map<String, Object> params);
}
