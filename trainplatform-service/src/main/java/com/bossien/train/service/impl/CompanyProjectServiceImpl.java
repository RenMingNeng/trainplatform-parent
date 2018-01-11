package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.CompanyProjectMapper;
import com.bossien.train.domain.CompanyProject;
import com.bossien.train.domain.ProjectBasic;
import com.bossien.train.domain.User;
import com.bossien.train.service.ICompanyProjectService;
import com.bossien.train.service.IProjectBasicService;
import com.bossien.train.service.IProjectDossierService;
import com.bossien.train.service.ISequenceService;
import com.bossien.train.util.CollectionUtils;
import com.bossien.train.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2017-08-02.
 */
@Service
public class CompanyProjectServiceImpl implements ICompanyProjectService {

    @Autowired
    private CompanyProjectMapper companyProjectMapper;
    @Autowired
    private IProjectBasicService projectBasicService;
    @Autowired
    private IProjectDossierService projectDossierService;

    @Override
    public CompanyProject build(User user) {
        CompanyProject companyProject = new CompanyProject();
        companyProject.setCreateUser(user.getUserName());
        companyProject.setCreateTime(DateUtils.formatDateTime(new Date()));
        companyProject.setOperUser(user.getUserName());
        companyProject.setOperTime(DateUtils.formatDateTime(new Date()));
        return companyProject;
    }

    @Override
    public void saveProjectCompany(Map<String, Object> params, User user) {
        String projectBasicId = (String)params.get("project_id");
        String companyIds = (String)params.get("company_ids");
        ProjectBasic projectBasic = projectBasicService.selectById(projectBasicId);
        if(StringUtils.isEmpty(companyIds)) {
            return;
        }
        if(null == projectBasic) {
            return;
        }
        List<String> companyIds_ = Arrays.asList(companyIds.split(","));
        Map<String,Object> param = new HashMap<>();
        param.put("companyIds",companyIds_);
        param.put("project_id",projectBasicId);
        //保存前先做删除操作（防止重复添加）
        companyProjectMapper.deleteBatch(param);
        Integer a = companyIds_.size()/900+1;
        List<List<String>> subList = CollectionUtils.subList(companyIds_, companyIds_.size()/a);
        List<CompanyProject> companyProjects = null;
        CompanyProject companyProject = null;
        for(List<String> list : subList){
            companyProjects = new ArrayList<CompanyProject>();
            for(String companyId : list){
                companyProject = this.build(user);
                companyProject.setCompanyId(companyId);
                companyProject.setProjectId(projectBasic.getId());
                companyProjects.add(companyProject);
            }
            companyProjectMapper.insertBatch(companyProjects);
        }
    }

    @Override
    public List<String> selectCompanyIdsByProjectId(String project_id) {

        return companyProjectMapper.selectCompanyIdsByProjectId(project_id);
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {
        return companyProjectMapper.selectCount(params);
    }


    @Override
    public List<CompanyProject> selectList(Map<String, Object> params) {
        return companyProjectMapper.selectList(params);
    }

    @Override
    public int insert(Map<String, Object> params) {
        return companyProjectMapper.insert(params);
    }

    @Override
    public List<String> selectProjectIdsByCompanyId(String companyId) {
        if(null == companyId || companyId.equals("")){
            return new ArrayList<>();
        }
        return companyProjectMapper.selectProjectIdsByCompanyId(companyId);
    }
    /**
     * 根据projectId删除
     * @param params
     * @return
     */
    @Override
    public int delete(Map<String, Object> params){
        return companyProjectMapper.delete(params);
    }
    /**
     * 根据companyId查询projectIds
     * @param param
     * @return
     */
    @Override
    public List<String> selectProjectIds(Map<String, Object> param){
        return companyProjectMapper.selectProjectIds(param);
    }

    @Override
    public void deleteProjectCompany(Map<String, Object> params) {
        companyProjectMapper.deleteProjectCompany(params);
    }
}
