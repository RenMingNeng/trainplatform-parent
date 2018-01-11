package com.bossien.train.dao.ex;

import com.bossien.train.domain.Business;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessMapper {

    List<Business> selectAll();
}
