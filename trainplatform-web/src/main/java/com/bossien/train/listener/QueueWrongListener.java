package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Map;

/**
 * 按需复写父类方法，真实业务请直接继承AbstractListener
 * que 监听器
 *
 * 我的错题 监听器
 *
 * @author DF
 *
 */
@Component("queueWrongListener")
public class QueueWrongListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(QueueWrongListener.class);

    // 操作mongodb的集合名,等同于mysql中的表
    private final String collectionName_question_wrong_answers = "question_wrong_answers";

    @Autowired
    private MongoOperations mongoTemplate;

    @Override
    public <V> void mapMessageHander(Map<String, V> map) throws ParseException {
        super.mapMessageHander(map);

        String project_id = String.valueOf( map.get("project_id"));
        String question_id = String.valueOf( map.get("question_id"));
        String user_id = String.valueOf( map.get("user_id"));
        if (null == question_id || question_id.equals("") || question_id.equals("null") ||
                null == project_id || project_id.equals("") || project_id.equals("null") ||
                null == user_id || user_id.equals("") || user_id.equals("null")){
            return;
        }

        Query query = new Query(new Criteria().andOperator(
                        Criteria.where("project_id").is(project_id),
                        Criteria.where("question_id").is(question_id),
                        Criteria.where("user_id").is(user_id)));
        mongoTemplate.remove(query, collectionName_question_wrong_answers);
    }
}
