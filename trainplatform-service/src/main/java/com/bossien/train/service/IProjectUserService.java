package com.bossien.train.service;

import com.bossien.train.domain.ProjectUser;
import com.bossien.train.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/24.
 */
public interface IProjectUserService {

    /**
     * 批量保存项目用户
     * @param param
     * @return
     */
    List<Map<String, Object>> saveBatch(Map<String, Object> param, User user) throws Exception;

    /**
     * 根据用户Ids和项目Id删除项目用户
     * @param params
     * @return
     */
    int deleteBatch(Map<String, Object> params);

    /**
     * 查询项目用户的数量
     * @param params
     * @return
     */
    int selectCount(Map<String, Object> params);

    /**
     * 查询用户ids集合
     * @param params
     * @return
     */
    List<String> selectUserIds(Map<String, Object> params);

    /**
     * 根据属性查询项目用户
     * @param params
     * @return
     */
    List<Map<String, Object>> selectList(Map<String, Object> params);

    /**
     * 查询单条数据
     * @param params
     * @return
     */
    ProjectUser selectOne(Map<String, Object> params);
    /**
     * 根据projectId和UserId查询角色Id
     * @param projectId
     * @param userId
     * @return
     */
    ProjectUser selectByProjectIdAndUserId( String projectId, String userId);

    /**
     * 根据角色和项目查询项目用户
     * @param params
     * @return
     */
    List<Map<String, Object>> selectProjectUser(Map<String, Object> params);
    /**
     * 根据projectId删除
     * @param params
     * @return
     */
    int deleteByProjectId(Map<String, Object> params);
    /**
     * 根据projectId和userId查询roleId
     * @param params
     * @return
     */
    String selectRoleId(Map<String, Object> params);
    /**
     * 参与培训人次
     * @param params
     * @return
     */
    int selectUserCount(Map<String, Object> params);

    /**
     * 查询当前学员的所有项目
     * @param params
     * @return
     */
    List<String> selectProjectIdByUserId(Map<String, Object> params);


    /**
     * 根据projectId和roleIds确认数据是否最新
     * @param params
     */
    void confirmProjectUser(Map<String, Object> params);

    /**
     * 高级设置中查询项目用户信息
     * @param params
     * @return
     */
    List<Map<String, Object>> selectProjectUserMap(Map<String, Object> params);

    /**
     * 高级设置中修改项目用户信息
     * @param params
     * @return
     */
    int update(Map<String, Object> params);
    /**
     * 根据projectId查询项目用户
     * @param params
     * @return
     */
    List<ProjectUser> selectProjectUsers(Map<String, Object> params);

    /**
     * 批量插入项目用户信息
     * @param item
     * @return
     */
    int insertBatch(List<ProjectUser> item);

    /**
     * 查询项目用户的用户Id和用户姓名
     * @param projectId
     * @return
     */
    List<Map<String, Object>> selectUsers(String projectId);
}
