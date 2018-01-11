package com.bossien.train.service;

import com.bossien.train.domain.CompanyTj;

/**
 * Created by A on 2017/7/25.
 */
public interface IClassHoursService {

    /**
     * 根据时间查询监管数据
     */
    CompanyTj selectSuperviceByTime(String companyId, String companyName, String startTime, String endTime);
}
