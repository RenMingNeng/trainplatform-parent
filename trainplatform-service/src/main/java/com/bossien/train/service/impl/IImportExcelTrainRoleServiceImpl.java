package com.bossien.train.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bossien.train.common.AppConstant;
import com.bossien.train.dao.ap.CompanyTrainRoleMapper;
import com.bossien.train.dao.ap.TrainRoleMapper;
import com.bossien.train.domain.CompanyTrainRole;
import com.bossien.train.domain.ExcelResultEntity;
import com.bossien.train.domain.TrainRole;
import com.bossien.train.domain.User;
import com.bossien.train.service.IImportExcelTrainRoleService;
import com.bossien.train.service.ISequenceService;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.ExcelUtil;
import com.bossien.train.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/8/15.
 */
@Service
public class IImportExcelTrainRoleServiceImpl implements IImportExcelTrainRoleService {
    @Autowired
    private TrainRoleMapper trainRoleMapper;
    @Autowired
    private CompanyTrainRoleMapper companyTrainRoleMapper;
    @Autowired
    private ISequenceService sequenceService;


    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Logger logger = LoggerFactory.getLogger(IImportExcelTrainRoleServiceImpl.class);

    @Override
    public ExcelResultEntity handlerExcel(MultipartFile file, String param_, User user) {
        // 其他参数
        TrainRole trainRole = new TrainRole();
        Map<String, String> params = null;
        if (!StringUtils.isEmpty(param_)) {
            param_ = URLDecoder.decode(param_);
            params = (Map<String, String>) JSONObject.toJavaObject(JSONObject.parseObject(param_), Map.class);
        }
        // 解析excel获取数据
        List<List<String>> lList = readExcelData(file);
        List<List<String>> list = StringUtil.removeListByEmpty(lList);
        // 导出excel结果集
        LinkedList<Map<String, Object>> lMap = new LinkedList<Map<String, Object>>();
        // 逐行校验excel并导入
        for (int i = 0; i < list.size(); i++) {
            this.verifyAndInsert(list.get(i), lMap, trainRole, user);
        }
        // 错误文件头部
        Map<String, Object> headerMap = this.getHeader();
        // 生成错误文件
        XSSFWorkbook workbook = ExcelUtil.createExcel(lMap, headerMap);
        File temp = null;
        File tempFile = new File(AppConstant.TP_EXCEL_TEMP_PATH);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        try {
            temp = File.createTempFile("temp", ".xlsx", tempFile);
            workbook.write(new BufferedOutputStream(new FileOutputStream(temp)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ExcelResultEntity(list, lMap, headerMap, temp.getName(), null);
    }

    // 读取excel数据
    public List<List<String>> readExcelData(MultipartFile file) {
        BufferedOutputStream stream = null;
        File temporaryfile = null;
        try {
            byte[] filebyte = file.getBytes();
            temporaryfile = File.createTempFile("Template", ".xlsx");
            stream = new BufferedOutputStream(new FileOutputStream(temporaryfile));
            stream.write(filebyte);
            return ExcelUtil.getSheetDataByExcel(temporaryfile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                stream.close();
                temporaryfile.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 获取导出的头部
    private Map<String, Object> getHeader() {
        Map<String, Object> header = new LinkedHashMap<String, Object>();
        //受训角色名称
        header.put("roleName", "受训角色");
        // 导入结果
        header.put("result", "导入结果");
        // 导入描述
        header.put("description", "导入描述");
        return header;
    }

    // 逐行校验
    private void verifyAndInsert(List<String> list, List<Map<String, Object>> lMap, TrainRole trainRole, User user) {
        String msg = "";
        // 校验-姓名-roleName
        msg = this.verifyUserName(list.get(0),user);
        if (!StringUtils.isEmpty(msg)) {
            this.insertError(list, lMap, msg);
            return;
        }
        // 规则：人员信息做覆盖处理,之前的受训角色全部禁用,插入excel中指定的受训角色
        trainRole.setVarId(sequenceService.generator());
        trainRole.setRoleName(list.get(0));
        trainRole.setCreateUser(user.getCreateUser());
        trainRole.setSource("2");//来源默认是企业自制
        trainRole.setCompanyId(user.getCompanyId());
        trainRole.setIsValid("1");//新增操作默认有效
        Date date = new Date();
        trainRole.setCreateDate(DateUtils.convertDate2StringTime(date));
        trainRole.setOperDate(DateUtils.convertDate2StringTime(date));
        trainRole.setOperUser(user.getOperUser());
        trainRoleMapper.insert(trainRole);
        CompanyTrainRole record = new CompanyTrainRole();
        record.setIntTrainRoleId(trainRole.getVarId());
        record.setIntCompanyId(user.getCompanyId());
        companyTrainRoleMapper.insertSelective(record);
    }


    // 获取性别代码
    private String getSex(String name) {
        if (StringUtils.isEmpty(name)) {
            return "";
        }
        if (User.Sex.TYPE_1.getName().equals(name)) {
            return User.Sex.TYPE_1.getValue();
        }
        if (User.Sex.TYPE_2.getName().equals(name)) {
            return User.Sex.TYPE_2.getValue();
        }
        return "";
    }

    // 收集错误数据
    private void insertError(List<String> list, List<Map<String, Object>> lMap, String msg) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        // 姓名
        map.put("roleName", list.get(0));
        // 导入结果
        map.put("result", "失败");
        // 错误信息
        map.put("description", msg);
        lMap.add(map);
    }

    // 校验-姓名-varUserName
    private String verifyUserName(String varUserName,User user) {
        String msg = "";
        Map<String, Object> params = new HashMap<String, Object>();
        List<String> stringList = companyTrainRoleMapper.selectByCompanyId(user.getCompanyId());
        params.put("ids", stringList);
        params.put("chrSource", "2");
        List<TrainRole> trainRoles = trainRoleMapper.selectVerify(params);
        for (TrainRole trainSubject: trainRoles) {
            if (trainSubject.getRoleName().equals(varUserName)){
                msg = "姓名不能重复";
                return msg;
            }
        }

        if (StringUtils.isEmpty(varUserName)) {
            msg = "姓名不能为空";
            return msg;
        }
        return msg;
    }


}

