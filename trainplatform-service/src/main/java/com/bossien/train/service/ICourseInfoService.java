package com.bossien.train.service;

import com.bossien.train.domain.CourseInfo;
import com.bossien.train.domain.ProjectCourse;
import com.bossien.train.domain.dto.CourseMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/26.
 */
public interface ICourseInfoService {

    Integer selectCount(Map<String, Object> params);

    List<Map<String, Object>> selectList(Map<String, Object> params);
    List<CourseInfo> selectAll();
    /**
     * 通过课程主键id查询课程
     *
     * @param ids
     * @return
     */
    List<CourseInfo> selectCourseByIds(Map<String, Object> ids);

    /**
     * 通过课程id查询课程总数
     *
     * @param ids
     * @return
     */
    Integer selectCourseCountByIds(Map<String, Object> ids);
    /**
     * 通过课程id查询课程信息
     *
     * @param courseId
     * @return
     */
    CourseInfo selectOne(String courseId);

    /**
     * 将projectInfo中的学时要求，必选题量与courseInfo中的数据组装到map中
     *
     * @param projectCourses
     * @param courseInfos
     * @return
     */
    List<Map<String, Object>> saveProjectCourseInfoDate(List<ProjectCourse> projectCourses, List<CourseInfo> courseInfos);

    /**
     * 通过课程id查询课程信息
     *
     * @param courseId
     * @return
     */
    CourseInfo selectCourseInfoByCourseId(String courseId);

    /**
     * 根据courseTypeId或者courseName查询课程
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> selectCourseList(Map<String, Object> params);

    /**
     * 根据courseTypeId或者courseName查询课程总条数
     *
     * @param params
     * @return
     */
    Integer selectCourseCount(Map<String, Object> params);

    /**
     * 课程下的总题量
     *
     * @param params
     * @return
     */
    int selectCourseQuestionCount(Map<String, Object> params);

    /**
     * 课程转移
     *
     * @param params
     */


    void courseMove(Map<String, Object> params);

    int insertSelective(Map<String, Object> params);

    int insertMessage(CourseMessage courseMessage);

    int updateMessage(CourseMessage courseMessage);

    int insertBatch(List< Map<String, Object>> list);

    int updateBatch(List< Map<String, Object>> list);

    int updateByPrimaryKeySelective(Map<String, Object> params);

    int deleteByPrimaryKey(Map<String, Object> params);

    void deleteByCourseId(Map<String, Object> params);

    /**
     * 根据courseIds查询课程信息
     * @param courseIds
     * @return
     */
    List<CourseInfo> selectCourses(List<String> courseIds);
}
