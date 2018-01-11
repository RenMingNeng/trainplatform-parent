package com.bossien.train.service;

import com.bossien.train.domain.User;

import java.util.Map;

/**
 * Created by A on 2017/11/27.
 */
public interface IDeptSuperviseService {

    /**
     * 查询列表（不包含时间）
     * @param companyId
     * @param deptId
     * @param deptName
     * @param search
     * @param pageNum
     * @param pageSize
     * @return
     */
    Map<String, Object> queryForPagination(String companyId, String deptId, String deptName, String search,
                                           Integer pageNum, Integer pageSize);

    /**
     * 查询列表（包含时间）
     * @param companyId
     * @param deptId
     * @param deptName
     * @param search
     * @param startTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    Map<String, Object> queryForPagination(String companyId, String deptId, String deptName, String search,
            String startTime, String endTime, Integer pageNum, Integer pageSize);

    /**
     * 部门监管
     * @param deptId
     * @param deptName
     * @param search
     * @param companyName
     * @param user
     * @param pageNum
     * @param pageSize
     * @param deptMap
     * @param deptTotalInfoMap
     * @return
     */
    Map<String, Object> queryForPagination(String deptId, String deptName, String search,
                                           String companyName, User user, Integer pageNum, Integer pageSize,
                                           Map<String, String> deptMap, Map<String, Map<String, Object>> deptTotalInfoMap);


    /**
     * 点击公司id
     * @param companyId
     * @param companyName
     * @param search
     * @param user
     * @param pageNum
     * @param pageSize
     * @return
     */
    Map<String, Object> queryForPagination(String companyId, String companyName, String search,
                                           User user, Integer pageNum, Integer pageSize);

    /**
     * 查询本公司所有部门的子节点集合
     * @param companyId
     * @return
     */
    Map<String, String> getChildsByDeptPid(String companyId, String deptId, String deptName, String startTime, String endTime, Map<String, Map<String, Object>> deptTotalInfoMap);
}
