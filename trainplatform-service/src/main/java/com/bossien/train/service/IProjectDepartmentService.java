package com.bossien.train.service;

import com.bossien.train.domain.ProjectDepartment;

import com.bossien.train.domain.User;
import java.util.List;
import java.util.Map;

/**
 * 项目受训单位表
 * Created by Administrator on 2017/7/27.
 */
public interface IProjectDepartmentService {
    /**
     * 根据项目id和公司id查询受训单位
     * @param map
     * @return
     */
    public List<ProjectDepartment> selectByProjectIdAndCompanyId(Map<String,Object> map);

    /**
     * 查询受训单位信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> selectProDeptInfo(Map<String,Object> map);

    /**
     * 创建项目部门对象
     * @param user
     * @return
     */
    public ProjectDepartment build(User user);

    /**
     * 批量保存项目部门信息
     * @param user
     * @param param
     * @return
     */
    public int saveBatchProjectDepartment(User user, Map<String, Object> param) throws Exception;

    /**
     * 组装数据便于select2展示
     * @param projectDepartments
     * @return
     */
    Object assembleProjectDepartmentData(List<ProjectDepartment> projectDepartments);
    /**
     * 根据projectId删除
     * @param map
     * @return
     */
    int delete(Map<String,Object> map);

    /**
     * 修改项目
     * @param user
     * @param map
     * @return
     */
    Integer updateProjectDepartment(User user,Map<String,Object> map);

    int update(Map<String,Object> map);
}
