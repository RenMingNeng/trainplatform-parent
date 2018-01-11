package com.bossien.train.service;

import com.bossien.train.domain.Feedback;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/26.
 */
public interface IFeedbackService {

    Integer insert(Feedback feedback);

    Integer selectCount(Map<String, Object> params);

    List<Feedback> selectList(Map<String, Object> params);

    void update(Feedback feedback);
}
