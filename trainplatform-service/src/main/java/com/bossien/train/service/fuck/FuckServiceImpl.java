package com.bossien.train.service.fuck;

import com.bossien.train.dao.tp.FuckMapper;
import com.bossien.train.service.fuck.impl.IFuckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FuckServiceImpl implements IFuckService {

    @Autowired
    private FuckMapper fuckMapper;

    @Override
    public List<Map<String, Object>> selectList(Map<String, Object> params) {
        return fuckMapper.selectList(params);
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {
        return fuckMapper.selectCount(params);
    }
}
