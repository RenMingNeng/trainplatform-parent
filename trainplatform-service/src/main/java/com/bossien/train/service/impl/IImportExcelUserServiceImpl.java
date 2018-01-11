package com.bossien.train.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bossien.train.common.AppConstant;
import com.bossien.train.dao.ap.CompanyMapper;
import com.bossien.train.dao.ap.CompanyTrainRoleMapper;
import com.bossien.train.dao.ap.TrainRoleMapper;
import com.bossien.train.dao.tp.*;
import com.bossien.train.domain.*;
import com.bossien.train.service.IImportExcelUserService;
import com.bossien.train.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/8/15.
 */
@Service
public class IImportExcelUserServiceImpl implements IImportExcelUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserWorkMapper userWorkMapper;
    @Autowired
    private UserTrainRoleMapper userTrainRoleMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private TrainRoleMapper trainRoleMapper;
    @Autowired
    private DepartmentMapper deptMapper;
    @Autowired
    private PersonDossierMapper personDossierMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private CompanyTrainRoleMapper companyTrainRoleMapper;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ExcelResultEntity handlerExcel(MultipartFile file, User user, String param_) {
        // 其他参数
        Map<String, Object> params = null;
        if (!StringUtils.isEmpty(param_)) {
            param_ = URLDecoder.decode(param_);
            params = (Map<String, Object>) JSONObject.toJavaObject(JSONObject.parseObject(param_), Map.class);
        }
        // 获取部门id
        String departmentId =String.valueOf(params.get("departmentId"));
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("intId", params.get("companyId"));
        Company company = companyMapper.selectByOne(data);
        if(null == company){
            company = new Company();
        }
        // 解析excel获取数据
        List<List<String>> lList = readExcelData(file);
        List<List<String>> list = StringUtil.removeListByEmpty(lList);
        // 导出excel结果集
        LinkedList<Map<String, Object>> lMap = new LinkedList<Map<String, Object>>();
        // 逐行校验excel并导入
        List<String> idsList=new ArrayList<String>();
        List<User> uList=new ArrayList<User>();
        for (int i = 0; i < list.size(); i++) {
            this.verifyAndInsert(list.get(i), lMap, user, departmentId, company, uList);
        }
        //处理合格的数据
        insertData(uList,idsList,lMap);

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
        return new ExcelResultEntity(list, lMap, headerMap, temp.getName(),idsList);
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

    public void insertData(List<User> uList,List<String> idsList,LinkedList<Map<String, Object>> lMap){
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String,Object> deptMap=new HashMap<String,Object>();
        List<String> excellist=null;
        User u=null;
        if(uList.size()>0){
            List<List<User>> listUser = StringUtil.subList(uList, uList.size()/1000);
            for(List<User> userList:listUser) {
                for (User usr : userList) {

                    //插入用户表
                    param.put("user_account", usr.getUserAccount());
                   /* param.put("isValid", User.IsValid.TYPE_1.getValue());*/
                    User user_ = userMapper.selectOne(param);
                    if (null != user_) {
                        //如果导入的学员在数据中存在并且学员类型是管理员
                        if(User.UserType.TYPE_1.getValue().equals(user_.getUserType())){
                            //重新给该管理员添加角色
                            insertRole(user_);

                            //插入用户受训角色表
                            insertUserTrainRole(user_);
                        }else{
                            excellist=new ArrayList<String>();
                            String msg="该用户已经存在";
                            excellist.add(usr.getUserName());
                            excellist.add(usr.getUserAccount());
                            excellist.add(usr.getUserPasswd());
                            excellist.add(usr.getTroleName());
                            excellist.add(usr.getMobileNo());
                            excellist.add(usr.getSex());
                            excellist.add(usr.getIdNumber());
                            this.insertError(excellist, lMap, msg);

                            //人员信息修改 账号不能修改 只修改能修改的必要字段
                            /*u=new User();
                            u.setId(user_.getId());
                            u.setCompanyId(usr.getCompanyId());
                            u.setDepartmentId(usr.getDepartmentId());
                            u.setDepartmentName(usr.getDepartmentName());
                            u.setUserName(usr.getUserName());
                            u.setUserPasswd(MD5Utils.encoderByMd5With32Bit(usr.getUserPasswd()));
                            u.setIdNumber(usr.getIdNumber());
                            u.setSex(usr.getSex());
                            u.setOperDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            userMapper.updateById(u);*/

                            //将用户id放入list
                            //idsList.add(u.getId());

                            //修改用户工作信息
//                            usr.setId(user_.getId());
//                            updateUserWork(usr,deptMap);

                            //修改用户受训角色信息(先删除所有再插入新角色)
                        /*    UserTrainRole utr=new UserTrainRole();
                            utr.setUserId(user_.getId());
                            userTrainRoleMapper.delete(utr);

                            //插入用户受训角色表
                            insertUserTrainRole(usr);*/
                        }

                    }else{
                        usr.setUserPasswd(MD5Utils.encoderByMd5With32Bit(usr.getUserPasswd()));
                        //新增
                        userMapper.insert(usr);

                        //将用户id放入list
                        idsList.add(usr.getId());

                        //插入用户角色表
                        insertRole(usr);

                        //插入用户工作信息表
                        insertUserWork(usr,deptMap);

                        //插入用户受训角色表
                        insertUserTrainRole(usr);

                    }

                }
            }//for

        }//if

    }

    //插入数据到用户角色表
    void insertRole(User usr){
        // 获取学员角色的roleId
        Role role = new Role(null, null, null);
        role.setRoleName(PropertiesUtils.getValue("SYS_ROLE_COMPANY_USER"));
        role = roleMapper.selectOne(role);
        // 人员角色关联表-批量新增
        UserRole userRole = buildUserRole(usr);
        userRole.setUserId(usr.getId());
        userRole.setRoleId(role.getId());
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("userId", usr.getId());
        para.put("roleId", role.getId());
        // 去重 如果不存在就插入
        if (null == userRoleMapper.selectOne(para)) {
            userRoleMapper.insert(userRole);
        }
    }

    //插入数据到用户工作信息表
    void insertUserWork(User usr,Map<String,Object> deptMap){
        // 人员变动记录-新增
        UserWork userWork = bulidUserWork(usr);
        userWork.setUserId(usr.getId());// 用户序号
        userWork.setCompanyId(usr.getCompanyId());// 所属单位
        userWork.setDeptId(usr.getDepartmentId());// 所属部门

        if(!StringUtils.isEmpty(usr.getDepartmentId())){
            deptMap.put("id",usr.getDepartmentId());
            Department depart=deptMapper.selectByParams(deptMap);
            if(depart!=null){
                userWork.setDeptName(depart.getDeptName());// 所属各级部门名称
            }
        }

        userWork.setOperater(UserWork.Operater.TYPE_1.getValue()); // 操作记录(1.人员登记/2：人员转移/3：人员退厂)
        userWorkMapper.insert(userWork);
    }

    //插入数据到用户工作信息表
    void updateUserWork(User usr,Map<String,Object> deptMap){
        // 人员变动记录-新增
        UserWork userWork = bulidUserWork(usr);
        userWork.setUserId(usr.getId());// 用户序号
        userWork.setCompanyId(usr.getCompanyId());// 所属单位
        userWork.setDeptId(usr.getDepartmentId());// 所属部门

        if(!StringUtils.isEmpty(usr.getDepartmentId())){
            deptMap.put("id",usr.getDepartmentId());
            Department depart=deptMapper.selectByParams(deptMap);
            if(depart!=null){
                userWork.setDeptName(depart.getDeptName());// 所属各级部门名称
            }
        }

        userWork.setOperater(UserWork.Operater.TYPE_4.getValue()); // 操作记录(1.人员登记/2：人员转移/3：人员退厂)
        userWorkMapper.update(userWork);
    }

    //插入数据到用户角色表
    void insertUserTrainRole(User usr){
        // 人员受训角色关联表-批量新增
        String roleNames =usr.getTroleName();
        // 查询该单位下受训角色list
        List<Map<String, Object>> roleLMap=new ArrayList<Map<String, Object>>();
        Map<String, Object> params = new HashMap<String, Object>();
        List<String> companyTrainRoles=companyTrainRoleMapper.selectByCompanyId(usr.getCompanyId());
        if(companyTrainRoles.size()>0){
            params.put("ids",companyTrainRoles);
            roleLMap = trainRoleMapper.selectByParams(params);
        }

        UserTrainRole userTrainRole = null;
        UserTrainRole ustr = new UserTrainRole();
        if (StringUtils.isEmpty(roleNames)) {
            // 不填写受训角色默认为入场人员
            userTrainRole = bulidUserTrainRole(usr);
            userTrainRole.setUserId(usr.getId());
            userTrainRole.setTrainRoleId("-1");//默认角色
            userTrainRole.setIsValid(UserTrainRole.IsValid.TYPE_1.getValue());
            userTrainRole.setRoleName("默认角色");
            userTrainRoleMapper.insert(userTrainRole);
        } else {
            roleNames = roleNames.replaceAll("；", ";").replaceAll(",", ";").replaceAll("，", ";");// 中文封号,逗号容错
            List<String> varRoleNames_ = Arrays.asList(roleNames.split(";"));
            for (String varRoleName : varRoleNames_) {

                ustr.setUserId(usr.getId());
                ustr.setTrainRoleId(getRoleId(roleLMap, varRoleName));
                ustr.setRoleName(getRoleName(roleLMap, varRoleName));
                // 去重
                UserTrainRole utr = userTrainRoleMapper.selectOne(ustr);
                if(utr==null){
                    userTrainRole = bulidUserTrainRole(usr);
                    userTrainRole.setUserId(usr.getId());
                    userTrainRole.setTrainRoleId(getRoleId(roleLMap, varRoleName));
                    userTrainRole.setRoleName(getRoleName(roleLMap, varRoleName));
                    userTrainRole.setIsValid(UserTrainRole.IsValid.TYPE_1.getValue());
                    userTrainRoleMapper.insert(userTrainRole);
                }


            }
        }
    }

    // 获取导出的头部
    private Map<String, Object> getHeader() {
        Map<String, Object> header = new LinkedHashMap<String, Object>();
        // 姓名
        header.put("userName", "姓名");
        // 账号
        header.put("userAccount", "账号");
        // 密码
        header.put("userPasswd", "密码");
        // 受训角色
        header.put("roleNames", "受训角色");
        // 手机号
        header.put("mobileNo", "手机号");
        // 性别
        header.put("sex", "性别");
        // 身份证号
        header.put("idNumber", "身份证号");
        // 导入结果
        header.put("result", "导入结果");
        // 导入描述
        header.put("description", "导入描述");
        return header;
    }

    // 逐行校验
    private void verifyAndInsert(List<String> list, List<Map<String, Object>> lMap, User user, String departmentId, Company company,List<User> uList) {
        String msg = "";
        // 校验-姓名-userName
        msg = this.verifyUserName(StringUtil.trim(list.get(0)));
        if (!StringUtils.isEmpty(msg)) {
            this.insertError(list, lMap, msg);
            return;
        }
        // 校验-账号-userAccount
        msg = this.verifyUserAccount(StringUtil.trim(list.get(1)));
        if (!StringUtils.isEmpty(msg)) {
            this.insertError(list, lMap, msg);
            return;
        }
        // 校验-密码-userPasswd
        msg = this.verifyUserPasswd(StringUtil.trim(list.get(2)));
        if (!StringUtils.isEmpty(msg)) {
            this.insertError(list, lMap, msg);
            return;
        }
        // 查询该单位下受训角色list
        List<Map<String, Object>> roleLMap=null;
        Map<String, Object> params = new HashMap<String, Object>();
        List<String> companyTrainRoles=companyTrainRoleMapper.selectByCompanyId(company.getIntId().toString());
        if(companyTrainRoles.size()>0){
            params.put("ids",companyTrainRoles);
            roleLMap = trainRoleMapper.selectByParams(params);
        }
        // 校验-受训角色-varRoleNames
        msg = this.verifyRoleNames(list.get(3), roleLMap);
        if (!StringUtils.isEmpty(msg)) {
            this.insertError(list, lMap, msg);
            return;
        }

        // 校验-手机号-varMobileNo
        msg = this.verifyMobileNo(StringUtil.trim(list.get(4)));
        if (!StringUtils.isEmpty(msg)) {
            this.insertError(list, lMap, msg);
            return;
        }
        // 校验-性别-chrSex
        msg = this.verifySex(StringUtil.trim(list.get(5)));
        if (!StringUtils.isEmpty(msg)) {
            this.insertError(list, lMap, msg);
            return;
        }
        // 校验-身份证号-varIdNumber
        msg = this.verifyIdNumber(StringUtil.trim(list.get(6)));
        if (!StringUtils.isEmpty(msg)) {
            this.insertError(list, lMap, msg);
            return;
        }

        // 封装用户数据
        Map<String, Object> para = new HashMap<String, Object>();
        User users=new User();
        users = bulidUser(user);
        users.setUserName(StringUtil.trim(list.get(0)));// 姓名
        users.setUserAccount(StringUtil.trim(list.get(1)));// 账号
        //用户密码进行MD5加密
        //users.setUserPasswd(MD5Utils.encoderByMd5With32Bit(StringUtil.trim(list.get(2))));// 密码
        users.setUserPasswd(StringUtil.trim(list.get(2)));
        users.setTroleName(list.get(3)); //受训角色
        users.setMobileNo(StringUtil.trim(list.get(4)));// 手机号
        users.setSex(this.getSex(StringUtil.trim(list.get(5))));// 性别
        users.setIdType(User.IdType.TYPE_1.getValue());// 证件类型:1: 身份证 2: 居住证 3: 签证 4: 护照5: 军人证6: 港澳通行证7: 台胞证
        users.setIdNumber(StringUtil.trim(list.get(6)));// 身份证号
        users.setCompanyId(company.getIntId().toString());// 所属单位
        //学员表插入部门名称
        if(departmentId!=null && !"".equals(departmentId)){
            users.setDepartmentId(departmentId);// 所属部门
            para.put("id",departmentId);
            para.put("isValid",User.IsValid.TYPE_1.getValue());
            Department dept = deptMapper.selectByParams(para);
            if(dept!=null){
                users.setDepartmentName(dept.getDeptName());
            }else{
                users.setDepartmentName("");
            }
        }else{
            //因开发需要，没选择部门时，部门id还是插入单位id 不能为空
            users.setDepartmentId(company.getIntId().toString());// 所属部门默认是无部门的
//            if(null != company && !StringUtil.isEmpty(company.getVarName())){
                users.setDepartmentName("");
//            }
        }
        users.setRegistType(User.RegistType.TYPE_2.getValue());// 注册类型
        users.setIsValid(User.IsValid.TYPE_1.getValue());// 是否有效
        users.setSupporter(User.Supporter.TYPE_1.getValue());//载体
        users.setUserType(User.UserType.TYPE_3.getValue());//用户类型(学员)
        uList.add(users);

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
        map.put("userName", list.get(0));
        // 账号
        map.put("userAccount", list.get(1));
        // 密码
        map.put("userPasswd", list.get(2));
        // 受训角色
        map.put("roleNames", list.get(3));
        // 手机号
        map.put("mobileNo", list.get(4));
        // 性别
        map.put("sex", list.get(5));
        // 身份证号
        map.put("idNumber", list.get(6));
        // 导入结果
        map.put("result", "失败");
        // 错误信息
        map.put("description", msg);
        lMap.add(map);
    }

    // 校验-姓名-varUserName
    private String verifyUserName(String varUserName) {
        String msg = "";
        if (StringUtils.isEmpty(varUserName)) {
            msg = "姓名不能为空";
            return msg;
        }

        return msg;
    }

    // 校验-账号-varUserAccount
    private String verifyUserAccount(String userAccount) {
        String msg = "";
        if (StringUtils.isEmpty(userAccount)) {
            msg = "账号不能为空";
            return msg;
        }
       /* Map<String, Object> umap = new HashMap<String, Object>();
        umap.put("user_account", userAccount);
        User user = userMapper.selectOne(umap);
        if (null != user) {
            msg = "账号已经存在";
            return msg;
        }*/
        return msg;
    }

    // 校验-密码-varUserPasswd
    private String verifyUserPasswd(String varUserPasswd) {
        String msg = "";
        if (StringUtils.isEmpty(varUserPasswd)) {
            msg = "密码不能为空";
            return msg;
        }
        if(varUserPasswd.length() < 6){
            msg = "密码长度不少于6位";
            return msg;
        }
        return msg;
    }

    // 校验-受训角色-varRoleNames
    private String verifyRoleNames(String varRoleNames, List<Map<String, Object>> roleLMap) {
        String msg = "";
        if (!StringUtils.isEmpty(varRoleNames)) {
            // 中文封号,逗号容错
            varRoleNames = varRoleNames.replaceAll("；", ";").replaceAll(",", ";").replaceAll("，", ";");
            // 校验受训角色是否在该单位下受训角色中
            List<String> varRoleNames_ = Arrays.asList(varRoleNames.split(";"));
            for (String varRoleName : varRoleNames_) {
                if(roleLMap==null || roleLMap.size()==0){
                    msg = MessageFormat.format("该单位下不存在名称为{0}的受训角色", varRoleName);
                    return msg;
                }else{
                    if (!this.contains(roleLMap, varRoleName)) {
                        msg = MessageFormat.format("该单位下不存在名称为{0}的受训角色", varRoleName);
                        return msg;
                    }

                }
            }
        }
        return msg;
    }

    // 校验-手机号-varMobileNo
    private String verifyMobileNo(String varMobileNo) {
        String msg = "";
        if (!StringUtils.isEmpty(varMobileNo)) {
            if (11 != varMobileNo.length()) {
                msg = "手机号必须11位";
                return msg;
            }
        }
        return msg;
    }

    // 校验-身份证号-varIdNumber
    private String verifyIdNumber(String varIdNumber) {
        String msg = "";
        if (!StringUtils.isEmpty(varIdNumber)) {
            if (18 != varIdNumber.length()) {
                msg = "身份证必须18位";
                return msg;
            }
        }
        return msg;
    }

    // 校验-性别-chrSex
    private String verifySex(String chrSex) {
        String msg = "";
        if (!StringUtils.isEmpty(chrSex)) {
            if (!"男".equals(chrSex) && !"女".equals(chrSex)) {
                msg = "性别请填写【男/女】";
                return msg;
            }
        }
        return msg;
    }

    private boolean contains(List<Map<String, Object>> roleLMap, String varRoleName) {
        boolean contains = false;
        for (Map<String, Object> map : roleLMap) {
            if (map.containsValue(varRoleName)) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    private String  getRoleId(List<Map<String, Object>> roleLMap, String varRoleName) {
        String varRoleId = "";
        if(roleLMap.size()>0 && !roleLMap.isEmpty()){
            for (Map<String, Object> map : roleLMap) {
                if (map.containsValue(varRoleName)) {
                    varRoleId = (String) map.get("varId");
                    break;
                }
            }
        }
        return varRoleId;
    }

    private String getRoleName(List<Map<String, Object>> roleLMap, String roleName) {
        String role_name = "";
        if(roleLMap.size()>0 && !roleLMap.isEmpty()) {
            for (Map<String, Object> map : roleLMap) {
                if (map.containsValue(roleName)) {
                    role_name = (String) map.get("varRoleName");
                    break;
                }
            }
        }
        return role_name;
    }


    public User bulidUser(User user) {
        // 当前时间
        String sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // user_
        User user_ = new User();
        // 序号
        user_.setId(UUIDGenerator.genID()+UUIDGenerator.genID());
        // 创建用户
        user_.setCreateUser(user.getUserName());
        // 创建日期
        user_.setCreateDate(sdr);
        // 操作用户
        user_.setOperUser(user.getUserName());
        // 操作日期
        user_.setOperDate(sdr);
        // 创建该条记录的用户所属的部门序号
        user_.setDepartmentId(user.getDepartmentId());
        //载体
        user_.setSupporter(User.Supporter.TYPE_1.getValue());
        //用户类型
        user_.setUserType(User.UserType.TYPE_3.getValue());
        user_.setRegistDate(sdr);// 注册时间
        // return
        return user_;
    }


    @Override
    public UserWork bulidUserWork(User user) {
        // 当前时间
        String sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // userWork
        UserWork userWork = new UserWork();
        userWork.setId(UUIDGenerator.genID()+UUIDGenerator.genID());
        // 创建用户
        userWork.setCreateUser(user.getCreateUser());
        // 创建日期
        userWork.setCreateTime(sdr);
        // 操作用户
        userWork.setOperUser(user.getCreateUser());
        // 操作日期
        userWork.setOperTime(sdr);
        userWork.setEntryTime(sdr);// 入职时间
        // return
        return userWork;
    }

    @Override
    public UserTrainRole bulidUserTrainRole(User user) {
        // 当前时间
        String sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // userTrainRole
        UserTrainRole userTrainRole = new UserTrainRole();
        // 创建用户
        userTrainRole.setCreateUser(user.getCreateUser());
        // 创建日期
        userTrainRole.setCreateTime(sdr);
        // 操作用户
        userTrainRole.setOperUser(user.getCreateUser());
        // 操作日期
        userTrainRole.setOperTime(sdr);
        // return
        return userTrainRole;
    }

    public PersonDossier buildPersonDossier(User user){
        String sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // userRole
        PersonDossier pde = new PersonDossier();
        pde.setId(UUIDGenerator.genID()+UUIDGenerator.genID());
        // 创建用户
        pde.setCreateUser(user.getCreateUser());
        // 创建日期
        pde.setCreateTime(sdr);
        // 操作用户
        pde.setOperUser(user.getCreateUser());
        // 操作日期
        pde.setOperTime(sdr);
        // return
        return pde;
    }

    public UserRole buildUserRole(User user) {
        // 当前时间
        String sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // userRole
        UserRole userRole = new UserRole();
        userRole.setId(UUIDGenerator.genID()+UUIDGenerator.genID());
        // 创建用户
        userRole.setCreateUser(user.getCreateUser());
        // 创建日期
        userRole.setCreateDate(sdr);
        // 操作用户
        userRole.setOperUser(user.getCreateUser());
        // 操作日期
        userRole.setOperDate(sdr);
        // return
        return userRole;
    }

}

