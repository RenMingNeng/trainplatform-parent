package com.bossien.train.dao.tp;

import com.bossien.train.domain.CompanySupervise;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-07-31.
 */
@Repository
public interface CompanySuperviseMapper {
    /**
     * 新增
     * @param companySupervise
     */
    void insert(CompanySupervise companySupervise);

    /**
     * 批量
     * @param companySupervises
     */
    void insertBatch(List<CompanySupervise> companySupervises);

    /**
     * 修改
     * @param companySupervise
     */
    void update(CompanySupervise companySupervise);

    /**
     * 查询列表
     * @param params
     * @return
     */
    List<CompanySupervise> selectList(Map<String, Object> params);

    /**
     * 统计
     * @param params
     * @return
     */
    Integer selectCount(Map<String, Object> params);

    /**
     * 查询单条记录
     * @param params
     * @return
     */
    CompanySupervise selectOne(Map<String, Object> params);

    /**
     * 删除
     * @param params
     */
    void delete(Map<String, Object> params);

    /**
     * 查询监管中所有子企业id
     * @param params
     * @return
     */
    String getChildCompanyIds(Map<String, Object> params);

    /**
     * 批量上移
     * @param params
     */
    void upNodes(Map<String, Object> params);

    /**
     * 批量下移
     * @param params
     */
    void downNodes(Map<String, Object> params);
}
