package com.bossien.train.dao.tp;

import com.bossien.train.domain.ProjectDossier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by A on 2017/7/25.
 */

@Repository
public interface ProjectDossierMapper {

    /**
     * 新增
     * @param params
     */
    void insert(Map<String, Object> params);

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
     * 批量新增
     * @param projectDossiers
     */
    void insertBatch(List<ProjectDossier> projectDossiers);

    /**
     * 批量删除
     * @param param
     */
    void deleteBatch(Map<String, Object> param);

    /**
     * 根据projectId和companyId删除
     * @param params
     * @return
     */
    int deleteByProjectId(Map<String, Object> params);

    /**
     * 根据companyId和projectType查询projectIds
     * @param params
     * @return
     */
    List<String> selectProjectIds(Map<String, Object> params);
}
