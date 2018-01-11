package com.bossien.train.controller;

import com.bossien.train.domain.Department;
import com.bossien.train.domain.Page;
import com.bossien.train.domain.User;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IDepartmentService;
import com.bossien.train.service.IUserManagerService;
import com.bossien.train.util.ParamsUtil;
import com.bossien.train.util.PropertiesUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 单位内部管理
 * Created by Administrator on 2017/7/26.
 */
@Controller
@RequestMapping("admin/company")
public class CompanyController extends BasicController {

    private static final Logger logger = LogManager.getLogger(CompanyController.class);

    @Autowired
    private IUserManagerService userManagerService;
    @Autowired
    private IDepartmentService deptService;

    @RequestMapping("/userList")
    @ResponseBody
    public Object list(HttpServletRequest request,
               @RequestParam(value="userName", required = false, defaultValue = "") String userName,
               @RequestParam(value="departmentId", required = false, defaultValue = "") String departmentId,
               //@RequestParam(value="searchType", required = false, defaultValue = "") String searchType,
               @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
               @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response<Object> resp = new Response<Object>();
        User users=getCurrUser(request);
        Page<User> page = new Page<User>();
        Map<String, Object> params = new HashMap<String, Object>();
        List<String> deptList=new ArrayList<String>();
        params.put("companyId", users.getCompanyId());
        params.put("userName", ParamsUtil.joinLike(userName));
        if(pageSize > 1000) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }
        /*if(!"".equals(userName) && userName!=null) {
            if (!"".equals(searchType) && searchType != null) {
                if ("1".equals(searchType)) {//按姓名搜索
                    params.put("userName", ParamsUtil.joinLike(userName));
                } else {//按账号搜索
                    params.put("userAccount", ParamsUtil.joinLike(userName));
                }
            }
        }*/
        if(departmentId!=null && !"".equals(departmentId)){
            params.put("departmentId", departmentId);
            deptList=deptService.getTreeDepartment(params,deptList);
        }
        List<String> dtList=new ArrayList<String>();
        Set set=new HashSet();
        for(String deptId:deptList){
            if(set.add(deptId)){
                dtList.add(deptId);
            }
        }
        params.put("departmentIdList", dtList);
        //params.put("userType", '3');
        params.put("isValid", PropertiesUtils.getValue("CHRISVAl_ID"));
        params.put("roleName", PropertiesUtils.getValue("SYS_ROLE_COMPANY_USER"));
        Integer count = userManagerService.queryAllUserCount(params);

        page = new Page<User>(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("endNum", page.getPageSize());
        List<User> userList = userManagerService.queryAllUserList(params);
        page.setDataList(userList);
        resp.setResult(page);
        return resp;
    }

}
