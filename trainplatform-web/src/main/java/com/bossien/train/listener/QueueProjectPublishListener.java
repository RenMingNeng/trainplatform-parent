package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.framework.utils.SendJPushContentUtils;
import com.bossien.train.domain.ProjectInfo;
import com.bossien.train.service.IProjectInfoService;
import com.bossien.train.service.IProjectUserService;
import com.bossien.train.service.IUserService;
import com.bossien.train.util.MapUtils;
import com.bossien.train.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 按需复写父类方法，真实业务请直接继承AbstractListener
 * que 监听器
 *
 * 发布项目监听
 *
 * @author DF
 *
 */
@Component("queueProjectPublishListener")
public class QueueProjectPublishListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(QueueProjectPublishListener.class);
    @Autowired
    private IProjectUserService projectUserService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private IUserService userService;

    @Override
    public <V> void mapMessageHander(Map<String, V> map) throws ParseException {
        super.mapMessageHander(map);

        String projectId = (String) map.get("projectId");
        if(StringUtils.isEmpty(projectId)){
            return;
        }

        //项目信息
        ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(projectId);
        if(null == projectInfo){
            return;
        }

        //查询用户
        Map<String, Object> params = MapUtils.newConcurrentHashMap();
        params.put("projectId", projectId);
        List<String> userIds = projectUserService.selectUserIds(params);
        if(null == userIds || userIds.size() < 1){
            return;
        }

        //分批发送
        List<List<String>> userIdsList = StringUtil.subList(userIds, 1000);
        List<String> userAccounts;
        for(List<String> users: userIdsList){
            //查询用户账号
            params = MapUtils.newConcurrentHashMap();
            params.put("userIds", users);
            userAccounts = userService.selectUserAccountList(params);
            if(null == userAccounts || userAccounts.size() < 1){
                continue;
            }
            //发送消息
            SendJPushContentUtils.toContent(projectInfo.getCreateUser(), projectInfo.getCreateTime(),
                    projectInfo.getProjectName(), projectInfo.getProjectTrainTime(),
                    projectInfo.getProjectExamTime(), userAccounts);
        }
    }

}
