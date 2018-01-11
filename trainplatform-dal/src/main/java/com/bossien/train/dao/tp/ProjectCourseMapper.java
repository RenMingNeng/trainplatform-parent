package com.bossien.train.dao.tp;

import com.bossien.train.domain.ProjectCourse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 项目信息
 * Created by Administrator on 2017/7/25.
 */
@Repository
public interface ProjectCourseMapper {
    /**
     * 根据projectId和roleId查询相关课程
     *
     * @param map
     * @return
     */
    public List<ProjectCourse> selectByProjectIdAndRoleId(Map<String, Object> map);

    /**
     * 批量插入项目课程信息
     *
     * @param item
     * @return
     */
    int insertBatch(List<ProjectCourse> item);

    /**
     * 根据项目Id和角色Id查询项目角色总学时
     *
     * @param param
     * @return
     */
    int selectNecessaryHour(Map<String, Object> param);

    /**
     * 根据projectId和roleId删除项目课程
     *
     * @param params
     * @return
     */
    int deleteBatch(Map<String, Object> params);

    /**
     * 查询项目课程ids集合
     *
     * @param param
     * @return
     */
    List<String> selectCourseIds(Map<String, Object> param);

    /**
     * 根据属性查询项目课程
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> selectList(Map<String, Object> params);

    /**
     * 学员项目详情中的课程数量统计
     *
     * @param params
     * @return
     */
    int selectCourseCount(Map<String, Object> params);

    int courseCount(Map<String, Object> params);

    /**
     * 根据属性查询项目课程数量
     *
     * @param params
     * @return
     */
    int selectCount(Map<String, Object> params);

    /**
     * 查询项目课程信息集合
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> selectProjectCourseInfo(Map<String, Object> params);

    /**
     * 根据projectId和roleId查询主键ids
     *
     * @param params
     * @return
     */
    List<String> selectIds(Map<String, Object> params);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int delete(String id);

    /**
     * 根据项目Id和角色Id查询项目课程总学时和总题量
     *
     * @param params
     * @return
     */
    Map<String, Object> selectProjectCourseMap(Map<String, Object> params);

    /**
     * 根据projectId和courseId修改
     *
     * @param params
     * @return
     */
    int update(Map<String, Object> params);

    /**
     * 根据项目id和课程id批量删除
     *
     * @param param
     */
    void deleteBatchByCourseIds(Map<String, Object> param);

    /**
     * 根据projectId删除
     *
     * @param params
     * @return
     */
    int deleteProjectId(Map<String, Object> params);

    void deleteProjectCourse(Map<String, Object> params);

    /**
     * 高级设置中查询项目课程信息
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> selectProjectCourse(Map<String, Object> params);

    /**
     * 高级设置中查询项目课程信息
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> selectProjectRoleAndCourse(Map<String, Object> params);

    /**
     * 高级设置中查询项目课程总条数
     *
     * @param params
     * @return
     */
    int selectProjectCourseCount(Map<String, Object> params);

    /**
     * 根据项目Id获取角色Id集合
     *
     * @param projectId
     * @return
     */
    List<String> selectRoleIds(String projectId);

    /**
     * 获取每页数据中的courseIds集合
     *
     * @param params
     * @return
     */
    List<String> selectProjectCourseIds(Map<String, Object> params);

    /**
     * 根据projectId查询课程集合
     *
     * @param params
     * @return
     */
    List<ProjectCourse> selectProjectCourses(Map<String, Object> params);

    /**
     * 通过projectId统计总题量
     * @param projectId
     * @return
     */
    List<Map<String,Object>> selectTotalQuestionByProjectId(@Param(value="projectId") String projectId);


    /**
     * 修改课程试题数量
     * @param params
     */
    void updateQuestionCount(Map<String, Object> params);

    /**
     * 批量修改试题数量
     * @param items
     */
    void updateBatch(List<Map<String, Object>> items);

    /**
     * 获取项目中课程id集合
     * @param params
     * @return
     */
    List<String> selectCourseIdList(Map<String, Object> params);
}
