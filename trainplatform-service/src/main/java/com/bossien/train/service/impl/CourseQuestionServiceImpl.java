package com.bossien.train.service.impl;

import com.bossien.train.dao.ex.CourseQuestionMapper;
import com.bossien.train.dao.ex.QuestionMapper;
import com.bossien.train.dao.tp.CourseInfoMapper;
import com.bossien.train.dao.tp.ProjectCourseMapper;
import com.bossien.train.dao.tp.ProjectUserMapper;
import com.bossien.train.domain.*;
import com.bossien.train.domain.dto.CourseQuestionDTO;
import com.bossien.train.domain.dto.CourseQuestionMessage;
import com.bossien.train.service.ICourseQuestionService;
import com.bossien.train.service.ISequenceService;
import com.bossien.train.util.PropertiesUtils;
import com.google.common.collect.Maps;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by huangzhaoyong on 2017/7/25.
 */

@Service
public class CourseQuestionServiceImpl implements ICourseQuestionService {

    /**
     * 随机练习的试题数量
     */
    public static String EXERCISE_RANDOM_QUESTION_NUMBER = PropertiesUtils.getValue("EXERCISE_RANDOM_QUESTION_NUMBER");

    @Autowired
    private CourseQuestionMapper courseQuestionMapper;
    @Autowired
    private ProjectUserMapper projectUserMapper;
    @Autowired
    private ProjectCourseMapper projectCourseMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CourseInfoMapper courseInfoMapper;


    @Override
    public List<CourseQuestion> selectList(Map<String, Object> params) {

        return courseQuestionMapper.selectList(params);
    }

    @Override
    public List<String> selectQuestionIdList(Map<String, Object> params) {
        Object project_id = params.get("project_id");
        Object user_id = params.get("user_id");
        Object chrType = params.get("chrType");
        if (null != project_id && !project_id.equals("") &&
            null != user_id && !user_id.equals("") &&
            null != chrType && !chrType.equals("")) {
            List<String> intCourseIds = new ArrayList<>();
            //根据用户、项目找角色role_id
            ProjectUser projectUser = projectUserMapper.selectOne(params);
            //根据项目/角色找课程List<String>
            params.put("roleId", projectUser.getRoleId());
            params.put("projectId", project_id);
            List<ProjectCourse> courses = projectCourseMapper.selectByProjectIdAndRoleId(params);
            for (ProjectCourse course : courses) {
                intCourseIds.add(course.getCourseId());
            }
            params = new HashedMap();
            params.put("intCourseIds", intCourseIds);
            params.put("chrType", chrType);
            params.put("random", EXERCISE_RANDOM_QUESTION_NUMBER);
        }
        return courseQuestionMapper.selectQuestionIdList(params);
    }

    @Override
    public Map<String, String> selectCourseIdByQuestionId(String project_id, String user_id) {
        Map<String, Object> params = new HashedMap();
        params.put("userId", user_id);
        params.put("projectId", project_id);

        //先查询角色 项目id+用户id
        ProjectUser projectUser = projectUserMapper.selectOne(params);
        if (null == projectUser) {
            return new HashedMap();
        }

        //再根据角色查询课程集合
        params.put("roleId", projectUser.getRoleId());
        List<String> courseList = projectCourseMapper.selectCourseIds(params);
        //根据课程集合查询所有题
        params.put("intCourseIds", courseList);
        List<CourseQuestion> courseQuestionList = courseQuestionMapper.selectList(params);
        return listToMap(courseQuestionList);
    }

    @Override
    public List<Question> selectQuestionByProjectAndUserId(String project_id, String user_id) {
        Map<String, Object> params = new HashedMap();
        params.put("userId", user_id);
        params.put("projectId", project_id);

        //先查询角色 项目id+用户id
        ProjectUser projectUser = projectUserMapper.selectOne(params);
        if (null == projectUser) {
            return new ArrayList<>();
        }

        //再根据角色查询课程集合
        params.put("roleId", projectUser.getRoleId());
        List<String> courseList = projectCourseMapper.selectCourseIds(params);
        //根据课程集合查询所有题
        params.put("intCourseIds", courseList);
        List<CourseQuestion> courseQuestionList = courseQuestionMapper.selectList(params);
        List<String> questionList = new ArrayList<>();
        for (CourseQuestion course : courseQuestionList) {
            questionList.add(course.getIntQuestionId());
        }

        params.put("intIds", questionList);
        params.put("chrValid", "1");
        return questionMapper.selectList(params);
    }

    @Override
    public int insertMessage(CourseQuestionMessage courseQuestionMessage) {
        CourseQuestion courseQuestion = new CourseQuestion();
        courseQuestion.setIntCourseId(courseQuestionMessage.getCourseId());
        courseQuestion.setIntQuestionId(courseQuestionMessage.getCourseQuestionId());
        courseQuestionMapper.insertSelective(courseQuestion);
  /*    Maps.newConcurrentMap();*/
        /*修改CourseInfo表中课程的题目数量*/
        Map<String, Object> map = new HashMap<>();
        map.put("intCourseId", courseQuestionMessage.getCourseId());
        Integer questionCount = courseQuestionMapper.selectIntQuestionIdCount(map);//查询课程下的多少题目
        CourseInfo courseInfo = courseInfoMapper.selectCourseInfoByCourseId(courseQuestionMessage.getCourseId());
        if (null != courseInfo) {
            Map<String, Object> params = new HashMap<>();
            params.put("courseId", courseQuestionMessage.getCourseId());
            params.put("questionCount", questionCount);
            courseInfoMapper.updateByPrimaryKeySelective(params);
        }
        return 0;
    }

    @Override
    public int insertBatch(List<CourseQuestion> list) {
        return courseQuestionMapper.insertBatch(list);
    }

    @Override
    public int updateByQuestionId(CourseQuestionMessage courseQuestionMessage) {
        CourseQuestion courseAttachment = new CourseQuestion();
        courseAttachment.setIntCourseId(courseQuestionMessage.getCourseId());
        courseAttachment.setIntQuestionId(courseQuestionMessage.getCourseQuestionId());
        courseQuestionMapper.updateByQuestionId(courseAttachment);
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Map<String, Object> params) {
        return courseQuestionMapper.deleteByPrimaryKey(params);
    }

    @Override
    public int selectIntQuestionIdCount(Map<String, Object> params) {
        return courseQuestionMapper.selectIntQuestionIdCount(params);
    }

    @Override
    public CourseQuestion selectOne(Map<String, Object> params) {
        return courseQuestionMapper.selectOne(params);
    }

    @Override
    public CourseQuestion selectById(Map<String, Object> params) {
        return courseQuestionMapper.selectById(params);
    }

    @Override
    public List<CourseQuestion> seletcQuestionCount(Map<String, Object> params) {
        return courseQuestionMapper.seletcQuestionCount(params);
    }

    /**
     * list转换成map<question_id,course_id>
     */
    public Map<String, String> listToMap(List<CourseQuestion> courseQuestionList) {
        Map<String, String> map = new HashedMap();
        for (CourseQuestion courseQuestion : courseQuestionList) {
            map.put(courseQuestion.getIntQuestionId(), courseQuestion.getIntCourseId());
        }
        return map;
    }
}
