package com.bossien.train.service;

import com.bossien.train.domain.ExamQuestion;
import com.bossien.train.domain.Question;
import com.bossien.train.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Created by huangzhaoyong on 2017/7/25.
 */
public interface IExamQuestionService {
    /**
     * 新增
     * @param examQuestion
     */
    void insert(ExamQuestion examQuestion);
//
//    /**
//     * 批量插入公司项目信息
//     * @param examQuestions
//     * @return
//     */
//    int insertBatch(List<ExamQuestion> examQuestions);
//
    /**
     * 查询列表
     * @param params
     * @return
     */
    List<String> selectList(Map<String, Object> params);
//
    /**
     * 根据参数查询试卷的题
     * @param params
     * @return
     */
    List<Question> selectQuestionList(Map<String, Object> params);
//
//    /**
//     * 统计
//     * @param params
//     * @return
//     */
//    Integer selectCount(Map<String, Object> params);
//
    /**
     * 查询单条记录
     * @param params
     * @return
     */
    String createPaper(Map<String, Object> params, User user);

    /**
     * 批量删除
     * @param params
     */
    void deleteBatch(Map<String, Object> params);
}
