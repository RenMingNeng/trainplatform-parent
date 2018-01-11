package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.ExamPaperInfoMapper;
import com.bossien.train.domain.ExamPaperInfo;
import com.bossien.train.domain.ExamStrategy;
import com.bossien.train.service.IBaseService;
import com.bossien.train.service.IExamPaperInfoService;
import com.bossien.train.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by huangzhaoyong on 2017/7/25.
 */

@Service
public class ExamPaperInfoServiceImpl implements IExamPaperInfoService {

    @Autowired
    private ExamPaperInfoMapper examPaperInfoMapper;
    @Autowired
    private IBaseService<ExamPaperInfo> baseService;

    @Override
    public void insert(String exam_no, String exam_type, ExamStrategy examStrategy, String userId, String userName) {
        Map<String, Object> examPaperInfo = baseService.build(userName);
        examPaperInfo.put("examNo", exam_no);
        examPaperInfo.put("userId", userId);
        examPaperInfo.put("projectId", examStrategy.getProjectId());
        examPaperInfo.put("roleId", examStrategy.getRoleId());
        examPaperInfo.put("examType", exam_type);
        examPaperInfo.put("singleScore", examStrategy.getSingleScore());
        examPaperInfo.put("manyScore", examStrategy.getManyScore());
        examPaperInfo.put("judgeScore", examStrategy.getJudgeScore());
        examPaperInfo.put("filloutScore", examStrategy.getFilloutScore());
        examPaperInfo.put("quesAnsScore", examStrategy.getQuesAnsScore());
        examPaperInfo.put("passScore", examStrategy.getPassScore());
        examPaperInfo.put("totalScore", examStrategy.getTotalScore());
        examPaperInfo.put("examDuration", examStrategy.getExamDuration());
        examPaperInfo.put("examStatus", "1");
        examPaperInfo.put("necessaryHour", examStrategy.getNecessaryHour());
        examPaperInfo.put("createTime", DateUtils.formatDateTime(new Date()));
        examPaperInfoMapper.insert(examPaperInfo);
    }

    @Override
    public List<ExamPaperInfo> selectList(Map<String, Object> params) {

        return examPaperInfoMapper.selectList(params);
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {

        return examPaperInfoMapper.selectCount(params);
    }

    @Override
    public void update(String exam_no) {

        examPaperInfoMapper.update(exam_no);
    }

    @Override
    public ExamPaperInfo selectOne(Map<String, Object> params) {

        return examPaperInfoMapper.selectOne(params);
    }

    @Override
    public void deleteBatch(Map<String, Object> params) {
        examPaperInfoMapper.deleteBatch(params);
    }
}
