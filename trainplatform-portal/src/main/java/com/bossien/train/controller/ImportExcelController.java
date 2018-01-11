package com.bossien.train.controller;

import com.bossien.framework.mq.sender.QueuePersonDossierInfoSender;
import com.bossien.train.common.AppConstant;
import com.bossien.train.domain.ExcelResultEntity;
import com.bossien.train.domain.User;
import com.bossien.train.domain.eum.ExcelImportEnum;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IImportExcelTrainRoleService;
import com.bossien.train.service.IImportExcelUserService;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

/**
 * excel文件上传
 * Created by Administrator on 2017/7/26.
 */
@Controller
@RequestMapping("admin/excelImport")
public class ImportExcelController extends BasicController {

    private static final Logger logger = LogManager.getLogger(ImportExcelController.class);
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    @Autowired
    private IImportExcelUserService importUserService;

    @Autowired
    private IImportExcelTrainRoleService importExcelTrainRoleService;
    @Autowired
    private QueuePersonDossierInfoSender queuePersonDossierInfoSender;

    /**
     * 去excel导入页面
     * @param request
     * @param params
     * @param tempType 模板类型
     * @param exportTye  1调用学员导出方法 2调用角色导出方法
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/to_import")
    public ModelAndView userImport(HttpServletRequest request,String params,String tempType,
                                   @RequestParam(value="exportTye", required=false, defaultValue = "") String exportTye)throws Exception {
        ModelAndView mv = new ModelAndView();
        if (ExcelImportEnum.USER_TEMP.getValue().equals(tempType)) {//用户导入模板
            mv.addObject("tempName", ExcelImportEnum.USER_TEMP.getTempName());
        } else if (ExcelImportEnum.ROLE_TEMP.getValue().equals(tempType)) {
            mv.addObject("tempName", ExcelImportEnum.ROLE_TEMP.getTempName());
        } else {

        }
        mv.addObject("params", params);
        mv.addObject("exportTye", exportTye);
        mv.setViewName("common/import");
        return mv;
    }


    // 模板下载
    @ResponseBody
    @RequestMapping(value = "/template_download", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<byte[]> tempDownload(HttpServletRequest request,
                                               @RequestParam(value = "tempName", required = true, defaultValue = "") String tempName) {
        try {
            String path = request.getSession().getServletContext().getRealPath("/template")+File.separator+tempName;
            File file = new File(path);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", new String(tempName.getBytes("UTF-8"), "iso-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            byte[] rs = FileUtils.readFileToByteArray(file);
            return new ResponseEntity<byte[]>(rs, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    // 导入结果下载
    @ResponseBody
    @RequestMapping(value = "/result_download", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<byte[]> importDownLoad(
            @RequestParam(value = "fileName", required = true, defaultValue = "") String fileName) {
        try {
            File file = new File(AppConstant.TP_EXCEL_TEMP_PATH + File.separator + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
            headers.setContentType(APPLICATION_OCTET_STREAM);
            byte[] rs = FileUtils.readFileToByteArray(file);
            return new ResponseEntity<byte[]>(rs, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param tempName
     * @param params
     * @param exportTye 1调用学员导出方法 2调用角色导出方法
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/import")
    public void importExcelData (
            @RequestParam(value = "tempName", required = true, defaultValue = "") String tempName,
            @RequestParam(value = "params", required = false, defaultValue = "") String params,
            @RequestParam(value = "exportTye", required = false, defaultValue = "") String exportTye,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Response<Object> resp = new Response<Object>();
        User user = getCurrUser(request);

        if (!(request instanceof MultipartHttpServletRequest)) {
            resp.setMessage("上传文件为空");
            resp.setResult(Boolean.FALSE);
            writeMessageUft8(response, resp);
            return;
        }
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartHttpServletRequest.getFile("file");
        if (null == file || file.isEmpty()) {
            resp.setMessage("上传文件为空");
            resp.setResult(Boolean.FALSE);
            writeMessageUft8(response, resp);
            return;
        }

        ExcelResultEntity excelResultEntity = null;
        //OperExcelFactory op=new OperExcelFactory();
        if (ExcelImportEnum.USER_TEMP.getValue().equals(exportTye)) {
            //导入学员实现
            excelResultEntity = importUserService.handlerExcel(file, user, params);
        } else {
            excelResultEntity = importExcelTrainRoleService.handlerExcel(file, params, user);
            //导入角色实现
        }
        //ExcelResultEntity excelResultEntity = importService.handlerExcel(file, user, params);
        if (excelResultEntity != null) {
            resp.setResult(excelResultEntity);
            resp.setCode(10000);
            resp.setMessage("导入成功");
            //导入学员成功后发送消息  将数据插入人员档案表
            if (ExcelImportEnum.USER_TEMP.getValue().equals(exportTye)) {
                // 发送到activemq的cppub.queue
                Map<String,Object> queueMap = new ConcurrentHashMap<String, Object>();
                queueMap.put("userIds",excelResultEntity.getIdsList());
                queueMap.put("companyId",user.getCompanyId());
                queueMap.put("userName",user.getUserName());
                EXECUTOR.execute(new ImportExcelController.PersonDossierDeal(queueMap));
            }
        } else {
            resp.setResult(excelResultEntity);
            resp.setMessage("导入失败");
        }
        writeMessageUft8(response, resp);
        //return JSONObject.toJSONString(resp);
    }

    /**
     * 异步处理数据
     */
    private class PersonDossierDeal extends Thread {

        private Map<String,Object> queueMap;

        public PersonDossierDeal(Map<String,Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queuePersonDossierInfoSender.sendMapMessage("tp.personDossier.queue", queueMap);
            }catch(Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }
}
