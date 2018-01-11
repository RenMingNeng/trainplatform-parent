package com.bossien.train.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.bossien.train.dao.tp.*;
import com.bossien.train.domain.*;
import com.bossien.train.service.*;
import com.bossien.train.util.MapUtils;
import com.bossien.train.util.ParamsUtil;
import com.bossien.train.util.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/8/16.
 */
@Service
public class VideoPositionServiceImpl implements IVideoPositionService {
    @Autowired VideoPositionMapper videoPositionMapper;
    @Autowired private  ISequenceService sequenceService;


    public Map<String,Object> build( Map<String,Object> params){
        params.put("id",sequenceService.generator());
        params.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        params.put("operTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return params;
    }

    @Override
    public int insert(Map<String, Object> params) {
        return videoPositionMapper.insert(params);
    }

    @Override
    public int selectCount(Map<String, Object> params) {
        return videoPositionMapper.selectCount(params);
    }

    @Override
    public int delete(Map<String, Object> params) {
        return videoPositionMapper.delete(params);
    }

    @Override
    public Integer  selectOne(Map<String, Object> params) {
        return videoPositionMapper.selectOne(params);
    }

    @Override
    public void saveLastPosition(Map<String, Object> params) {
        //先查询记录条数
        int count = videoPositionMapper.selectCount(params);
        //大于3条就先删除最早一条然后添加最新一条
        if(count  >= 3){
            videoPositionMapper.delete(params);
        }
        //添加最新记录位置
        params = this.build(params);
        videoPositionMapper.insert(params);
    }
}
