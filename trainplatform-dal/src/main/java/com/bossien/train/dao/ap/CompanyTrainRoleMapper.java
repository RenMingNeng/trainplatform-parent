package com.bossien.train.dao.ap;

import com.bossien.train.domain.CompanyTrainRole;

import java.util.List;
import java.util.Map;

public interface CompanyTrainRoleMapper {
    int deleteByPrimaryKey(Integer intId);

    /*通过公司id和角色id删除*/
    int deleteByAllId(Map<String, Object> map);

    int deleteByTrainRoleId(String[] intTrainRoleId);

    int insert(CompanyTrainRole record);

    int insertBatch(List<CompanyTrainRole> list);

    int insertSelective(CompanyTrainRole record);

    CompanyTrainRole selectByPrimaryKey(String intId);

    List<String> selectByCompanyId(String intId);

    CompanyTrainRole selectByTrainRoleId(String intTrainRoleId);

    /*公司id和受训角色id一起查询*/
    CompanyTrainRole selectById(Map<String, Object> map);

    CompanyTrainRole selectByOne(Map<String, Object> map);

    int updateByPrimaryKeySelective(CompanyTrainRole record);

    int updateByPrimaryKey(CompanyTrainRole record);

    int updateByTrainRoleId(CompanyTrainRole record);

}