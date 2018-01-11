package com.bossien.train.service;

import com.bossien.train.domain.User;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/13.
 */
public interface ICompanyCategoryService {

    /**
     * 单位类型树
     * @return
     */
    List<Map<String,Object>> treeNodes();
}
