package com.bossien.train.dao.ap;

import com.bossien.train.domain.CompanyCategory;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/9/13.
 */
@Repository
public interface CompanyCategoryMapper {

    List<CompanyCategory> selectAll();
}
