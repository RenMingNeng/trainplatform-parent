package com.bossien.train.service;

import com.bossien.train.domain.ExamDossierInfo;

/**
 * Created by A on 2017/7/25.
 */
public interface IExamDossierService {
    /**
     * 修改
     * @param examDossierInfo
     */
    void update(ExamDossierInfo examDossierInfo);

    /**
     * 查询单条记录
     * @param projectId
     * @return
     */
    ExamDossierInfo selectOne(String projectId);

    /**
     * 批量删除
     * @param projectId
     */
    void delete(String projectId);

    /**
     * 新增
     * @param examDossierInfo
     */
    void insert(ExamDossierInfo examDossierInfo);
}
