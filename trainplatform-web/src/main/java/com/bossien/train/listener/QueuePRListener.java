package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.service.IProjectCustomService;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 按需复写父类方法，真实业务请直接继承AbstractListener
 * que 监听器
 * @author DF
 *
 */
@Component("queuePRListener")
public class QueuePRListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(QueuePRListener.class);

    @Autowired
    private IProjectCustomService projectCustomService;

    @Override
    public <V> void mapMessageHander(Map<String, V> map) throws ParseException {
        super.mapMessageHander(map);
        String operateType = (String)map.get("operateType");
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        if("course".equals(operateType)){
            params.clear();
            params.put("projectId",map.get("projectId").toString());
            params.put("projectType",map.get("projectType").toString());
            params.put("userName",map.get("userName").toString());
            params.put("roleCourses",map.get("roleCourses").toString());
            projectCustomService.updateRoleOrCourseInfo(params);
        }else if("user".equals(operateType)){
            params.clear();
            params.put("projectId",map.get("projectId").toString());
            params.put("projectType",map.get("projectType").toString());
            params.put("userName",map.get("userName").toString());
            params.put("roleUsers",map.get("roleUsers").toString());
            params.put("roles",map.get("roles").toString());
            projectCustomService.updateRoleOrUserInfo(params);
        }
    }
}
