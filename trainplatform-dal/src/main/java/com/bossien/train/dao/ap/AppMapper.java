package com.bossien.train.dao.ap;


import com.bossien.train.domain.Company;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppMapper {

    Company selectOne(@Param(value = "intId") String id);

    Integer insert(Company company);

}
