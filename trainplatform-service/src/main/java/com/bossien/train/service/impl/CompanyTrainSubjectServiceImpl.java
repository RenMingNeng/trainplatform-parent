package com.bossien.train.service.impl;

import com.bossien.train.dao.ap.CompanyTrainSubjectMapper;
import com.bossien.train.domain.CompanyTrainSubject;
import com.bossien.train.service.ICompanyTrainSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/21.
 */
@Service
public class CompanyTrainSubjectServiceImpl implements ICompanyTrainSubjectService {

    @Autowired
    private CompanyTrainSubjectMapper companyTrainSubjectMapper;

    @Override
    public int deleteByPrimaryKey(Integer intId) {
        return companyTrainSubjectMapper.deleteByPrimaryKey(intId);
    }

    @Override
    public int deleteByTrainSubjectId(Map<String, Object> map) {
        return companyTrainSubjectMapper.deleteByTrainSubjectId(map);
    }

    @Override
    public int insert(CompanyTrainSubject record) {
        return companyTrainSubjectMapper.insert(record);
    }

    @Override
    public int insertSelective(CompanyTrainSubject record) {
        return companyTrainSubjectMapper.insertSelective(record);
    }

    @Override
    public int insertBatch(List<CompanyTrainSubject> list) {
        return companyTrainSubjectMapper.insertBatch(list);
    }

    @Override
    public CompanyTrainSubject selectByPrimaryKey(String intId) {
        return companyTrainSubjectMapper.selectByPrimaryKey(intId);
    }

    @Override
    public List<CompanyTrainSubject> selectByintTrainSubjectId(String trainSubjectId) {
        return companyTrainSubjectMapper.selectByintTrainSubjectId(trainSubjectId);
    }

    @Override
    public List<String> selectByCompanyId(String intCompanyId) {
        return companyTrainSubjectMapper.selectByCompanyId(intCompanyId);
    }

    @Override
    public int updateByPrimaryKeySelective(CompanyTrainSubject record) {
        return companyTrainSubjectMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByTrainSubjectId(CompanyTrainSubject record) {
        return companyTrainSubjectMapper.updateByTrainSubjectId(record);
    }

    @Override
    public List<CompanyTrainSubject> selectByAllId(Map<String, Object> map) {
        return companyTrainSubjectMapper.selectByAllId(map);
    }

    @Override
    public CompanyTrainSubject selectById(Map<String, Object> map) {
        return companyTrainSubjectMapper.selectById(map);
    }

    @Override
    public int updateByPrimaryKey(CompanyTrainSubject record) {
        return companyTrainSubjectMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<String> selectCompanySubjectId(String company_id) {
        return companyTrainSubjectMapper.selectByCompanyId(company_id);
    }

}
