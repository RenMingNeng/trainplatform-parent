package com.bossien.train.listener;


import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.framework.mq.sender.AbstractSender;
import com.bossien.train.domain.UserLoginInfo;
import java.text.ParseException;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 按需复写父类方法，真实业务请直接继承AbstractListener
 * que 监听器
 * @author DF
 *
 */
@Component("queueSignInListener")
public class QueueSignInListener extends AbstractListener {

    // 操作mongodb的集合名,等同于mysql中的表
    private final String collectionName = "user_login_info";

    @Autowired
    private MongoOperations mongoTemplate;

    @Override
    public <V> void mapMessageHander(Map<String, V> map) throws ParseException {
        super.mapMessageHander(map);
        logger.info("queueSignInListener mapMessageHander map data is " + new Gson().toJson(map));

        String userId = (String) map.get("userId");
        // 根据用户id查找用户登陆信息条目
        UserLoginInfo userLoginInfo = mongoTemplate.findOne(new Query(Criteria.where("userId").is(userId)), UserLoginInfo.class, collectionName);
        if(null == userLoginInfo){
            // 用户首次登录,初始化
            userLoginInfo = new UserLoginInfo(userId, 1 , 0);
            mongoTemplate.insert(userLoginInfo, collectionName);
            return;
        }
        // 登陆次数加1
        Integer loginCount = userLoginInfo.getLoginCount()+1;
        // 修改符合条件的第一条记录
        mongoTemplate.updateFirst(new Query(Criteria.where("userId").is(userId)), new Update().set("loginCount", loginCount), collectionName);
    }
}
