package com.bossien.train.service;

import com.bossien.train.domain.TrainRole;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/4.
 */
public interface IProjectCustomService {

    /**
     * 查询项目课程与角色集合
     * @param params
     * @return
     */
    List<Map<String, Object>> selectCourseAndRole(Map<String, Object> params);

    /**
     * 查询项目用户与角色集合
     * @param params
     * @return
     */
    List<Map<String, Object>> selectUserAndRole(Map<String, Object> params);


    /**
     * 高级设置保存项目、角色、课程信息
     * @param params
     * @return
     */
    int saveProjectRoleCourse(Map<String, Object> params);

    /**
     * 高级设置修改项目、角色、用户信息
     * @param params
     * @return
     */
    int updateProjectRoleUser(Map<String, Object> params);

    /**
     * 高级设置修改项目、角色、课程信息
     * @param param
     * @return
     */
    int updateRoleOrCourseInfo(Map<String, Object> param);

    /**
     * 高级设置修改项目、角色、用户信息
     * @param param
     * @return
     */
    int updateRoleOrUserInfo(Map<String, Object> param);
}
