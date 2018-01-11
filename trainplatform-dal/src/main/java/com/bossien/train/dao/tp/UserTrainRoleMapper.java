package com.bossien.train.dao.tp;

import com.bossien.train.domain.UserTrainRole;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserTrainRoleMapper {

    UserTrainRole selectOne(UserTrainRole userTrainRole);

    int insert(UserTrainRole userTrainRole);

    int update(UserTrainRole userTrainRole);

    int insertBatch(List<UserTrainRole> list);

    List<UserTrainRole> selectUserRoles(UserTrainRole userTrainRole);

    /**
     * 根据参数查询用户受训角色集合
     * @param params
     * @return
     */
    List<UserTrainRole> selectList(Map<String, Object> params);

    List<UserTrainRole> selectRoleByUserId(UserTrainRole userTrainRole);

    int delete(UserTrainRole userTrainRole);

    int batchDelUtrole(Map<String, Object> params);
}
