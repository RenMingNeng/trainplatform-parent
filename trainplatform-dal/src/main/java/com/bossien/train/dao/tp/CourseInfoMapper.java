package com.bossien.train.dao.tp;

import com.bossien.train.domain.CourseInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 项目信息
 * Created by Administrator on 2017/7/25.
 */
@Repository
public interface CourseInfoMapper {


    Integer selectCount(Map<String, Object> params);

    List<Map<String, Object>> selectList(Map<String, Object> params);

    List<CourseInfo> selectAll();

    /**
     * 通过课程主键id查询课程
     */
    List<CourseInfo> selectCourseByIds(Map<String, Object> courseids);

    /**
     * 通过课程id查询课程总数
     */
    Integer selectCourseCountByIds(Map<String, Object> courseids);

    /**
     * 通过课程id查询课程信息
     */
    CourseInfo selectCourseInfoByCourseId(String courseId);
    /**
     * 通过课程id查询课程信息
     *
     * @param courseId
     * @return
     */
    CourseInfo selectOne(String courseId);

    /**
     * 根据courseTypeId或者courseName查询课程
     */
    public List<Map<String, Object>> selectCourseList(Map<String, Object> params);

    /**
     * 根据courseTypeId或者courseName查询课程总条数
     */
    Integer selectCourseCount(Map<String, Object> params);

    /**
     * 课程下的总题量
     */
    int selectCourseQuestionCount(Map<String, Object> params);

    int insertSelective(Map<String, Object> params);

    int updateByPrimaryKeySelective(Map<String, Object> params);

    int insertBatch(List<Map<String, Object>> list);

    int updateBatch(List<Map<String, Object>> list);

    int deleteByPrimaryKey(Map<String, Object> params);

    void deleteByCourseId(Map<String, Object> params);

    /**
     * 修改
     */
    int update(Map<String, Object> params);

    /**
     * 根据courseIds查询课程信息
     * @param courseIds
     * @return
     */
    List<CourseInfo> selectCourses(List<String> courseIds);

}
