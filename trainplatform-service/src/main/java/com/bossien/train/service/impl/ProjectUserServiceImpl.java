package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.ProjectUserMapper;
import com.bossien.train.dao.tp.UserMapper;
import com.bossien.train.domain.ProjectBasic;
import com.bossien.train.domain.ProjectUser;
import com.bossien.train.domain.User;
import com.bossien.train.service.IBaseService;
import com.bossien.train.service.IProjectInfoService;
import com.bossien.train.service.IProjectUserService;
import com.bossien.train.util.JsonUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("projectUserService")
public class ProjectUserServiceImpl implements IProjectUserService {
    protected static Logger LOG= LoggerFactory.getLogger(ProjectUserServiceImpl.class);

    @Autowired
    private ProjectUserMapper projectUserMapper;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private UserMapper userMapper;

    @Override
    public int deleteBatch(Map<String, Object> params) {
        return projectUserMapper.deleteBatch(params);
    }

    @Override
    public int selectCount(Map<String, Object> params) {
        return projectUserMapper.selectCount(params);
    }

    @Override
    public List<String> selectUserIds(Map<String, Object> params) {
        return projectUserMapper.selectUserIds(params);
    }

    @Override
    public List<Map<String, Object>> selectList(Map<String, Object> params) {
        List<Map<String, Object>> userList = projectUserMapper.selectList(params);
        return userList;
    }

    @Override
    public ProjectUser selectOne(Map<String, Object> params) {

        return projectUserMapper.selectOne(params);
    }

    /**
     * 根据projectId和UserId查询角色Id
     * @param projectId
     * @param userId
     * @return
     */
//    @Cacheable(value = "projectUserCache#(60 * 60)", key = "'selectByProjectIdAndUserId'.concat('_').concat(#projectId).concat('_').concat(#userId)")
    @Override
    public ProjectUser selectByProjectIdAndUserId( String projectId, String userId){
        return projectUserMapper.selectByProjectIdAndUserId(projectId,userId);
    }

    @Override
    public List<Map<String, Object>> selectProjectUser(Map<String, Object> params) {
        return projectUserMapper.selectProjectUser(params);
    }

    @Override
    public List<Map<String, Object>> saveBatch(Map<String, Object> param, User user) throws  Exception{
        ProjectBasic projectBasic = (ProjectBasic)param.get("projectBasic");
        String users = param.get("users").toString();
        Class<Map> clazz = Map.class;
        //获取用户集合
        List<Map> userList = JsonUtils.joinToList(users, clazz);
        //创建项目用户集合
        List<ProjectUser> item = new ArrayList<>();
        //根据项目Id查询项目用户的用户Id和用户名称
        Map<String, Object> map = new HashedMap();
        map.put("projectId",projectBasic.getId());
        List<String> uList = projectUserMapper.selectUserIds(map);
        if (uList == null || uList.size()==0) {
            uList = new ArrayList<>();
        }
        ProjectUser projectUser = null;
        List<Map<String, Object>> userList_ = new ArrayList<>();
        for (Map<String, Object> userMap: userList) {
            if(uList.contains(userMap.get("id").toString())){
                continue;
            }
            //创建项目用户对象
            projectUser = new ProjectUser();
            projectUser = (ProjectUser)baseService.build(user, projectUser);
            projectUser.setProjectId(projectBasic.getId());
            projectUser.setUserId(userMap.get("id").toString());
            projectUser.setUserName(userMap.get("userName").toString());
            projectUser.setRoleId(param.get("roleId").toString());
            projectUser.setRoleName(param.get("roleName").toString());
            projectUser.setDepartmentName(userMap.get("deptName").toString());
            //添加项目用户到项目用户集合
            item.add(projectUser);
            userList_.add(userMap);
        }
        if(item != null && item.size()>0){
            //批量添加项目用户信息
            projectUserMapper.insertBatch(item);
        }
        return userList_;
    }
    /**
     * 根据projectId删除
     * @param params
     * @return
     */
    @Override
    public int deleteByProjectId(Map<String, Object> params){
        return projectUserMapper.deleteByProjectId(params);
    }
    /**
     * 根据projectId和userId查询roleId
     * @param params
     * @return
     */
    @Override
    public String selectRoleId(Map<String, Object> params){
        return projectUserMapper.selectRoleId(params);
    }
    /**
     * 参与培训人次
     * @param params
     * @return
     */
    @Override
    public int selectUserCount(Map<String, Object> params){
       List<String> projectIds=projectInfoService.selectProjectIds(params);
       params.put("projectIds",projectIds);
        return projectUserMapper.selectUserCount(params);
    }

    @Override
    public List<String> selectProjectIdByUserId(Map<String, Object> params) {
        return projectUserMapper.selectProjectIdByUserId(params);
    }


    /**
     * 根据projectId和roleIds确认数据是否最新
     * @param params
     */
    @Override
    public void confirmProjectUser(Map<String, Object> params){
        List<String> ids=projectUserMapper.selectIds(params);
        if(ids!=null && ids.size()>0){
            for (String id:ids) {
                projectUserMapper.delete(id);
            }
        }

    }

    @Override
    public List<Map<String, Object>> selectProjectUserMap(Map<String, Object> params) {
        return projectUserMapper.selectProjectUserMap(params);
    }

    @Override
    public int update(Map<String, Object> params) {
        return projectUserMapper.update(params);
    }
    /**
     * 根据projectId查询项目用户
     * @param params
     * @return
     */
    @Override
   public List<ProjectUser> selectProjectUsers(Map<String, Object> params){
      return   projectUserMapper.selectProjectUsers(params);
    }

    @Override
    public int insertBatch(List<ProjectUser> item) {
        return projectUserMapper.insertBatch(item);
    }

    @Override
    public List<Map<String, Object>> selectUsers(String projectId) {
        return projectUserMapper.selectUsers(projectId);
    }
}
