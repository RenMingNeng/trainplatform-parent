package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.ProjectCourseMapper;
import com.bossien.train.dao.tp.ProjectStatisticsInfoMapper;
import com.bossien.train.dao.tp.UserMapper;
import com.bossien.train.domain.*;
import com.bossien.train.domain.eum.ProjectStatusEnum;
import com.bossien.train.domain.eum.ProjectTypeEnum;
import com.bossien.train.service.*;
import com.bossien.train.util.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

/**
 * Created by Administrator on 2017/7/31.
 */

@Service
public class CreatePrivateProjectServiceImpl implements ICreatePrivateProjectService {

    private String containExamType = PropertiesUtils.getValue("contain_exam_type");

    private String containExerciseType = PropertiesUtils.getValue("contain_exercise_type");

    @Autowired
    private IProjectBasicService projectBasicService;
    @Autowired
    private IProjectRoleService projectRoleService;
    @Autowired
    private IProjectDepartmentService projectDepartmentService;
    @Autowired
    private IProjectUserService projectUserService;
    @Autowired
    private IProjectCourseService projectCourseService;
    @Autowired
    private IExamStrategyService examStrategyService;
    @Autowired
    private IProjectCourseInfoService projectCourseInfoService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private ICourseInfoService courseInfoService;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private ICompanyProjectService companyProjectService;
    @Autowired
    private ISequenceService sequenceService;
    @Autowired
    private IProjectStatisticsInfoService projectStatisticsInfoService;
    @Autowired
    private IProjectExerciseOrderService projectExerciseOrderService;
    @Autowired
    private IProjectDossierService projectDossierService;
    @Autowired
    private ProjectCourseMapper projectCourseMapper;
    @Autowired
    private ICourseQuestionService courseQuestionService;
    @Autowired
    private IQuestionService questionService;
    @Autowired
    private ISubjectCourseService subjectCourseService;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IUserManagerService userManagerService;
    @Autowired
    private IExamQuestionService examQuestionService;
    @Autowired
    private IExamScoreService examScoreService;
    @Autowired
    private IExamPaperInfoService examPaperInfoService;
    @Autowired
    private ICompanyCourseService companyCourseService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProjectStatisticsInfoMapper projectStatisticsInfoMapper;
    @Override
    public ProjectBasic saveProjectBasicInfo(Map<String, Object> param, User user) throws Exception{
        ProjectBasic projectBasic = (ProjectBasic)param.get("projectBasic");
        String projectId = projectBasic.getId();
        String projectType = projectBasic.getProjectType();
        String departments = param.get("departments").toString();
        param.put("projectId",projectId);
        //保存项目基础信息
        projectBasicService.save(projectBasic);
        //保存公司项目信息
        saveCompanyProject(projectBasic, user);
        //departments不为空保存项目部门信息
        if(StringUtils.isNotEmpty(departments)){
            //保存项目部门信息
            projectDepartmentService.saveBatchProjectDepartment(user, param);
        }
        //主题不为空时保存项目课程信息
        if(StringUtils.isNotEmpty(projectBasic.getSubjectId())){
            insertCourseInfo(projectBasic, user);
        }
        //初始化项目角色信息
        saveProjectRole(projectId, user);
        if(StringUtils.contains(containExamType,projectType)){
            //带考试的项目就初始化组卷策略
            Map<String, Object> params = new HashedMap();
            params.put("projectId", projectId);
            params.put("userName", user.getUserName());
            params.put("roleId", PropertiesUtils.getValue("role_id"));
            params.put("roleName", "默认角色");
            saveExamStrategy(params);
        }
        return projectBasic;
    }

    private void saveBatchProjectUser(List<Map> depList, String projectId, User user){
        Map<String, Object> params = new HashedMap();
        List<String> ids = new ArrayList<String>();
        Map<String, String> deptIdName = new HashedMap();
        for (Map map: depList) {
            String deptId = map.get("id").toString();
            ids.add(deptId);
            deptIdName.put(deptId, map.get("text").toString());
        }
        params.put("deptIds",ids);
        params.put("isValid","1");
        List<Map<String, Object>> userList = userMapper.selectUserAndDeptId(params);
        List<ProjectUser> item = new ArrayList<ProjectUser>();
        params.clear();
        params.put("projectId",projectId);
        List<String> projectUserIds = projectUserService.selectUserIds(params);
        if(projectUserIds == null || projectUserIds.size() == 0){
            projectUserIds = new ArrayList<>();
        }
        ProjectUser projectUser = null;
        for (Map<String, Object> userMap: userList) {
            if(projectUserIds.contains(userMap.get("userId").toString())){
                continue;
            }
            //创建项目用户对象
            projectUser = new ProjectUser(projectId,userMap.get("userId").toString(),userMap.get("userName").toString(),
                    PropertiesUtils.getValue("role_id"),"默认角色",
                    userMap.get("deptId") == null ? "" : deptIdName.get(userMap.get("deptId").toString()));
            projectUser = (ProjectUser)baseService.build(user, projectUser);
            //添加项目用户到项目用户集合
            item.add(projectUser);
        }
        if(item != null && item.size()>0){
            //批量添加项目用户信息
            List<List<ProjectUser>> projectUserList = StringUtil.subList(item, 1000);
            Iterator<List<ProjectUser>> it = projectUserList.iterator();
            while (it.hasNext()) {
                List<ProjectUser> projectUsers = it.next();
                projectUserService.insertBatch(projectUsers);
            }
        }
    }

    private String getDepartmentName(Map map){
        String departmentName = map.get("text").toString();
        map.put("isValid","1");
        Department department=departmentService.selectOne(map);
        Map<String, Object> params = new HashedMap();
        if(department != null){
            params.put("id",department.getParentId());
            params.put("isValid",department.getIsValid());
        }
        String deptName = departmentService.getParentDepartmentName(params,departmentName);
        if(StringUtils.isNotEmpty(deptName)){
            return deptName;
        }
        return departmentName;
    }


    private List<Map<String,Object>> getDeptList(Map map, String projectId) {
        String deptId = map.get("id").toString();
        String departmentName = map.get("text").toString();
        map.put("isValid","1");
        Department department=departmentService.selectOne(map);
        Map<String, Object> params = new HashedMap();
        if(department != null){
            params.put("id",department.getParentId());
            params.put("isValid",department.getIsValid());
        }
        String deptName = departmentService.getParentDepartmentName(params,departmentName);
        params.put("deptId",deptId);
        params.put("isValid","1");
        List<Map<String, Object>> userList = userManagerService.selectUserByDeptId(params);
        List<Map<String, Object>> projectUsers = projectStatisticsInfoService.selectUsers(projectId);
        if(userList != null && userList.size()>0 && projectUsers != null && projectUsers.size()>0){
            userList.removeAll(projectUsers);
        }
        for (Map<String, Object> userMap: userList) {
            userMap.put("deptName",deptName);
        }
        return userList;
    }

    private void insertCourseInfo(ProjectBasic projectBasic, User user) {
        //根据主题Id查询课程Ids集合
        List<String> courseIds_ = subjectCourseService.selectCourseIds(projectBasic.getSubjectId());
        if(courseIds_ != null && courseIds_.size()>0){
            Map<String, Object> map = new HashedMap();
            map.put("companyId", user.getCompanyId());
            map.put("courseIds", courseIds_);
            List<String> courseIds = companyCourseService.selectCourseIds(map);
            if(courseIds != null && courseIds.size()>0){
                Map<String, Object> coursesMap = new HashedMap();
                coursesMap.put("courseids",courseIds);
                //获取课程信息集合
                List<CourseInfo> courseList = courseInfoService.selectCourseByIds(coursesMap);
                //创建项目课程集合
                List<ProjectCourse> item = new ArrayList<>();
                for (CourseInfo courseInfo: courseList) {
                    //创建项目课程
                    ProjectCourse projectCourse = new ProjectCourse();
                    projectCourse = (ProjectCourse)baseService.build(user, projectCourse);
                    projectCourse.setProjectId(projectBasic.getId());
                    projectCourse.setCourseId(courseInfo.getCourseId());
                    projectCourse.setCourseNo(courseInfo.getCourseNo());
                    projectCourse.setCourseName(courseInfo.getCourseName());
                    projectCourse.setRoleId(PropertiesUtils.getValue("role_id"));
                    projectCourse.setRoleName("默认角色");
                    projectCourse.setRequirement(courseInfo.getClassHour());
                    projectCourse.setClassHour(courseInfo.getClassHour());
                    projectCourse.setQuestionCount(courseInfo.getQuestionCount());
                    projectCourse.setSelectCount(0);
                    //添加项目课程到集合
                    item.add(projectCourse);
                }
                if(item != null && item.size() > 0){
                    //批量插入项目课程
                    projectCourseMapper.insertBatch(item);
                }
            }
        }
    }

    private void saveProjectRole(String projectId, User user) {
        Map<String, Object> params = baseService.buildMap(user);
        params.put("projectId",projectId);
        params.put("roleId",PropertiesUtils.getValue("role_id"));
        params.put("roleName","默认角色");
        //保存项目角色信息
        projectRoleService.insert(params);
    }

    private void saveExamStrategy(Map<String, Object> params) {
        params.put("necessaryHour",0);
        params.put("singleAllCount",0);
        params.put("manyAllCount",0);
        params.put("judgeAllCount",0);
        //保存组卷策略
        examStrategyService.insert(params);
    }

    private void saveCompanyProject(ProjectBasic projectBasic, User user) {
        Map<String, Object> params = new HashedMap();
        params.put("projectId",projectBasic.getId());
        params.put("companyId",user.getCompanyId());
        params.put("create_user",user.getUserName());
        params.put("create_time",DateUtils.formatDateTime(new Date()));
        params.put("oper_user",user.getUserName());
        params.put("oper_time",DateUtils.formatDateTime(new Date()));
        companyProjectService.insert(params);
    }

    @Override
    public void saveProjectInfo(Map<String, Object> param, User user) throws Exception{
        //保存项目用户信息
        projectUserService.saveBatch(param, user);
    }

    @Override
    public List<String> saveProjectCourseInfo(Map<String, Object> params, User user) throws Exception{
        //保存项目课程信息
        List<String> courseIds = projectCourseService.saveBatch(params, user);
        return courseIds;
    }

    @Override
    public void deleteProjectCourse(Map<String, Object> param) {
         projectCourseService.deleteBatchByCourseIds(param);
    }

    @Override
    public void deleteProjectUser(Map<String, Object> param) {
        String projectStatus = param.get("projectStatus").toString();
        String projectType = param.get("projectType").toString();
        if(ProjectStatusEnum.ProjectStatus_3.getValue().equals(projectStatus)){
            if(StringUtils.contains(containExamType,projectType)){
                //批量删除用户考试试题
                examQuestionService.deleteBatch(param);
                //批量删除用户考试成绩
                examScoreService.deleteBatch(param);
            }
        }
        //批量删除项目用户
        projectUserService.deleteBatch(param);
    }

    @Override
    public void saveProjectByCourse(Map<String, Object> param) {
        String projectId = param.get("id").toString();
        //得到角色集合
        List<Map> roleList = (List<Map>)param.get("roleList");
        //获取课程Id集合
        List<String> courseList = (List<String>)param.get("courseList");
        String projectType = param.get("projectType").toString();
        for (Map roleMap: roleList) {
            roleMap.put("projectId", projectId);
            //从项目用户中获取项目用户
           List<Map<String, Object>> projectUsers =  projectUserService.selectProjectUser(roleMap);
            if(projectUsers == null || projectUsers.size() == 0){
                continue;
            }
           //根据角色Id和项目Id获取所有课程的总学时和总题量
           Map<String, Object> courseMap = projectCourseService.selectProjectCourseMap(roleMap);
           //获取项目详细信息
           ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(projectId);
           //保存个人学时统计信息
           List<Map<String, Object>> slist = projectStatisticsInfoService.selectStatisticsInfoList(roleMap);
           if(slist != null && slist.size()>0){
               //修改个人学时统计信息
               ProjectStatisticsInfo projectStatisticsInfo = new ProjectStatisticsInfo();
               projectStatisticsInfo.setProjectId(projectId);
               projectStatisticsInfo.setRoleId(roleMap.get("roleId").toString());
               projectStatisticsInfo.setRequirementStudyTime(Long.valueOf(courseMap.get("requirementStudytime").toString()));
               projectStatisticsInfo.setTotalQuestion(Integer.valueOf(courseMap.get("totalQuestion").toString()));
               projectStatisticsInfo.setOperTime(DateUtils.formatDateTime(new Date()));
               projectStatisticsInfoService.update(projectStatisticsInfo);
           }else{
               enterProjectStatisticsInfo(roleMap, projectUsers, courseMap, projectInfo);
           }
           if(StringUtils.contains(containExerciseType,projectType)){
               List<Map<String, Object>> elist = projectExerciseOrderService.selectExerciseInfoList(roleMap);
               if(elist != null && elist.size()>0){
                   //修改个人练习记录
                   ProjectExerciseOrder projectExerciseOrder = new ProjectExerciseOrder();
                   projectExerciseOrder.setProjectId(projectId);
                   projectExerciseOrder.setRoleId(roleMap.get("roleId").toString());
                   projectExerciseOrder.setTotalQuestion(Integer.valueOf(courseMap.get("totalQuestion").toString()));
                   projectExerciseOrder.setNotAnswered(Integer.valueOf(courseMap.get("totalQuestion").toString()));
                   projectExerciseOrder.setOperTime(DateUtils.formatDateTime(new Date()));
                   projectExerciseOrderService.update(projectExerciseOrder);
               }else{
                   //保存个人练习记录
                   saveProjectExerciseInfo(roleMap, projectUsers, courseMap, projectInfo);
               }
           }

           //保存项目课程详细信息
           enterProjectCourseInfoSave(projectUsers, roleMap, courseList, projectId);
        }
    }

    @Override
    public void saveProjectByUser(Map<String, Object> param) {
        //得到用户集合
        param.put("projectId", param.get("id").toString());
        Integer personCount = projectUserService.selectCount(param);
        Map<String, Object> param_ = new HashedMap();
        param_.put("projectId",param.get("projectId").toString());
        param_.put("personCount",personCount.toString());
        param_.put("operTime", DateUtils.formatDateTime(new Date()));
        //修改项目详情的人员数量
        projectInfoService.update(param_);
        //修改项目档案的人员数量
        projectDossierService.update(param_);
        //获取项目课程信息
        List<Map<String, Object>> item = projectCourseService.selectProjectCourseInfo(param);
        if(item == null || item.size()==0){
            return;
        }
        //获取用户集合
        List<Map<String, Object>> userList = (List<Map<String, Object>>)param.get("userList");
        List<Map<String, Object>> projectUsers = new ArrayList<>();
        List<String> userIds = new ArrayList<>();
        //从项目用户详情信息表中查询用户Id集合
        Map<String, Object> map = null;
        for (Map userMap: userList) {
            map = new HashedMap();
            map.put("userId", userMap.get("id"));
            map.put("userName", userMap.get("userName"));
            map.put("deptName", userMap.get("deptName"));
            userIds.add(userMap.get("id").toString());
            projectUsers.add(map);
        }
        //根据角色Id和项目Id获取所有课程的总学时和总题量
        Map<String, Object> courseMap = projectCourseMapper.selectProjectCourseMap(param);
        //获取项目详细信息
        ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(param.get("projectId").toString());
        //保存个人学时统计信息
        enterProjectStatisticsInfo(param, projectUsers, courseMap, projectInfo);
        String projectType = param.get("projectType").toString();
        if(StringUtils.contains(containExerciseType,projectType)){
            //保存个人练习记录
            saveProjectExerciseInfo(param, projectUsers, courseMap, projectInfo);
        }
        List<Map<String, Object>> items = new ArrayList<>();
        for (Map<String, Object> maps: item) {
            items = getProjectCourseInfo(items,userIds,map,maps);
        }
        //保存项目课程详细信息
        List<List<Map<String, Object>>> lists = CollectionUtils.subList(items,1000);
        for (List<Map<String, Object>>  list : lists) {
            projectCourseInfoService.insertBatch(list);
        }
        System.out.println("--------------------");
    }

    @Override
    public int deleteProjectByCourse(Map<String, Object> param) {
        int count = 0;
        String projectId = param.get("id").toString();
        String projectType = param.get("projectType").toString();
        //获取角色Id集合
        List<String> roleList = projectCourseService.selectRoleIds(projectId);
        //获取课程Id集合
        List<String> courseIds = (List<String> )param.get("courseIds");
        for(String roleId : roleList){
            Map<String, Object> params = new HashedMap();
            params.put("projectId",projectId);
            params.put("roleId",roleId);
            params.put("courseIds",courseIds);
            //修改组卷策略信息
            ExamStrategy examStrategy = new ExamStrategy();
            User user = new User();
            examStrategy.setProjectId(projectId);
            examStrategy.setRoleId(roleId);
            user.setUserName(param.get("userName").toString());
            examStrategyService.updateExamStrategy(examStrategy, params, user);
            //从项目用户中获取项目用户
            List<Map<String, Object>> projectUsers =  projectUserService.selectProjectUser(params);
            if(projectUsers == null || projectUsers.size()==0) {
                return count;
            }
            //批量删除项目课程详细信息
            projectCourseInfoService.deleteBatch(params);
            //根据角色Id和项目Id获取所有课程的总学时和总题量
            Map<String, Object> courseMap = projectCourseService.selectProjectCourseMap(params);
            //修改个人学时统计信息
            ProjectStatisticsInfo projectStatisticsInfo = new ProjectStatisticsInfo();
            projectStatisticsInfo.setProjectId(projectId);
            projectStatisticsInfo.setRoleId(roleId);
            projectStatisticsInfo.setRequirementStudyTime(
                    Long.valueOf(courseMap.get("requirementStudytime").toString()));
            projectStatisticsInfo
                    .setTotalQuestion(Integer.valueOf(courseMap.get("totalQuestion").toString()));
            projectStatisticsInfo.setOperTime(DateUtils.formatDateTime(new Date()));
            projectStatisticsInfoService.update(projectStatisticsInfo);
            if (!StringUtils.contains(containExerciseType,projectType)) {
               continue;
            }
            List<Map<String, Object>> elist = projectExerciseOrderService
                    .selectExerciseInfoList(params);
            if (elist != null && elist.size() > 0) {
                //修改个人练习记录
                ProjectExerciseOrder projectExerciseOrder = new ProjectExerciseOrder();
                projectExerciseOrder.setProjectId(projectId);
                projectExerciseOrder.setRoleId(roleId);
                projectExerciseOrder.setTotalQuestion(
                        Integer.valueOf(courseMap.get("totalQuestion").toString()));
                projectExerciseOrder.setNotAnswered(Integer.valueOf(courseMap.get("totalQuestion").toString()));
                projectExerciseOrder.setOperTime(DateUtils.formatDateTime(new Date()));
                projectExerciseOrderService.update(projectExerciseOrder);
            }
        }
        count = 1;
        return count;
    }

    @Override
    public int deleteProjectByUser(Map<String, Object> param) {
        int count = 0;
        String projectId = param.get("id").toString();
        String projectType = param.get("projectType").toString();
        String projectStatus = param.get("projectStatus").toString();
        //获取用户Id集合
        List<String> userIds = (List<String> )param.get("userIds");
        Map<String, Object> params = new HashedMap();
        params.put("projectId", projectId);
        params.put("userIds", userIds);
        //批量删除项目课程详情信息
        projectCourseInfoService.deleteBatch(params);
        //批量删除个人统计信息
        projectStatisticsInfoService.delete(params);
        //批量删除个人练习信息
        if (StringUtils.contains(containExerciseType,projectType)) {
        projectExerciseOrderService.delete(params);
        }
        if(ProjectStatusEnum.ProjectStatus_3.getValue().equals(projectStatus)){
            if(StringUtils.contains(containExamType,projectType)){
                //批量删除用户考试详细信息
                examPaperInfoService.deleteBatch(params);
            }
        }
        Integer personCount = projectUserService.selectCount(params);
        Map<String, Object> param_ = new HashedMap();
        param_.put("projectId",projectId);
        param_.put("personCount",personCount.toString());
        param_.put("operTime", DateUtils.formatDateTime(new Date()));
        //修改项目详情的人员数量
        projectInfoService.update(param_);
        //修改项目档案的人员数量
        projectDossierService.update(param_);
        count = 1;
        return count;
    }

    @Override
    public int updateInfo(Map<String, Object> params) throws ParseException {
        int count = 0;
        //更新项目基础信息
        projectBasicService.update(params);
        //更新项目详情信息
        ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(params.get("projectId").toString());
        String projectStatus = params.get("projectStatus").toString();
        if(projectInfo != null){
            String beginTime = projectInfo.getProjectStartTime();
            String endTime = projectInfo.getProjectEndTime();
            params.put("projectStatus", DateUtils.getProjectStatus(beginTime, endTime, projectStatus));
        }
        projectInfoService.update(params);
        count = 1;
        return count;
    }

    private void saveProjectExerciseInfo(Map roleMap, List<Map<String, Object>> projectUsers,
        Map<String, Object> courseMap, ProjectInfo projectInfo) {
        List<Map<String, Object>> item = new ArrayList<>();
        Map<String, Object> params = null;
        for (Map<String, Object> userMap: projectUsers) {
            params = new HashedMap();
            params.put("id", sequenceService.generator());
            params.put("projectId", projectInfo.getId());
            params.put("userId", userMap.get("userId").toString());
            params.put("userName", userMap.get("userName").toString());
            params.put("roleId", roleMap.get("roleId").toString());
            params.put("roleName", roleMap.get("roleName").toString());
            params.put("deptName", userMap.get("deptName").toString());
            params.put("totalQuestion", courseMap.get("totalQuestion"));
            params.put("answerStudytime", 0);
            params.put("yetAnswered", 0);
            params.put("notAnswered", courseMap.get("totalQuestion"));
            params.put("correctAnswered", 0);
            params.put("failAnswered", 0);
            params.put("correctRate", 0);
            params.put("createUser", projectInfo.getCreateUser());
            params.put("createTime", DateUtils.formatDateTime(new Date()));
            params.put("operUser", projectInfo.getOperUser());
            params.put("operTime", DateUtils.formatDateTime(new Date()));
            item.add(params);
        }
        //批量保存个人练习详情信息
        List<List<Map<String, Object>>> lists = CollectionUtils.subList(item,1000);
        for (List<Map<String, Object>>  list : lists) {
            projectExerciseOrderService.insertBatch(list);
        }
    }

    private void enterProjectStatisticsInfo(Map<String, Object> roleMap, List<Map<String, Object>> projectUsers, Map<String, Object> courseMap,
        ProjectInfo projectInfo) {
        List<Map<String, Object>> item = new ArrayList<>();
        Map<String, Object> params = null;
        for (Map<String, Object> userMap: projectUsers) {
            params = new HashedMap();
            params.put("id", sequenceService.generator());
            params.put("projectId", projectInfo.getId());
            params.put("projectName", projectInfo.getProjectName());
            params.put("projectStartTime", projectInfo.getProjectStartTime());
            params.put("projectEndTime", projectInfo.getProjectEndTime());
            params.put("userId", userMap.get("userId").toString());
            params.put("userName", userMap.get("userName").toString());
            params.put("roleId", roleMap.get("roleId").toString());
            params.put("roleName", roleMap.get("roleName").toString());
            params.put("deptName", userMap.get("deptName").toString());
            params.put("requirementStudytime", courseMap.get("requirementStudytime"));
            params.put("examTimeInfo", projectInfo.getProjectExamTime());
            params.put("totalQuestion", courseMap.get("totalQuestion"));
            params.put("createUser", projectInfo.getCreateUser());
            params.put("createTime", DateUtils.formatDateTime(new Date()));
            params.put("operUser", projectInfo.getOperUser());
            params.put("operTime", DateUtils.formatDateTime(new Date()));
            item.add(params);
        }
        //批量保存个人学时统计信息
        List<List<Map<String, Object>>> lists = CollectionUtils.subList(item,1000);
        for (List<Map<String, Object>>  list : lists) {
            projectStatisticsInfoService.insertBatch(list);
        }
    }

    private void enterProjectCourseInfoSave(List<Map<String, Object>> projectUsers, Map roleMap, List<String> courseList, String projectId) {
        List<String> userIds_ = new ArrayList<>();
        for (Map<String, Object> map : projectUsers) {
            userIds_.add(map.get("userId").toString());
        }
        Map<String, Object> params = new HashedMap();
        params.put("projectId",projectId);
        List<String> userIds = new ArrayList<>();
        Map<String, Object> param_ = null;
        List<Map<String, Object>> item = new ArrayList<>();
        for (String courseId : courseList) {
            userIds = userIds_;
            params.put("courseId",courseId);
            List<String> userList = projectCourseInfoService.selectUserIds(params);
            if(userList != null && userList.size()>0){
                userIds.removeAll(userList);
            }
            roleMap.put("courseId", courseId);
            //获取项目课程详细信息属性值
            List<Map<String, Object>> items = projectCourseService.selectProjectCourseInfo(roleMap);
            item = getProjectCourseInfo(item,userIds,param_,items.get(0));
        }
        List<List<Map<String, Object>>> lists = CollectionUtils.subList(item,1000);
        Iterator<List<Map<String, Object>>> it = lists.iterator();
        while (it.hasNext()) {
            List<Map<String, Object>> list = it.next();
            projectCourseInfoService.insertBatch(list);
        }
    }

    private List<Map<String, Object>> getProjectCourseInfo(List<Map<String, Object>> item,List<String> list,Map<String, Object> params,Map<String, Object> param) {
        for (String userId : list) {
            params = new HashedMap();
            params.putAll(param);
            params.put("userId", userId);
            params.put("id", sequenceService.generator());
            params.put("totalStudytime", 0);
            params.put("answerStudytime", 0);
            params.put("trainStudytime", 0);
            params.put("yetAnswered", 0);
            params.put("correctAnswered", 0);
            params.put("correctRate", 0);
            params.put("finishStatus", "-1");
            params.put("createTime", DateUtils.formatDateTime(new Date()));
            params.put("operTime", DateUtils.formatDateTime(new Date()));
            item.add(params);
        }
        return item;
    }

    private void insertProjectCourseInfo(List<Map<String, Object>> item, String userId) {
        for (Map<String, Object> params: item) {
            params.put("userId", userId);
            params.put("id", sequenceService.generator());
            params.put("totalStudytime", 0);
            params.put("answerStudytime", 0);
            params.put("trainStudytime", 0);
            params.put("yetAnswered", 0);
            params.put("correctAnswered", 0);
            params.put("correctRate", 0);
            params.put("finishStatus", "-1");
            params.put("createTime", DateUtils.formatDateTime(new Date()));
            params.put("operTime", DateUtils.formatDateTime(new Date()));
        }
        //批量保存项目课程详情信息
        if(item !=null && item.size()>0) {
            projectCourseInfoService.insertBatch(item);
        }
    }
    /**
     * 修改项目
     * @param user
     * @param params
     */
    @Override
    public void updateProject(User user, Map<String,Object> params){
        //修改projectBasic表数据
        projectBasicService.update(params);
        //修改projectDepartment表的数据
        projectDepartmentService.updateProjectDepartment(user,params);
    }

    private void changeProjectUser(User user, Map<String, Object> params) {
        String projectId = params.get("projectId").toString();
        Map<String, Object> param_ = getList(params);
        if(null == param_){
            return;
        }
        List<ProjectDepartment> dList = (List<ProjectDepartment>)param_.get("dList");
        List<Map> deptMap = (List<Map>)param_.get("deptMap");
        //删除项目用户
        if(dList != null && dList.size()>0){
            deleteProjectUserByDepartmentId(dList, projectId);
        }
        if(deptMap != null && deptMap.size()>0){
            saveBatchProjectUser(deptMap,projectId,user);
        }
    }

    private Map<String,Object> getList(Map<String, Object> params) {
        Class<ProjectDepartment> clazz_ = ProjectDepartment.class;
        List<ProjectDepartment> pList = JsonUtils.joinToList(params.get("pList").toString(), clazz_);
        String departments = params.get("departments").toString();
        Class<Map> clazz = Map.class;
        //获取部门集合
        List<Map> depList = JsonUtils.joinToList(departments, clazz);
        List<Map> deptMap = new ArrayList<>();
        deptMap.addAll(depList);
        List<ProjectDepartment> dList = new ArrayList<>();
        if(pList != null && pList.size()>0){
            dList.addAll(pList);
            for (Map map: depList) {
                String deptId = map.get("id").toString();
                for (ProjectDepartment proDepartment: pList) {
                    String departmentId = proDepartment.getDeptId();
                    if(StringUtils.equals(deptId,departmentId)){
                        //库中有就移除
                        deptMap.remove(map);
                        dList.remove(proDepartment);
                        continue;
                    }
                }
            }
        }
        Map<String,Object> param = new HashedMap();
        param.put("dList",dList);
        param.put("deptMap",deptMap);
        return param;
    }

    private void deleteProjectUserByDepartmentId(List<ProjectDepartment> pList,
        String projectId) {
        for (ProjectDepartment projectDepartment: pList) {
            Map<String, Object> param = new HashedMap();
            param.put("deptId", projectDepartment.getDeptId());
            //根据部门Id得到用户集合
            List<String> userIds = userManagerService.selectUserIds(param);
            if(userIds != null && userIds.size()>0){
                param.clear();
                param.put("projectId", projectId);
                param.put("userIds", userIds);
                //批量删除项目用户
                projectUserService.deleteBatch(param);
            }
        }
    }

    /**
     * 消息推送的修改projectInfo和projectDossier表数据
     * @param params
     */
    @Override
    public void update(Map<String, Object> params) {
        //修改projectInfo表的数据
        projectInfoService.update(params);
        //修改projectDossierInfo表的数据
        projectDossierService.update(params);
        //如果角色有删除就删除相应的个人项目档案统计和个人练习统计信息和项目课程统计信息
        // operateProjectInfo(params);
    }

    @Override
    public void saveInfo(Map<String, Object> params) {
        String projectType = params.get("projectType").toString();
        String projectId = params.get("id").toString();
        Map<String, Object> param = new HashedMap();
        param.put("projectId", projectId);
        param.put("roleId", PropertiesUtils.getValue("role_id"));
        param.put("roleName", "默认角色");
        //查询项目课程信息
        List<Map<String, Object>> projectCourses = projectCourseService.selectProjectCourseInfo(param);
        //从项目用户中获取项目用户
        List<Map<String, Object>> projectUsers =  projectUserService.selectProjectUser(param);
        if(projectCourses != null && projectCourses.size()>0 && projectUsers != null && projectUsers.size()>0){
            insertInfoDetail(projectCourses,projectUsers,param,projectType,projectId);
        }
    }

    @Override
    public void insertProjectUser(Map<String, Object> params) {
        String departments = params.get("departments").toString();
        Class<Map> clazz = Map.class;
        //获取部门集合
        List<Map> depList = JsonUtils.joinToList(departments, clazz);
        if(depList == null || depList.size() == 0){
           return;
        }
        String projectId = params.get("id").toString();
        User user = JsonUtils.readValue(params.get("user").toString(),User.class);
        //保存项目用户信息
        saveBatchProjectUser(depList,projectId,user);
    }

    private void insertInfoDetail(List<Map<String, Object>> projectCourses,
        List<Map<String, Object>> projectUsers, Map<String, Object> param, String projectType,
        String projectId) {
        List<String> userIds = new ArrayList<>();
        for (Map<String, Object> projectUser: projectUsers) {
            userIds.add(projectUser.get("userId").toString());
        }
        //根据角色Id和项目Id获取所有课程的总学时和总题量
        Map<String, Object> courseMap = projectCourseMapper.selectProjectCourseMap(param);
        //获取项目详细信息
        ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(projectId);
        //保存个人学时统计信息
        enterProjectStatisticsInfo(param, projectUsers, courseMap, projectInfo);
        if(StringUtils.contains(containExerciseType,projectType)){
            //保存个人练习统计信息
            saveProjectExerciseInfo(param, projectUsers, courseMap, projectInfo);
        }
        //保存项目课程详细信息
        List<Map<String, Object>> item = new ArrayList<>();
        Map<String, Object> param_ = null;
        for (Map<String, Object> projectCourse: projectCourses) {
            item = getProjectCourseInfo(item,userIds,param_,projectCourse);
        }
        List<List<Map<String, Object>>> lists = CollectionUtils.subList(item,1000);
        for (List<Map<String, Object>> list : lists) {
            projectCourseInfoService.insertBatch(list);
        }
    }

    @Override
    public void updateInfoDetail(Map<String, Object> params) {
        String projectType = params.get("projectType").toString();
        String projectId = params.get("projectId").toString();
        String companyId = params.get("companyId").toString();
        Map<String, Object> param_ = getList(params);
        if(null == param_){
            return;
        }
        List<ProjectDepartment> dList = (List<ProjectDepartment>)param_.get("dList");
        List<Map> deptMap = (List<Map>)param_.get("deptMap");
        if(dList != null && dList.size()>0){
            deleteInfoDetail(projectType,projectId,companyId,dList);
        }
        if(deptMap != null && deptMap.size()>0){
            saveInfoDetail(projectType,projectId,companyId,deptMap);
        }
    }

    @Override
    public void saveByProjectRole(Map<String, Object> params, User user) throws Exception{
        String roles = params.get("roles").toString();
        String projectType = params.get("projectType").toString();
        Map<String, Object> param = new HashedMap();
        param.put("projectId", params.get("projectId").toString());
        param.put("roleId", PropertiesUtils.getValue("role_id"));
        List<String> roleMap = projectRoleService.selectRoleId(param);
        List<String> roleIds = new ArrayList<>();
        roleIds.addAll(roleMap);
        Class<Map> clazz = Map.class;
        List<Map> roleList = JsonUtils.joinToList(roles, clazz);
        List<Map> roleList_ = new ArrayList<>();
        roleList_.addAll(roleList);
        for (String roleId: roleMap) {
            for (Map map: roleList) {
                String roleId_ = map.get("roleId").toString();
                if(StringUtils.equals(roleId,roleId_)) {
                    roleList_.remove(map);
                    roleIds.remove(roleId);
                    continue;
                }
            }
        }
        if(roleList_ != null && roleList_.size()>0){
            params.put("roleList",roleList_);
            //批量保存项目角色信息
            projectRoleService.saveBatch(user,params);
        }
        if(roleIds != null && roleIds.size()>0){
            params.put("roleIds",roleIds);
            //批量删除项目角色
            projectRoleService.delete(params);
            //批量删除角色课程
            projectCourseService.deleteProjectId(params);
            if(StringUtils.contains(containExamType,projectType)){
                //批量删除考试组卷策略
                examStrategyService.deleteByProjectId(params);
            }
        }
    }

    @Override
    public void updateProjectUser(Map<String, Object> params) {
        User user = JsonUtils.readValue(params.get("user").toString(),User.class);
        //部门修改，修改项目用户信息
        changeProjectUser(user, params);
    }

    private void saveInfoDetail(String projectType, String projectId, String companyId,
        List<Map> deptMap) {
        Map<String, Object> params = new HashedMap();
        params.put("projectId",projectId);
        params.put("roleId", PropertiesUtils.getValue("role_id"));
        params.put("roleName", "默认角色");
        //查询项目课程信息
        List<Map<String, Object>> projectCourses = projectCourseService.selectProjectCourseInfo(params);
        if(projectCourses != null && projectCourses.size()>0){
            List<Map<String, Object>> projectUsers = new ArrayList<>();
            for (Map map: deptMap) {
                List<Map<String, Object>> userList = getDeptList(map,projectId);
                projectUsers.addAll(userList);
            }
            insertInfoDetail(projectCourses,projectUsers,params,projectType,projectId);
        }
    }

    private void deleteInfoDetail(String projectType, String projectId, String companyId,
        List<ProjectDepartment> dList) {
        List<String> deptIds = new ArrayList<>();
        Map<String, Object> params = new HashedMap();
        for (ProjectDepartment projectDepartment: dList) {
            deptIds.add(projectDepartment.getDeptId());
        }
        params.put("deptIds", deptIds);
        List<String> userIds = userManagerService.selectUserIds(params);
        if(userIds != null && userIds.size()>0){
            params.clear();
            params.put("projectId", projectId);
            params.put("userIds", userIds);
            //删除项目课程详细信息
            projectCourseInfoService.deleteBatch(params);
            //删除个人练习详细信息
            projectStatisticsInfoService.delete(params);
            if(StringUtils.contains(containExerciseType,projectType)){
                //删除个人练习统计信息
                projectExerciseOrderService.delete(params);
            }
        }
     }
}
