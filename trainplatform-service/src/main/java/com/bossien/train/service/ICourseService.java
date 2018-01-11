package com.bossien.train.service;

import com.bossien.train.domain.Course;
import com.bossien.train.domain.dto.CourseDTO;
import com.bossien.train.domain.dto.CourseMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/26.
 */
public interface ICourseService {

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
    public Course selectById(String intId);

    /*
    * 添加数据
    * */
    int insertSelective(CourseMessage courseMessage);

    /*
    * 更新数据*/
    int updateByVarCodeSelective(CourseMessage courseMessage);

    int deleteByPrimaryKey(Map<String, Object> params);

    int insertBatch(List<Course> list);

    int updateBatch(List<Course> list);
}
