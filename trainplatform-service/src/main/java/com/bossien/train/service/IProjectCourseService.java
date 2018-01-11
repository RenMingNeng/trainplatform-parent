package com.bossien.train.service;

import com.bossien.train.domain.CourseInfo;
import com.bossien.train.domain.ProjectCourse;
import com.bossien.train.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 对项目和课程关联表（Project_course）的操作
 * Created by Administrator on 2017/7/25.
 */
    public interface IProjectCourseService {
        /**
         * 根据projectId和roleId查询相关课程
         * @param map
         * @return
         */
        public List<ProjectCourse> selectByProjectIdAndRoleId(Map<String,Object> map);

    /**
     * 批量保存项目课程信息
     * @param params
     * @param user
     * @returnc
     */
    List<String> saveBatch(Map<String, Object> params, User user) throws Exception;

    /**
     * 根据projectId和roleId删除项目课程
     * @param params
     * @return
     */
    int deleteBatch(Map<String, Object> params);

    /**
     * 查询项目课程ids集合
     * @param param
     * @return
     */
    List<String > selectCourseIds(Map<String, Object> param);

    /**
     * 查询项目课程ids集合（加缓存）
     * @param projectId
     * @param roleId
     * @return
     */
    List<String > selectCourseIds(String projectId, String roleId);

    /**
     * 根据属性查询项目课程
     * @param params
     * @return
     */
    List<Map<String, Object>> selectList(Map<String, Object> params);
    /**
     * 学员项目详情中的课程数量统计
     * @param params
     * @return
     */
    int selectCourseCount(Map<String, Object> params);
    int courseCount(Map<String, Object> params);
    /**
     *根据属性查询项目课程数量
     * @param params
     * @return
     */
    int selectCount(Map<String, Object> params);

    /**
     * 批量保存公开项目课程信息
     * @param params
     * @param user
     * @throws Exception
     */
    void saveBatch_Public(Map<String, Object> params, User user) throws Exception;

    /**
     * 查询项目课程信息集合
     * @param params
     * @return
     */
    List<Map<String, Object>> selectProjectCourseInfo(Map<String, Object> params);

    /**
     * 根据projectId和roleIds确认数据是否最新
     * @param params
     */
    void confirmProjectCourse(Map<String, Object> params);

    /**
     *根据项目Id和角色Id查询项目课程总学时和总题量
     * @param params
     * @return
     */
    Map<String, Object> selectProjectCourseMap(Map<String, Object> params);

    /**
     * 根据projectId和courseId修改
     * @param params
     * @return
     */
    int update(Map<String, Object> params);
    /**
     * 根据projectId删除
     * @param params
     * @return
     */
    int deleteProjectId(Map<String, Object> params);

    void deleteProjectCourse(Map<String, Object> params);

    /**
     * 高级设置中查询项目课程信息
     * @param params
     * @return
     */
    List<Map<String, Object>> selectProjectCourseList(Map<String, Object> params);
    /**
     * 高级设置中查询项目课程总条数
     * @param params
     * @return
     */
    int selectProjectCourseCount(Map<String, Object> params);

    /**
     * 批量保存
     * @param item
     * @return
     */
    int insertBatch(List<ProjectCourse> item);

    /**
     * 根据项目id和课程id批量删除
     * @param param
     */
    void deleteBatchByCourseIds(Map<String, Object> param);

    /**
     * 根据项目Id获取角色Id集合
     * @param projectId
     * @return
     */
    List<String> selectRoleIds(String projectId);
    /**
     * 根据projectId查询课程集合
     * @param params
     * @return
     */
    List<ProjectCourse> selectProjectCourses(Map<String, Object> params);

    /**
     * 插入课程信息
     * @param courseInfo
     * @return
     */
    int insertCourseInfo(CourseInfo courseInfo);

    void updateQuestionCount(String projectId);

    /**
     * 通过projectId统计总题量
     * @param projectId
     * @return
     */
    List<Map<String,Object>> selectTotalQuestionByProjectId( String projectId);
}
