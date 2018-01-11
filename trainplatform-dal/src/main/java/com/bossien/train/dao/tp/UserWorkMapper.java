package com.bossien.train.dao.tp;

import com.bossien.train.domain.UserWork;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserWorkMapper {

    UserWork selectOne(Map<String,Object> params);

    int insert(UserWork userWork);

    int update(UserWork userWork);

    List<UserWork> selectList(UserWork userWork);

    int delete(UserWork userWork);

    int batchDelUserWork(Map<String,Object> params);

}
