package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.service.ICreatePrivateProjectService;
import com.bossien.train.service.IProjectManagerService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 按需复写父类方法，真实业务请直接继承AbstractListener
 * que 监听器
 *
 * @author DF
 */
@Component("queueUpdateAndDeleteProjectListener")
public class QueueUpdateAndDeleteProjectListener extends AbstractListener {


    public static final Logger logger = LoggerFactory.getLogger(QueueUpdateAndDeleteProjectListener.class);
    @Autowired
    private IProjectManagerService projectManagerService;
    @Autowired
    private ICreatePrivateProjectService createPrivateProjectService;


    @Override
    public <V> void mapMessageHander(Map<String, V> map) throws ParseException {
        super.mapMessageHander(map);

        String operType = (String) map.get("operType");
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        if ("delete".equals(operType)) {
            if (StringUtils.isEmpty(map.get("companyId").toString())) {
                return;
            }
            params.put("companyId", map.get("companyId").toString());
            params.put("projectId", map.get("projectId").toString());
            projectManagerService.deleteProject(params);
        }
        if ("update".equals(operType)) {
            params.clear();
            params.put("projectId", map.get("projectId").toString());
            params.put("projectType", map.get("projectType").toString());
            params.put("projectName", map.get("projectName").toString());
            params.put("projectTrainInfo", map.get("projectTrainInfo").toString());
            params.put("projectExerciseInfo", map.get("projectExerciseInfo").toString());
            params.put("projectExamInfo", map.get("projectExamInfo").toString());
            params.put("projectTrainTime", map.get("projectTrainTime").toString());
            params.put("projectExerciseTime", map.get("projectExerciseTime").toString());
            params.put("projectExamTime", map.get("projectExamTime").toString());
            params.put("operUser", map.get("operUser").toString());
            params.put("operTime", map.get("operTime").toString());
            // params.put("roles",map.get("roles").toString());
            // params.put("roleName",map.get("roleName").toString());
            params.put("departments", map.get("departments").toString());
            params.put("user", map.get("user").toString());
            params.put("projectStartTime", map.get("projectStartTime").toString());
            params.put("projectEndTime", map.get("projectEndTime").toString());
            params.put("intRetestTime", map.get("intRetestTime").toString());
            params.put("personCount", map.get("personCount").toString());
            params.put("projectStatus", map.get("projectStatus").toString());
            params.put("pList", map.get("pList").toString());
            params.put("companyId", map.get("companyId").toString());
            createPrivateProjectService.updateProjectUser(params);
            projectManagerService.update(params);
            createPrivateProjectService.updateInfoDetail(params);
        }
    }
}
