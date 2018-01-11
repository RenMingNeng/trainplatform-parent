package com.bossien.train.dao.tp;

import com.bossien.train.domain.ProjectUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProjectUserMapper {
	/**
	 * 分页查询某用户的项目信息
	 * @param userId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<ProjectUser> pagingByUser(@Param("userId") String userId, @Param("startNum") int pageNum,@Param("pageSize") int pageSize);
//	List<ProjectUser> pagingByUser(Map<String,Object> params);

	/**
	 * 统计某用户的所有项目总数
	 * @param userId
	 * @return
	 */
	Integer selectProjectCountByUserId(@Param("userId") String userId);

	/**
	 * 批量插入项目用户信息
	 * @param item
	 * @return
	 */
	int insertBatch(List<ProjectUser> item);

	/**
	 * 查询单条数据
	 * @param params
	 * @return
	 */
	ProjectUser selectOne(Map<String, Object> params);

	/**
	 * 根据projectId和UserId查询角色Id
	 * @param projectId
	 * @param userId
	 * @return
	 */
	ProjectUser selectByProjectIdAndUserId(@Param("projectId") String projectId, @Param("userId") String userId);

	/**
	 * 根据用户Ids和项目Id删除项目用户
	 * @param params
	 * @return
	 */
	int deleteBatch(Map<String, Object> params);

	/**
	 * 根据属性查询项目用户的数量
	 * @param params
	 * @return
	 */
	int selectCount(Map<String, Object> params);

	/**
	 * 查询用户ids集合
	 * @param params
	 * @return
	 */
	List<String> selectUserIds(Map<String, Object> params);

	/**
	 * 根据属性查询项目用户
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> selectList(Map<String, Object> params);

	/**
	 * 根据角色和项目查询项目用户
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> selectProjectUser(Map<String, Object> params);

	/**
	 * 根据projectId删除
	 * @param params
	 * @return
	 */
    int deleteByProjectId(Map<String, Object> params);

	/**
	 * 根据projectId和userId查询roleId
	 * @param params
	 * @return
	 */
	String selectRoleId(Map<String, Object> params);

	/**
	 * 参与培训人次
	 * @param params
	 * @return
	 */
	int selectUserCount(Map<String, Object> params);
	/**
	 * 根据projectId和roleId查询主键ids
	 * @param params
	 * @return
	 */
	List<String> selectIds(Map<String, Object> params);

	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	int delete(String id);

	/**
	 * 查询当前学员的所有项目id
	 * @param params
	 * @return
	 */
    List<String> selectProjectIdByUserId(Map<String, Object> params);

	/**
	 * 查询项目用户的用户Id和用户姓名跟部门名称
	 * @param projectId
	 * @return
	 */
	List<Map<String, Object>> selectProjectUserList(String projectId);

	/**
	 * 查询项目用户的用户Id和用户姓名
	 * @param projectId
	 * @return
	 */
	List<Map<String, Object>> selectUsers(String projectId);

	/**
	 * 高级设置中查询项目用户信息
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> selectProjectUserMap(Map<String, Object> params);

	/**
	 * 高级设置中修改项目用户信息
	 * @param params
	 * @return
	 */
	int update(Map<String, Object> params);

	/**
	 * 根据projectId查询项目用户
	 * @param params
	 * @return
	 */
	List<ProjectUser> selectProjectUsers(Map<String, Object> params);
}