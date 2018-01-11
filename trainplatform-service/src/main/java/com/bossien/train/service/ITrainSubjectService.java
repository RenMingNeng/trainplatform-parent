package com.bossien.train.service;

import com.bossien.train.domain.Page;
import com.bossien.train.domain.TrainSubject;
import com.bossien.train.domain.dto.TrainSubjectDTO;
import com.bossien.train.domain.dto.TrainSubjectMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by A on zhaoli 2017/8/2.
 */

public interface ITrainSubjectService {
    /**
     * 新增
     */
    void insertMessage(TrainSubjectMessage trainSubjectMessage);

    void insert(TrainSubject dto);

    int insertBatch(List<TrainSubject> list);

    int updateBatch(List<TrainSubject> list);

    /**
     * 修改
     */
    void updateMessage(TrainSubjectMessage trainSubjectMessage);

    void update(Map<String, Object> params);

    /**
     * 删除
     */
    void deleteBySourse(Map<String, Object> params);

    /**
     * 查询列表
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

    /**
     * 根据companyId查询受训主题
     *
     * @param params
     * @return
     */
    List<TrainSubject> selectByCompanyId(Map<String, Object> params);


    void deleteByCodes(String[] codes);

    /**
     * 查询单位热门主题
     *
     * @param params
     * @return
     */
    List<TrainSubject> selectHotSubject(Map<String, Object> params);

    Integer select_count(Map<String, Object> params);

    List<Map<String, Object>> select_list(Map<String, Object> params);

    /**
     * 更具授权id和公司id查询
     *
     * @param params
     * @return
     */
    TrainSubject selectSubject(Map<String, Object> params);

    TrainSubject selectById(Map<String, Object> params);

    void TarinSubjcetSynchronized(List<Map<String, String>> dataObj, TrainSubjectDTO dto);
}
