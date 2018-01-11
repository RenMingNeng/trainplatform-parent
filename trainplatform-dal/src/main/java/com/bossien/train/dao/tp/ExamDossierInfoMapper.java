package com.bossien.train.dao.tp;

import com.bossien.train.domain.ExamDossierInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by A on 2017/7/25.
 *
 * 考试项目统计表
 */

@Repository
public interface ExamDossierInfoMapper {

    /**
     * 新增
     * @param examDossierInfo
     */
    void insert(ExamDossierInfo examDossierInfo);

    /**
     * 新增
     * @param examDossierInfos
     */
    void insertBatch(List<ExamDossierInfo> examDossierInfos);

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
     * 删除
     * @param projectId
     */
    void delete(String projectId);
}
