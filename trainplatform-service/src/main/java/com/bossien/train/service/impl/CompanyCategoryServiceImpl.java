package com.bossien.train.service.impl;

import com.bossien.train.dao.ap.CompanyCategoryMapper;
import com.bossien.train.domain.CompanyCategory;
import com.bossien.train.domain.CompanyType;
import com.bossien.train.domain.User;
import com.bossien.train.service.ICompanyCategoryService;
import com.bossien.train.util.TokenUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/9/13.
 */
@Service
public class CompanyCategoryServiceImpl implements ICompanyCategoryService{

    @Autowired
    private CompanyCategoryMapper companyCategoryMapper;

    @Cacheable(value = "companyCategoryCache#(60 * 60 * 24)", key = "'treeNodes'")
    @Override
    public List<Map<String, Object>> treeNodes() {
        List<CompanyCategory> list = companyCategoryMapper.selectAll();
        return this.companyCategoryTree(list);
    }

    private List<Map<String,Object>> companyCategoryTree(List<CompanyCategory> list) {
        List<Map<String, Object>> lm = new ArrayList<Map<String, Object>>();
        for (CompanyCategory companyCategory : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", companyCategory.getIntId());
            map.put("name", companyCategory.getVarName());
            map.put("open", false);
            map.put("pid", companyCategory.getIntPid());
            map.put("intOrder", companyCategory.getIntOrder());
            lm.add(map);
        }
        List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
        res.add(TokenUtil.assembleTreeNodes(lm).get(0));
        return res;
    }
}
