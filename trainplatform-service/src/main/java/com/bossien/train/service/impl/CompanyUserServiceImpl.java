package com.bossien.train.service.impl;

import com.bossien.train.dao.ap.CompanyUserMapper;
import com.bossien.train.domain.CompanyUser;
import com.bossien.train.domain.dto.CompanyUserMessage;
import com.bossien.train.service.ICompanyUserService;
import com.bossien.train.util.PropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/11.
 */
@Service
public class CompanyUserServiceImpl implements ICompanyUserService {

    @Autowired
    private CompanyUserMapper companyUserMapper;



    @Override
    public int deleteByPrimaryKey(Map<String, Object> map) {
        return companyUserMapper.deleteByPrimaryKey(map);
    }

    @Override
    public int insert(CompanyUser record) {
        return companyUserMapper.insert(record);
    }

    @Override
    public int insertMessage(CompanyUserMessage companyUserMessage) {
        CompanyUser record = new CompanyUser();
        record.setIntId(companyUserMessage.getId());
        record.setVarAccount(companyUserMessage.getAccount());
        record.setVarPasswd(companyUserMessage.getPasswd());
        record.setChrIsValid(companyUserMessage.getIsValid());
        record.setVarName(companyUserMessage.getName());
        record.setChrSex(companyUserMessage.getSex());
        record.setChrSupporter(companyUserMessage.getSupporter());
        record.setVarMobileNo(companyUserMessage.getMobileNo());
        record.setChrUserType(companyUserMessage.getUserType());
        record.setVarDepartmentId(companyUserMessage.getDepartmentId());
        record.setChrRegistType(companyUserMessage.getRegistType());
        record.setDatRegistDate(companyUserMessage.getRegistDate());
        record.setIntCompanyId(companyUserMessage.getCompanyId());
        record.setDatCreateDate(companyUserMessage.getCreateDate());
        record.setVarCreateUser(companyUserMessage.getCreateUser());
        record.setVarOperUser(companyUserMessage.getOperUser());
        record.setDatOperDate(companyUserMessage.getOperDate());
        companyUserMapper.insertSelective(record);
        return 0;
    }

    @Override
    public CompanyUser selectByOne(Map<String, Object> params) {
        return companyUserMapper.selectByOne(params);
    }

    @Override
    public CompanyUser selectById(Map<String, Object> params) {
        return companyUserMapper.selectById(params);
    }

    @Override
    public int updateMessage(CompanyUserMessage companyUserMessage) {
        CompanyUser record = new CompanyUser();
        record.setIntId(companyUserMessage.getId());
        record.setVarAccount(companyUserMessage.getAccount());
        record.setVarPasswd(companyUserMessage.getPasswd());
        record.setChrIsValid(companyUserMessage.getIsValid());
        record.setVarName(companyUserMessage.getName());
        record.setChrSex(companyUserMessage.getSex());
        record.setChrSupporter(companyUserMessage.getSupporter());
        record.setVarMobileNo(companyUserMessage.getMobileNo());
        record.setChrUserType(companyUserMessage.getUserType());
        record.setIntCompanyId(companyUserMessage.getCompanyId());
        record.setVarDepartmentId(companyUserMessage.getDepartmentId());
        record.setChrRegistType(companyUserMessage.getRegistType());
        record.setDatRegistDate(companyUserMessage.getRegistDate());
        record.setDatCreateDate(companyUserMessage.getCreateDate());
        record.setVarCreateUser(companyUserMessage.getCreateUser());
        record.setVarOperUser(companyUserMessage.getOperUser());
        record.setDatOperDate(companyUserMessage.getOperDate());
        companyUserMapper.updateByPrimaryKeySelective(record);


        return 0;
    }

    @Override
    public List<String> selectAccount(String[] id) {
        return companyUserMapper.selectAccount(id);
    }
}