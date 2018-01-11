package com.bossien.train.dao.ap;

import com.bossien.train.domain.CompanyCourse;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/3.
 */
@Repository
public interface CompanyCourseMapper {
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

    int selectIntCourseIdCount(Map<String, Object> map);

    int insertSelective(Map<String, Object> map);

    int insertBatch(List<CompanyCourse> list);

    int updateByCourseId(Map<String, Object> map);

    int deleteByPrimaryKey(Map<String, Object> map);

    CompanyCourse selectOne(Map<String, Object> map);

    CompanyCourse selectById(Map<String, Object> map);

    List<CompanyCourse> selectRefByCompanyId(Map<String, Object> map);

    /**
     * 根据公司Id和课程Ids查询课课程Id
     *
     * @param map
     * @return
     */
    List<String> selectCourseIds(Map<String, Object> map);

}
