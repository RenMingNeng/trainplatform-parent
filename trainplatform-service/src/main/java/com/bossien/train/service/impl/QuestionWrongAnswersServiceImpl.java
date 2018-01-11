package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.QuestionWrongAnswersMapper;
import com.bossien.train.domain.User;
import com.bossien.train.service.IBaseService;
import com.bossien.train.service.IQuestionWrongAnswersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by huangzhaoyong on 2017/7/25.
 */

@Service
public class QuestionWrongAnswersServiceImpl implements IQuestionWrongAnswersService {

    @Autowired
    private QuestionWrongAnswersMapper questionWrongAnswersMapper;
    @Autowired
    private IBaseService baseService;

    @Override
    public void insert(Map<String, Object> params, User user) {
        int count = questionWrongAnswersMapper.selectCount(params);
        if(count < 3){//錯題超過三次不存
            params.putAll(baseService.build(user));
            questionWrongAnswersMapper.insert(params);
        }
    }

    @Override
    public List<String> selectList(Map<String, Object> params) {

        return questionWrongAnswersMapper.selectList(params);
    }

    @Override
    public List<String> selectEasyWrongList(Map<String, Object> params) {

        return questionWrongAnswersMapper.selectEasyWrongList(params);
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {

        return questionWrongAnswersMapper.selectCount(params);
    }

    @Override
    public Map<String, Object> selectOne(Map<String, Object> params) {

        return questionWrongAnswersMapper.selectOne(params);
    }

    @Override
    public void delete(String question_id, String project_id) {

        questionWrongAnswersMapper.delete(question_id, project_id);
    }
}
