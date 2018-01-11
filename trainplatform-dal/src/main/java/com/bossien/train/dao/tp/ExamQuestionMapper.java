package com.bossien.train.dao.tp;

import com.bossien.train.domain.ExamQuestion;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by A on 2017/7/25.
 */

@Repository
public interface ExamQuestionMapper {

    /**
     * 新增
     * @param examQuestion
     */
    void insert(ExamQuestion examQuestion);

    /**
     * 批量插入公司项目信息
     * @param examQuestions
     * @return
     */
    int insertBatch(List<ExamQuestion> examQuestions);

    /**
     * 查询列表
     * @param params
     * @return
     */
    List<String> selectList(Map<String, Object> params);

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
     * 批量删除
     * @param params
     */
    void deleteBatch(Map<String, Object> params);
}
