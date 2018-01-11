package com.bossien.train.service;

import com.bossien.train.domain.ExcelResultEntity;
import com.bossien.train.domain.User;
import com.bossien.train.domain.UserTrainRole;
import com.bossien.train.domain.UserWork;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Administrator on 2017/7/31.
 */
public interface IImportExcelUserService {

    ExcelResultEntity handlerExcel(MultipartFile file, User user, String params);

    UserTrainRole bulidUserTrainRole(User user);

    UserWork bulidUserWork(User user);

}
