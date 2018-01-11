package com.bossien.train.dao.tp;

import com.bossien.train.domain.UserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserRoleMapper {

    UserRole selectOne(Map<String, Object> params);

    List<UserRole> selectList(Map<String, Object> params);

    int insert(UserRole userRole);

    int update(UserRole userRole);

    int deleteByUserRole(List<String> codes);

    int delete(UserRole userRole);

    int deleteByRoleId(Map<String, Object> map);

    int batchDelUserRole(Map<String, Object> params);
}
