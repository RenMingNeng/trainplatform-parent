package com.bossien.train.dao.tp;

import com.bossien.train.domain.CompanyTj;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CompanyTjMapper {
    /**
     * 添加
     * @param params
     * @return
     */
   int insert(Map<String,Object> params);

    /**
     * 修改
     * @param params
     * @return
     */
   int update(Map<String,Object> params);

    /**
     * 查询单条
     * @param params
     * @return
     */
   CompanyTj selectOne(Map<String,Object> params);

    /**
     * 查询列表
     * @param params
     * @return
     */
    List<CompanyTj> selectList(Map<String, Object> params);

    /**
     * 统计
     * @param params
     * @return
     */
    Integer selectCount(Map<String, Object> params);

    /**
     * 统计信息
     * @return
     */
    CompanyTj selectTotal(Map<String, Object> params);
}