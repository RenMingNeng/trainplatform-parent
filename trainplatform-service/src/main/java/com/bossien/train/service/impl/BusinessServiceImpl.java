package com.bossien.train.service.impl;

import com.bossien.train.dao.ex.BusinessMapper;
import com.bossien.train.domain.Business;
import com.bossien.train.domain.User;
import com.bossien.train.service.IBusinessService;
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
public class BusinessServiceImpl implements IBusinessService{

    @Autowired
    private BusinessMapper businessMapper;

    @Cacheable(value = "businessCache#(60 * 60 * 24)", key = "'treeNodes'")
    @Override
    public List<Map<String, Object>> treeNodes() {
        List<Business> list = businessMapper.selectAll();
        return this.businessTree(list);
    }

    private List<Map<String,Object>> businessTree(List<Business> list) {
        List<Map<String, Object>> lm = new ArrayList<Map<String, Object>>();
        for (Business business : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", business.getIntId());
            map.put("name", business.getVarName());
            if("1".equals(business.getIntId())){
                map.put("open", true);
            }else{
                map.put("open", false);
            }
            map.put("pid", business.getIntPid());
            map.put("intOrder", business.getIntOrder());
            lm.add(map);
        }
        List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
        res.add(TokenUtil.assembleTreeNodes(lm).get(0));
        return res;
    }
}
