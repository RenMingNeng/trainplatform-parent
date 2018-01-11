package com.bossien.train.dao.ap;

import com.bossien.train.domain.CompanyType;
import com.bossien.train.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-08-03.
 */
@Repository
public interface CompanyTypeMapper {

    /**
     * 单位类型树
     *
     * @return
     */
    List<CompanyType> selectAllType();

    String getCTChiList(Map<String, Object> params);

    int insertSelective(Map<String, Object> params);

    int updateByPrimaryKeySelective(Map<String, Object> params);

    int deleteByPrimaryKey(Map<String, Object> params);

    CompanyType selectByPrimaryKey(Map<String, Object> params);

    CompanyType selectById(Map<String, Object> params);

    int insertBatch(List<Map<String, Object>> list);

    int updateBatch(List<Map<String, Object>> list);
}
