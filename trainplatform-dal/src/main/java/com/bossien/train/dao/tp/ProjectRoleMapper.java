package com.bossien.train.dao.tp;

import com.bossien.train.domain.ProjectRole;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRoleMapper {

	/**
	 * 批量插入项目角色信息
	 * @param item
	 * @return
	 */
	int insertBatch(List<ProjectRole> item);

	/**
	 * 插入项目角色信息
	 * @param params
	 * @return
	 */
	int insert(Map<String, Object> params);

	/**
	 * 根据projectId查询受训角色
	 * @param projectId
	 * @return
	 */
	List<ProjectRole> selectByProjectId(String projectId);

	/**
	 * 根据项目Id查寻受训角色
	 * @param projectId
	 * @return
	 */
	List<String> selectRoleName(String projectId);

	/**
	 * 根据projectId删除角色
	 * @param map
	 */
	int delete(Map<String,Object> map);

	/**
	 * 根据projectId和roleId查询受训角色
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> selectProjectRole(Map<String, Object> params);

	/**
	 * 查询角色Id
	 * @param params
	 * @return
	 */
	List<String> selectRoleId(Map<String, Object> params);
}