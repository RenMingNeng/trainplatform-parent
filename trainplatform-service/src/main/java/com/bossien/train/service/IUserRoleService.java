package com.bossien.train.service;

import com.bossien.train.domain.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/31.
 */
public interface IUserRoleService {

    UserRole selectOne(Map<String, Object> params);

    List<UserRole> selectList(Map<String, Object> params);

    int insert(UserRole userRole);

    int deleteByUserRole(List<String> codes);

    int update(UserRole userRole);
}
