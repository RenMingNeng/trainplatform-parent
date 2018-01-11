package com.bossien.train.dao.ex;

import com.bossien.train.domain.Question;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by A on 2017/7/25.
 */

@Repository
public interface QuestionMapper {

    /**
     * 查询列表
     *
     * @param params
     * @return
     */
    List<Question> selectList(Map<String, Object> params);

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
    Question selectOne(Map<String, Object> params);

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

    int insertSelective(Question question);

    int updateByPrimaryKeySelective(Question question);

    int insertBatch(List<Question> list);

    int updateBatch(List<Question> list);

    int deleteByPrimaryKey(Map<String, Object> params);

    int deleteByBatch(Map<String, Object> params);
}
