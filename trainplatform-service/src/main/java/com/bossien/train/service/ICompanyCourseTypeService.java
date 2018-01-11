package com.bossien.train.service;

import com.bossien.train.domain.CompanyCourseType;
import com.bossien.train.domain.dto.CompanyCourseTypeMessage;

import java.util.List;
import java.util.Map;

/**
 * 公司课程类别授权表
 * Created by Administrator on 2017/8/2.
 */
public interface ICompanyCourseTypeService {
    /**
     * 根据companyId查询课程类别集合
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> selectByCompanyId(Map<String, Object> params);

    /**
     * 组装课程分类树
     */
    List<Map<String, Object>> assemblecourseTypeTree(List<Map<String, Object>> list);

    /**
     * 查询当前类别及其子类别
     *
     * @param map
     * @return
     */
    String getAPCTypeChiList(Map<String, Object> map);

    /**
     * 递归查询该类别的上级名称
     *
     * @param
     * @return
     */
    String getParentTypeName(String varId, String typeName) throws Exception;

    int insertSelective(CompanyCourseTypeMessage courseTypeMessage);

    int updateByPrimaryKeySelective(CompanyCourseTypeMessage courseTypeMessage);

    CompanyCourseType selectById(String varId);

    CompanyCourseType selectByOne(String varId);

    int insertBatch(List<CompanyCourseType> list);

    int deleteBatch(List<Map<String, Object>> list);

    int updateBatch(List<CompanyCourseType> list);
}
