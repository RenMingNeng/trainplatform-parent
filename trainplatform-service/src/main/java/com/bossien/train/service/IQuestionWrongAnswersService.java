package com.bossien.train.service;

import com.bossien.train.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Created by huangzhaoyong on 2017/7/25.
 */
public interface IQuestionWrongAnswersService {
    /**
     * 新增
     * @param params
     */
    void insert(Map<String, Object> params, User user);

    /**
     * 查询列表
     * @param params
     * @return
     */
    List<String> selectList(Map<String, Object> params);

    /**
     * 易错题
     * @param params
     * @return
     */
    List<String> selectEasyWrongList(Map<String, Object> params);

    /**
     * 统计
     * @param params
     * @return
     */
    Integer selectCount(Map<String, Object> params);

    /**
     * 查询单条记录
     * @param params
     * @return
     */
    Map<String, Object> selectOne(Map<String, Object> params);

    /**
     * 删除
     * @param question_id
     */
    void delete(String question_id, String project_id);
}
