package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.UserMapper;
import com.bossien.train.domain.Msg;
import com.bossien.train.domain.MsgText;
import com.bossien.train.domain.Page;
import com.bossien.train.domain.User;
import com.bossien.train.service.IMsgService;
import com.bossien.train.service.ISequenceService;
import com.bossien.train.util.CollectionUtils;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by huangzhaoyong on 2017/7/25.
 */

@Service
public class MsgServiceImpl implements IMsgService {
    /**
     * 发送者与接收者关联关系
     */
    private final String mongo_msg = "message";
    /**
     * 消息内容表
     */
    private final String mongo_msg_text = "message_text";
    @Autowired
    private MongoOperations mongoTemplate;
    @Autowired
    private ISequenceService sequenceService;
    @Autowired
    private UserMapper userMapper;

    @Override
    public void insert(Msg msg, MsgText msgText) {
        //添加：消息内容
        mongoTemplate.insert(msgText, mongo_msg_text);

        //添加：发送者与接收者关联关系
        mongoTemplate.insert(msg, mongo_msg);
    }

    @Override
    public void insert(List<Msg> msgs) {
        //添加：发送者与接收者关联关系
        mongoTemplate.insert(msgs, mongo_msg);
    }

    @Override
    public void insertList(MsgText msgText, User user, List<String> recCompanyIds, List<String> recDeptIds) {
        //生成消息Id并保存
        String msgId = sequenceService.generator();
        msgText.setMsgId(msgId);
        msgText.setCreateDate(DateUtils.formatDateTime(new Date()));
        mongoTemplate.insert(msgText, mongo_msg_text);

        //查询所有用户
        Set<String> setList = new HashSet<String>();
        Map<String, Object> params = MapUtils.newHashMap();

        //查询公司中的人
        if(null != recCompanyIds && recCompanyIds.size() > 0){
            params.put("companyIds", recCompanyIds);
            params.put("is_valid", User.IsValid.TYPE_1.getValue());
            setList.addAll(userMapper.selectUserIds(params));
        }

        //查询部门中的人
        if(null != recCompanyIds && recCompanyIds.size() > 0){
            params = MapUtils.newHashMap();
            params.put("deptIds", recDeptIds);
            params.put("is_valid", User.IsValid.TYPE_1.getValue());
            setList.addAll(userMapper.selectUserIds(params));
        }

        //添加自己
        setList.add(user.getId());

        //添加消息
        Iterator<String> it = setList.iterator();
        List<Msg> msgs = new ArrayList<Msg>();
        while (it.hasNext()){
            String userId = it.next();
            msgs.add(new Msg(user.getId(), user.getUserName(), userId, msgId));
        }
        insert(msgs);
    }

    @Override
    public void update(String recId, String msgId) {
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("recId").is(recId),
                Criteria.where("msgId").is(msgId)
        ));
        Update update =new Update();
        update.set("status", Msg.MsgStatus.status_2.getValue());
        mongoTemplate.updateFirst(query, update, mongo_msg);
    }

    @Override
    public Page<Map<String, Object>> queryForPagination(Map<String,Object> param, Integer pageNum, Integer pageSize, User user) {
        String recId =(String)param.get("userId");
        Integer count =selectCount(recId);
        Page<Map<String, Object>> page = new Page(count, pageNum, pageSize);
        List<Map<String, Object>> list = selectList(param, page.getStartNum().longValue(), page.getPageSize().longValue());
        page.setDataList(list);
        return page;
    }

    @Override
    public List<Map<String, Object>> selectList(Map<String,Object> param) {
        return selectList(param, 0L, 0L);
    }

    @Override
    public List<Map<String, Object>> selectList(Map<String,Object> param, Long start, Long size) {
        String recId =(String)param.get("userId");
        String msgTitle =(String)param.get("MsgTitle");

        //发送者与接收者关联关系 列表
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("recId").is(recId)
        ));
        List<Msg> msgList = mongoTemplate.find(query, Msg.class, mongo_msg);
        Set<String> msgIds = new HashSet<String>();
        for(Msg msg: msgList){
            msgIds.add(msg.getMsgId());
        }
        if(msgIds.size() < 1){
            return new ArrayList<Map<String, Object>>();
        }


        //消息内容表 列表
        Aggregation aggregation;
        if(start == 0 && size == 0){
            if(msgTitle != null){
                Criteria criteria = new Criteria().andOperator(
                        Criteria.where("msgId").in(msgIds),
                        Criteria.where("msgTitle").regex(".*?\\" + msgTitle + ".*")
                );
                aggregation = Aggregation.newAggregation(
                        Aggregation.match(criteria),
                        Aggregation.sort(new Sort(
                                new Sort.Order(Sort.Direction.ASC, "isTop"),
                                new Sort.Order(Sort.Direction.DESC, "createDate"))
                        )
                );
            }else{
                aggregation = Aggregation.newAggregation(
                        Aggregation.match(Criteria.where("msgId").in(msgIds)),
                        Aggregation.sort(new Sort(
                                new Sort.Order(Sort.Direction.ASC, "isTop"),
                                new Sort.Order(Sort.Direction.DESC, "createDate"))
                        )
                );
            }
        }else{
            if(msgTitle != null) {
                Criteria criteria = new Criteria().andOperator(
                        Criteria.where("msgId").in(msgIds),
                        Criteria.where("msgTitle").regex(".*?\\" + msgTitle + ".*")
                );
                aggregation = Aggregation.newAggregation(
                        Aggregation.match(criteria),
                        Aggregation.sort(new Sort(
                                new Sort.Order(Sort.Direction.ASC, "isTop"),
                                new Sort.Order(Sort.Direction.DESC, "createDate"))
                        ),
                        Aggregation.skip(start),
                        Aggregation.limit(size)
                );
            }else{
                aggregation = Aggregation.newAggregation(
                        Aggregation.match(Criteria.where("msgId").in(msgIds)),
                        Aggregation.sort(new Sort(
                                new Sort.Order(Sort.Direction.ASC, "isTop"),
                                new Sort.Order(Sort.Direction.DESC, "createDate"))
                        ),
                        Aggregation.skip(start),
                        Aggregation.limit(size)
                );
            }

        }
        AggregationResults<MsgText> aggregationResults = mongoTemplate.aggregate(aggregation, mongo_msg_text, MsgText.class);
        List<MsgText> msgTexts = aggregationResults.getMappedResults();

        //组装数据
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, List<Msg>> listMap = CollectionUtils.listToMap(msgList, "msgId");
        Map<String, Object> data;
        List<Msg> msgs;
        for(MsgText msgText: msgTexts){
            data = MapUtils.newHashMap();
            MapUtils.putAll(data, msgText);
            msgs = listMap.get(msgText.getMsgId());
            if(null != msgs && msgs.size() > 0){
                MapUtils.putAll(data, msgs.get(0));
            }
            list.add(data);
        }
        return list;
    }

    @Override
    public Integer selectCount(String recId) {
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("recId").is(recId)
        ));
        Long count = mongoTemplate.count(query, Msg.class, mongo_msg);
        return count.intValue();
    }

    @Override
    public MsgText selectOne(String msgId) {
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("msgId").is(msgId)
        ));
        return mongoTemplate.findOne(query, MsgText.class, mongo_msg_text);
    }

    @Override
    public void deleteBatch(List<String> msgIds) {
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("msgId").in(msgIds)
        ));
        mongoTemplate.remove(query, MsgText.class, mongo_msg_text);
    }
}
