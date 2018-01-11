package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.domain.UserLoginInfo;
import com.google.gson.Gson;
import java.text.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Component("queueSignOutListener")
public class QueueSignOutListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(QueueSignOutListener.class);

    // 操作mongodb的集合名,等同于mysql中的表
    private final String collectionName = "user_login_info";

    @Autowired
    private MongoOperations mongoTemplate;

    @Override
    public <V> void mapMessageHander(Map<String, V> map) throws ParseException {
        super.mapMessageHander(map);
        logger.info("queueSignOutListener mapMessageHander map data is " + new Gson().toJson(map));

        String userId = (String) map.get("userId");
        Long accessTime = (Long) map.get("accessTime");
        // 根据用户id查找用户登陆信息条目
        UserLoginInfo userLoginInfo = mongoTemplate.findOne(new Query(Criteria.where("userId").is(userId)), UserLoginInfo.class, collectionName);
        if(null == userLoginInfo) {
            return;
        }
        // 登陆时长累加
        Long duration = userLoginInfo.getDuration() + accessTime;
        // 修改符合条件的第一条记录
        mongoTemplate.updateFirst(new Query(Criteria.where("userId").is(userId)), new Update().set("duration", duration), collectionName);

    }
}
