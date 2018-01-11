package com.bossien.train.dao.ap;

import com.bossien.train.domain.Company;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-07-31.
 */
@Repository
public interface CompanyMapper {

    List<Map<String, Object>> selectList(Map<String, Object> params);

    Integer selectCount(Map<String, Object> params);

    Company selectOne(Map<String, Object> params);

    Integer select_count(Map<String, Object> params);

    List<Map<String, Object>> select_list(Map<String, Object> params);

    int insert(Map<String, Object> params);

    int insertSelective(Map<String, Object> params);

    int updateByPrimaryKeySelective(Map<String, Object> params);

    Company selectById(Map<String, Object> params);

    Company selectByOne(Map<String, Object> params);

    int insertBatch(List<Map<String, Object>> list);

    int updateBatch(List<Map<String, Object>> list);

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
     * 根据id查询公司名称和父Id
     *
     * @param companyId
     * @return
     */
    Map<String, Object> getCompanyInfo(String companyId);
}
