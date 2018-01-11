package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.domain.ExamStrategy;
import com.bossien.train.domain.User;
import com.bossien.train.service.ICreatePrivateProjectService;
import com.bossien.train.service.IExamStrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 按需复写父类方法，真实业务请直接继承AbstractListener
 * que 监听器
 *
 * @author DF
 */
@Component("queueProjectCourseListener")
public class QueueProjectCourseListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(QueueProjectCourseListener.class);

    @Autowired
    private ICreatePrivateProjectService createPrivateProjectService;
    @Autowired
    private IExamStrategyService examStrategyService;


    @Override
    public <V> void mapMessageHander(Map<String, V> map) throws ParseException {
        super.mapMessageHander(map);
        String operateType = map.get("operateType").toString();
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        ExamStrategy examStrategy = null;
        User user = new User();
        user.setUserName(map.get("userName").toString());
        if ("saveCourse".equals(operateType)) {
            params.clear();
            //得到角色集合
            List<Map> roleList = (List<Map>)map.get("roleList");
            for (Map roleMap: roleList) {
                //修改组卷策略
                examStrategy = new ExamStrategy();
                roleMap.put("projectId",map.get("id").toString());
                examStrategy.setProjectId(map.get("id").toString());
                examStrategy.setRoleId(roleMap.get("roleId").toString());
                examStrategyService.updateExamStrategy(examStrategy, roleMap, user, operateType, (List<String>) map.get("courseList"));
            }
            params.put("id", map.get("id").toString());
            params.put("roleList", roleList);
            params.put("courseList", (List<String>) map.get("courseList"));
            params.put("userName", map.get("userName").toString());
            params.put("projectType", map.get("projectType").toString());
            createPrivateProjectService.saveProjectByCourse(params);
        }else if ("deleteCourse".equals(operateType)) {
            params.clear();
            params.put("projectId", map.get("id").toString());
            params.put("roleId", "-1");
            examStrategy = new ExamStrategy();
            examStrategy.setProjectId(map.get("id").toString());
            examStrategy.setRoleId("-1");
            examStrategyService.updateExamStrategy(examStrategy, params, user, operateType, (List<String>) map.get("courseIds"));
            params.clear();
            params.put("id", map.get("id").toString());
            params.put("courseIds", (List<String>) map.get("courseIds"));
            params.put("projectType", map.get("projectType").toString());
            params.put("userName", map.get("userName").toString());
            createPrivateProjectService.deleteProjectByCourse(params);
        }
    }


}
