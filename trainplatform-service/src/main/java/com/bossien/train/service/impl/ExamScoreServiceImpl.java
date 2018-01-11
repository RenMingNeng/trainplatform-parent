package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.ExamScoreMapper;
import com.bossien.train.domain.ExamScore;
import com.bossien.train.service.IExamScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/27.
 */
@Service
public class ExamScoreServiceImpl implements IExamScoreService {
    @Autowired
    private ExamScoreMapper examScoreMapper;
    @Override
    public List<ExamScore> selectExamScoreList(Map<String, Object> params) {
        return examScoreMapper.selectExamScoreList(params);
    }

    @Override
    public int selectExamScoreCount(Map<String, Object> params) {
        return examScoreMapper.selectExamScoreCount(params);
    }

    @Override
    public int selectCount(Map<String, Object> params) {
        return examScoreMapper.selectCount(params);
    }

    @Override
    public void insert(ExamScore examScore) {

        examScoreMapper.insert(examScore);
    }

    @Override
    public void deleteBatch(Map<String, Object> params) {
        examScoreMapper.deleteBatch(params);
    }

    @Override
    public ExamScore selectByExamNo(String exam_no) {

        return examScoreMapper.selectByExamNo(exam_no);
    }

    @Override
    public int selectCountGroupByProjectIdOrUserId(Map<String, Object> params) {

        return examScoreMapper.selectCountGroupByProjectIdOrUserId(params);
    }
}
