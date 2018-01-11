package com.bossien.train.service;

import com.bossien.train.domain.ExamAnswers;
import com.bossien.train.domain.Question;
import com.bossien.train.domain.QuestionCollection;
import com.bossien.train.domain.dto.QuestionDTO;
import com.bossien.train.domain.dto.QuestionMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by huangzhaoyong on 2017/7/25.
 */
public interface IQuestionService {

    /**
     * 查询列表
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> selectList(Map<String, Object> params);

    /**
     * 查询列表
     *
     * @param params
     * @param collects 收藏集合
     * @param answers  考试答题集合
     * @return
     */
    List<Map<String, Object>> selectList(Map<String, Object> params,
                                         List<QuestionCollection> collects, List<ExamAnswers> answers);

    /**
     * 查询列表
     *
     * @param params
     * @param collects 收藏集合
     * @return
     */
    List<Map<String, Object>> selectList(Map<String, Object> params, List<QuestionCollection> collects);

    /**
     * 根据类型（单选、多选、判断专项练习）查询题库
     *
     * @param params
     * @param type
     * @param collects
     * @return
     */
    List<Map<String, Object>> selectListByType(Map<String, Object> params, String type, List<QuestionCollection> collects);

    /**
     * 统计
     *
     * @param params
     * @return
     */
    Integer selectCount(Map<String, Object> params);

    /**
     * 查询单条记录
     *
     * @param params
     * @return
     */
    Map<String, Object> selectOne(Map<String, Object> params);

    Question selectById(Map<String, Object> params);

    /**
     * 查询试题类型的数量
     *
     * @param params
     * @return
     */
    List<Integer> selectQuestionsTypeCount(Map<String, Object> params);

    /**
     * 查询试题类型数量
     *
     * @param params
     * @return
     */
    Map<String, Object> selectTypeCount(Map<String, Object> params);

    /**
     * 组装文件
     *
     * @param questions
     * @param collects  收藏
     * @param answers   答题记录
     * @return
     */
    int insertSelective(QuestionMessage questionMessage);

    int updateByPrimaryKeySelective(QuestionMessage questionMessage);

    int insertBatch(List<Question> list);

    int updateBatch(List<Question> list);

    int deleteByPrimaryKey(Map<String, Object> params);
}
