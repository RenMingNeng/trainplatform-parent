package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.ProjectInfoMapper;
import com.bossien.train.domain.*;
import com.bossien.train.domain.eum.ProjectStatusEnum;
import com.bossien.train.domain.eum.ProjectTypeEnum;
import com.bossien.train.service.*;
import com.bossien.train.util.*;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/7/26.
 */
@Service
public class ProjectManagerServiceImpl implements IProjectManagerService {
    @Autowired
    private ProjectInfoMapper projectInfoMapper;
    @Autowired
    private IProjectBasicService projectBasicService;
    @Autowired
    private IProjectRoleService projectRoleService;
    @Autowired
    private IProjectDepartmentService projectDepartmentService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private IProjectDossierService projectDossierService;
    @Autowired
    private IProjectCourseService projectCourseService;
    @Autowired
    private IProjectUserService projectUserService;
    @Autowired
    private IProjectCourseInfoService projectCourseInfoService;
    @Autowired
    private IProjectExerciseOrderService projectExerciseOrderService;
    @Autowired
    private IProjectStatisticsInfoService projectStatisticsInfoService;
    @Autowired
    private IExamStrategyService examStrategyService;
    @Autowired
    private ICompanyProjectService companyProjectService;
    @Autowired
    private IExamScoreService examScoreService;
    @Autowired
    private IExamDossierService examDossierService;
    @Autowired
    private ICompanyTjService companyTjService;

    /**
     * 修改项目
     * @param user
     * @param params
     *//*
    @Override
    public void updateProject(User user, Map<String,Object> params){
        //修改projectBasic表数据
       projectBasicService.update(params);
        //修改projectRole表的数据
       projectRoleService.updateProjectRole(user,params);
       //修改projectDepartment表的数据
       projectDepartmentService.updateProjectDepartment(user,params);
       //确认课程、用户、组卷策略与角色的关联是否为正确
       //projectCourseService.confirmProjectCourse(params);
       this.confirmCourseAndUserAndStrategy(params);
    }
*/
   /* *//**
     * 确认课程、用户、组卷策略与角色的关联是否为正确
     * @param params
     *//*
    public void confirmCourseAndUserAndStrategy(Map<String,Object> params){
        String roles = params.get("roles").toString();
        int count = 0;
        Class<Map> clazz = Map.class;
        List<Map> roleList = JsonUtils.joinToList(roles, clazz);
        //项目角色对象为空不做删除也不做添加
        if(roleList == null || roleList.size()==0){
            return;
        }
        List<String> roleIds=new ArrayList<String>();
        for (Map map: roleList){
            roleIds.add(map.get("id").toString());
        }
        params.put("roleIds",roleIds);
        //确认课程与角色的关联是否正确
        projectCourseService.confirmProjectCourse(params);
        //确认用户与角色的关联数据是否正确
        projectUserService.confirmProjectUser(params);
       //确认是否含有考试项目
        String projectType=params.get("projectType").toString();
        String containExamtype= PropertiesUtils.getValue("contain_exam_type");
        if(containExamtype.contains(projectType)){
            //确认组卷策略与角色的关联数据是否正确
            examStrategyService.confirmExamStrategy(params);
        }
    }*/
    /**
     * 删除未开发未发布的项目（projectBasic和projectInfo表）
     * @param params
     */
    @Override
    public void delete(Map<String,Object> params){
        //删除CompanyProject表的数据
        companyProjectService.delete(params);
        //删除projectBasic表的数据
        projectBasicService.delete(params);
        //删除projectRole表的数据
        projectRoleService.delete(params);
        //删除projectDepartment表的数据
        projectDepartmentService.delete(params);
        //删除projectCourse表的数据
        projectCourseService.deleteProjectId(params);
        //删除projectUser表的数据
        projectUserService.deleteByProjectId(params);
        //删除ExamStrategy表的数据
        examStrategyService.deleteByProjectId(params);
    }

    /**
     * 删除项目
     * @param params
     */
    @Override
    public void deleteProject(Map<String,Object> params){
        //删除projectInfo表的数据
        projectInfoService.delete(params);
        //删除projectCourseInfo表的数据
        projectCourseInfoService.deleteBatch(params);
        //删除projectDossierInfo表的数据
        projectDossierService.deleteByProjectId(params);
        //删除projectExerciseOrder表的数据
        projectExerciseOrderService.delete(params);
        //删除projectStaticticsInfo表的数据
        projectStatisticsInfoService.delete(params);
        //删除ExamDossierInfo表的数据
        examDossierService.delete(params.get("projectId").toString());
    }

    @Override
    public void updateProjectPublish(Map<String, Object> params) throws Exception{
        Integer type = Integer.parseInt(params.get("type").toString());
        params.put("operTime", DateUtils.formatDateTime(new Date()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = params.get("startTime").toString();
        String endTime = params.get("endTime").toString();
        Date date = new Date();
        if(null != type) {
            if (type == 1) {
                params.put("projectStatus", ProjectStatusEnum.ProjectStatus_2.getValue());
                if(sdf.parse(startTime).before(date) && sdf.parse(endTime).after(date)){
                    params.put("projectStatus", ProjectStatusEnum.ProjectStatus_3.getValue());
                }

                if(sdf.parse(endTime).before(date)){
                    params.put("projectStatus", ProjectStatusEnum.ProjectStatus_4.getValue());
                }
            }
            if (type == 2) {
                params.put("projectStatus", ProjectStatusEnum.ProjectStatus_1.getValue());
            }
            if (type == 3) {
                String project_status = params.get("project_status").toString();
                if(project_status.equals(ProjectStatusEnum.ProjectStatus_2.getValue())){
                    params.put("projectStatus", ProjectStatusEnum.ProjectStatus_3.getValue());
                }

                if(project_status.equals(ProjectStatusEnum.ProjectStatus_3.getValue())){
                    params.put("projectStatus", ProjectStatusEnum.ProjectStatus_4.getValue());
                }
            }
            //修改项目基础项目状态
            projectBasicService.update(params);
            //修改项目详细项目状态
            projectInfoService.update(params);

        }
    }

    @Override
    public Map<String, Object> selectTrainProcess(Map<String, Object> params) {
        List<Map<String, Object>> lMap = projectStatisticsInfoService.selectListMap(params);
        // 总数据
        Map<String, Object> map_ = new HashedMap();
        int trainProcess_0_count = 0;
        int trainProcess_0_50_count = 0;
        int trainProcess_50_100_count = 0;
        int trainProcess_100_count = 0;
        int trainProcess_sum_count = (null==lMap || lMap.isEmpty()) ? 0 : lMap.size();
        // 学员进度（培训学时/应修学时）
        double processPercent = 0.00d;
        // 已修学时
        double sumIntStudyTime1 = 0.00d;
        // 应修学时
        double sumIntRequirement = 0.00d;
        // 学员进度之和
        double sumProcessPercent = 0.00d;
        for(Map<String, Object> map : lMap){
            sumIntStudyTime1 = Double.valueOf(map.get("totalStudyTime").toString());
            sumIntRequirement = Double.valueOf(map.get("requirementStudyTime").toString());
            if(sumIntRequirement<=0){
                if((sumIntStudyTime1)<=0){
                    processPercent = 0;
                }else{
                    processPercent = 1;
                }
            }else{
                processPercent = (sumIntStudyTime1)/(sumIntRequirement*60);
            }
            sumProcessPercent += (processPercent>=1)?1:processPercent;
            if(processPercent<=0){
                trainProcess_0_count++;
                continue;
            }
            if(processPercent<=0.5){//百分之50
                trainProcess_0_50_count++;
                continue;
            }
            if(processPercent<1){
                trainProcess_50_100_count++;
                continue;
            }
            trainProcess_100_count++;
            continue;
        }
        String trainProcess_0_percent = MathUtil.percent((double)trainProcess_0_count, (double)trainProcess_sum_count);
        String trainProcess_0_50_percent = MathUtil.percent((double)trainProcess_0_50_count, (double)trainProcess_sum_count);
        String trainProcess_50_100_percent = MathUtil.percent((double)trainProcess_50_100_count, (double)trainProcess_sum_count);
        String trainProcess_100_percent = MathUtil.percent((double)trainProcess_100_count, (double)trainProcess_sum_count);
        String trainProcess_sum_percent = MathUtil.percent((double)trainProcess_sum_count, (double)trainProcess_sum_count);

        map_.put("trainProcess_0_count", trainProcess_0_count);
        map_.put("trainProcess_0_50_count", trainProcess_0_50_count);
        map_.put("trainProcess_50_100_count", trainProcess_50_100_count);
        map_.put("trainProcess_100_count", trainProcess_100_count);
        map_.put("trainProcess_sum_count", trainProcess_sum_count);
        map_.put("trainProcess_0_percent", trainProcess_0_percent);
        map_.put("trainProcess_0_50_percent", trainProcess_0_50_percent);
        map_.put("trainProcess_50_100_percent", trainProcess_50_100_percent);
        map_.put("trainProcess_100_percent", trainProcess_100_percent);
        map_.put("trainProcess_sum_percent", trainProcess_sum_percent);
        // 项目总进度 = 进度平均值
        map_.put("trainProcess_percent", MathUtil.percent(sumProcessPercent, Double.valueOf(lMap.size())));
        return map_;
    }

    @Override
    public Map<String, Object> selectExamInfo(Map<String, Object> params) {
        Map<String, Object> map = new HashedMap();
        //查询考试用户数量
        Integer userCount = projectUserService.selectCount(params);
        //查询考试过学员数量
        params.put("examType","2");
        Integer examCount = examScoreService.selectCount(params);
        //查询考试合格人数数量
        params.put("isPassed","1");
        Integer passCount = examScoreService.selectCount(params);
        //参考率
        String examPercent = "";
        double userCount_ = 0.00d;
        double examCount_ = 0.00d;
        double passCount_ = 0.00d;
        userCount_ = Double.valueOf(userCount.toString());
        examCount_ = Double.valueOf(examCount.toString());
        passCount_ = Double.valueOf(passCount.toString());
        examPercent = MathUtil.percent(examCount_,userCount_);
        //合格率
        String passPercent = "";
        passPercent = MathUtil.percent(passCount_,examCount_);
        map.put("userCount",userCount);
        map.put("examCount",examCount);
        map.put("passCount",passCount);
        map.put("examPercent",examPercent);
        map.put("passPercent",passPercent);
        return map;
    }

    @Override
    public void update(Map<String, Object> params) {
        Integer personCount = projectUserService.selectCount(params);
        params.put("personCount",personCount.toString());
        //修改projectInfo表的数据
        projectInfoService.update(params);
        //修改projectDossierInfo表的数据
        projectDossierService.update(params);
        //如果角色有删除就删除相应的个人项目档案统计和个人练习统计信息和项目课程统计信息
       // operateProjectInfo(params);
    }

    private void operateProjectInfo(Map<String, Object> params) {
        String roles = params.get("roles").toString();
        String projectId = params.get("projectId").toString();
        Class<Map> clazz = Map.class;
        List<Map> roleList = JsonUtils.joinToList(roles, clazz);
        //项目角色对象为空不做删除也不做添加
        if(roleList == null || roleList.size()==0){
            return;
        }
        List<String> roleIds=new ArrayList<String>();
        for (Map map: roleList){
            roleIds.add(map.get("id").toString());
        }
        params.put("roleIds",roleIds);
        Map<String, Object> param= new HashedMap();
        //查询项目用户信息
        List<String> uList = projectUserService.selectUserIds(params);
        if(uList != null && uList.size()>0){
            //查询项目课程统计表id集合
            params.put("userIds",uList);
            List<String> cList = projectCourseInfoService.selectIdList(params);
            if(cList != null && cList.size()>0){
                param.clear();
                param.put("ids",cList);
                //批量删除项目课程详细信息
                projectCourseInfoService.deleteBatch(param);
            }
        }

        //查询项目个人统计信息id集合
        List<String> sList = projectStatisticsInfoService.selectIdList(params);
        if(sList != null && sList.size()>0){
            param.clear();
            param.put("ids",sList);
            //批量删除项目课程详细信息
            projectStatisticsInfoService.delete(param);
        }

        String projectType = params.get("projectType").toString();
        if(ProjectTypeEnum.QuestionType_2.getValue().equals(projectType) || ProjectTypeEnum.QuestionType_4.getValue().equals(projectType) ||
            ProjectTypeEnum.QuestionType_6.getValue().equals(projectType) || ProjectTypeEnum.QuestionType_7.getValue().equals(projectType)){
            //查询项目个人练习统计信息id集合
            List<String> eList = projectExerciseOrderService.selectIdList(params);
            if(eList != null && eList.size()>0){
                param.clear();
                param.put("ids",eList);
                //批量删除项目个人练习统计详细信息
                projectExerciseOrderService.delete(param);
            }
        }
    }

    /**
     * 修改projectCourse和projectCourseInfo表的数据
     * @param params
     */
    @Override
    public void updateRequirementAndSelectCount(Map<String,Object> params){
        //修改projectCourse表的数据
        projectCourseService.update(params);

        //修改projectCourseInfo表的数据
        if(null == params.get("requirement")){
            return;
        }
        ProjectCourseInfo projectCourseInfo = new ProjectCourseInfo();
        projectCourseInfo.setCourseId(params.get("courseId").toString());
        projectCourseInfo.setProjectId(params.get("projectId").toString());
        projectCourseInfo.setRequirementStudyTime(Long.parseLong(params.get("requirement").toString()));
        //修改projectCourseInfo表的数据
        projectCourseInfoService.update(projectCourseInfo);

        ProjectStatisticsInfo projectStatisticsInfo = new ProjectStatisticsInfo();
        projectStatisticsInfo.setProjectId(params.get("projectId").toString());
        projectStatisticsInfo.setRoleId(params.get("roleId").toString());
        //根据角色Id和项目Id获取所有课程的总学时和总题量
        Map<String, Object> courseMap = projectCourseService.selectProjectCourseMap(params);
        projectStatisticsInfo.setRequirementStudyTime(Long.parseLong(courseMap.get("requirementStudytime").toString()));

        //修改projectStatisticsInfo表的数据
        projectStatisticsInfoService.update(projectStatisticsInfo);
    }
}
