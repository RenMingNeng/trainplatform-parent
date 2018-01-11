package com.bossien.train.service.impl;

import com.bossien.train.dao.ap.CompanyTrainRoleMapper;
import com.bossien.train.domain.CompanyTrainRole;
import com.bossien.train.domain.TrainRole;
import com.bossien.train.service.ICompanyTrainRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/21.
 */
@Service
public class CompanyTrainRoleServiceImpl implements ICompanyTrainRoleService {

    @Autowired
    private CompanyTrainRoleMapper companyTrainRoleMapper;

    @Override
    public int deleteByPrimaryKey(Integer intId) {
        return companyTrainRoleMapper.deleteByPrimaryKey(intId);
    }

    @Override
    public int deleteByTrainRoleId(String[] intTrainRoleId) {

        return companyTrainRoleMapper.deleteByTrainRoleId(intTrainRoleId);
    }

    @Override
    public int deleteByAllId(Map<String, Object> map) {
        return companyTrainRoleMapper.deleteByAllId(map);
    }

    @Override
    public int insert(CompanyTrainRole record) {
        return companyTrainRoleMapper.insert(record);
    }

    @Override
    public int insertBatch(List<CompanyTrainRole> list) {
        return companyTrainRoleMapper.insertBatch(list);
    }

    @Override
    public int insertSelective(CompanyTrainRole record) {
        return companyTrainRoleMapper.insertSelective(record);
    }

    @Override
    public CompanyTrainRole selectByPrimaryKey(String intId) {
        return companyTrainRoleMapper.selectByPrimaryKey(intId);
    }

    @Override
    public CompanyTrainRole selectByTrainRoleId(String intTrainRoleId) {
        return companyTrainRoleMapper.selectByTrainRoleId(intTrainRoleId);
    }

    @Override
    public List<String> selectByCompanyId(String intId) {
        return companyTrainRoleMapper.selectByCompanyId(intId);
    }

    @Override
    public int updateByPrimaryKeySelective(CompanyTrainRole record) {
        return companyTrainRoleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(CompanyTrainRole record) {
        return companyTrainRoleMapper.updateByPrimaryKey(record);
    }


    @Override
    public CompanyTrainRole selectById(Map<String, Object> map) {
        return companyTrainRoleMapper.selectById(map);
    }


}
