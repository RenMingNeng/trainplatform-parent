package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.UserRoleMapper;
import com.bossien.train.domain.UserRole;
import com.bossien.train.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/31.
 */
@Service
public class UserRoleServiceImpl implements IUserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public UserRole selectOne(Map<String, Object> params) {
        return userRoleMapper.selectOne(params);
    }

    @Override
    public List<UserRole> selectList(Map<String, Object> params) {
        return userRoleMapper.selectList(params);
    }

    @Override
    public int insert(UserRole userRole) {
        return userRoleMapper.insert(userRole);
    }

    @Override
    public int deleteByUserRole(List<String> codes) {
        return userRoleMapper.deleteByUserRole(codes);
    }

    @Override
    public int update(UserRole userRole) {
        return userRoleMapper.update(userRole);
    }
}
