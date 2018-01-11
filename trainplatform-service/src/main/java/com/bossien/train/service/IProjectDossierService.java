package com.bossien.train.service;

import com.bossien.train.domain.Page;
import com.bossien.train.domain.User;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by A on 2017/7/25.
 */
public interface IProjectDossierService {
    /**
     * 新增
     * @param params
     */
    void insert(Map<String, Object> params);
//
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
     * 分页
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<Map<String, Object>> queryForPagination(Map<String, Object> params,
                                                 Integer pageNum, Integer pageSize, User user);

    /**
     * 查询单条记录
     * @param params
     * @return
     */
    Map<String, Object> selectOne(Map<String, Object> params);

    /**
     * 批量插入（公开项目）
     * @param params
     */
    void insertBatch(Map<String, Object> params);

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

    /**
     * 导出统计信息
     * @param projectId
     */
    void export(String projectId, String projectType, String userId, OutputStream output) throws Exception;

    /**
     * 批量删除项目档案
     * @param params
     */
    void deleteBatch(Map<String, Object> params);
}
