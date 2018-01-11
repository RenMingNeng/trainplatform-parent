package com.bossien.train.dao.tp;

import com.bossien.train.domain.CourseInfo;
import com.bossien.train.domain.VideoPosition;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 项目信息
 * Created by Administrator on 2017/7/25.
 */
@Repository
public interface VideoPositionMapper {

    /**
     * 添加数据
     * @param params
     * @return
     */
    int insert(Map<String,Object> params);

    /**
     * 根据courseId、userId和videoId查询记录条数
     * @param params
     * @return
     */
    int selectCount(Map<String,Object> params);

    /**
     * 删除最早的一条数据
     * @param params
     * @return
     */
    int delete(Map<String,Object> params);

    /**
     * 查询最新一条记录
     * @param params
     * @return
     */
    Integer  selectOne(Map<String,Object> params);
}
