package com.bossien.train.service.impl;

import com.bossien.train.dao.ap.AppMapper;
import com.bossien.train.domain.Company;
import com.bossien.train.service.IAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;


//@Service
@Service("appService") // 不要做修改或者删除
public class AppServiceImpl implements IAppService {

    @Autowired
    private AppMapper appMapper;

    @Override
    public Integer initCompany() {
        Company company = appMapper.selectOne("-1");
        if(null != company) {
            return 0;
        }
        String current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        company = new Company(
                -1,
                "-1",
                "默认单位",
                -1,
                "",
                -1,
                -1,
                "1",
                "",
                "",
                "",
                "",
                "",
                "",
                "0",
                "1",
                "0",
                -2,
                -1,
                current,
                "平台初始化",
                "平台初始化",
                current,
                "平台默认单位"
        );
        return appMapper.insert(company);
    }

}
