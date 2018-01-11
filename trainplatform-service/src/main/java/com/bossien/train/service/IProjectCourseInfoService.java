package com.bossien.train.service;


import com.bossien.train.domain.ProjectBasic;
import com.bossien.train.domain.ProjectCourseInfo;

import com.bossien.train.domain.User;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/24.
 */
public interface IProjectCourseInfoService {

    List<Map<String, Object>> selectList(Map<String, Object> params);

    Integer selectCount(Map<String, Object> params);

    /**
     * 批量添加项目课程信息
     * @param item
     * @return
     */
    int insertBatch(List<Map<String, Object>> item);

    /**
     * 批量删除项目课程详细
     * @param params
     * @return
     */
    int deleteBatch(Map<String, Object> params);

    void saveProjectCourseInfo(ProjectBasic projectBasic);

    ProjectCourseInfo selectOne(Map<String, Object> params);

    Integer update(ProjectCourseInfo projectCourseInfo);

    Integer insert(ProjectCourseInfo projectCourseInfo);

    /**
     * 查询课程id集合
     * @param params
     * @return
     */
    List<String> selectIds(Map<String, Object> params);

    /**
     * 查询用户id集合
     * @param params
     * @return
     */
    List<String> selectUserIds(Map<String, Object> params);

    /**
     * 查询id集合
     * @param params
     * @return
     */
    List<String> selectIdList(Map<String, Object> params);

    /**
     * 获取用户在么个项目下的课程集合
     * @param params
     * @return
     */
    List<Map<String, Object>> selectCourseList(Map<String, Object> params);
}
