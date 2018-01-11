package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.service.ICreatePublicProjectService;
import com.bossien.train.service.IExamStrategyService;
import com.bossien.train.service.IProjectDossierService;
import com.bossien.train.service.IProjectInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 按需复写父类方法，真实业务请直接继承AbstractListener
 * que 监听器
 * @author DF
 *
 */
@Component("queueCPPubListener")
public class QueueCPPubListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(QueueCPPubListener.class);

    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private IProjectDossierService projectDossierService;
    @Autowired
    private ICreatePublicProjectService createPublicProjectService;

    @Override
    public <V> void mapMessageHander(Map<String, V> map) throws ParseException {
        super.mapMessageHander(map);
        String queueNo = String.valueOf( map.get("queueNo"));
        if(null == queueNo || queueNo.equals("null") || queueNo.equals("")){
            return;
        }
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        // 项目详情保存
        if("projectInfo".equals(queueNo)){
            params.put("id",map.get("id").toString());
            params.put("project_type",map.get("project_type").toString());
            params.put("subject_name",map.get("subject_name").toString());
            params.put("project_name",map.get("project_name").toString());
            params.put("datBeginTime",map.get("datBeginTime").toString());
            params.put("datEndTime",map.get("datEndTime").toString());
            params.put("examBeginTime",map.get("examBeginTime").toString());
            params.put("examEndTime",map.get("examEndTime").toString());
            params.put("intRetestTime",map.get("intRetestTime").toString());

            params.put("roleName","默认角色");
            params.put("person_count",0);
            projectInfoService.save(params);
        }
        // 项目详情修改
        if("updateProjectInfo".equals(queueNo)){
            params.clear();
            params.put("projectId",map.get("projectId").toString());
            params.put("projectStartTime",map.get("projectStartTime").toString());
            params.put("projectEndTime",map.get("projectEndTime").toString());
            params.put("project_train_Time",map.get("project_train_Time").toString());
            params.put("project_exercise_Time",map.get("project_exercise_Time").toString());
            params.put("project_exam_Time",map.get("project_exam_Time").toString());
            params.put("intRetestTime",map.get("intRetestTime").toString());
            params.put("intTrainPeriod",map.get("intTrainPeriod").toString());
            params.put("operUser",map.get("operUser").toString());
            params.put("operTime",map.get("operTime").toString());
            projectInfoService.update(params);
        }
        // 项目发布
        if("projectPublish".equals(queueNo)){
            params.clear();
            params.put("project_id",map.get("project_id").toString());
            projectInfoService.publish(params);
        }
        // 项目档案保存
        if("projectDossier".equals(queueNo)){
            params.clear();
            params.put("projectId",map.get("project_id").toString());
            params.put("companyIds",map.get("company_ids").toString());
            params.put("datBeginTime",map.get("datBeginTime").toString());
            params.put("datEndTime",map.get("datEndTime").toString());
            projectDossierService.insertBatch(params);
        }
        // 项目档案删除(删除项目单位到时候异步删除项目档案)
        if("deleteProjectDossier".equals(queueNo)){
            params.clear();
            params.put("projectId",map.get("projectId").toString());
            params.put("companyIds",map.get("companyIds").toString());
            projectDossierService.deleteBatch(params);
        }
        // 选择课程，修改组卷策略
        if("updateExamStrategy".equals(queueNo)){
            params.clear();
            params.put("id", map.get("id").toString());
            params.put("roleId", map.get("roleId").toString());
            params.put("userName", map.get("userName").toString());
            createPublicProjectService.updateExamStrategy(params);

        }
        // 公开型项目删除相关数据
        if("projectDelete".equals(queueNo)){
            params.clear();
            params.put("projectId", map.get("project_id").toString());
            createPublicProjectService.delete(params);
        }
    }
}
