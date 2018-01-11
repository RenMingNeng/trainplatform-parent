package com.bossien.train.dao.ex;

import com.bossien.train.domain.CourseQuestion;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by A on 2017/7/25.
 */

@Repository
public interface CourseQuestionMapper {

    /**
     * 查询列表
     */
    List<CourseQuestion> selectList(Map<String, Object> params);

    List<CourseQuestion> seletcQuestionCount(Map<String, Object> params);

    /**
     * 查询题库id列表
     */
    List<String> selectQuestionIdList(Map<String, Object> params);

    int insertSelective(CourseQuestion courseQuestion);

    int insertBatch(List<CourseQuestion> list);

    int updateByQuestionId(CourseQuestion courseQuestion);

    int deleteByPrimaryKey(Map<String, Object> params);

    int selectIntQuestionIdCount(Map<String, Object> params);

    CourseQuestion selectOne(Map<String, Object> params);

    CourseQuestion selectById(Map<String, Object> params);
}
