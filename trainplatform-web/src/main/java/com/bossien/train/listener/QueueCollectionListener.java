package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.domain.QuestionCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 按需复写父类方法，真实业务请直接继承AbstractListener
 * que 监听器
 *
 * 题库监听器
 *
 * @author DF
 *
 */
@Component("queueCollectionListener")
public class QueueCollectionListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(QueueCollectionListener.class);

    // 操作mongodb的集合名,等同于mysql中的表
    private final String   collectionName_question_collection = "question_collection";

    @Autowired
    private MongoOperations mongoTemplate;

    @Override
    public <V> void mapMessageHander(Map<String, V> map) throws ParseException {
        super.mapMessageHander(map);

        String type = String.valueOf( map.get("type"));
        if(null == type || type.equals("null") || type.equals("")){
            return;
        }
        String project_id = String.valueOf( map.get("project_id"));
        String question_id = String.valueOf( map.get("question_id"));
        if (null == question_id || question_id.equals("") || question_id.equals("null") ||
                null == project_id || project_id.equals("") || project_id.equals("null")){
            return;
        }
        if(type.equals("save")){
            String user_id = String.valueOf( map.get("user_id"));
            String user_name = String.valueOf( map.get("user_name"));
            if(null == user_id || user_id.equals("null") ||
                    null == user_name || user_name.equals("null")){
                return;
            }

            //判断重复
            long collectCount = mongoTemplate.count(new Query(
                    new Criteria().andOperator(
                        Criteria.where("project_id").is(project_id),
                        Criteria.where("question_id").is(question_id),
                        Criteria.where("user_id").is(user_id)
                    )),
                    collectionName_question_collection
            );
            if(collectCount > 0){
                return;
            }
            //添加
            QuestionCollection questionCollection = new QuestionCollection(
                    project_id,
                    question_id,
                    user_id,
                    user_name,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            mongoTemplate.insert(questionCollection, collectionName_question_collection);

        }else if(type.equals("delete")){
            String user_id = String.valueOf( map.get("user_id"));
            if(null == user_id || user_id.equals("") || user_id.equals("null")){
                return;
            }

            Criteria criteria = new Criteria().andOperator(
                    Criteria.where("project_id").is(project_id),
                    Criteria.where("question_id").is(question_id),
                    Criteria.where("user_id").is(user_id)
            );
            mongoTemplate.remove(new Query(criteria), collectionName_question_collection);
        }
    }
}
