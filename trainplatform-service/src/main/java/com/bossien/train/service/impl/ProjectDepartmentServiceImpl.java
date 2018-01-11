package com.bossien.train.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bossien.train.dao.tp.ProjectDepartmentMapper;
import com.bossien.train.domain.Company;
import com.bossien.train.domain.ProjectDepartment;
import com.bossien.train.domain.ProjectRole;
import com.bossien.train.domain.User;
import com.bossien.train.service.ICompanyService;
import com.bossien.train.service.IProjectDepartmentService;
import com.bossien.train.service.ISequenceService;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.JsonUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 项目受训单位表
 * Created by Administrator on 2017/7/27.
 */
@Service("projectDepartmentService")
public class ProjectDepartmentServiceImpl implements IProjectDepartmentService {
    @Autowired
    private ProjectDepartmentMapper  projectDepartmentMapper;

    @Autowired
    private ISequenceService sequenceService;

    @Autowired
    ICompanyService companyService;

    /**
     * 根据项目id和公司id查询受训单位
     * @param map
     * @return
     */
    @Override
    public List<ProjectDepartment> selectByProjectIdAndCompanyId(Map<String,Object> map){
       return  projectDepartmentMapper.selectByProjectIdAndCompanyId(map);
    }

    @Override
    public List<Map<String, Object>> selectProDeptInfo(Map<String, Object> map) {
        List<ProjectDepartment> projectDepartmentList = projectDepartmentMapper.selectProDeptInfo(map);
        map.clear();
        Company company = null;
        Map<String, Object> params = null;
        List<Map<String, Object>> proDeptInfo = new ArrayList<>();
        for (ProjectDepartment projectDepartment: projectDepartmentList) {
            map.put("companyId",projectDepartment.getCompanyId());
            company = companyService.selectOne(map);
            if(company == null){
                company = new Company();
            }
            params = new HashedMap();
            params.put("companyName",company.getVarName());
            params.put("deptName",projectDepartment.getDeptName().replace(",","、"));
            proDeptInfo.add(params);
        }
        return proDeptInfo;
    }

    @Override
    public ProjectDepartment build(User user) {
        ProjectDepartment projectDepartment = new ProjectDepartment();
        projectDepartment.setId(sequenceService.generator());
        projectDepartment.setCreateUser(user.getUserAccount());
        projectDepartment.setCreateTime(DateUtils.formatDateTime(new Date()));
        projectDepartment.setOperateUser(user.getUserAccount());
        projectDepartment.setOperateTime(DateUtils.formatDateTime(new Date()));
        return projectDepartment;
    }

    @Override
    public int saveBatchProjectDepartment(User user, Map<String, Object> param) throws Exception {
        return saveProjectDepartment(user, param);
    }

    private Integer saveProjectDepartment(User user, Map<String, Object> param) {
        int count = 0;
        String departments = param.get("departments").toString();
        String projectId = param.get("projectId").toString();
        Class<Map> clazz = Map.class;
        List<Map> depList = JsonUtils.joinToList(departments, clazz);
        List<ProjectDepartment> item = new ArrayList<>();
        for (Map map: depList) {
            ProjectDepartment projectDepartment = build(user);
            projectDepartment.setProjectId(projectId);
            if(StringUtils.isNotEmpty(map.get("type").toString())){
                projectDepartment.setCompanyId(map.get("type").toString());
            }else{
                projectDepartment.setCompanyId(map.get("id").toString());
            }
            projectDepartment.setDeptId(map.get("id").toString());
            projectDepartment.setDeptName(map.get("text").toString());
            item.add(projectDepartment);
        }
        //先删除已有的数据
        this.delete(param);
        //项目部门对象为空不做添加
        if(item != null && item.size()>0){
            count = projectDepartmentMapper.insertBatch(item);
        }
        return count;
    }

    /**
     * 组装数据便于select2展示
     * @param projectDepartments
     * @return
     */
    @Override
    public Object assembleProjectDepartmentData(List<ProjectDepartment> projectDepartments){
        JSONArray jsonArray = new JSONArray();
        for (ProjectDepartment projectDepartment:projectDepartments) {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",projectDepartment.getDeptId());
            jsonObject.put("text",projectDepartment.getDeptName());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 根据projectId删除
     * @param map
     * @return
     */
    @Override
    public int delete(Map<String,Object> map){
        return  projectDepartmentMapper.delete(map);
    }
    /**
     * 修改项目
     * @param user
     * @param params
     * @return
     */
   @Override
   public Integer updateProjectDepartment(User user,Map<String,Object> params){
       return  saveProjectDepartment(user, params);
    }

    @Override
    public int update(Map<String, Object> map) {
        return projectDepartmentMapper.update(map);
    }
}
