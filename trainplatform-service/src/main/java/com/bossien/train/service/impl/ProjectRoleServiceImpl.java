package com.bossien.train.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bossien.train.dao.tp.ProjectRoleMapper;
import com.bossien.train.domain.ProjectRole;
import com.bossien.train.domain.TrainRole;
import com.bossien.train.domain.User;
import com.bossien.train.service.IProjectRoleService;
import com.bossien.train.service.ISequenceService;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.JsonUtils;
import com.bossien.train.util.PropertiesUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/7/31.
 */
@Service
public class ProjectRoleServiceImpl implements IProjectRoleService {
    @Autowired
    ProjectRoleMapper projectRoleMapper;
    @Autowired
    private ISequenceService sequenceService;
    @Override
    public ProjectRole build(User user) {
        ProjectRole projectRole = new ProjectRole();
        projectRole.setId(sequenceService.generator());
        projectRole.setCreateUser(user.getUserAccount());
        projectRole.setCreateTime(DateUtils.formatDateTime(new Date()));
        projectRole.setOperUser(user.getUserAccount());
        projectRole.setOperTime(DateUtils.formatDateTime(new Date()));
        return projectRole;
    }

    @Override
    public int saveBatch(User user, Map<String, Object> param) throws Exception{
        String projectId = param.get("projectId").toString();
        int count = 0;
        List<Map> roleList = (List<Map>)param.get("roleList");
        List<ProjectRole> item = new ArrayList<>();
        for (Map map: roleList){
            ProjectRole projectRole = this.build(user);
            projectRole.setProjectId(projectId);
            projectRole.setRoleId(map.get("roleId").toString());
            projectRole.setRoleName(map.get("roleName").toString());
            item.add(projectRole);
        }
        //项目角色对象为空不做添加
        if(item != null && item.size()>0){
            count = projectRoleMapper.insertBatch(item);
        }
        return count;
    }

    /**
     * 根据projectId查询受训角色
     * @param projectId
     * @return
     */
    @Override
    public List<ProjectRole> selectByProjectId(String projectId){
        return projectRoleMapper.selectByProjectId(projectId);
    }

    @Override
    public String selectRoleName(String projectId) {
        List<String> roleNames = projectRoleMapper.selectRoleName(projectId);
        String roleName = "";
        if(roleNames != null && roleNames.size()>0){
            for ( String roleName_: roleNames) {
                roleName +=(roleName==""?roleName_:(","+roleName_));
            }
        }
        return roleName;
    }
    /**
     * 根据projectId删除角色
     * @param map
     */
    @Override
    public int delete(Map<String,Object> map){
       return projectRoleMapper.delete(map);
    }

    /**
     * 修改项目角色
     * @param params
     */
    @Override
    public Integer updateProjectRole(User user,Map<String,Object> params){
        String roles = params.get("roles").toString();
        String projectId = params.get("projectId").toString();
        int count = 0;
        Class<Map> clazz = Map.class;
        List<Map> roleList = JsonUtils.joinToList(roles, clazz);
        //项目角色对象为空不做删除也不做添加
        if(roleList == null || roleList.size()==0){
            return count;
        }
        List<ProjectRole> item = new ArrayList<>();
        for (Map map: roleList){
            ProjectRole projectRole = this.build(user);
            projectRole.setProjectId(projectId);
            projectRole.setRoleId(map.get("id").toString());
            projectRole.setRoleName(map.get("text").toString());
            item.add(projectRole);
        }
        //先删除已有的数据
        this.delete(params);
        //批量添加数据
        count=projectRoleMapper.insertBatch(item);
        return  count;
    }

    @Override
    public int insert(Map<String, Object> params) {
        return projectRoleMapper.insert(params);
    }

    @Override
    public List<Map<String, Object>> selectProjectRole(Map<String, Object> params) {
        return projectRoleMapper.selectProjectRole(params);
    }

    @Override
    public List<String> selectRoleId(Map<String, Object> params) {
        return projectRoleMapper.selectRoleId(params);
    }

}
