package com.bossien.train.service;

import com.bossien.train.domain.Page;
import com.bossien.train.domain.PersonDossier;
import com.bossien.train.domain.User;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by A on 2017/7/25.
 */
public interface IPersonDossierService {
    /**
     * 新增
     * @param personDossier
     */
    void insert(PersonDossier personDossier);

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
     * 分页
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<Map<String, Object>> queryForPagination(Map<String, Object> params,
                                                 Integer pageNum, Integer pageSize, User user);

    Page<Map<String, Object>> queryPersonDossierForPagination(Map<String, Object> params,Integer pageNum, Integer pageSize, User user);

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
     * 根据用户id集合导出个人档案，
     * @param params 为空时查询出该公司所有的人员
     */
    void export(Map<String, Object> params, OutputStream output) throws Exception;

    /**
     * 查询用户排行
     * @param userId
     * @param companyId
     * @return
     */
    Map<String, Object> selectRank(String userId, String companyId);

    int updateByUserIds(Map<String, Object> params);

    /**
     * 添加数据到人员档案表（sso对接数据）
     * @param map
     * @return
     */
    void add(Map<String,Object> map);

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
}
