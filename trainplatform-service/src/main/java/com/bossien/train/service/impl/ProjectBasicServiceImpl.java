package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.ProjectBasicMapper;
import com.bossien.train.domain.Company;
import com.bossien.train.domain.ProjectBasic;
import com.bossien.train.domain.User;
import com.bossien.train.service.ICompanyService;
import com.bossien.train.service.IProjectBasicService;
import com.bossien.train.service.ISequenceService;
import com.bossien.train.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/31.
 */
@Service
public class ProjectBasicServiceImpl implements IProjectBasicService {
    @Autowired
    private ProjectBasicMapper projectBasicMapper;

    @Autowired
    private ISequenceService sequenceService;
    @Autowired
    private ICompanyService companyService;

    @Override
    public ProjectBasic build(User user) {
        Map<String, Object> params = new HashMap<String, Object>();
        // 查询单位
        params.put("companyId", user.getCompanyId());
        Company company = companyService.selectOne(params);
        String createUser = user.getUserName();
        if(company!=null){
            createUser = company.getVarName() +  "-" + createUser;
        }
        ProjectBasic projectBasic= new ProjectBasic();
        projectBasic.setId(sequenceService.generator());
        projectBasic.setCreateUser(createUser);
        projectBasic.setCreateTime(DateUtils.formatDateTime(new Date()));
        projectBasic.setOperUser(createUser);
        projectBasic.setOperTime(DateUtils.formatDateTime(new Date()));
        return projectBasic;
    }

    @Override
    public int save(ProjectBasic projectBasic) {
        int count = projectBasicMapper.insert(projectBasic);
        return count;
    }

    @Override
    public int update(Map<String, Object> params) {
        return projectBasicMapper.update(params);
    }

//    @Cacheable(value = "projectBasicCache#(60 * 60)", key = "'selectById'.concat('_').concat(#id)")
    @Override
    public ProjectBasic selectById(String id) {
        return projectBasicMapper.selectById(id);
    }
    /**
     * 根据id删除项目
     * @param params
     * @return
     */
    @Override
    public int delete(Map<String, Object> params){
        return projectBasicMapper.delete(params);
    }



    @Override
    public List<String> selectSubjectIdByProjectId(List<String> project_ids) {

        return projectBasicMapper.selectSubjectIdByProjectId(project_ids);
    }

    @Override
    public Integer updateProjectStatus(String projectId, String projectStatus) {
        if(StringUtils.isEmpty(projectId) || StringUtils.isEmpty(projectStatus)) {
            return 0;
        }
        return projectBasicMapper.updateProjectStatus(projectId, projectStatus);
    }

    @Override
    public String selectProjectStatus(String projectId) {

        return projectBasicMapper.selectProjectStatus(projectId);
    }

    @Override
    public void checkProjectStatus(Map<String, Object> param) {

        projectBasicMapper.checkProjectStatus(param);
    }

    @Override
    public List<ProjectBasic> selectPublicProject(Map<String, Object> param) {
        return projectBasicMapper.selectPublicProject(param);
    }

    @Override
    public List<String> selectProjectIdsByStatus(Map<String, Object> param) {
        return projectBasicMapper.selectProjectIdsByStatus(param);
    }

}
