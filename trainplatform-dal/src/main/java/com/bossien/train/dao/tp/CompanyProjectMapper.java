package com.bossien.train.dao.tp;

import com.bossien.train.domain.CompanyProject;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CompanyProjectMapper {

	/**
	 * 批量插入公司项目信息
	 * @param item
	 * @return
	 */
	int insertBatch(List<CompanyProject> item);

	/**
	 * 插入公司项目信息
	 * @param params
	 * @return
	 */
	int insert(Map<String, Object> params);

    Integer selectCount(Map<String, Object> params);

	List<CompanyProject> selectList(Map<String, Object> params);

    List<String> selectCompanyIdsByProjectId(String project_id);

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
	 * 批量删除
	 * @param param
	 */
    void deleteBatch(Map<String, Object> param);

	/**
	 * 根据companyId查询projectIds
	 * @param param
	 * @return
	 */
	List<String> selectProjectIds(Map<String, Object> param);

	/**
	 * 根据项目id 单位id删除项目档案
	 * @param params
	 */
    void deleteProjectCompany(Map<String, Object> params);
}