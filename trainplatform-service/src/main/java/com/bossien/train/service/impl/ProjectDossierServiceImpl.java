package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.CompanyProjectMapper;
import com.bossien.train.dao.tp.ExamDossierInfoMapper;
import com.bossien.train.dao.tp.ProjectDossierMapper;
import com.bossien.train.domain.*;
import com.bossien.train.domain.eum.ProjectStatusEnum;
import com.bossien.train.domain.eum.ProjectTypeEnum;
import com.bossien.train.service.*;
import com.bossien.train.util.CollectionUtils;
import com.bossien.train.util.ExportExcelUtils;
import com.bossien.train.util.JsonUtils;
import com.bossien.train.util.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by A on 2017/7/25.
 */

@Service
public class ProjectDossierServiceImpl implements IProjectDossierService {

    @Autowired
    private ProjectDossierMapper projectDossierMapper;
    @Autowired
    private ISequenceService sequenceService;
    @Autowired
    private CompanyProjectMapper companyProjectMapper;
    @Autowired
    private IProjectBasicService projectBasicService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private IExamDossierService examDossierService;
    @Autowired
    private IExamStrategyService examStrategyService;
    @Autowired
    private IProjectStatisticsInfoService projectStatisticsInfoService;
    @Autowired
    private ExamDossierInfoMapper examDossierInfoMapper;
    @Autowired
    private IProjectDepartmentService projectDepartmentService;

    @Override
    public void insert(Map<String, Object> params) {

        projectDossierMapper.insert(params);
    }

//    @Override
    @Override
    public void update(Map<String, Object> params) {

        projectDossierMapper.update(params);

        Object personCount = params.get("personCount");
        Object projectId = params.get("projectId");
        if(null != personCount && null != projectId){
            ProjectBasic projectBasic = projectBasicService.selectById(projectId.toString());
            //包含考试项目时添加
            String projectType = projectBasic.getProjectType();
            if(projectType.equals(ProjectTypeEnum.QuestionType_3.getValue()) ||
                    projectType.equals(ProjectTypeEnum.QuestionType_5.getValue()) ||
                    projectType.equals(ProjectTypeEnum.QuestionType_6.getValue()) ||
                    projectType.equals(ProjectTypeEnum.QuestionType_7.getValue())){

                ExamDossierInfo examDossierInfo = examDossierInfoMapper.selectOne(projectId.toString());
                if(null != examDossierInfo){
                    examDossierInfoMapper.delete(projectId.toString());
                }
                examDossierInfoMapper.insert(new ExamDossierInfo(projectId.toString(),
                        Integer.parseInt(personCount.toString())));
            }
        }
    }

    @Override
    public List<Map<String, Object>> selectList(Map<String, Object> params) {

        return projectDossierMapper.selectList(params);
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {

        return projectDossierMapper.selectCount(params);
    }

    @Override
    public Page<Map<String, Object>> queryForPagination(Map<String, Object> params,
                                                        Integer pageNum, Integer pageSize, User user) {
        //获取当前公司所有的项目id集合（过滤掉未发布的公开型项目）
        List<String> projects = companyProjectMapper.selectProjectIdsByCompanyId(user.getCompanyId());
        if(null == projects || projects.size() == 0) {
            return new Page(0, pageNum, pageSize);
        }
//        Map<String, Object> param = new HashMap<String, Object>();
//        param.put("projects",projects);
//        param.put("projectMode",'1');
//        param.put("projectStatus",'1');  // 过滤掉未发布的公开项目
//        List<String> projects_ = projectBasicService.selectProjectIdsByStatus(param);
//        if(null != projects_ && projects_.size() > 0){
//            projects.removeAll(projects_);   //  移除未发布的公开型项目
//        }

        //只显示已经结束的项目
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("projectIds", projects);
        param.put("projectStatus", Arrays.asList(ProjectStatusEnum.ProjectStatus_4.getValue()));
        projects = projectInfoService.selectProjectIds(param);

        List<Map<String, Object>> listMap = new ArrayList<>();
        Page<Map<String, Object>> page;
        if(null != projects && projects.size() > 0){
            params.put("projects", projects);
            params.put("companyId", user.getCompanyId());
            Integer count = projectDossierMapper.selectCount(params);
            page = new Page(count, pageNum, pageSize);
            params.put("startNum", page.getStartNum());
            params.put("endNum", page.getPageSize());
            listMap = projectDossierMapper.selectList(params);

            for(Map<String, Object> map : listMap){
                Object projectType = map.get("project_type");
                Object projectId = map.get("project_id");
                if(null == projectType){
                    continue;
                }
                map.put("project_type_name", ProjectTypeEnum.get(projectType.toString()).getName());
                ProjectBasic projectBasic = projectBasicService.selectById(projectId.toString());

                map.put("project_Mode", projectBasic.getProjectMode());
            }
        }else{
            page = new Page(0, pageNum, pageSize);
        }
        page.setDataList(listMap);
        return page;
    }

    @Override
    public Map<String, Object> selectOne(Map<String, Object> params) {

        return projectDossierMapper.selectOne(params);
    }

    @Override
    public void insertBatch(Map<String, Object> params) {
        String projectId = String.valueOf(params.get("projectId"));
        String companyIds = String.valueOf(params.get("companyIds"));
        ProjectBasic projectBasic = projectBasicService.selectById(projectId);
        if(null == projectBasic || StringUtils.isEmpty(companyIds)) {
            return;
        }
        String projectInfo;
        if(StringUtils.isNotEmpty(projectBasic.getProjectTrainInfo())){
            projectInfo =  projectBasic.getProjectTrainInfo();
        }else{
            projectInfo =  projectBasic.getProjectExerciseInfo();
        }
        Map map = JsonUtils.readJson2Map(projectInfo);
        String project_start_time = map.get("beginTime").toString();
        String project_end_time = map.get("endTime").toString();
        List<String> companyIds_ = Arrays.asList(companyIds.split(","));
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("projectId",projectId);
        param.put("companyIds",companyIds_);
        //添加前先做删除操作（防止重复添加）
        projectDossierMapper.deleteBatch(param);
        Integer a = companyIds_.size()/900+1;
        List<List<String>> subList = CollectionUtils.subList(companyIds_, companyIds_.size()/a);
        List<ProjectDossier> projectDossiers = null;
        ProjectDossier projectDossier = null;
        for(List<String> list : subList){
            projectDossiers = new ArrayList<ProjectDossier>();
            for(String companyId : list){
                projectDossier = new ProjectDossier();
                projectDossier.setId(sequenceService.generator());
                projectDossier.setCreateUser(projectBasic.getCreateUser());
                projectDossier.setCreateTime(projectBasic.getCreateTime());
                projectDossier.setOperUser(projectBasic.getCreateUser());
                projectDossier.setOperTime(projectBasic.getOperTime());
                projectDossier.setProjectId(projectBasic.getId());
                projectDossier.setCompanyId(companyId);
                projectDossier.setProjectName(projectBasic.getProjectName());
                projectDossier.setProjectType(projectBasic.getProjectType());
//                projectDossier.setProjectTypeName(ProjectTypeEnum.get(projectBasic.getProjectType()).getName());
                projectDossier.setProjectStartTime(project_start_time);
                projectDossier.setProjectEndTime(project_end_time);
                projectDossiers.add(projectDossier);
            }
            projectDossierMapper.insertBatch(projectDossiers);
        }
    }

    /**
     * 根据projectId和companyId删除
     * @param params
     * @return
     */
    @Override
    public int deleteByProjectId(Map<String, Object> params){
        return  projectDossierMapper.deleteByProjectId(params);
    }
    /**
     * 根据companyId和projectType查询projectIds
     * @param params
     * @return
     */
    @Override
    public List<String> selectProjectIds(Map<String, Object> params){
        return projectDossierMapper.selectProjectIds(params);
    }

    @Override
    public void export(String projectId, String projectType, String userId, OutputStream output) throws Exception{
        LinkedHashMap<String, Object> projectInfoMap = new LinkedHashMap();
        ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(projectId);
        ExamDossierInfo examDossierInfo = examDossierService.selectOne(projectId);
        //求组卷策略
        Map<String, Object> params = new HashedMap();
        params.put("projectId", projectId);
        List<ExamStrategy> examStrategys = examStrategyService.selectList(params);
        String passScore = "";
        String examDuration = "";
        if(examStrategys.size() == 1){
            passScore = examStrategys.get(0).getPassScore() + "";
            examDuration = examStrategys.get(0).getExamDuration() + "";
        }else{
            for(ExamStrategy examStrategy : examStrategys){
                if(!examStrategy.getRoleId().equals("-1")){
                    passScore += passScore == "" ? examStrategy.getPassScore() + "(" + examStrategy.getRoleName() + ")" :
                            ";" + examStrategy.getPassScore() + "(" + examStrategy.getRoleName() + ")";

                    examDuration += examDuration == "" ? examStrategy.getExamDuration() + "(" + examStrategy.getRoleName() + ")" :
                            ";" + examStrategy.getExamDuration() + "(" + examStrategy.getRoleName() + ")";
                }
            }
        }
        projectInfoMap.put("项目名称", projectInfo.getProjectName());
        projectInfoMap.put("项目创建人", projectInfo.getCreateUser());
        projectInfoMap.put("创建时间", projectInfo.getCreateTime().substring(0, 19));
        projectInfoMap.put("项目时间", projectInfo.getProjectStartTime().substring(0,10) + "至" + projectInfo.getProjectEndTime().substring(0,10));
//        projectInfoMap.put("受训角色", projectInfo.getRoleName());

        String deptName = "";
        List<ProjectDepartment> projectDepartments = projectDepartmentService.selectByProjectIdAndCompanyId(params);
        for (ProjectDepartment projectDepartment: projectDepartments){
            deptName += deptName.equals("") ? projectDepartment.getDeptName() : "," + projectDepartment.getDeptName();
        }
        projectInfoMap.put("受训单位", deptName);
        projectInfoMap.put("项目总人数(人)", projectInfo.getPersonCount());
        if(null != examDossierInfo){
            projectInfoMap.put("考试时间", projectInfo.getProjectExamTime());
            projectInfoMap.put("考试合格率(%)", examDossierInfo.getExamPassRate());
            projectInfoMap.put("参加考试人数", examDossierInfo.getYetExamCount());
            projectInfoMap.put("未考试人数", examDossierInfo.getNotExamCount());
            projectInfoMap.put("考试合格人数", examDossierInfo.getQualifiedCount());
            projectInfoMap.put("不合格人数", examDossierInfo.getUnqualifiedCount());
            projectInfoMap.put("合格分数", passScore);
            projectInfoMap.put("考试时长(min)", examDuration);
        }

        List<ProjectStatisticsInfo> projectStatisticsInfos = projectStatisticsInfoService.selectListByProjectId(projectId);
        List<Map<String, Object>> infos = new ArrayList<Map<String, Object>>();
        Map<String, Object> data;
        for (ProjectStatisticsInfo projectStatisticsInfo: projectStatisticsInfos){
            Long totalStudyTime = projectStatisticsInfo.getTotalStudyTime();
            projectStatisticsInfo.setUpdateTotalStudyTime(Double.parseDouble(new DecimalFormat("0.00").
                    format(totalStudyTime / 60.00)));

            data = MapUtils.newLinkedHashMap();
            data.put("userName", projectStatisticsInfo.getUserName());
            data.put("deptName", projectStatisticsInfo.getDeptName());
            data.put("requirementStudyTime", projectStatisticsInfo.getRequirementStudyTime().toString());
            data.put("updateTotalStudyTime", projectStatisticsInfo.getUpdateTotalStudyTime().toString());
            data.put("yetAnswered", projectStatisticsInfo.getYetAnswered().toString());
            data.put("correctAnswered", projectStatisticsInfo.getCorrectAnswered().toString());
            data.put("correctRate", projectStatisticsInfo.getCorrectRate().toString());
            data.put("examScore", projectStatisticsInfo.getExamScore().toString());

            //不包含培训时-236
            if(ProjectTypeEnum.QuestionType_2.getValue().equals(projectType) ||
                    ProjectTypeEnum.QuestionType_3.getValue().equals(projectType) ||
                    ProjectTypeEnum.QuestionType_6.getValue().equals(projectType)){
                //应修学时(单位分)
                data.put("requirementStudyTime", "\\");
            }
            //不包含练习时-135
            if(ProjectTypeEnum.QuestionType_1.getValue().equals(projectType) ||
                    ProjectTypeEnum.QuestionType_3.getValue().equals(projectType) ||
                    ProjectTypeEnum.QuestionType_5.getValue().equals(projectType)){
                data.put("yetAnswered", "\\");
                data.put("correctAnswered", "\\");
                data.put("correctRate", "\\");
            }
            //不包含考试时-124
            if(ProjectTypeEnum.QuestionType_1.getValue().equals(projectType) ||
                    ProjectTypeEnum.QuestionType_2.getValue().equals(projectType) ||
                    ProjectTypeEnum.QuestionType_4.getValue().equals(projectType)){
                data.put("examScore", "\\");
            }

            //考试
            if(ProjectTypeEnum.QuestionType_3.getValue().equals(projectType)){
                data.put("updateTotalStudyTime", "\\");
            }
            infos.add(data);
        }
        ExportExcelUtils<Map<String, Object>> exportExcelUtils = new ExportExcelUtils<Map<String, Object>>();
        exportExcelUtils.exportExcel(projectInfoMap, "项目概况", infos, output);
    }

    @Override
    public void deleteBatch(Map<String, Object> params) {
        projectDossierMapper.deleteBatch(params);
    }
}
