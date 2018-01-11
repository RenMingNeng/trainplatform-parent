package com.bossien.train.dao.tp;

import com.bossien.train.domain.CompanyPermission;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by A on 2017/7/25.
 */

@Repository
public interface CompanyPermissionMapper {

    /**
     * 新增
     * @param companyPermission
     */
    int insert(CompanyPermission companyPermission);

    /**
     * 批量添加
     * @param companyPermissions
     * @return
     */
    int insertBatch(List<CompanyPermission> companyPermissions);

    /**
     * 查询列表
     * @param params
     * @return
     */
    List<Map<String, Object>> selectList(Map<String, Object> params);

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
    CompanyPermission selectOne(Map<String, Object> params);

    /**
     * 删除
     * @param companys
     */
    void delete(List<String> companys);
}
