package com.bossien.train.service;

import com.bossien.train.dao.ap.CompanyCourseMapper;
import com.bossien.train.domain.CompanyCourse;
import com.bossien.train.domain.dto.CompanyCourseDTO;
import com.bossien.train.domain.dto.CompanyCourseMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/3.
 */
public interface ICompanyCourseService {
    /**
     * 根据CompanyId查询courseIds
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> selectByCompanyId(Map<String, Object> map);

    /**
     * 统计公司下的课程数量
     *
     * @param map
     * @return
     */
    int selectCompanyCourseCount(Map<String, Object> map);


    int insertSelective(CompanyCourseMessage companyCourseMessage);

    int insertBatch(List<CompanyCourse> list);

    int updateByCourseId(CompanyCourseMessage companyCourseMessage);

    int deleteByPrimaryKey(Map<String, Object> map);

    CompanyCourse selectOne(Map<String, Object> map);

    CompanyCourse selectById(Map<String, Object> map);

    Map<String, String> selectRefByCompanyId(Map<String, Object> map);

    int selectIntCourseIdCount(Map<String, Object> map);

    /**
     * 根据公司Id和课程Ids查询课课程Id
     *
     * @param map
     * @return
     */
    List<String> selectCourseIds(Map<String, Object> map);
}
