package com.bossien.train.service;

import com.bossien.train.domain.CompanyTrainSubject;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/21.
 */
public interface ICompanyTrainSubjectService {
    int deleteByPrimaryKey(Integer intId);

    int deleteByTrainSubjectId(Map<String, Object> map);

    int insert(CompanyTrainSubject record);

    int insertSelective(CompanyTrainSubject record);

    int insertBatch(List<CompanyTrainSubject> list);

    CompanyTrainSubject selectByPrimaryKey(String intId);

    List<CompanyTrainSubject> selectByintTrainSubjectId(String trainSubjectId);

    List<String> selectByCompanyId(String intCompanyId);

    int updateByPrimaryKeySelective(CompanyTrainSubject record);

    int updateByTrainSubjectId(CompanyTrainSubject record);

    List<CompanyTrainSubject> selectByAllId(Map<String, Object> map);

    CompanyTrainSubject selectById(Map<String, Object> map);

    int updateByPrimaryKey(CompanyTrainSubject record);

    List<String> selectCompanySubjectId(String company_id);
}
