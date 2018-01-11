package com.bossien.train.service;

import com.bossien.train.domain.ProjectStatisticsInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/24.
 */
public interface IProjectStatisticsInfoService {


    ProjectStatisticsInfo statistics(ProjectStatisticsInfo project_statistics_info);

    /**
     * 查询列表
     * @param params
     * @return
     */
    List<Map<String, Object>> selectList(Map<String, Object> params);

    /**
     * 根据项目id查询
     * @param projectId
     * @return
     */
    List<ProjectStatisticsInfo> selectListByProjectId(String projectId);

    /**
     * 统计
     * @param params
     * @return
     */
    Integer selectCount(Map<String, Object> params);

    ProjectStatisticsInfo selectOne(Map<String, Object> params);
    ProjectStatisticsInfo selectOne(String projectId, String userId);

    Integer update(ProjectStatisticsInfo projectStatisticsInfo);

    Integer insert(ProjectStatisticsInfo projectStatisticsInfo);

    /**
     *批量插入个人信息统计表
     * @param item
     * @return
     */
    int insertBatch(List<Map<String, Object>> item);

    /**
     * 根据项目id查询人员统计信息集合
     * @param param
     * @return
     */
    List<Map<String, Object>> selectStatisticsInfoList(Map<String, Object> param);

    /**
     * 根据属性删除信息
     * @param params
     * @return
     */
    int delete(Map<String, Object> params);

    /**
     * 获取参与培训人次
     * @param params
     * @return
     */
    int selectJoinTrainOrExamUserCount(Map<String, Object> params);
    /**
     * 获取培训人数
     * @param params
     * @return
     */
    int selectTrainUserCount(Map<String, Object> params);

    /**
     * 完成培训人次
     * @param params
     * @return
     */
    int selectCompleteTrainUserCount(Map<String, Object> params);
    /**
     * 考试合格人次
     * @param params
     * @return
     */
    int selectPassExamUserCount(Map<String, Object> params);

    /**
     * 累计学时
     * @param params
     * @return
     */
    double selecttotalClassHour(Map<String, Object> params);

    /**
     * 年度学时统计
     * @param params
     * @return
     */
   double selecttotalYearClassHour(Map<String, Object> params);


    /**
     * 根据属性查询学时
     * @param params
     * @return
     */
    List<Map<String, Object>> selectListMap(Map<String, Object> params);

    /**
     * 根据项目Id和角色Ids查询id集合
     * @param params
     * @return
     */
    List<String> selectIdList(Map<String, Object> params);

    /**
     * 高级设置中修改项目个人档案详细信息
     * @param params
     * @return
     */
    int updateInfo(Map<String, Object> params);

    /**
     * 查询用户培训次数
     * @param params
     * @return
     */
    int selectTrainCount(Map<String, Object> params);

    /**
     * 查询项目用户的用户Id和用户姓名
     * @param projectId
     * @return
     */
    List<Map<String, Object>> selectUsers(String projectId);
    /**
     * 批量更新总题量字段
     * @param param
     */
    void updateBatch(List<Map<String,Object>> param);
}
