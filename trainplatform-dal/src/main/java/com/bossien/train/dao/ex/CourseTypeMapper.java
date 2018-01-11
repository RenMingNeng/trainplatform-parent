package com.bossien.train.dao.ex;

import com.bossien.train.domain.CourseType;
import com.bossien.train.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 课程类别
 * Created by Administrator on 2017/8/2.
 */
@Repository
public interface CourseTypeMapper {
    /**
     * 通过intIds查询课程类别
     *
     * @param intIds
     * @return
     */
    List<CourseType> selectByIntIds(List<String> intIds);

    /**
     * 查询全部的课程树
     *
     * @param user
     * @return
     */
    List<CourseType> selectAllType(User user);

    /**
     * 获取子节点
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
