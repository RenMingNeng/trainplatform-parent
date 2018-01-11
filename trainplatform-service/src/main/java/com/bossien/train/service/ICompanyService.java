package com.bossien.train.service;

import com.alibaba.fastjson.JSONArray;
import com.bossien.train.domain.Company;
import com.bossien.train.domain.Department;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-07-31.
 */
public interface ICompanyService {

    List<Map<String, Object>> selectList(Map<String, Object> params);

    Integer selectCount(Map<String, Object> params);

    Company selectOne(Map<String, Object> params);

    Company selectByOne(Map<String, Object> params);

    Integer select_count(Map<String, Object> params);

    List<Map<String, Object>> select_list(Map<String, Object> params);

    /*添加数据*/
    int insert(Map<String, Object> params);

    int insertSelective(Map<String, Object> params);

    /*修改数据*/
    int updateByPrimaryKeySelective(Map<String, Object> params);

    int insertBatch(List<Map<String, Object>> list);

    int updateBatch(List<Map<String, Object>> list);

    /*通过id查询*/
    Company selectById(Map<String, Object> params);

    /**
     * 修改公司的有效状态
     *
     * @param params
     */
    void updateCompanyValid(Map<String, Object> params);

    /**
     * 获取所有子公司id
     *
     * @param companyId
     * @return
     */
    String getChildCompanyIds(String companyId);

    /**
     * 根据id集合查询公司对象集合
     *
     * @param ids
     * @return
     */
    List<Company> getChildCompanyList(List<String> ids);

    /**
     * 获取该公司的所有子公司树
     *
     * @param companyId
     * @return
     */
    String getCompanyTree(String companyId);

    /**
     * 根据id查询公司名称和父Id
     *
     * @param companyId
     * @return
     */
    Map<String, Object> getCompanyInfo(String companyId);

    Object getDepts(List<Company> companyList);

    JSONArray queryCompanyTreeData(List<Company> list,String companyId,String path);

    List<String> getAllCompanyId(String cids);
}
