package com.bossien.train.service;

import com.bossien.train.domain.CompanyProject;
import com.bossien.train.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-08-02.
 */
public interface ICompanyProjectService {


    /**
     * 创建项目单位对象
     * @param user
     * @return
     */
    CompanyProject build(User user);
    /**
     * 保存培训单位（公开项目）
     * @param params
     * @param user
     */
    void saveProjectCompany(Map<String, Object> params, User user);

    List<String> selectCompanyIdsByProjectId(String project_id);

    Integer selectCount(Map<String, Object> params);

    List<CompanyProject> selectList(Map<String, Object> params);

    /**
     * 插入公司项目信息
     * @param params
     * @return
     */
    int insert(Map<String, Object> params);

    /**
     * 根据公司id查询所有的项目
     * @param companyId
     * @return
     */
    List<String> selectProjectIdsByCompanyId(String companyId);
    /**
     * 根据projectId删除
     * @param params
     * @return
     */
    int delete(Map<String, Object> params);
    /**
     * 根据companyId查询projectIds
     * @param param
     * @return
     */
    List<String> selectProjectIds(Map<String, Object> param);

    void deleteProjectCompany(Map<String, Object> params);
}
