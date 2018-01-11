package com.bossien.train.service.impl;

import com.bossien.train.dao.ex.CourseTypeMapper;
import com.bossien.train.domain.CourseType;
import com.bossien.train.domain.User;
import com.bossien.train.service.ICourseTypeService;
import com.bossien.train.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/2.
 */
@Service
public class CourseTypeServiceImpl implements ICourseTypeService {
    @Autowired
    private CourseTypeMapper courseTypeMapper;

    /**
     * 通过intIds查询课程类别
     *
     * @param intIds
     * @return
     */
    @Override
    public List<CourseType> selectByIntIds(List<String> intIds) {

        return courseTypeMapper.selectByIntIds(intIds);
    }


    /**
     * 授权课程分类树
     */
    @Override
    public List<Map<String, Object>> assemblecourseTypeTree(List<CourseType> list) {
        List<Map<String, Object>> lm = new ArrayList<Map<String, Object>>();
        for (CourseType coursetype : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", coursetype.getIntId());
            map.put("name", coursetype.getVarName());
            map.put("open", true);
            map.put("pid", coursetype.getIntPid());
            map.put("intOrder", coursetype.getIntOrder());
            // map.put("varId", type.getVarId());
            lm.add(map);
        }
        List<Map<String, Object>> result = TokenUtil.assembleTreeNodes(lm);
       /* if (result.size() > 0) {
            return result.get(0);
        }*/
        return result;
    }


    /**
     * 加载课程树 (全部课程)
     *
     * @param user
     * @return
     */
    @Override
    public List<Map<String, Object>> zTreeNodes(User user) {

        List<CourseType> list = courseTypeMapper.selectAllType(user);

        return this.ex_courseTypeTree(list);
    }

    @Override
    public String getCSTChiList(Map<String, Object> params) {

        return courseTypeMapper.getCSTChiList(params);
    }

    @Override
    public int deleteByAll() {
        return courseTypeMapper.deleteByAll();
    }

    @Override
    public int deleteByPrimaryKey(int id) {
        return courseTypeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(CourseType courseType) {
        return courseTypeMapper.insertSelective(courseType);
    }

    @Override
    public int insertBatch(List<CourseType> item) {
        return courseTypeMapper.insertBatch(item);
    }


    /**
     * 全部课程分类树 renmingneng
     */
    public List<Map<String, Object>> ex_courseTypeTree(List<CourseType> list) {
        List<Map<String, Object>> lm = new ArrayList<Map<String, Object>>();
        for (CourseType type : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", type.getIntId());
            map.put("name", type.getVarName());
            if (("0").equals(type.getIntPid())) {
                map.put("open", true);
            } else {
                map.put("open", false);
            }
            map.put("pid", type.getIntPid());
            map.put("intOrder", type.getIntOrder());
            lm.add(map);
        }
        List<Map<String, Object>> res = TokenUtil.assembleTreeNodes(lm);
        return TokenUtil.assembleTreeNodes(lm);
    }


}
