package com.bossien.train.service;

import com.bossien.train.domain.CompanyTj;
import com.bossien.train.domain.User;
import com.bossien.train.domain.VideoPosition;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/3.
 */
public interface IVideoPositionService {
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
    Integer selectOne(Map<String,Object> params);

    /**
     * 保存学习位置
     * @param params
     */
    void saveLastPosition(Map<String,Object> params);
}
