package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.FeedbackMapper;
import com.bossien.train.domain.Feedback;
import com.bossien.train.service.IFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FeedbackServiceImpl implements IFeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public Integer insert(Feedback feedback) {
        return feedbackMapper.insert(feedback);
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {
        return feedbackMapper.selectCount(params);
    }

    @Override
    public List<Feedback> selectList(Map<String, Object> params) {
        return feedbackMapper.selectList(params);
    }

    @Override
    public void update(Feedback feedback) {
        feedbackMapper.update(feedback);
    }
}
