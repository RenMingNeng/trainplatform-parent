package com.bossien.train.dao.tp;

import com.bossien.train.domain.ProjectBasic;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectBasicMapper {

	/**
     * 插入项目基础信息
	 * @param projectBasic
     * @return
     */
	int insert(ProjectBasic projectBasic);

	/**
	 * 修改项目基础信息
	 * @param params
	 * @return
	 */
	int update(Map<String, Object> params);

	/**
	 * 根据id获取项目基础信息
	 * @param id
	 * @return
	 */
    ProjectBasic selectById(String id);

	/**
	 * 根据id删除项目
	 * @param params
	 * @return
	 */
	int delete(Map<String, Object> params);


	/**
	 * 根据项目id查询主题id
	 * @param project_ids
	 * @return
	 */
	List<String> selectSubjectIdByProjectId(List<String> project_ids);


    Integer updateProjectStatus(@Param(value = "projectId") String projectId, @Param(value = "projectStatus")String projectStatus);

    String selectProjectStatus(String projectId);

    void checkProjectStatus(Map<String, Object> param);

	List<ProjectBasic> selectPublicProject(Map<String, Object> param);

    List<String> selectProjectIdsByStatus(Map<String, Object> param);
}