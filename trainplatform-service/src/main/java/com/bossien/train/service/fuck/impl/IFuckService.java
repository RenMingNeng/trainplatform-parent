package com.bossien.train.service.fuck.impl;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/24.
 */
public interface IFuckService {

    List<Map<String, Object>> selectList(Map<String, Object> params);

    Integer selectCount(Map<String, Object> params);
}
