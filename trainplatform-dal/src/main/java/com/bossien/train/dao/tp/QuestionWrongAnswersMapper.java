package com.bossien.train.dao.tp;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by A on 2017/7/25.
 */

@Repository
public interface QuestionWrongAnswersMapper {

    /**
     * 新增
     * @param params
     */
    void insert(Map<String, Object> params);

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
     * 刪除
     * @param question_id
     * @param project_id
     */
    void delete(String question_id, String project_id);
}
