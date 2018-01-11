package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.service.ICreatePrivateProjectService;
import com.bossien.train.service.IProjectInfoService;
import org.apache.commons.lang.StringUtils;
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
@Component("queueCPPriListener")
public class QueueCPPriListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(QueueCPPriListener.class);


    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private ICreatePrivateProjectService createPrivateProjectService;


    @Override
    public <V> void mapMessageHander(Map<String, V> map) throws ParseException {
        super.mapMessageHander(map);
        String operateType = map.get("operateType").toString();
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        if ("saveProject".equals(operateType)) {
            params.clear();
            params.put("id", map.get("id").toString());
            params.put("companyId", map.get("companyId").toString());
            params.put("subject_name", map.get("subject_name").toString());
            params.put("trainBeginTime", map.get("trainBeginTime").toString());
            params.put("trainEndTime", map.get("trainEndTime").toString());
            params.put("exerciseBeginTime", map.get("exerciseBeginTime").toString());
            params.put("exerciseEndTime", map.get("exerciseEndTime").toString());
            params.put("examBeginTime", map.get("examBeginTime").toString());
            params.put("examEndTime", map.get("examEndTime").toString());
            params.put("intRetestTime", map.get("intRetestTime").toString());
            params.put("projectType", map.get("projectType").toString());
            params.put("person_count", 0);
            params.put("departments", map.get("departments").toString());
            params.put("user", map.get("user").toString());
            //保存项目用户
            createPrivateProjectService.insertProjectUser(params);
            //项目详情保存
            projectInfoService.save(params);
            //项目统计信息保存
            createPrivateProjectService.saveInfo(params);
        }
    }
}
