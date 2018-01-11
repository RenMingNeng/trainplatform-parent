package com.bossien.train.service;

import com.bossien.train.domain.ExamPaperInfo;
import com.bossien.train.domain.ExamStrategy;

import java.util.List;
import java.util.Map;

/**
 * Created by huangzhaoyong on 2017/7/25.
 */
public interface IExamPaperInfoService {
    /**
     * 新增
     * @param exam_no
     * @param exam_type
     * @param examStrategy
     * @param userId
     * @param userName
     */
    void insert(String exam_no, String exam_type, ExamStrategy examStrategy, String userId, String userName);

    /**
     * 查询列表
     * @param params
     * @return
     */
    List<ExamPaperInfo> selectList(Map<String, Object> params);

    /**
     * 统计
     * @param params
     * @return
     */
    Integer selectCount(Map<String, Object> params);

    /**
     * 修改
     * @param exam_no
     */
    void update(String exam_no);

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
