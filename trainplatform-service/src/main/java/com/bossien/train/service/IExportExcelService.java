package com.bossien.train.service;

import com.bossien.train.domain.ExcelResultEntity;
import com.bossien.train.domain.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */
public interface IExportExcelService {

    void queryExportData(HttpServletResponse response, User user, String params, String depId, List<String> companyIds,boolean flag);

}
