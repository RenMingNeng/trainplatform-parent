package com.bossien.train.dao.tp;

import com.bossien.train.domain.ExerciseAnswers;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by A on 2017/7/25.
 */

@Repository
public interface ExerciseAnswersMapper {

    /**
     * 新增
     * @param exerciseAnswer
     */
    void insert(ExerciseAnswers exerciseAnswer);

    /**
     * 批量添加
     * @param exerciseAnswers
     */
    void insertBatch(List<ExerciseAnswers> exerciseAnswers);

    /**
     * 修改
     * @param params
     */
    void update(Map<String, Object> params);

    /**
     * 查询列表
     * @param params
     * @return
     */
    List<Map<String, Object>> selectList(Map<String, Object> params);

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
}
