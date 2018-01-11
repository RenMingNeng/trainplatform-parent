package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.QuestionCollectionMapper;
import com.bossien.train.domain.User;
import com.bossien.train.service.IBaseService;
import com.bossien.train.service.IQuestionCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by huangzhaoyong on 2017/7/25.
 */

@Service
public class QuestionCollectionServiceImpl implements IQuestionCollectionService {

    @Autowired
    private QuestionCollectionMapper questionCollectionMapper;
    @Autowired
    private IBaseService baseService;

    @Override
    public void insert(Map<String, Object> params, User user) {
        params.putAll(baseService.build(user));
        questionCollectionMapper.insert(params);
    }

    @Override
    public List<String> selectList(Map<String, Object> params) {

        return questionCollectionMapper.selectList(params);
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {

        return questionCollectionMapper.selectCount(params);
    }

    @Override
    public Map<String, Object> selectOne(Map<String, Object> params) {

        return questionCollectionMapper.selectOne(params);
    }

    @Override
    public void delete(String question_id, String project_id) {

        questionCollectionMapper.delete(question_id, project_id);
    }
}
