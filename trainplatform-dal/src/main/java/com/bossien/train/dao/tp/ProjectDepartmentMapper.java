package com.bossien.train.dao.tp;

import com.bossien.train.domain.ProjectDepartment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/27.
 */
@Repository
public interface ProjectDepartmentMapper {
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
    public List<ProjectDepartment> selectProDeptInfo(Map<String,Object> map);

    /**
     * 批量插入项目部门表
     * @param item
     * @return
     */
    int insertBatch(List<ProjectDepartment> item);

    /**
     * 根据projectId删除
     * @param map
     * @return
     */
    int delete(Map<String,Object> map);

    int update(Map<String,Object> map);
}
