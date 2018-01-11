package com.bossien.train.dao.ap;

import com.bossien.train.domain.TrainSubject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by zhaoli on 2017/8/2.
 */

@Repository
public interface TrainSubjectMapper {

    /**
     * 新增
     *
     * @param obj
     */
    void insert(TrainSubject obj);

    /**
     * 修改
     *
     * @param params
     */
    void update(Map<String, Object> params);

    void updateMessage(Map<String, Object> params);

    int insertBatch(List<TrainSubject> list);

    int updateBatch(List<TrainSubject> list);

    /**
     * 分页查询列表
     *
     * @param params
     * @return
     */
    List<TrainSubject> selectList(Map<String, Object> params);

    /**
     * 验证是否重复
     */
    List<TrainSubject> selectVerify(Map<String, Object> params);

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
    TrainSubject selectOne(TrainSubject params);

    void deleteBySourse(Map<String, Object> params);

    void deleteByCodes(@Param("codes") String[] codes);

    /**
     * 根据companyId查询受训主题
     *
     * @param params
     * @return
     */
    List<TrainSubject> selectByCompanyId(Map<String, Object> params);

    /**
     * 根据主题id获取主题集合
     *
     * @param subject_ids
     * @return
     */
    List<TrainSubject> selectSubjectByIds(List<String> subject_ids);

    TrainSubject selectSubject(Map<String, Object> params);

    TrainSubject selectById(Map<String, Object> params);

    List<Map<String, Object>> select_list(Map<String, Object> params);

    Integer select_count(Map<String, Object> params);

    List<TrainSubject> selectAll();
}
