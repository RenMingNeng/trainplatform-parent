package com.bossien.train.service;

import com.bossien.train.domain.CompanyType;
import com.bossien.train.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-08-03.
 */
public interface ICompanyTypeService {
    /**
     * 单位类型树
     *
     * @param user
     * @return
     */
    List<Map<String, Object>> treeNodes(User user);

    String getCTChiList(Map<String, Object> params);

    int insertSelective(Map<String, Object> params);

    int updateByPrimaryKeySelective(Map<String, Object> params);

    int deleteByPrimaryKey(Map<String, Object> params);

    CompanyType selectByPrimaryKey(Map<String, Object> params);

    CompanyType selectById(Map<String, Object> params);

    int insertBatch(List<Map<String, Object>> list);

    int updateBatch(List<Map<String, Object>> list);
}
