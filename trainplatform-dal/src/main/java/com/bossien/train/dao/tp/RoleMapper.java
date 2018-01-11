package com.bossien.train.dao.tp;

import com.bossien.train.domain.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMapper {

    Role selectOne(Role role);

}
