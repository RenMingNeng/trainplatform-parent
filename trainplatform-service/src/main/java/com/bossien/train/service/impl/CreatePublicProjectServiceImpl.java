package com.bossien.train.service.impl;

import com.bossien.train.domain.ExamStrategy;
import com.bossien.train.domain.ProjectBasic;
import com.bossien.train.domain.User;
import com.bossien.train.service.*;
import com.bossien.train.util.JsonUtils;
import com.bossien.train.util.StringUtil;
import com.google.gson.Gson;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.AfterTransaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-08-03.
 */
@Service
public class CreatePublicProjectServiceImpl implements ICreatePublicProjectService{

    @Autowired
    private IProjectBasicService projectBasicService;
    @Autowired
    private IExamStrategyService examStrategyService;
    @Autowired
    private IProjectCourseService projectCourseService;
    @Autowired
    private IProjectDossierService projectDossierService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private ICompanyProjectService companyProjectService;


    @Override
    public void savePublicProject(Map<String, Object> params, User user) throws Exception {
        ProjectBasic projectBasic = (ProjectBasic)params.get("projectBasic");
        //项目时间
        String datBeginTime = (String) params.get("datBeginTime");
        String datEndTime = (String) params.get("datEndTime");
        //考试时间
        String examBeginTime = (String) params.get("examBeginTime");
        String examEndTime = (String) params.get("examEndTime");
        String count = (String) params.get("intRetestTime");
        Map<String ,String> map = new HashMap<String ,String>();
        map.put("beginTime",datBeginTime);
        map.put("endTime",datEndTime);
        String project_info = JsonUtils.writeValueAsString(map);
        map.clear();
        map.put("beginTime",examBeginTime);
        map.put("endTime",examEndTime);
        map.put("count",count);
        String project_info_exam = JsonUtils.writeValueAsString(map);
        if("1".equals(projectBasic.getProjectType())){          //培训
            projectBasic.setProjectTrainInfo(project_info);
        }else if("2".equals(projectBasic.getProjectType())){    //练习
            projectBasic.setProjectExerciseInfo(project_info);
        }else if("3".equals(projectBasic.getProjectType())){    //考试
            projectBasic.setProjectExamInfo(project_info_exam);
        }else if("4".equals(projectBasic.getProjectType())){    //培训、练习
            projectBasic.setProjectTrainInfo(project_info);
            projectBasic.setProjectExerciseInfo(project_info);
        }else if("5".equals(projectBasic.getProjectType())){    //培训、考试
            projectBasic.setProjectTrainInfo(project_info);
            projectBasic.setProjectExamInfo(project_info_exam);
        }else if("6".equals(projectBasic.getProjectType())){    //练习、考试
            projectBasic.setProjectExerciseInfo(project_info);
            projectBasic.setProjectExamInfo(project_info_exam);
        }else{                                                  //培训、练习、考试
            projectBasic.setProjectExerciseInfo(project_info);
            projectBasic.setProjectTrainInfo(project_info);
            projectBasic.setProjectExamInfo(project_info_exam);
        }
        //项目基础信息保存
        projectBasicService.save(projectBasic);
        // 包含考试项目的初始化组卷策略
        if("3567".contains(projectBasic.getProjectType())){
            this.initExamStrategy(projectBasic.getId(),user.getUserName());
        }
    }

    //初始化组卷策略
    private void initExamStrategy(String projectId,String userName) {
        Map<String, Object> param = new HashedMap();
        param.put("roleId", "-1");
        param.put("roleName", "默认角色");
        param.put("projectId", projectId);
        param.put("userName", userName);
        param.put("necessaryHour","0");
        param.put("singleAllCount","0");
        param.put("manyAllCount","0");
        param.put("judgeAllCount","0");
        examStrategyService.insert(param);
    }

    @Override
    public void updatePublicProject(Map<String, Object> params, User user) throws Exception {
        String projectId = (String) params.get("projectId");
        String trainBeginTime = (String) params.get("trainBeginTime");
        String trainEndTime = (String) params.get("trainEndTime");
        String exerciseBeginTime = (String) params.get("exerciseBeginTime");
        String exerciseEndTime = (String) params.get("exerciseEndTime");
        String examBeginTime = (String) params.get("examBeginTime");
        String examEndTime = (String) params.get("examEndTime");
        String intRetestTime = (String) params.get("intRetestTime");
        ProjectBasic projectBasic = projectBasicService.selectById(projectId);
        if(null == projectBasic) {
            return;
        }
        Gson gson = new Gson();
        Map<String,Object> map = new HashMap<String,Object>();
        if("1".contains(projectBasic.getProjectType())){        //培训
            map.put("beginTime",trainBeginTime);
            map.put("endTime",trainEndTime);
            params.put("projectTrainInfo",JsonUtils.writeValueAsString(map));
        }else if("2".equals(projectBasic.getProjectType())){    //练习
            map.put("beginTime",exerciseBeginTime);
            map.put("endTime",exerciseEndTime);
            params.put("projectExerciseInfo",JsonUtils.writeValueAsString(map));
        }else if("3".equals(projectBasic.getProjectType())){    //考试
            map.put("beginTime",examBeginTime);
            map.put("endTime",examEndTime);
            map.put("count",intRetestTime);
            params.put("projectExamInfo",JsonUtils.writeValueAsString(map));
        }else if("4".equals(projectBasic.getProjectType())){    //培训、练习
            map.put("beginTime",trainBeginTime);
            map.put("endTime",trainEndTime);
            params.put("projectTrainInfo",JsonUtils.writeValueAsString(map));
            map.clear();
            map.put("beginTime",exerciseBeginTime);
            map.put("endTime",exerciseEndTime);
            params.put("projectExerciseInfo",JsonUtils.writeValueAsString(map));
        }else if("5".equals(projectBasic.getProjectType())){    //培训、考试
            map.put("beginTime",trainBeginTime);
            map.put("endTime",trainEndTime);
            params.put("projectTrainInfo",JsonUtils.writeValueAsString(map));
            map.clear();
            map.put("beginTime",examBeginTime);
            map.put("endTime",examEndTime);
            map.put("count",intRetestTime);
            params.put("projectExamInfo",JsonUtils.writeValueAsString(map));
        }else if("6".equals(projectBasic.getProjectType())){    //练习、考试
            map.put("beginTime",exerciseBeginTime);
            map.put("endTime",exerciseEndTime);
            params.put("projectExerciseInfo",JsonUtils.writeValueAsString(map));
            map.clear();
            map.put("beginTime",examBeginTime);
            map.put("endTime",examEndTime);
            map.put("count",intRetestTime);
            params.put("projectExamInfo",JsonUtils.writeValueAsString(map));
        }else{                                                  //培训、练习、考试
            map.put("beginTime",trainBeginTime);
            map.put("endTime",trainEndTime);
            params.put("projectTrainInfo",JsonUtils.writeValueAsString(map));
            map.clear();
            map.put("beginTime",exerciseBeginTime);
            map.put("endTime",exerciseEndTime);
            params.put("projectExerciseInfo",JsonUtils.writeValueAsString(map));
            map.clear();
            map.put("beginTime",examBeginTime);
            map.put("endTime",examEndTime);
            map.put("count",intRetestTime);
            params.put("projectExamInfo",JsonUtils.writeValueAsString(map));
        }
        // 项目基础信息修改保存
        projectBasicService.update(params);
    }

    @Override
    public void updateExamStrategy(Map<String, Object> param) {
        String projectId = param.get("id").toString();
        String roleId = param.get("roleId").toString();
        Map<String, Object> params = new HashMap<>();
        params.put("projectId", projectId);
        params.put("roleId", roleId);
        //修改组卷策略
        ExamStrategy examStrategy = new ExamStrategy();
        User user = new User();
        examStrategy.setProjectId(projectId);
        examStrategy.setRoleId(roleId);
        user.setUserName(param.get("userName").toString());
        examStrategyService.updateExamStrategy(examStrategy, params, user);
    }

    @Override
    public void delete(Map<String, Object> params) {
        // 删除项目详情
        projectInfoService.delete(params);
        // 删除组卷策略
        examStrategyService.deleteByProjectId(params);
        // 删除项目课程
        projectCourseService.deleteProjectId(params);
        // 删除项目单位
        companyProjectService.delete(params);
        // 删除项目档案
        projectDossierService.deleteByProjectId(params);
    }

}
