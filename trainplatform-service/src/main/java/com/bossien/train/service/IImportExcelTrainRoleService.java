package com.bossien.train.service;

import com.bossien.train.domain.ExcelResultEntity;
import com.bossien.train.domain.TrainRole;

import com.bossien.train.domain.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/7/31.
 */
public interface IImportExcelTrainRoleService {

    ExcelResultEntity handlerExcel(MultipartFile file, String param_, User user);


}
