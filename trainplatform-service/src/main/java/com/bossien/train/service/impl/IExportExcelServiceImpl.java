package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.DepartmentMapper;
import com.bossien.train.dao.tp.UserMapper;
import com.bossien.train.dao.tp.UserTrainRoleMapper;
import com.bossien.train.domain.User;
import com.bossien.train.domain.UserTrainRole;
import com.bossien.train.service.IExportExcelService;
import com.bossien.train.util.ExcelUtil;
import com.bossien.train.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by Administrator on 2017/8/16.
 */
@Service
public class IExportExcelServiceImpl  implements IExportExcelService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserTrainRoleMapper userTrainRoleMapper;
    @Autowired
    private DepartmentMapper departmentMapper;

    //封装导出excel的业务数据
    @Override
    public void  queryExportData(HttpServletResponse response,User user, String params, String depId,List<String> companyIds,boolean flag) {
            // params
            Map<String, Object> ids = new HashMap<String, Object>();
            List<String> isdList=new ArrayList<String>();
            List<String> deptIds=new ArrayList<String>();
            if(!StringUtil.isEmpty(params)){
                String[] idsStr = params.split(",");
                if(idsStr.length>0){
                    for(int k=0;k<idsStr.length;k++){
                        if(idsStr[k]!=null){
                            isdList.add(idsStr[k]);
                        }
                    }
                }
                ids.put("ids", isdList);
            }else if(!StringUtil.isEmpty(depId)){
                ids.put("isValid", User.IsValid.TYPE_1.getValue());
                if("-1".equals(depId)){
                    ids.put("isValid", User.IsValid.TYPE_2.getValue());
                }
                if(flag){ //是否递归查询子部门人员
                    String deptids = departmentMapper.getChildById(depId);
                    if(!org.springframework.util.StringUtils.isEmpty(deptids)){
                        deptIds = Arrays.asList(deptids.replace("$,", "").split(","));
                        ids.put("deptIds",deptIds);

                    }
                }else{
                    ids.put("deptId",depId);
                }
            }else{
                ids.put("isValid", User.IsValid.TYPE_1.getValue());
                if(!flag){
                    ids.put("type",User.IsValid.TYPE_1.getValue());
                }
            }
            ids.put("companyIds", companyIds);

            // 导出的excel数据集
            UserTrainRole utr=new UserTrainRole();
            List<Map<String, Object>> userList=new ArrayList<Map<String, Object>>();
            //查询用户结果集
            List<Map<String, Object>> lMap = userMapper.selectExcelUser(ids);
            for(Map<String, Object> umap:lMap){
               if(umap.get("userId")!=null && !"".equals(umap.get("userId"))){
                   String uid=umap.get("userId").toString();
                   utr.setUserId(uid);
                   List<UserTrainRole> utrList = userTrainRoleMapper.selectRoleByUserId(utr);
                   String rolenames = queryUserTroleNames(utrList);
                   umap.put("roleNames",rolenames.replace("默认角色","").replace(",","  "));
               }
               if(null==umap.get("sex") || "".equals(umap.get("sex"))){
                   umap.put("sex","");
               }else {
                   if(User.Sex.TYPE_1.getValue().equals(umap.get("sex").toString())){
                       umap.put("sex",User.Sex.TYPE_1.getName());
                   }else if(User.Sex.TYPE_2.getValue().equals(umap.get("sex").toString())){
                       umap.put("sex",User.Sex.TYPE_2.getName());
                   }else{
                       umap.put("sex","");
                   }

               }
                userList.add(umap);
            }
            // 文件头部
            Map<String, Object> headerMap = this.getHeader();
            // 生成文件
           ExcelUtil.exportExcelFile(response,userList,headerMap);

    }

    public String queryUserTroleNames(List<UserTrainRole> utrList){
        String str="";
        if(utrList!=null && utrList.size()>0){
            for(UserTrainRole utr:utrList){
                str+="".equals(utr.getRoleName()) ? utr.getRoleName() :","+utr.getRoleName();
            }
        }
        if(!"".equals(str)){
            str=str.substring(str.indexOf(",")+1,str.length());
        }
        return str;
    }

    // 获取导出excel头部
    private Map<String, Object> getHeader() {
        Map<String, Object> header = new LinkedHashMap<String, Object>();
        // 姓名
        header.put("userName", "姓名");
        // 账号
        header.put("userAccount", "账号");
        // 密码
       // header.put("userPasswd", "密码");
        // 受训角色
        header.put("roleNames", "受训角色");
        // 手机号
        header.put("mobileNo", "手机号");
        // 性别
        header.put("sex", "性别");
        // 身份证号
        header.put("idNumber", "身份证号");
        return header;
    }
}
