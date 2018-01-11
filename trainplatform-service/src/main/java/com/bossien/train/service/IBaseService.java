package com.bossien.train.service;

import com.bossien.train.domain.User;

import java.util.Map;

/**
 * Created by huangzhaoyong on 2017/8/1.
 */
public interface IBaseService<T> {

    T build(User user, T t);

    T build(String user_name, T t);

    T updateBuild(String user_name, T t);

    Map<String, Object> build(User user);

    Map<String, Object> build(String user_name);

    Map<String, Object> buildMap(User user);
}
