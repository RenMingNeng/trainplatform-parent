package com.bossien.train.service.impl;

import com.bossien.train.dao.ap.CompanyTypeMapper;
import com.bossien.train.domain.CompanyType;
import com.bossien.train.domain.User;
import com.bossien.train.service.ICompanyTypeService;
import com.bossien.train.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompanyTypeServiceImpl implements ICompanyTypeService {


    @Autowired
    private CompanyTypeMapper companyTypeMapper;

    @Override
    public List<Map<String, Object>> treeNodes(User user) {

        List<CompanyType> list = companyTypeMapper.selectAllType();

        return this.companyTypeTree(list);
    }

    @Override
    public String getCTChiList(Map<String, Object> params) {
        return companyTypeMapper.getCTChiList(params);
    }

    @Override
    public int insertSelective(Map<String, Object> params) {
        return companyTypeMapper.insertSelective(params);
    }

    @Override
    public int updateByPrimaryKeySelective(Map<String, Object> params) {
        return companyTypeMapper.updateByPrimaryKeySelective(params);
    }

    @Override
    public int deleteByPrimaryKey(Map<String, Object> params) {
        return companyTypeMapper.deleteByPrimaryKey(params);
    }

    @Override
    public CompanyType selectByPrimaryKey(Map<String, Object> params) {
        return companyTypeMapper.selectByPrimaryKey(params);
    }

    @Override
    public CompanyType selectById(Map<String, Object> params) {
        return companyTypeMapper.selectById(params);
    }

    @Override
    public int insertBatch(List<Map<String, Object>> list) {
        return companyTypeMapper.insertBatch(list);
    }

    @Override
    public int updateBatch(List<Map<String, Object>> list) {
        return companyTypeMapper.updateBatch(list);
    }

    /**
     * 全部单位分类树
     */
    public List<Map<String, Object>> companyTypeTree(List<CompanyType> list) {
        List<Map<String, Object>> lm = new ArrayList<Map<String, Object>>();
        for (CompanyType companyType : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", companyType.getIntId());
            map.put("name", companyType.getVarName());
            map.put("open", false);
            map.put("pid", companyType.getIntPid());
            map.put("intOrder", companyType.getIntOrder());
            lm.add(map);
        }
        List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
        res.add(TokenUtil.assembleTreeNodes(lm).get(0));
        return res;
    }


}
