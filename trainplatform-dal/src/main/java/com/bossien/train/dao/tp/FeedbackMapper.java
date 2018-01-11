package com.bossien.train.dao.tp;

import com.bossien.train.domain.Feedback;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface FeedbackMapper {

    Integer insert(Feedback feedback);

    Integer selectCount(Map<String, Object> params);

    List<Feedback> selectList(Map<String, Object> params);

    void update(Feedback feedback);
}
