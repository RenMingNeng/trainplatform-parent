package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.UserMapper;
import com.bossien.train.dao.tp.UserRoleMapper;
import com.bossien.train.domain.Role;
import com.bossien.train.domain.User;
import com.bossien.train.domain.UserExtra;
import com.bossien.train.domain.UserRole;
import com.bossien.train.domain.dto.CompanyUserMessage;
import com.bossien.train.service.ISequenceService;
import com.bossien.train.service.IUserExtraService;
import com.bossien.train.service.IUserService;
import com.bossien.train.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class UserExtraServiceImpl implements IUserExtraService {
    // 操作mongodb的集合名,等同于mysql中的表
    private final String collectionName_user_extra = "user_extra";

    @Autowired
    private MongoOperations mongoTemplate;
    public UserExtra build(UserExtra userExtra){
        userExtra.setOper_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        userExtra.setCreate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return  userExtra;
    }

    @Override
    public void insert(UserExtra userExtra) {
        //添加数据
        mongoTemplate.insert(userExtra,collectionName_user_extra);
    }

    @Override
    public void update(UserExtra userExtra) {
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("user_id").is(userExtra.getUser_id())));
        Update update =new Update();
        update.set("guide_mark",userExtra.getGuide_mark());
        //修改数据
        mongoTemplate.updateFirst(query,update,collectionName_user_extra);
    }

    @Override
    public UserExtra selectOne(String user_id) {
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("user_id").is(user_id)));
       UserExtra userExtra = mongoTemplate.findOne(query,UserExtra.class,collectionName_user_extra);

        return userExtra;
    }
}
