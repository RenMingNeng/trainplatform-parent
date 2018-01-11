package com.bossien.train.controller;

import com.alibaba.fastjson.JSONArray;
import com.bossien.framework.event.Event001;
import com.bossien.framework.event.Event002;
import com.bossien.train.domain.Company;
import com.bossien.train.domain.Department;
import com.bossien.train.domain.User;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.ICompanyService;
import com.bossien.train.service.IDepartmentService;
import com.bossien.train.service.ISequenceService;
import com.bossien.train.service.IUserManagerService;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.ParamsUtil;
import com.bossien.train.util.PropertiesUtils;
import com.bossien.train.util.UUIDGenerator;
import com.google.common.collect.Maps;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 单位管理
 * Created by Administrator on 2017/7/26.
 */
@Controller
@RequestMapping("admin/department")
public class DepartmentController extends BasicController {
    private static final Logger logger = LogManager.getLogger(DepartmentController.class);

    @Autowired
    private IDepartmentService deptService;
    @Autowired
    private ICompanyService companyService;
    @Autowired
    private ISequenceService sequenceService;
    @Autowired
    private IUserManagerService userManagerService;

    @RequestMapping("/deptTree")
    @ResponseBody
    public Object list(HttpServletRequest request) throws ServiceException {
        Response<Object> resp = new Response<Object>();

        Map<String,Object> params=new HashMap<String,Object>();
        User user=getCurrUser(request);
        params.put("companyId", user.getCompanyId());
        params.put("isValid", Department.IsValid.TYPE_1.getValue());
        List<Department> deptList = deptService.selectDepartmentByCompanyId(params);
        //查询管理员所在公司信息
        Company company = companyService.selectOne(params);
        /*Company company=new Company();
        company.setIntId(Integer.valueOf(user.getCompanyId()));
        company.setVarName("西安博晟安全公司");*/
        JSONArray array = deptService.queryTreeData(company, deptList);
        String str = array.toJSONString();
        resp.setResult(str);
        return resp;
    }

    // 部门树节点---部门名称判重
    @ResponseBody
    @RequestMapping(value = "/checkDepartmentName")
    public Object checkDepartmentName(
            @RequestParam(value="departmentName", required=true, defaultValue="") String departmentName,
            @RequestParam(value="companyId", required=true, defaultValue="") String companyId,
            @RequestParam(value="parentId", required=true, defaultValue="") String parentId,
            @RequestParam(value="operType", required=true, defaultValue="") String operType
            ){
        Response<Object> resp = new Response<Object>();
        Map<String,Object> params=new HashMap<String,Object>();
        Map<String,Object> para=new HashMap<String,Object>();
        // 根据部门名称查找部门
        params.put("isValid", Department.IsValid.TYPE_1.getValue());
        params.put("companyId", companyId);
        params.put("deptName", departmentName);
        if("edit".equals(operType)){//如果是修改则找到修改节点的上级节点的父节点
            para.put("companyId", companyId);
            para.put("id", parentId);
            para.put("isValid", Department.IsValid.TYPE_1.getValue());
            Department dept= deptService.queryDeptByParams(para);
            if(dept!=null){
                params.put("parentId", dept.getParentId());
            }
        }else{
            params.put("parentId", parentId);
        }
        Department departments = deptService.queryDeptByParams(params);
        if(departments!=null){   //表示该部门名称已存在
            resp.setResult(Boolean.TRUE);
        }else{
            resp.setResult(Boolean.FALSE);
        }
        return resp;
    }

   //新增部门
    @ResponseBody
    @RequestMapping(value = "/addDepartment")
    public Object addDepartment(HttpServletRequest request,
            @RequestParam(value="companyId", required=true, defaultValue="") String companyId,
            @RequestParam(value="departmentId", required=true, defaultValue="") String departmentId,
            @RequestParam(value="departmentName", required=true, defaultValue="") String departmentName){	// 部门新的名称
        Response<Object> resp = new Response<Object>();
        User user=getCurrUser(request);
        Department dept=new Department();
        dept=deptService.buildDept(dept,user);
        dept.setId(UUIDGenerator.genID()+UUIDGenerator.genID());
        dept.setCompanyId(companyId);
        dept.setOrderNum(deptService.queryMaxOrder(companyId)+1);
        dept.setDeptName(departmentName);
        if("".equals(departmentId)){//如果是根节点下添加部门
            dept.setParentId(companyId);
        }else{//如果是部门节点下添加部门
            dept.setParentId(departmentId);
        }
        int result= deptService.insert(dept);
        if(result>0){   //表示该部门新增成功
            resp.setResult(Boolean.TRUE);
        }else{
            resp.setResult(Boolean.FALSE);
            resp.setMessage("新增失败");
        }
        return resp;
    }


    /**
     * 修改部门名称
     * @param request
     * @param companyId
     * @param departmentId
     * @param departmentName 部门新的名称
     * @param departmentNames 修改的当前节点的父节点名称
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateDepartment")
    public Object updateDepartment(HttpServletRequest request,
                                @RequestParam(value="companyId", required=true, defaultValue="") String companyId,
                                @RequestParam(value="departmentId", required=true, defaultValue="") String departmentId,
                                @RequestParam(value="departmentName", required=true, defaultValue="") String departmentName,
                                   @RequestParam(value="departmentNames", required=true, defaultValue="") String departmentNames){
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        Department dept=new Department();
        dept.setDeptName(departmentName);
        dept.setId(departmentId);
        dept.setCompanyId(companyId);
        dept.setOperTime(DateUtils.formatDateTime(new Date()));
        int result= deptService.update(dept);

        if(result>0){   //表示该部门新增成功
            resp.setResult(Boolean.TRUE);
        }else{
            resp.setResult(Boolean.FALSE);
            resp.setMessage("修改失败");
        }

        //修改人员表和人员档案表的部门名称
        WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
        Map<String, Object> params = Maps.newConcurrentMap();
        params.put("departmentId", departmentId);
        params.put("companyId", companyId);
        params.put("departmentName", departmentName);
        params.put("departmentNames", departmentNames);
        Event002 event002 = new Event002(params);
        wac.publishEvent(event002);

        return resp;
    }

    //删除部门
    @ResponseBody
    @RequestMapping(value = "/delDepartment")
    public Object delDepartment(HttpServletRequest request,
       @RequestParam(value="companyId", required=true, defaultValue="") String companyId,
       @RequestParam(value="departmentId", required=true, defaultValue="") String departmentId){
        Response<Object> resp = new Response<Object>();
        Map<String, Object> para=new HashMap<String, Object>();
        Department dept=new Department();
        List<String> delist=new ArrayList<String>();
        int result=0;
        int count=0;
        Map<String,Object> params=new HashMap<String,Object>();
        List<String> deptlist=new ArrayList<String>();
        if(departmentId!=null && !"".equals(departmentId)){
            params.put("departmentId",departmentId);
            params.put("companyId",companyId);
            params.put("isValid",Department.IsValid.TYPE_1.getValue());
            delist = deptService.getTreeDepartment(params, deptlist);
            if(!delist.isEmpty() && delist.size()>0){
                for(String deptId:delist){
                    para.put("deptId",deptId);
                    List<Map<String, Object>> umap = userManagerService.selectUserByDeptId(para);
                    if(umap.size()>0){
                        count++;
                    }

                }

            }
        }
        if(count==0){
            for(String deptId:delist) {
                dept.setId(deptId);
                dept.setCompanyId(companyId);
                //删除做逻辑删除 状态置为无效
                dept.setIsValid(Department.IsValid.TYPE_2.getValue());
                result = deptService.update(dept);
            }
        }
        if(result>0){   //表示该部门删除成功
            resp.setResult(Boolean.TRUE);
        }else{
            resp.setResult(Boolean.FALSE);
            resp.setMessage("删除失败");
        }
        return resp;
    }

    // 部门树-上移
    @ResponseBody
    @RequestMapping(value = "/moveUp")
    public Object move_prev(
            @RequestParam(value="companyId", required=true, defaultValue="") String companyId, // 单位id
            @RequestParam(value="departmentId", required=true, defaultValue="") String departmentId){  // 部门id
        Response<Object> resp = new Response<Object>();

        // 查找部门
        Map<String,Object> para=new HashMap<String,Object>();
        para.put("companyId",companyId);
        para.put("id",departmentId);
        Department department = deptService.queryDeptByParams(para);
        // params
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("companyId", companyId);
        params.put("parentId", department.getParentId());
        params.put("orderNum", department.getOrderNum());
        // 查找同级上位的节点
        Department departmentPrev = deptService.getPreNode(params);
        if(null==departmentPrev){
            resp.setMessage("无法上移");
            resp.setResult(Boolean.FALSE);
            return resp;
        }
        // 两个部门交换部门排序号
        params.clear();
        params.put("departmentId1", department.getId());
        params.put("departmentId2", departmentPrev.getId());
        int rest= deptService.changeOrderNum(params);
        if(rest>0){
            resp.setResult(Boolean.TRUE);
        }else{
            resp.setResult(Boolean.FALSE);
        }
        return resp;
    }

    // 部门树-下移
    @ResponseBody
    @RequestMapping(value = "/moveDown")
    public Object moveDown(
            @RequestParam(value="companyId", required=true, defaultValue="") String companyId, // 单位id
            @RequestParam(value="departmentId", required=true, defaultValue="") String departmentId){  // 部门id
        Response<Object> resp = new Response<Object>();

        // 查找部门
        Map<String,Object> para=new HashMap<String,Object>();
        para.put("companyId",companyId);
        para.put("id",departmentId);
        Department department = deptService.queryDeptByParams(para);
        // params
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("companyId", companyId);
        params.put("parentId", department.getParentId());
        params.put("orderNum", department.getOrderNum());
        // 查找同级下位的节点
        Department departmentNext = deptService.getNextNode(params);
        if(null==departmentNext){
            resp.setMessage("无法下移");
            resp.setResult(Boolean.FALSE);
            return resp;
        }
        // 两个部门交换部门排序号
        params.clear();
        params.put("departmentId1", department.getId());
        params.put("departmentId2", departmentNext.getId());
        int rest=deptService.changeOrderNum(params);
        if(rest>0){
            resp.setResult(Boolean.TRUE);
        }else{
            resp.setResult(Boolean.FALSE);
        }
        return resp;
    }
}
