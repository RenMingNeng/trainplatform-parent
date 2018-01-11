package com.bossien.train.controller;

import com.bossien.train.domain.User;
import com.bossien.train.service.IDepartmentService;
import com.bossien.train.service.IExportExcelService;
import com.bossien.train.service.IUserManagerService;
import com.bossien.train.util.RequestUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/8/16.
 */
@Controller
@RequestMapping("admin/excelExport")
public class ExportExcelController extends BasicController{
    private static final Logger logger = LogManager.getLogger(ExportExcelController.class);

    @Autowired
    private IExportExcelService exportExcelService;

    //去导出方法
    @RequestMapping(value = "/export", produces = "text/plain;charset=UTF-8")
    public String export(
            HttpServletResponse response,
            @RequestParam(value = "fileName", required = true, defaultValue = "") String fileName,    // 导出文件名称
            @RequestParam(value = "depId", required = false, defaultValue = "") String depId,       // 部门id
            @RequestParam(value = "companyIds", required = false, defaultValue = "") String[] companyIds,       // 公司id
            @RequestParam(value = "params", required = false, defaultValue = "") String params,// 其他参数
            @RequestParam(value = "flag", required = false, defaultValue = "") boolean flag) {
        try {
            if(fileName.indexOf(".")<0){
                return null;
            }
            // 导出文件后缀格式
            String fileExt = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            // 导出excel
            if(fileExt.equalsIgnoreCase(".xlsx")){
                return "forward:/admin/excelExport/excel_export";
            }
            // 导出zip(暂未实现)
            if(fileExt.equalsIgnoreCase(".zip")){
                return "forward:/admin/excelExport/export_mutil";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 导出
    @ResponseBody
    @RequestMapping(value = "/excel_export", produces = "text/plain;charset=UTF-8")
    public void single(HttpServletRequest request,HttpServletResponse response,
            @RequestParam(value = "fileName", required = true, defaultValue = "") String fileName, 	// 导出文件名
            @RequestParam(value = "depId", required = false, defaultValue = "") String depId,       // 部门id
            @RequestParam(value = "companyIds", required = false, defaultValue = "") String[] companyIds,       // 公司id
            @RequestParam(value = "params", required = false, defaultValue = "") String params,
                       @RequestParam(value = "flag", required = false, defaultValue = "") boolean flag) { 	// 其他参数
        try {
            User user=getCurrUser(request);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", MessageFormat.format("attachment;filename={0}", RequestUtils.getCNParam(request, fileName)));
            exportExcelService.queryExportData(response,user, params, depId, Arrays.asList(companyIds),flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
