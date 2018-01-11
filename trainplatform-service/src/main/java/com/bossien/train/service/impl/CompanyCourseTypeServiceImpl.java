package com.bossien.train.service.impl;

import com.bossien.train.dao.ap.CompanyCourseTypeMapper;
import com.bossien.train.domain.CompanyCourseType;
import com.bossien.train.domain.CourseType;
import com.bossien.train.domain.dto.CompanyCourseTypeMessage;
import com.bossien.train.service.ICompanyCourseTypeService;
import com.bossien.train.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公司课程类别授权表
 * Created by Administrator on 2017/8/2.
 */
@Service
public class CompanyCourseTypeServiceImpl implements ICompanyCourseTypeService {
    @Autowired
    private CompanyCourseTypeMapper companyCourseTypeMapper;


    /**
     * 根据companyId查询课程类别集合
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> selectByCompanyId(Map<String, Object> params) {
        return companyCourseTypeMapper.selectByCompanyId(params);
    }


    /**
     * 组装课程分类树
     */
    @Override
    public List<Map<String, Object>> assemblecourseTypeTree(List<Map<String, Object>> list) {
        List<Map<String, Object>> lm = new ArrayList<Map<String, Object>>();
        for (Map map1 : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", map1.get("varId"));
            map.put("name", map1.get("varName"));
            map.put("open", true);
            map.put("pid", map1.get("intPid"));
            map.put("intOrder", map1.get("intOrder"));
            // map.put("varId", type.getVarId());
            lm.add(map);
        }
        List<Map<String, Object>> result = TokenUtil.assembleTreeNodes(lm);
        if (result.size() > 0) {
            List<Map<String, Object>> lm1 = new ArrayList<Map<String, Object>>();
            lm1.add(result.get(0));
            return lm1;
        }
        return result;
    }


    /**
     * 去掉集合中重复的  renmingneng
     *
     * @param data
     * @return
     */
    public List<CourseType> getDistinctList(List<CourseType> data) {
        String ids = "";
        List<CourseType> result = new ArrayList<CourseType>();
        for (CourseType type : data) {
            if (ids.indexOf(type.getIntId()) != -1) {
                continue;
            }
            ids = ids == "" ? type.getIntId() : "," + type.getIntId();
            result.add(type);
        }
        return result;
    }

    /**
     * 查询当前类别及其子类别
     *
     * @param map
     * @return
     */
    @Override
    public String getAPCTypeChiList(Map<String, Object> map) {
        return companyCourseTypeMapper.getAPCTypeChiList(map);
    }

    ;

    /**
     * 递归查询该类别的上级名称
     *
     * @param
     * @return
     */
    @Override
    public String getParentTypeName(String varId, String typeName) throws Exception {
        CompanyCourseType companyCourseType = companyCourseTypeMapper.selectByOne(varId);
        if (null == companyCourseType) {
            return typeName;
        }
        if (!"".equals(typeName)) {
            typeName = companyCourseType.getVarName() + "/" + typeName;
        } else {
            typeName = companyCourseType.getVarName();
        }

        typeName = getParentTypeName(companyCourseType.getIntPid(), typeName);
        return typeName;
    }

    @Override
    public int insertSelective(CompanyCourseTypeMessage courseTypeMessage) {
        CompanyCourseType companyCourseType = new CompanyCourseType();
        companyCourseType.setVarId(courseTypeMessage.getCompanyCourseTypeId());
        companyCourseType.setChrSource(courseTypeMessage.getCompanyCourseTypeSource());
        companyCourseType.setVarName(courseTypeMessage.getCompanyCourseTypeName());
        companyCourseType.setVarDesc(courseTypeMessage.getCompanyCourseTypeDesc());
        companyCourseType.setIntPid(courseTypeMessage.getCompanyCourseTypePid());
        companyCourseType.setIntOrder(courseTypeMessage.getCompanyCourseTypeOrder());
        companyCourseType.setIntLevel(courseTypeMessage.getCompanyCourseTypeLevel());
        companyCourseType.setChrStatus(courseTypeMessage.getCompanyCourseTypeStatus());
        companyCourseType.setIntCompanyId(courseTypeMessage.getCompanyId());
        companyCourseType.setDatCreateDate(courseTypeMessage.getDatCreateDate());
        companyCourseType.setDatOperDate(courseTypeMessage.getDatOperDate());
        companyCourseType.setVarCreateUser(courseTypeMessage.getCreateUser());
        companyCourseType.setVarOperUser(courseTypeMessage.getOperUser());
        companyCourseTypeMapper.insertSelective(companyCourseType);
        return 0;
    }

    @Override
    public int updateByPrimaryKeySelective(CompanyCourseTypeMessage courseTypeMessage) {
        CompanyCourseType companyCourseType = new CompanyCourseType();
        companyCourseType.setChrSource(courseTypeMessage.getCompanyCourseTypeSource());
        companyCourseType.setVarId(courseTypeMessage.getCompanyCourseTypeId());
        companyCourseType.setVarName(courseTypeMessage.getCompanyCourseTypeName());
        companyCourseType.setVarDesc(courseTypeMessage.getCompanyCourseTypeDesc());
        companyCourseType.setIntPid(courseTypeMessage.getCompanyCourseTypePid());
        companyCourseType.setIntOrder(courseTypeMessage.getCompanyCourseTypeOrder());
        companyCourseType.setIntLevel(courseTypeMessage.getCompanyCourseTypeLevel());
        companyCourseType.setChrStatus(courseTypeMessage.getCompanyCourseTypeStatus());
        companyCourseType.setIntCompanyId(courseTypeMessage.getCompanyId());
        companyCourseType.setDatCreateDate(courseTypeMessage.getDatCreateDate());
        companyCourseType.setDatOperDate(courseTypeMessage.getDatOperDate());
        companyCourseType.setVarCreateUser(courseTypeMessage.getCreateUser());
        companyCourseType.setVarOperUser(courseTypeMessage.getOperUser());
        companyCourseTypeMapper.updateByPrimaryKeySelective(companyCourseType);
        return 0;
    }

    @Override
    public CompanyCourseType selectById(String varId) {
        return companyCourseTypeMapper.selectById(varId);
    }

    @Override
    public CompanyCourseType selectByOne(String varId) {
        return companyCourseTypeMapper.selectByOne(varId);
    }

    @Override
    public int insertBatch(List<CompanyCourseType> list) {
        return companyCourseTypeMapper.insertBatch(list);
    }

    @Override
    public int deleteBatch(List<Map<String, Object>> list) {
        return companyCourseTypeMapper.deleteBatch(list);
    }

    @Override
    public int updateBatch(List<CompanyCourseType> list) {
        return companyCourseTypeMapper.updateBatch(list);
    }
}
