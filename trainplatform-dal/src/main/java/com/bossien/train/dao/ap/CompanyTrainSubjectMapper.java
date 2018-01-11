package com.bossien.train.dao.ap;

import com.bossien.train.domain.CompanyTrainSubject;

import java.util.List;
import java.util.Map;

public interface CompanyTrainSubjectMapper {
    int deleteByPrimaryKey(Integer intId);

    int deleteByTrainSubjectId(Map<String, Object> map);

    int insert(CompanyTrainSubject record);

    int insertSelective(CompanyTrainSubject record);

    int insertBatch(List<CompanyTrainSubject> list);

    CompanyTrainSubject selectByPrimaryKey(String intId);

    List<CompanyTrainSubject> selectByintTrainSubjectId(String trainSubjectId);

    List<String> selectByCompanyId(String intCompanyId);

    List<CompanyTrainSubject> selectByAllId(Map<String, Object> map);

    CompanyTrainSubject selectById(Map<String, Object> map);

    int updateByPrimaryKeySelective(CompanyTrainSubject record);

    int updateByTrainSubjectId(CompanyTrainSubject record);


    int updateByPrimaryKey(CompanyTrainSubject record);
}