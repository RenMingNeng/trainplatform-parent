package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.CompanyPermissionMapper;
import com.bossien.train.domain.CompanyPermission;
import com.bossien.train.service.ICompanyPermissionService;
import com.bossien.train.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by A on 2017/7/25.
 */

@Service
public class CompanyPermissionServiceImpl implements ICompanyPermissionService {
    @Autowired
    private CompanyPermissionMapper companyPermissionMapper;

    @Override
    public int insert(List<String> companys, String permissions) {

        if(null == companys || companys.isEmpty()) {
            return 0;
        }
        // 批删
        companyPermissionMapper.delete(companys);
        // 批插
        List<CompanyPermission> companyPermissions = new ArrayList<CompanyPermission>();
        CompanyPermission companyPermission = null;
        for(String companyId : companys){
            companyPermission = new CompanyPermission(companyId, permissions);
            companyPermissions.add(companyPermission);
        }
        //分批保存
        Integer count = companyPermissions.size()/900+1;
        List<List<CompanyPermission>> subList = CollectionUtils.subList(companyPermissions,
                companyPermissions.size() / count);
        for(List<CompanyPermission> list : subList){
            companyPermissionMapper.insertBatch(list);
        }
        return 1;
    }

    @Override
    public int insertBatch(List<CompanyPermission> companyPermissions) {

        return companyPermissionMapper.insertBatch(companyPermissions);
    }

    @Override
    public List<Map<String, Object>> selectList(Map<String, Object> params) {

        return companyPermissionMapper.selectList(params);
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {

        return companyPermissionMapper.selectCount(params);
    }

    @Override
    public CompanyPermission selectOne(Map<String, Object> params) {

        return companyPermissionMapper.selectOne(params);
    }

    @Override
    public void delete(List<String> companys) {

        companyPermissionMapper.delete(companys);
    }
}
