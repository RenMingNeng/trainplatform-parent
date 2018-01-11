package com.bossien.train.service;

import com.bossien.train.domain.User;
import com.bossien.train.domain.UserExtra;
import com.bossien.train.domain.dto.CompanyUserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/31.
 */
public interface IUserExtraService {
    /**
     * 添加数据
     * @param userExtra
     */
    void insert(UserExtra userExtra) ;

    /**
     * 修改数据
     * @param userExtra
     */
    void update(UserExtra userExtra);

    /**
     * 根据user_id查询单条
     * @param user_id
     * @return
     */
    UserExtra selectOne(String user_id);
}
