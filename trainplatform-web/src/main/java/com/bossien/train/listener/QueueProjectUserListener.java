package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.service.ICreatePrivateProjectService;
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
@Component("queueProjectUserListener")
public class QueueProjectUserListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(QueueProjectUserListener.class);

    @Autowired
    private ICreatePrivateProjectService createPrivateProjectService;


    @Override
    public <V> void mapMessageHander(Map<String, V> map) throws ParseException {
        super.mapMessageHander(map);
        String operateType = map.get("operateType").toString();
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
      if ("saveUser".equals(operateType)) {
            params.clear();
            params.put("id", map.get("id").toString());
            params.put("userList", (List<Map<String,Object>>) map.get("userList"));
            params.put("roleId", map.get("roleId").toString());
            params.put("roleName", map.get("roleName").toString());
            params.put("projectType", map.get("projectType").toString());
            createPrivateProjectService.saveProjectByUser(params);
        }else if ("deleteUser".equals(operateType)) {
            params.clear();
            params.put("id", map.get("id").toString());
            params.put("userIds", (List<String>) map.get("userIds"));
            params.put("projectType", map.get("projectType").toString());
            params.put("projectStatus", map.get("projectStatus").toString());
            createPrivateProjectService.deleteProjectByUser(params);
        }
    }


}
