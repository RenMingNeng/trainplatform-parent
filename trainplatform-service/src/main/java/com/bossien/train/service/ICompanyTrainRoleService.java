package com.bossien.train.service;

import com.bossien.train.domain.CompanyTrainRole;
import com.bossien.train.domain.TrainRole;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/21.
 */
public interface ICompanyTrainRoleService {

    int deleteByPrimaryKey(Integer intId);

    int deleteByTrainRoleId(String[] intTrainRoleId);

    int deleteByAllId(Map<String, Object> map);

    int insert(CompanyTrainRole record);

    int insertBatch(List<CompanyTrainRole> list);

    int insertSelective(CompanyTrainRole record);

    CompanyTrainRole selectByPrimaryKey(String intId);

    CompanyTrainRole selectByTrainRoleId(String intTrainRoleId);

    List<String> selectByCompanyId(String intId);

    int updateByPrimaryKeySelective(CompanyTrainRole record);

    int updateByPrimaryKey(CompanyTrainRole record);

    CompanyTrainRole selectById(Map<String, Object> map);


}
