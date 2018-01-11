package com.bossien.train.dao.tp;

import com.bossien.train.domain.ExamScore;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 学员成绩信息
 * Created by Administrator on 2017/7/27.
 */
@Repository
public interface ExamScoreMapper {

    /**
     * 通过属性查询学员考试成绩记录
     * @param params
     * @return
     */
    List<ExamScore> selectExamScoreList(Map<String, Object> params);

    /**
     * 根据项目、用户分组查询最高成绩
     * @param params
     * @return
     */
    List<ExamScore> selectMastScoreByProjectAndUser(Map<String, Object> params);

    /**
     * 通过属性查询学员考试成绩数量
     * @param params
     * @return
     */
    int selectExamScoreCount(Map<String, Object> params);

    /**
     * insert添加
     * @param examScore
     */
    void insert(ExamScore examScore);

    /**
     * 通过属性查询考试学员数量
     * @param params
     * @return
     */
    int selectCount(Map<String, Object> params);

    /**
     * 批量删除
     * @param params
     */
    void deleteBatch(Map<String, Object> params);

    /**
     * 根据考试编号查询考试成绩
     * @param exam_no
     * @return
     */
    ExamScore selectByExamNo(String exam_no);

    /**
     * 查询考试人次、考试人数
     * @param params
     * @return
     */
    int selectCountGroupByProjectIdOrUserId(Map<String, Object> params);
}
