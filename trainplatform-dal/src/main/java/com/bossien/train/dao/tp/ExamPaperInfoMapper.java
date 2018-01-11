package com.bossien.train.dao.tp;

import com.bossien.train.domain.ExamPaperInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by A on 2017/7/25.
 */

@Repository
public interface ExamPaperInfoMapper {

    /**
     * 新增
     * @param params
     */
    void insert(Map<String, Object> params);

    /**
     * 查询列表
     * @param params
     * @return
     */
    List<ExamPaperInfo> selectList(Map<String, Object> params);

    /**
     * 修改
     * @param exam_no
     */
    void update(String exam_no);

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
    ExamPaperInfo selectOne(Map<String, Object> params);

    /**
     * 批量删除
     * @param params
     */
    void deleteBatch(Map<String, Object> params);
}
