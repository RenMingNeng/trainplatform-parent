package com.bossien.train.service;

import com.bossien.train.domain.CompanyUser;
import com.bossien.train.domain.dto.CompanyUserDTO;
import com.bossien.train.domain.dto.CompanyUserMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/8/11.
 */

public interface ICompanyUserService {

    int deleteByPrimaryKey(Map<String, Object> map);

    int insert(CompanyUser record);

    int insertMessage(CompanyUserMessage companyUserMessage);

    CompanyUser selectByOne(Map<String, Object> params);

    CompanyUser selectById(Map<String, Object> params);

    int updateMessage(CompanyUserMessage companyUserMessage);

    List<String> selectAccount(String[] id);
}
