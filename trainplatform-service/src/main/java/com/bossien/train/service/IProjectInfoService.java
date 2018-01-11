package com.bossien.train.service;


import com.bossien.train.domain.ProjectBasic;
import com.bossien.train.domain.ProjectInfo;
import com.bossien.train.domain.User;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/25.
 */
public interface IProjectInfoService {


    Integer selectCount(Map<String, Object> params);

    List<Map<String,Object>> selectList(Map<String, Object> params);

    /**
     * 通过项目id查询项目信息（项目详情）
     * @param id
     * @return
     */
    public ProjectInfo selectProjectInfoById(String id);


    /**
     * 项目详细信息保存
     * @param params
     */
    void save(Map<String, Object> params) throws ParseException;

    /**
     * 修改项目详情
     * @param params
     * @return
     */
    int update(Map<String, Object> params);

    /**
     * 根据projectId删除
     * @param params
     * @return
     */
    int delete(Map<String, Object> params);

    /**
     * 发布
     * @param params
     */
    void publish(Map<String, Object> params) throws ParseException;


    /**
     * 根据状态和类型查询projectIds
     * @param params
     * @return
     */
    List<String> selectProjectIds(Map<String, Object> params);

    /**
     * 根据状态和类型查询projectId总数
     * @param params
     * @return
     */
    int selectProjectIdCount(Map<String, Object> params);

    List<ProjectInfo> selectNeedPublish(String start, String end, String projectStatus);

    Integer updateProjectStatus(String projectId, String projectStatus);

    /**
     * 查询项目状态
     * @param projectId
     * @return
     */
    String selectProjectStatus(String projectId);

    Integer checkProjectStatus(Map<String, Object> params);
}
