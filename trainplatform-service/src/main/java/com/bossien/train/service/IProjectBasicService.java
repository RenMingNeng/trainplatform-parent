package com.bossien.train.service;


import com.bossien.train.domain.ProjectBasic;
import com.bossien.train.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/31.
 */
public interface IProjectBasicService {

    /**
     * 创建项目基础信息
     * @param user
     * @return
     */
    ProjectBasic build(User user);

    /**
     * 保存项目基础信息
     * @param projectBasic
     * @return
     */
    int save(ProjectBasic projectBasic);

    /**
     * 修改项目基础信息
     * @param params
     * @return
     */
    int update(Map<String, Object> params);

    /**
     * 根据id获取对象
     * @param id
     * @return
     */
    ProjectBasic selectById(String id);

    /**
     * 根据id删除项目
     * @param params
     * @return
     */
    int delete(Map<String, Object> params);


    /**
     * 根据项目id获取所有对应的主题id
     * @param project_ids
     * @return
     */
    List<String> selectSubjectIdByProjectId(List<String> project_ids);

    Integer updateProjectStatus(String id, String value);

    String selectProjectStatus(String projectId);

    void checkProjectStatus(Map<String, Object> param);

    List<ProjectBasic> selectPublicProject(Map<String, Object> param);

    List<String> selectProjectIdsByStatus(Map<String, Object> param);
}
