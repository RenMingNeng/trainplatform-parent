package com.bossien.train.service;

import com.bossien.train.domain.ProjectExerciseOrder;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/27.
 */
public interface IProjectExerciseOrderService {
    /**
     * 通过属性查询项目学员练习排行记录
     * @param params
     * @return
     */
    List<Map<String, Object>> selectProjectExerciseOrderList(Map<String, Object> params);

    /**
     * 通过属性查询项目学员练习排行数量
     * @param params
     * @return
     */
    int selectProjectExerciseOrderCount(Map<String, Object> params);

    /**
     *批量添加学员练习统计信息
     * @param item
     * @return
     */
    int insertBatch(List<Map<String, Object>> item);

    /**
     * 根据项目id查询人员练习统计信息集合
     * @param param
     * @return
     */
    List<Map<String, Object>> selectExerciseInfoList(Map<String, Object> param);

    /**
     * 查询一条
     * @param params
     * @return
     */
    ProjectExerciseOrder selectOne(Map<String, Object> params);

    /**
     * 修改
     * @param projectExerciseOrder
     */
    void update(ProjectExerciseOrder projectExerciseOrder);

    /**
     * 根据属性删除信息
     * @param params
     * @return
     */
    int delete(Map<String, Object> params);

    /**
     * 根据项目Id和角色Ids查询id集合
     * @param params
     * @return
     */
    List<String> selectIdList(Map<String, Object> params);

    /**
     * 高级设置中修改项目个人练习详细信息
     * @param params
     * @return
     */
    int updateInfo(Map<String, Object> params);
    /**
     * 批量更新总题量字段
     * @param param
     */
    void updateBatch(List<Map<String,Object>> param);
}
