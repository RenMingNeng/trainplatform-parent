package com.bossien.train.dao.ex;

import com.bossien.train.domain.Course;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CourseMapper {

    List<Course> selectList(Map<String, Object> params);

    List<Course> selectAll(Map<String, Object> params);

    Integer selectCount(Map<String, Object> params);

    Course selectOne(Map<String, Object> params);

    Course selectId(Map<String, Object> params);

    /**
     * 根据intTypeId或者courseName查询课程
     *
     * @param params
     * @return
     */
    public List<Map<String, Object>> selectCourseList(Map<String, Object> params);

    /**
     * 根据intTypeId或者courseName查询课程总条数
     *
     * @param params
     * @return
     */
    public Integer selectCourseCount(Map<String, Object> params);

    /**
     * 通过主键id查询单条
     *
     * @param intId
     * @return
     */
    Course selectById(String intId);

    int insertSelective(Course course);

    int updateByVarCodeSelective(Course course);

    int insertBatch(List<Course> list);

    int updateBatch(List<Course> list);

    int deleteByPrimaryKey(Map<String, Object> params);

    int deleteByKey(Map<String, Object> params);

}
