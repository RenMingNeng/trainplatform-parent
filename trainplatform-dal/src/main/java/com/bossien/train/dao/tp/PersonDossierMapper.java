package com.bossien.train.dao.tp;

import com.bossien.train.domain.PersonDossier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by A on 2017/7/25.
 */

@Repository
public interface PersonDossierMapper {

    /**
     * 新增
     * @param personDossier
     */
    int insert(PersonDossier personDossier);

    /**
     * 批量添加
     * @param personDossiers
     * @return
     */
    int insertBatch(List<PersonDossier> personDossiers);

    /**
     * 修改
     * @param params
     */
    void update(Map<String, Object> params);

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
    Map<String, Object> selectOne(Map<String, Object> params);

    /**
     * 查询用户排名
     * @param params
     * @return
     */
    Map<String, Object> selectRank(Map<String, Object> params);

     int updateByUserIds(Map<String, Object> params);

    /**
     * 变更单位时修改个人档案中的单位相关字段
     * @param params
     * @return
     */
     int updateCompanyId(Map<String, Object> params);

    /**
     * 查询学员自学学时
     * @param params
     * @return
     */
     PersonDossier selectStudySelf(Map<String, Object> params);

    /**
     * 批量删除人员档案信息
     * @param params
     * @return
     */
     int batchDelete(Map<String, Object> params);

    /**
     * 删除人员档案信息
     * @param userId
     * @return
     */
    int delete(String userId);
}
