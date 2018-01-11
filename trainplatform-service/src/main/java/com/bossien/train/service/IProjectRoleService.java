package com.bossien.train.service;

import com.bossien.train.domain.ProjectRole;
import com.bossien.train.domain.User;
import com.bossien.train.util.JsonUtils;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/31.
 */
public interface IProjectRoleService {

    /**
     * 创建项目角色用户
     * @param user
     * @return
     */
    public ProjectRole build(User user);

    /**
     * 批量保存项目角色信息
     * @param user
     * @param param
     * @return
     */
    int saveBatch(User user, Map<String, Object> param) throws Exception;
    /**
     * 根据projectId查询受训角色
     * @param projectId
     * @return
     */
    List<ProjectRole> selectByProjectId(String projectId);

    /**
     * 根据项目Id查寻受训角色
     * @param projectId
     * @return
     */
    String selectRoleName(String projectId);

    /**
     * 根据projectId删除角色
     * @param map
     */
    int delete(Map<String,Object> map);
    /**
     * 修改项目角色
     * @param params
     */
    Integer updateProjectRole(User user,Map<String,Object> params);

    /**
     * 插入项目角色信息
     * @param params
     * @return
     */
    int insert(Map<String, Object> params);

    /**
     * 根据projectId和roleId查询受训角色
     * @param params
     * @return
     */
    List<Map<String, Object>> selectProjectRole(Map<String, Object> params);

    /**
     * 查询角色Id
     * @param params
     * @return
     */
    List<String> selectRoleId(Map<String, Object> params);

}
