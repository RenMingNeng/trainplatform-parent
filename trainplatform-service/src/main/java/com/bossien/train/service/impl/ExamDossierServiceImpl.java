package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.ExamDossierInfoMapper;
import com.bossien.train.domain.ExamDossierInfo;
import com.bossien.train.service.IExamDossierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by A on 2017/7/25.
 */

@Service
public class ExamDossierServiceImpl implements IExamDossierService {
    @Autowired
    private ExamDossierInfoMapper examDossierInfoMapper;


    @Override
    public void update(ExamDossierInfo examDossierInfo) {

        examDossierInfoMapper.update(examDossierInfo);
    }

    @Override
    public ExamDossierInfo selectOne(String projectId) {

        return examDossierInfoMapper.selectOne(projectId);
    }

    @Override
    public void delete(String projectId) {

        examDossierInfoMapper.delete(projectId);
    }

    @Override
    public void insert(ExamDossierInfo examDossierInfo) {

        examDossierInfoMapper.insert(examDossierInfo);
    }
}
