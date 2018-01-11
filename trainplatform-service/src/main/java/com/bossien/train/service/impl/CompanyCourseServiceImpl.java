package com.bossien.train.service.impl;

import com.bossien.train.dao.ap.CompanyCourseMapper;
import com.bossien.train.dao.ap.CompanyMapper;
import com.bossien.train.domain.Company;
import com.bossien.train.domain.CompanyCourse;
import com.bossien.train.domain.dto.CompanyCourseDTO;
import com.bossien.train.domain.dto.CompanyCourseMessage;
import com.bossien.train.service.ICompanyCourseService;
import com.bossien.train.service.ICompanyService;
import com.bossien.train.service.ISequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公司授权课程表
 * Created by Administrator on 2017/8/3.
 */
@Service(value = "companyCourseService")
public class CompanyCourseServiceImpl implements ICompanyCourseService {
    @Autowired
    private CompanyCourseMapper companyCourseMapper;

    @Autowired
    private ISequenceService sequenceService;

    /**
     * 根据CompanyId查询courseIds
     *
     * @param map
     * @return
     */
    @Override
    public List<Map<String, Object>> selectByCompanyId(Map<String, Object> map) {
        return companyCourseMapper.selectByCompanyId(map);
    }

    @Override
    public int insertSelective(CompanyCourseMessage companyCourseMessage) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("varId", sequenceService.generator());
        params.put("intCompanyCourseTypeId", companyCourseMessage.getCompanyCourseTypeId());
        params.put("intCourseId", companyCourseMessage.getCourseId());
        params.put("intCompanyId", companyCourseMessage.getCompanyId());
        companyCourseMapper.insertSelective(params);
        return 0;
    }
    @Override
    public int insertBatch(List<CompanyCourse> list){

        return  companyCourseMapper.insertBatch(list);
    };

    @Override
    public int updateByCourseId(CompanyCourseMessage companyCourseMessage) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("intCompanyCourseTypeId", companyCourseMessage.getCompanyCourseTypeId());
        params.put("intCourseId", companyCourseMessage.getCourseId());
        params.put("intCompanyId", companyCourseMessage.getCompanyId());
        companyCourseMapper.updateByCourseId(params);

        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Map<String, Object> map) {
        return companyCourseMapper.deleteByPrimaryKey(map);
    }

    @Override
    public CompanyCourse selectOne(Map<String, Object> map) {
        return companyCourseMapper.selectOne(map);
    }

    @Override
    public CompanyCourse selectById(Map<String, Object> map) {
        return companyCourseMapper.selectById(map);
    }

    @Override
    public Map<String, String> selectRefByCompanyId(Map<String, Object> map) {
        List<CompanyCourse> courseRefs = companyCourseMapper.selectRefByCompanyId(map);
        Map<String, String> courseRefMap = new HashMap();
        for(CompanyCourse ref:courseRefs) {
            courseRefMap.put(ref.getIntCompanyCourseTypeId().toString() + "_" + ref.getIntCourseId().toString(),"1");
        }
        return courseRefMap;
    }

    @Override
    public int selectIntCourseIdCount(Map<String, Object> map) {
        return companyCourseMapper.selectIntCourseIdCount(map);
    }

    @Override
    public List<String> selectCourseIds(Map<String, Object> map) {
        return companyCourseMapper.selectCourseIds(map);
    }

    /**
     * 统计公司下的课程数量
     *
     * @param map
     * @return
     */
    @Override
    public int selectCompanyCourseCount(Map<String, Object> map) {
        return companyCourseMapper.selectCompanyCourseCount(map);
    }
}
