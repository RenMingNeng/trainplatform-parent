package com.bossien.train.dao.ap;

import com.bossien.train.domain.CompanyUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CompanyUserMapper {
    int deleteByPrimaryKey(Map<String, Object> map);

    int insert(CompanyUser record);

    int insertSelective(CompanyUser record);

    CompanyUser selectByOne(Map<String, Object> params);

    CompanyUser selectById(Map<String, Object> params);

    int updateByPrimaryKeySelective(CompanyUser record);

    List<String> selectAccount(@Param("codes") String[] id);


}