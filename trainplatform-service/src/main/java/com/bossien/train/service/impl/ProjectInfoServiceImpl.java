package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.ProjectInfoMapper;
import com.bossien.train.domain.ExamStrategy;
import com.bossien.train.domain.ProjectBasic;
import com.bossien.train.domain.ProjectInfo;
import com.bossien.train.domain.User;
import com.bossien.train.domain.eum.ProjectStatusEnum;
import com.bossien.train.domain.eum.ProjectTypeEnum;
import com.bossien.train.service.*;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.PropertiesUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 项目信息表
 * Created by Administrator on 2017/7/25.
 */
@Service(value = "projectInfoService")
public class ProjectInfoServiceImpl implements IProjectInfoService {


    @Autowired
    private ProjectInfoMapper projectInfoMapper;
    @Autowired
    private ISequenceService sequenceService;
    @Autowired
    private IProjectBasicService projectBasicService;
    @Autowired
    private IProjectRoleService projectRoleService;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private IProjectDossierService projectDossierService;
    @Autowired
    private ICompanyProjectService companyProjectService;
    @Autowired
    private IExamStrategyService examStrategyService;
    @Autowired
    private IProjectUserService projectUserService;

    @Override
    public Integer selectCount(Map<String, Object> params) {
        return projectInfoMapper.selectCount(params);
    }

    @Override
    public List<Map<String, Object>> selectList(Map<String, Object> params) {
        return projectInfoMapper.selectList(params);
    }

    /**
     * 通过项目id查询项目信息（项目详情）
     * @param id
     * @return
     */
//    @Cacheable(value = "projectInfoCache#(60 * 60)", key = "'selectProjectInfoById'.concat('_').concat(#id)")
    @Override
    public ProjectInfo selectProjectInfoById(String id){
       return  projectInfoMapper.selectProjectInfoById(id);
    }

    @Override
    public void save(Map<String, Object> params) throws ParseException{
        String id = (String) params.get("id");
        String intRetestTime = params.get("intRetestTime").toString();
        ProjectBasic projectBasic = projectBasicService.selectById(id);
        if(null == projectBasic) {
            return;
        }
        ProjectInfo projectInfo = this.build(projectBasic);
        projectInfo.setProjectName(projectBasic.getProjectName());
        projectInfo.setSubjectName((String) params.get("subject_name"));
        projectInfo.setPersonCount(Integer.parseInt(params.get("person_count").toString()));         //项目人数(公开项目初始值为0)
        projectInfo.setIntTrainPeriod(projectBasic.getTrainPeriod());            //项目周期
        projectInfo.setProjectType(projectBasic.getProjectType());               //项目类型
        if ("1".equals(projectBasic.getProjectMode())) {                        //公开型项目
            String projectTime = params.get("datBeginTime")+"至"+params.get("datEndTime"); //项目时间
            String examTime = params.get("examBeginTime")+"至"+params.get("examEndTime"); //考试时间
            projectInfo.setProjectStatus("1");                                  //项目状态
            projectInfo.setProjectMode("1");                                    //项目公开标识;1.公开项目 0.私有项目
            projectInfo.setIntRetestTime(Integer.valueOf(intRetestTime));      //考试次数
            projectInfo.setProjectStartTime((String) params.get("datBeginTime"));
            projectInfo.setProjectEndTime((String) params.get("datEndTime"));
            if("1".equals(projectBasic.getProjectType())){           //培训
                projectInfo.setProjectTrainTime(projectTime);
            }else if("2".equals(projectBasic.getProjectType())){     //练习
                projectInfo.setProjectExerciseTime(projectTime);
            }else if("3".equals(projectBasic.getProjectType())){     //考试
                projectInfo.setProjectExamTime(examTime);
            }else if("4".equals(projectBasic.getProjectType())){     //培训、练习
                projectInfo.setProjectTrainTime(projectTime);
                projectInfo.setProjectExerciseTime(projectTime);
            }else if("5".equals(projectBasic.getProjectType())){     //培训、考试
                projectInfo.setProjectTrainTime(projectTime);
                projectInfo.setProjectExamTime(examTime);
            }else if("6".equals(projectBasic.getProjectType())){     //练习、考试
                projectInfo.setProjectExerciseTime(projectTime);
                projectInfo.setProjectExamTime(examTime);
            }else{                                                   //培训、练习、考试
                projectInfo.setProjectTrainTime(projectTime);
                projectInfo.setProjectExerciseTime(projectTime);
                projectInfo.setProjectExamTime(examTime);
            }
            projectInfo.setRoleName((String) params.get("roleName"));
        }
        if ("0".equals(projectBasic.getProjectMode())){
            Map<String, Object> userMap = new HashedMap();
            userMap.put("projectId", id);
            Integer userCount = projectUserService.selectCount(userMap);
            projectInfo.setPersonCount(userCount);
            projectInfo = getProjectInfo(projectInfo,params,projectBasic);
            //带考试类型的项目修改组卷策略
            if(StringUtils.contains(PropertiesUtils.getValue("contain_exam_type"),projectBasic.getProjectType())){
                Map<String, Object> param = new HashedMap();
                param.put("projectId",id);
                param.put("roleId",PropertiesUtils.getValue("role_id"));
                ExamStrategy examStrategy = new ExamStrategy();
                User user = new User();
                examStrategy.setProjectId(id);
                examStrategy.setRoleId(PropertiesUtils.getValue("role_id"));
                user.setUserName(projectBasic.getCreateUser());
                examStrategyService.updateExamStrategy(examStrategy, param, user);
            }
            //保存项目档案
            saveProjectDossierInfo(projectInfo,params);
        }
        //保存数据
        projectInfoMapper.insert(projectInfo);

    }

    private void saveProjectDossierInfo(ProjectInfo projectInfo, Map<String, Object> params) {
        Map<String, Object> param = new HashedMap();
        param.put("id",sequenceService.generator());
        param.put("projectId",projectInfo.getId());
        param.put("companyId",params.get("companyId").toString());
        param.put("projectName",projectInfo.getProjectName());
        param.put("projectType",projectInfo.getProjectType());
        param.put("projectTypeName", ProjectTypeEnum.get(projectInfo.getProjectType()).getName());
        param.put("projectStartTime",projectInfo.getProjectStartTime());
        param.put("projectEndTime",projectInfo.getProjectEndTime());
        param.put("personCount",projectInfo.getPersonCount());
        param.put("createUser", projectInfo.getCreateUser());
        param.put("createTime",projectInfo.getCreateTime());
        param.put("operUser",projectInfo.getOperUser());
        param.put("operTime",projectInfo.getOperTime());
        //保存项目档案
        projectDossierService.insert(param);
    }

    private ProjectInfo getProjectInfo(ProjectInfo projectInfo, Map<String, Object> params, ProjectBasic projectBasic) throws ParseException {
        String trainBeginTime = params.get("trainBeginTime").toString();
        String trainEndTime = params.get("trainEndTime").toString();
        String exerciseBeginTime = params.get("exerciseBeginTime").toString();
        String exerciseEndTime = params.get("exerciseEndTime").toString();
        String examBeginTime = params.get("examBeginTime").toString();
        String examEndTime = params.get("examEndTime").toString();
        String intRetestTime = params.get("intRetestTime").toString();
        String beginTime = DateUtils.getMinDate(trainBeginTime, exerciseBeginTime, examBeginTime);
        String endTime = DateUtils.getBigDate(trainEndTime, exerciseEndTime, examEndTime);
        String projectStatus = projectBasic.getProjectStatus();
        projectInfo.setProjectStartTime(beginTime);
        projectInfo.setProjectEndTime(endTime);
        //projectInfo.setProjectStatus(DateUtils.getProjectStatus(beginTime, endTime, projectStatus));
        projectInfo.setProjectStatus(projectStatus);
        if(StringUtils.isNotEmpty(trainBeginTime) && StringUtils.isNotEmpty(trainEndTime)){
            projectInfo.setProjectTrainTime(trainBeginTime.substring(0,10) + " 至 " + trainEndTime.substring(0,10));
        }
        if(StringUtils.isNotEmpty(exerciseBeginTime) && StringUtils.isNotEmpty(exerciseEndTime)){
            projectInfo.setProjectExerciseTime(exerciseBeginTime.substring(0,10) + " 至 " + exerciseEndTime.substring(0,10));
        }
        if(StringUtils.isNotEmpty(examBeginTime) && StringUtils.isNotEmpty(examEndTime)){
            projectInfo.setProjectExamTime(examBeginTime.substring(0,16) + " 至 " + examEndTime.substring(0,16));
        }
        if(StringUtils.isNotEmpty(intRetestTime)){
            projectInfo.setIntRetestTime(Integer.parseInt(intRetestTime));
        }else{
            projectInfo.setIntRetestTime(0);
        }
        projectInfo.setRoleName(projectRoleService.selectRoleName(projectBasic.getId()));
        return projectInfo;
    }

    @Override
    public int update(Map<String, Object> params) {
        return projectInfoMapper.update(params);
    }


    //创建对象
    public ProjectInfo build(ProjectBasic projectBasic){
        if(null == projectBasic) {
            return null;
        }
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setId(projectBasic.getId());
        projectInfo.setCreateUser(projectBasic.getCreateUser());
        projectInfo.setCreateTime(DateUtils.formatDateTime(new Date()));
        projectInfo.setOperUser(projectBasic.getCreateUser());
        projectInfo.setOperTime(DateUtils.formatDateTime(new Date()));
        String project_type = projectBasic.getProjectType();
        return projectInfo;
    }
    /**
     * 根据projectId删除
     * @param params
     * @return
     */
    @Override
    public int delete(Map<String, Object> params){
        return projectInfoMapper.delete(params);
    }

    /**
     * 发布项目，根据当前时间判断项目状态：2未开始，3进行中，4已结束
     * @param params
     */
    @Override
    public void publish(Map<String, Object> params) throws ParseException {
        String projectId = params.get("project_id").toString();
        ProjectInfo projectInfo = this.selectProjectInfoById(projectId);
        if(null == projectInfo) {
            return;
        }
        String status;
        //项目开始时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginTime_ = projectInfo.getProjectStartTime();
        String endTime_ = projectInfo.getProjectEndTime();
        Date beginTime = sdf.parse(beginTime_);
        Date endTime = sdf.parse(endTime_);
        Date currentTime = new Date();
        if(beginTime.after(currentTime)){   //未开始
            status = "2";
        }else if(currentTime.after(beginTime) && endTime.after(currentTime)){//进行中
            status = "3";
        }else{                              //已结束
            status = "4";
        }
        params.put("project_status",status);
        projectInfoMapper.publish(params);
    }

    /**
     * 根据状态和类型查询projectIds
     * @param params
     * @return
     */
    @Override
    public List<String> selectProjectIds(Map<String, Object> params){
        List<String> projectIds=companyProjectService.selectProjectIds(params);
        params.put("projectIds",projectIds);
        return projectInfoMapper.selectProjectIds(params);
    }

    /**
     * 根据状态和类型查询projectId总数
     * @param params
     * @return
     */
    @Override
    public int selectProjectIdCount(Map<String, Object> params){
        List<String> projectIds=companyProjectService.selectProjectIds(params);
        if(projectIds.size() >0) {
            params.put("projectIds", projectIds);
            return projectInfoMapper.selectProjectIdCount(params);
        }
        return  0;
    }

    @Override
    public List<ProjectInfo> selectNeedPublish(String start, String end, String projectStatus) {
        return projectInfoMapper.selectNeedPublish(start, end, projectStatus);
    }

    @Override
    public Integer updateProjectStatus(String projectId, String projectStatus) {
        if(StringUtils.isEmpty(projectId) || StringUtils.isEmpty(projectStatus)) {
            return 0;
        }
        return projectInfoMapper.updateProjectStatus(projectId, projectStatus);
    }

    @Override
    public String selectProjectStatus(String projectId) {
        return projectInfoMapper.selectProjectStatus(projectId);
    }

    @Override
    public Integer checkProjectStatus(Map<String, Object> params) {
        Integer count = 0;
        String companyId = (String)params.get("companyId");
        Map<String, Object> param = new ConcurrentHashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new Date());
        List<String> list = companyProjectService.selectProjectIdsByCompanyId(companyId);
        if(null == list || list.isEmpty()) {
            return 0;
        }
        param.put("projectIds",list);
        param.put("projectStatus",ProjectStatusEnum.ProjectStatus_2.getValue());        // 未开始的项目
        param.put("currentTime",currentTime);                                           // 当前时间
        // 获取当前单位下未开始并且需要更新状态的项目id集合
        List<String> projectIds = projectInfoMapper.getIdsByStatus(param);
        if( null != projectIds && projectIds.size() > 0 ){
            param.clear();
            param.put("projectIds",projectIds);
            param.put("projectStatus",ProjectStatusEnum.ProjectStatus_3.getValue());
            // 更新未开始的项目状态projectInfo
            projectInfoMapper.checkProjectStatus(param);
            // 更新未开始的项目状态projectBase
            projectBasicService.checkProjectStatus(param);
            count = projectIds.size();
        }

        param.put("projectStatus", ProjectStatusEnum.ProjectStatus_3.getValue());        // 进行中的项目
        param.put("currentTime_1",currentTime);                                         // 当前时间
        param.put("currentTime","");
        // 获取当前单位下进行中并且需要更新状态的项目id集合
        List<String> IngProjectIds = projectInfoMapper.getIdsByStatus(param);
        if( null != IngProjectIds && IngProjectIds.size() > 0 ){
            param.clear();
            param.put("projectIds",IngProjectIds);
            param.put("projectStatus",ProjectStatusEnum.ProjectStatus_4.getValue());
            // 更新进行中的项目状态projectInfo
            projectInfoMapper.checkProjectStatus(param);
            // 更新进行中的项目状态projectBase
            projectBasicService.checkProjectStatus(param);
            count = IngProjectIds.size();
        }
        return count;
    }

}
