package com.bossien.train.service.impl;

import com.bossien.train.domain.*;
import com.bossien.train.service.*;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.JsonUtils;
import com.bossien.train.util.PropertiesUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by A on 2017/9/4.
 */
@Service
public class ProjectCustomServiceImpl implements IProjectCustomService{

    private String containExamType = PropertiesUtils.getValue("contain_exam_type");

    private String containExerciseType = PropertiesUtils.getValue("contain_exercise_type");

    @Autowired
    private ITrainRoleService trainRoleService;

    @Autowired
    private IProjectCourseService projectCourseService;

    @Autowired
    private IProjectUserService projectUserService;
    @Autowired
    private ICompanyTrainRoleService companyTrainRoleService;

    @Autowired
    private IProjectRoleService projectRoleService;

    @Autowired
    private IBaseService baseService;

    @Autowired
    private ISequenceService sequenceService;

    @Autowired
    private IExamStrategyService examStrategyService;

    @Autowired
    private IProjectStatisticsInfoService projectStatisticsInfoService;

    @Autowired
    private IProjectExerciseOrderService projectExerciseOrderService;

    @Autowired
    private IProjectCourseInfoService projectCourseInfoService;

    @Override
    public List<Map<String, Object>> selectCourseAndRole(Map<String, Object> params) {
        List<Map<String, Object>> projectCourseRoles = projectCourseService.selectProjectCourseList(params);
        return projectCourseRoles;
    }

    @Override
    public List<Map<String, Object>> selectUserAndRole(Map<String, Object> params) {
       // Map<String, Object> roleAndUser = new HashedMap();
        List<Map<String, Object>> projectUsers = projectUserService.selectProjectUserMap(params);
       // roleAndUser.put("projectUsers", projectUsers);
        return projectUsers ;
    }


    @Override
    public int saveProjectRoleCourse(Map<String, Object> params) {
        String roles = params.get("roles").toString();
        String courses = params.get("courses").toString();
        String projectId = params.get("projectId").toString();
        String projectType = params.get("projectType").toString();
        User user = (User)params.get("user");
        Class<Map> clazz = Map.class;
        //获取角色集合
        List<Map> roleList = JsonUtils.joinToList(roles, clazz);
        //获取角色课程集合
        for (Map roleMap: roleList) {
            roleMap.put("projectId", projectId);
            if(StringUtils.contains(containExamType,projectType)){
                //带有考试的项目就保存组卷策略
                saveExamStrategy(roleMap,user);
            }
        }
        Map<String, Object> param = new HashedMap();
        param.put("projectId",projectId);
        param.put("roleId",PropertiesUtils.getValue("role_id"));
        //保存项目课程信息
        saveProjectCourse(param, courses, user);
        //查询项目用户是否有默认角色用户，如果没有就删除项目课程中默认角色课程
       //deleteProjectCourseAndExamStrategy(param, projectType);
        return 0;
    }

    @Override
    public int updateProjectRoleUser(Map<String, Object> params) {
        String roleUsers = params.get("roleUsers").toString();
        String roles = params.get("roles").toString();
        String projectId = params.get("projectId").toString();
        String projectType = params.get("projectType").toString();
        User user = (User)params.get("user");
        Class<Map> clazz = Map.class;
        //获取角色用户集合
        List<Map> roleUserList = JsonUtils.joinToList(roleUsers, clazz);
        //获取角色集合
        List<Map> roleList = JsonUtils.joinToList(roles, clazz);
        for (Map roleMap: roleList) {
            Map<String, Object> param = new HashedMap();
            String roleId = roleMap.get("roleId").toString();
            List<String> userIds = new ArrayList<>();
            for (Map roleUserMap : roleUserList) {
                String roleId_ = roleUserMap.get("roleId").toString();
                String userId = roleUserMap.get("userId").toString();
                if(roleId_.equals(roleId)){
                    userIds.add(userId);
                    //roleUserList.remove(roleUserMap);
                }
            }
            param.put("userIds", userIds);
            param.put("roleId", roleId);
            param.put("projectId", projectId);
            param.put("roleName", roleMap.get("roleName").toString());
            param.put("operUser", user.getUserName());
            param.put("operTime", DateUtils.formatDateTime(new Date()));
            //修改项目用户信息
            projectUserService.update(param);
        }
    /*    Map<String, Object> param_ = new HashedMap();
        param_.put("projectId",projectId);
        param_.put("roleId","-1");
        //查询项目用户是否有默认角色用户，如果没有就删除项目课程中默认角色课程
       deleteProjectCourseAndExamStrategy(param_, projectType);*/
        return 0;
    }

    private void deleteProjectCourseAndExamStrategy(Map<String, Object> param, String projectType) {
        List<Map<String, Object>> projectUser = projectUserService.selectProjectUser(param);
        if(projectUser == null || projectUser.size()<1){
            //projectCourseService.deleteBatch(param);
            if(StringUtils.contains(containExamType,projectType)){
                //删除默认角色组卷策略
                examStrategyService.deleteByProjectId(param);
            }
        }
    }

    @Override
    public int updateRoleOrCourseInfo(Map<String, Object> param) {
        String roleCourses = param.get("roleCourses").toString();
        String projectId = param.get("projectId").toString();
        String projectType = param.get("projectType").toString();
        String userName = param.get("userName").toString();
        Class<Map> clazz = Map.class;
        List<Map> roleList = JsonUtils.joinToList(roleCourses, clazz);
        for (Map roleCourseMap: roleList) {
            roleCourseMap.put("projectId", projectId);
            List<Map<String, Object>> projectUser = projectUserService.selectProjectUser(roleCourseMap);
            if(projectUser != null && projectUser.size()>0){
                //修改项目课程详细信息
                updateProjectCourseInfo(roleCourseMap, userName);
                //修改个人项目档案统计信息
                updateProjectStatisticsInfo(roleCourseMap, userName);
                if(StringUtils.contains(containExerciseType,projectType)){
                    //带有练习的项目就修改个人练习统计信息
                    updateProjectExerciseInfo(roleCourseMap, userName);
                }
            }
            if(StringUtils.contains(containExamType,projectType)){
                //修改组卷策略的题量及学时
                ExamStrategy examStrategy = new ExamStrategy();
                examStrategy.setProjectId(projectId);
                examStrategy.setRoleId(roleCourseMap.get("roleId").toString());
                User user = new User();
                user.setUserName(userName);
                examStrategyService.updateExamStrategy(examStrategy, roleCourseMap, user);
            }
        }
        return 0;
    }

    private void updateProjectExerciseInfo(Map roleCourseMap, String userName) {
        //获取该角色下所有课程的应修学时，总题量
        Map<String, Object> params = projectCourseService.selectProjectCourseMap(roleCourseMap);
        if(params != null) {
            ProjectExerciseOrder projectExerciseOrder = new ProjectExerciseOrder();
            projectExerciseOrder.setProjectId(roleCourseMap.get("projectId").toString());
            projectExerciseOrder.setRoleId(roleCourseMap.get("roleId").toString());
            projectExerciseOrder.setRoleName(roleCourseMap.get("roleName").toString());
            projectExerciseOrder.setTotalQuestion(Integer.parseInt(params.get("totalQuestion").toString()));
            projectExerciseOrder.setOperUser(userName);
            projectExerciseOrder.setOperTime(DateUtils.formatDateTime(new Date()));
            //修改个人练习统计信息
            projectExerciseOrderService.update(projectExerciseOrder);
        }
    }

    @Override
    public int updateRoleOrUserInfo(Map<String, Object> param) {
        String roleUsers = param.get("roleUsers").toString();
        String roles = param.get("roles").toString();
        String projectId = param.get("projectId").toString();
        String projectType= param.get("projectType").toString();
        String userName = param.get("userName").toString();
        Class<Map> clazz = Map.class;
        //获取角色用户集合
        List<Map> roleUserList = JsonUtils.joinToList(roleUsers, clazz);
        //获取角色集合
        List<Map> roleList = JsonUtils.joinToList(roles, clazz);
        for (Map roleMap: roleList) {
            Map<String, Object> params = new HashedMap();
            String roleId = roleMap.get("roleId").toString();
            roleMap.put("projectId", projectId);
            updateProjectCourseInfo(roleMap, userName);
            List<String> userIds = new ArrayList<>();
            for (Map roleUserMap : roleUserList) {
                String roleId_ = roleUserMap.get("roleId").toString();
                String userId = roleUserMap.get("userId").toString();
                if (roleId_.equals(roleId)) {
                    userIds.add(userId);
                    //roleUserList.remove(roleUserMap);
                }
            }
            params.put("roleId", roleId);
            params.put("projectId", projectId);
            //获取总学时，总题量
           Map<String, Object> param_ = projectCourseService.selectProjectCourseMap(params);
           if(param_ != null){
               params.put("requirementStudytime",Long.parseLong(param_.get("requirementStudytime").toString()));
               params.put("totalQuestion", Integer.parseInt(param_.get("totalQuestion").toString()));
           }
            params.put("userIds",userIds);
            params.put("roleName",roleMap.get("roleName").toString());
            params.put("operUser", userName);
            params.put("operTime", DateUtils.formatDateTime(new Date()));
            //修改项目个人档案统计信息
            projectStatisticsInfoService.updateInfo(params);
            if(StringUtils.contains(containExerciseType,projectType)){
                //修改项目个人练习统计信息
                projectExerciseOrderService.updateInfo(params);
            }
        }
        return 0;
    }

    private void updateProjectCourseInfo(Map roleMap, String userName) {
        //获取项目课程信息
        List<Map<String, Object>> projectCourseList = projectCourseService.selectProjectCourseInfo(roleMap);
        for (Map<String, Object> projectCourse: projectCourseList) {
            ProjectCourseInfo projectCourseInfo = new ProjectCourseInfo();
            projectCourseInfo.setProjectId(projectCourse.get("projectId").toString());
            projectCourseInfo.setCourseId(projectCourse.get("courseId").toString());
            projectCourseInfo.setRequirementStudyTime(Long.parseLong(projectCourse.get("requirementStudytime").toString()));
            projectCourseInfo.setTotalQuestion(Integer.parseInt(projectCourse.get("totalQuestion").toString()));
            projectCourseInfo.setOperUser(userName);
            projectCourseInfo.setOperTime(DateUtils.formatDateTime(new Date()));
            //修改项目课程详细信息
            projectCourseInfoService.update(projectCourseInfo);
        }
    }

    private void updateProjectStatisticsInfo(Map roleCourseMap, String userName) {
        //获取该角色下所有课程的应修学时，总题量
        Map<String, Object> params = projectCourseService.selectProjectCourseMap(roleCourseMap);
        if(params != null) {
            ProjectStatisticsInfo projectStatisticsInfo = new ProjectStatisticsInfo();
            projectStatisticsInfo.setProjectId(roleCourseMap.get("projectId").toString());
            projectStatisticsInfo.setRoleId(roleCourseMap.get("roleId").toString());
            projectStatisticsInfo.setRoleName(roleCourseMap.get("roleName").toString());
            projectStatisticsInfo.setRequirementStudyTime(Long.parseLong(params.get("requirementStudytime").toString()));
            projectStatisticsInfo.setTotalQuestion(Integer.parseInt(params.get("totalQuestion").toString()));
            projectStatisticsInfo.setOperUser(userName);
            projectStatisticsInfo.setOperTime(DateUtils.formatDateTime(new Date()));
            //修改个人项目档案统计信息
            projectStatisticsInfoService.update(projectStatisticsInfo);
        }
    }

    private void saveExamStrategy(Map roleMap, User user) {
        //保存时先删除该考试组卷策略
        examStrategyService.deleteByProjectId(roleMap);
        roleMap.put("userName",user.getUserName());
        roleMap.put("necessaryHour",0);
        roleMap.put("singleAllCount",0);
        roleMap.put("manyAllCount",0);
        roleMap.put("judgeAllCount",0);
        //保存组卷策略
        examStrategyService.insert(roleMap);
    }

    private void saveProjectCourse(Map<String, Object> param, String courses, User user) {
        //保存时先删除该项目角色下的课程
        projectCourseService.deleteProjectId(param);
        Class<ProjectCourse> clazz = ProjectCourse.class;
        List<ProjectCourse> item = JsonUtils.joinToList(courses, clazz);
        for (ProjectCourse projectCourse: item) {
            projectCourse.setId(sequenceService.generator());
            projectCourse.setCreateUser(user.getUserName());
            projectCourse.setCreateTime(DateUtils.formatDateTime(new Date()));
            projectCourse.setOperUser(user.getUserName());
            projectCourse.setOperTime(DateUtils.formatDateTime(new Date()));
        }
        if(item != null && item.size()>0){
            //批量添加项目课程信息
            projectCourseService.insertBatch(item);
        }
    }

    private void saveProjectRole(Map roleMap, User user) {
        //添加之前先删除
        projectRoleService.delete(roleMap);
        //保存项目角色
        Map<String, Object> params = baseService.buildMap(user);
        params.put("projectId",roleMap.get("projectId").toString());
        params.put("roleId",roleMap.get("roleId").toString());
        params.put("roleName",roleMap.get("roleName").toString());
        projectRoleService.insert(params);
    }
}
