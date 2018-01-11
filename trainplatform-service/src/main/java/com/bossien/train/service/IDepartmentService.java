package com.bossien.train.service;

import com.alibaba.fastjson.JSONArray;
import com.bossien.train.domain.Company;
import com.bossien.train.domain.Department;
import com.bossien.train.domain.User;

import java.util.List;
import java.util.Map;

/**
 * 公司部门表
 * Created by Administrator on 2017/8/1.
 */
public interface IDepartmentService {

    Department queryDeptByParams(Map<String,Object> params);

    /**
     * 根据companyId查询部门
     * @param map
     * @return
     */
    public List<Department> selectDepartmentByCompanyId(Map<String,Object> map);

    JSONArray queryTreeData(Company company, List<Department> list);
    /*
     递归查询公司下所有部门
     */
    List<String> getTreeDepartment(Map<String,Object> params,List<String> departmentLists);

    /**
     * 根据用户Id查询用户部门
     * @param userId
     * @return
     */
    String selectByUserId(String userId);
    /**
     * 递归查询该部门的上级部门名称
     * @param map
     * @return
     */
    String  getParentDepartmentName(Map<String,Object> map,String deptName);
    /**
     * 将受训单位数据组组装到json数组，便于前端select2插件展示
     * @param departments
     * @return
     */
    Object assembleDepartmentData(List<Department> departments);

    int insert(Department department);

    Department buildDept(Department dept,User user);

    int queryMaxOrder(String companyId);

    int update(Department dept);

    Department getPreNode(Map<String, Object> params);

    Department getNextNode(Map<String, Object> params);

    int changeOrderNum(Map<String, Object> params);

    /**
     * 根据公司id查询单条
     * @param map
     * @return
     */
    Department selectOne(Map<String,Object> map);

    JSONArray queryDeptTreeData(List<Department> list,String companyId,String path);

}
