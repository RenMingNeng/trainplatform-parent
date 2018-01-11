package com.bossien.train.service;

import com.bossien.train.domain.CourseType;
import com.bossien.train.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 课程类别
 * Created by Administrator on 2017/8/2.
 */
public interface ICourseTypeService {
    /**
     * 通过intIds查询课程类别
     *
     * @param params
     * @return
     */
    List<CourseType> selectByIntIds(List<String> params);

    /**
     * 组装课程分类树
     *
     * @param list
     * @return
     */
    public List<Map<String, Object>> assemblecourseTypeTree(List<CourseType> list);

    //加载课程类别树
    List<Map<String, Object>> zTreeNodes(User user);

    /**
     * 获取所有子节点（包括自己）
     *
     * @param params
     * @return
     */
    String getCSTChiList(Map<String, Object> params);

    /**
     * 删除全部
     */
    int deleteByAll();

    /**
     * 根据主键删除
     */
    int deleteByPrimaryKey(int id);

    /**
     * 添加数据
     */
    int insertSelective(CourseType courseType);

    int insertBatch(List<CourseType> item);
}
