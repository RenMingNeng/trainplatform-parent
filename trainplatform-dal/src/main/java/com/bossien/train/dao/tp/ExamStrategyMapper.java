package com.bossien.train.dao.tp;

import com.bossien.train.domain.ExamStrategy;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 组卷策略表
 * Created by Administrator on 2017/7/27.
 */
@Repository
public interface ExamStrategyMapper {
    /**
     * 通过projectId和roleId查询组卷策略
     * @param map
     * @return
     */
     ExamStrategy selectByProjectIdAndRoleId(Map<String,Object> map);
    /**
     * 通过projectId和roleId查询组卷策略(用于缓存)
     * @param
     * @return
     */
     ExamStrategy selectOneByProjectIdAndRoleId(@Param("projectId") String projectId, @Param("roleId") String roleId);
    /**
     * 插入组卷策略信息
     * @param examStrategy
     * @return
     */
    int insert(ExamStrategy examStrategy);

    /**
     * 根据角色不同批量插入组卷策略信息
     * @param item
     * @return
     */
     int insertBatch(List<ExamStrategy> item);

    /**
     * 根据projectId删除
     * @param map
     * @return
     */
     int deleteByProjectId(Map<String,Object> map);

    /**
     * 修改
     * @param examStrategy
     */
    void update(ExamStrategy examStrategy);

    /**
     * 查询列表
     * @param map
     * @return
     */
    List<ExamStrategy> selectList(Map<String,Object> map);

    /**
     * 根据projectId和roleId查询主键ids
     * @param params
     * @return
     */
    List<String> selectIds(Map<String, Object> params);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int delete(String id);
}
