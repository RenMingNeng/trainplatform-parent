package com.bossien.train.controller;

import com.alibaba.fastjson.JSONArray;
import com.bossien.train.domain.Company;
import com.bossien.train.domain.Department;
import com.bossien.train.domain.User;
import com.bossien.train.domain.eum.TreeTypeEnum;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.ICompanyService;
import com.bossien.train.service.IDepartmentService;
import com.bossien.train.util.PropertiesUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 人员管理(新版需求)
 */
@Controller
@RequestMapping("admin/user")
public class NewUserManagerController extends BasicController {

    private static final Logger logger = LogManager.getLogger(NewUserManagerController.class);

    @Autowired
    private ICompanyService companyService;
    @Autowired
    private IDepartmentService deptService;


    /**
     * 人员管理列表页
     * @param request
     * @return
     */
    @RequestMapping(value = "/new_userList")
    public ModelAndView userManageListPage(HttpServletRequest request){
        logger.info("go to the userManagerListPage method");
        User usr=getCurrUser(request);
        ModelAndView mv = new ModelAndView();
        mv.addObject("createDeptUrl", PropertiesUtils.getValue("op_create_dept_url"));
        mv.addObject("createUserUrl", PropertiesUtils.getValue("op_create_user_url"));
        mv.addObject("updateDeptUrl", PropertiesUtils.getValue("op_update_dept_url"));
        mv.addObject("companyId",usr.getCompanyId());
        mv.setViewName("admin/newUserManage/new_userList");
        logger.info("com.bossien.train.controller.NewUserManagerController stop");
        return mv;
    }

    /**
     * 人员管理左侧树
     * @param request
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/groupTree")
    @ResponseBody
    public Object zTree(HttpServletRequest request,String id,String type) throws ServiceException {
        Response<Object> resp = new Response<Object>();
        String path=request.getServletContext().getInitParameter("resource_path");
        Map<String,Object> params=new HashMap<String,Object>();
        List<String> clist=new ArrayList<String>();
        JSONArray array=new JSONArray();
        User user=getCurrUser(request);
        if(id==null || type==null){
           type= TreeTypeEnum.TREE_TYPE_1.getValue();
        }

        if(type.equals(TreeTypeEnum.TREE_TYPE_1.getValue())){
            //递归查询单位下所有子节点
            String cids = companyService.getChildCompanyIds(user.getCompanyId());
            //将所有cids放入list
            clist=companyService.getAllCompanyId(cids);
            //查询所有单位信息
            List<Company> companylist = companyService.getChildCompanyList(clist);
            //组装生成树的json数据
            array = companyService.queryCompanyTreeData(companylist,user.getCompanyId(),path);
        }else if(type.equals(TreeTypeEnum.TREE_TYPE_2.getValue())){
            params.put("companyId", id);
            params.put("isValid", Department.IsValid.TYPE_1.getValue());
            List<Department> deptList = deptService.selectDepartmentByCompanyId(params);
            array=deptService.queryDeptTreeData(deptList,user.getCompanyId(),path);
        }else{
            String cids = companyService.getChildCompanyIds(user.getCompanyId());
            clist=companyService.getAllCompanyId(cids);
            List<Company> companylist = companyService.getChildCompanyList(clist);
            array = companyService.queryCompanyTreeData(companylist,user.getCompanyId(),path);
        }

        String str = array.toJSONString();
        resp.setResult(str);
        return resp;
    }

    @RequestMapping(value = "/to_redirect")
    public void redirect(HttpServletRequest request, String companyId, HttpServletResponse response) throws IOException {
        //User usr=getCurrUser(request);
        //ModelAndView mv = new ModelAndView();
        String url=PropertiesUtils.getValue("op_create_dept_url");
       response.sendRedirect(url+"&companyId="+companyId);
    }
}
