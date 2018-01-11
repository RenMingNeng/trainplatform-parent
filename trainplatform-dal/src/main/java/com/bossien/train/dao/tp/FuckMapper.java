package com.bossien.train.dao.tp;

import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface FuckMapper {

	List<Map<String, Object>> selectList(Map<String, Object> params);

	Integer selectCount(Map<String, Object> params);
}