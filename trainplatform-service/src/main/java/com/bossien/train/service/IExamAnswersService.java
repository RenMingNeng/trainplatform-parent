package com.bossien.train.service;

import com.bossien.train.domain.ExamPaperInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by huangzhaoyong on 2017/7/25.
 */
public interface IExamAnswersService {
    /**
     * 新增
     * @param detail
     * @param exam_no
     * @param user_name
     * @throws Exception
     */
    Map<String, Object> insert(String detail, String exam_no, String user_name, ExamPaperInfo examPaperInfo);

    /**
     * 查询列表
     * @param params
     * @return
     */
    List<Map<String, Object>> selectList(Map<String, Object> params);

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
    Map<String, Object> selectOne(Map<String, Object> params);
}
