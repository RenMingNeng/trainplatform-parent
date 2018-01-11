package com.bossien.train.service;

import com.bossien.train.domain.ExamStrategy;
import com.bossien.train.domain.User;

import java.util.List;
import java.util.Map;

/**
 * 组卷策略表
 * Created by Administrator on 2017/7/27.
 */
public interface IExamStrategyService {
    /**
     * 通过projectId和roleId查询组卷策略
     * @param map
     * @return
     */
    ExamStrategy selectByProjectIdAndRoleId(Map<String,Object> map);
    ExamStrategy selectByProjectIdAndRoleId(String projectId, String roleId);
    /**
     * 通过projectId和roleId查询组卷策略(用于缓存)
     * @param
     * @return
     */
    ExamStrategy selectOneByProjectIdAndRoleId(String projectId, String roleId);

    /**
     * 通过projectId和userId查询组卷策略
     * @param
     * @return
     */
    ExamStrategy selectProjectIdAndUserId(String projectId, String userId);

    /**
     *组装组卷策略数据（单选，多选.......）
     * @param examStrategy
     * @return
     */
    List<Map<String,Object>> assembleExamStrategy(ExamStrategy examStrategy);

    /**
     * 批量添加考试组卷策略数据
     * @param params
     * @return
     */
    int insertBatch(Map<String, Object> params);
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
    void update(ExamStrategy examStrategy, User user);

    /**
     * 插入组卷策略信息
     * @param params
     * @return
     */
    int insert(Map<String, Object> params);

    /**
     * 查询列表
     * @param params
     * @return
     */
    List<ExamStrategy> selectList(Map<String,Object> params);
    /**
     * 根据projectId和roleIds确认数据是否最新
     * @param params
     */
    void confirmExamStrategy(Map<String, Object> params);

    /**
     * 新增项目是调用，查询出该项目所有首选角色的组卷策略中总题量最小的值，保证所有的组卷策略都合格
     * @param projectId
     * @return
     */
    Map<String,Object> selectMinStrategyByProject(String projectId);

    /**
     * 修改组卷策略
     * @param examStrategy
     * @param param
     * @param user
     */
    void updateExamStrategy(ExamStrategy examStrategy, Map<String, Object> param, User user);

    /**
     * 检查组卷策略是否合格，发布之前
     * @return
     */
    String checkStrategy(String projectId);

    /**
     * 修改组卷策略
     * @param examStrategy
     * @param param
     * @param user
     */
    void updateExamStrategy(ExamStrategy examStrategy, Map<String, Object> param, User user, String operateType, List<String> courseIds);
}
