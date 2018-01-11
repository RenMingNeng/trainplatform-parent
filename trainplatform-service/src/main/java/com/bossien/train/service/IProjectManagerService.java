package com.bossien.train.service;

import com.bossien.train.domain.ProjectInfo;
import com.bossien.train.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/26.
 */
public interface IProjectManagerService {

/*
    *//**
     * 修改项目
     * @param user
     * @param params
     *//*
    void updateProject(User user, Map<String,Object> params);*/

    /**
     * 删除项目
     * @param params
     */
    void deleteProject(Map<String,Object> params);

    /**
     * 删除项目
     * @param params
     */
    void delete(Map<String,Object> params);

    /**
     * 修改项目发布状态
     * @param params
     */
    void updateProjectPublish(Map<String,Object> params) throws Exception;

    /**
     * 查询培训总进度
     * @param params
     * @return
     */
    Map<String, Object> selectTrainProcess(Map<String, Object> params);

    /**
     * 查询考试信息
     * @param params
     * @return
     */
    Map<String, Object> selectExamInfo(Map<String, Object> params);

    /**
     * 异步修改项目统计信息
     * @param params
     */
    void update(Map<String,Object> params);

    /**
     * 修改projectCourse和projectCourseInfo表的数据
     * @param params
     */
    void updateRequirementAndSelectCount(Map<String,Object> params);
}
