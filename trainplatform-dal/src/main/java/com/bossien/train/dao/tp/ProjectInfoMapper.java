package com.bossien.train.dao.tp;

import com.bossien.train.domain.ProjectInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProjectInfoMapper {

	/**
	 * 通过项目id查询项目信息（项目详情）
	 * @param id
	 * @return
	 */
	public ProjectInfo selectProjectInfoById(String id);
	/**
	 * 通过项目id查询项目集合（项目详情）
	 * @param params
	 * @return
	 */
	List<ProjectInfo> findProjectsByIds(Map<String, Object> params);

	/**
	 * 插入项目信息
	 * @param projectInfo
	 * @return
	 */
	int insert(ProjectInfo projectInfo );

	/**
	 * 修改项目详情
	 * @param params
	 * @return
	 */
	int update(Map<String, Object> params);

	/**
	 * 根据projectId删除
	 * @param params
	 * @return
	 */
	int delete(Map<String, Object> params);

    void publish(Map<String, Object> params);

    /**
     * 根据状态和类型查询projectIds
     * @param params
     * @return
     */
    List<String> selectProjectIds(Map<String, Object> params);

    /**
     * 根据状态和类型查询projectId总数
     * @param params
     * @return
     */
    int selectProjectIdCount(Map<String, Object> params);


    Integer selectCount(Map<String, Object> params);

	List<Map<String,Object>> selectList(Map<String, Object> params);

    List<ProjectInfo> selectNeedPublish(@Param(value = "start") String start, @Param(value = "end")String end, @Param(value = "projectStatus")String projectStatus);

    Integer updateProjectStatus(@Param(value = "projectId") String projectId, @Param(value = "projectStatus")String projectStatus);

    String selectProjectStatus(String projectId);

	List<String> getIdsByStatus(Map<String, Object> param);

	void checkProjectStatus(Map<String, Object> param);
}