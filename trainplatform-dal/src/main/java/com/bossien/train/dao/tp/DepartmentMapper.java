package com.bossien.train.dao.tp;

import com.bossien.train.domain.Department;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface DepartmentMapper {
	int insert(Department department);

	int update(Department department);

	List<Department> selectAllDeptList(Map<String,Object> params);

	Department selectByParams(Map<String,Object> params);

	/**
	 * 根据companyId查询部门
	 * @param map
	 * @return
	 */
	public List<Department> selectDepartmentByCompanyId(Map<String,Object> map);

	/**
	 * 根据用户Id查询用户部门
	 * @param userId
	 * @return
	 */
	String selectByUserId(String userId);

	/**
	 * 根据公司id查询单条
	 * @param map
	 * @return
	 */
	Department selectOne(Map<String,Object> map);

	int getMaxOrder(String companyId);

	Department getPreNode(Map<String, Object> params);

	Department getNextNode(Map<String, Object> params);

	int changeOrderNum(Map<String, Object> params);

	/**
	 * 统计
	 * @param params
	 * @return
	 */
	int selectCount(Map<String, Object> params);

	/**
	 * 查询记录
	 * @param map
	 * @return
	 */
	List<Department> selectList(Map<String,Object> map);

	/**
	 * 根据部门id查询所有子部门
	 * @param deptId
	 * @return
	 */
	String getChildById(String deptId);
}