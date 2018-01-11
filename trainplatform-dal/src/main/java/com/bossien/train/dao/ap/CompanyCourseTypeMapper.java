package com.bossien.train.dao.ap;

import com.bossien.train.domain.CompanyCourseType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-07-31.
 */
@Repository
public interface CompanyCourseTypeMapper {
    /**
     * 根据companyId查询课程类别集合
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> selectByCompanyId(Map<String, Object> params);

    /**
     * 查询当前类别及其子类别
     *
     * @param map
     * @return
     */
    String getAPCTypeChiList(Map<String, Object> map);

    /**
     * 根据varId查询
     *
     * @param varId
     * @return
     */
    CompanyCourseType selectById(String varId);

    CompanyCourseType selectByOne(String varId);

    int insertBatch(List<CompanyCourseType> list);

    int updateBatch(List<CompanyCourseType> list);

    int deleteBatch(List<Map<String, Object>> list);

    int insertSelective(CompanyCourseType companyCourseType);

    int updateByPrimaryKeySelective(CompanyCourseType companyCourseType);
}
