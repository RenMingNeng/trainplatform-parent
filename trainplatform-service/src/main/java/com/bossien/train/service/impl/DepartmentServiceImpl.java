package com.bossien.train.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bossien.train.dao.tp.DepartmentMapper;
import com.bossien.train.domain.Company;
import com.bossien.train.domain.Department;
import com.bossien.train.domain.User;
import com.bossien.train.domain.eum.TreeTypeEnum;
import com.bossien.train.service.IDepartmentService;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.PropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 公司部门表
 * Created by Administrator on 2017/8/1.
 */
@Service("departmentService")
public class DepartmentServiceImpl implements IDepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public Department queryDeptByParams(Map<String,Object> params) {
        return departmentMapper.selectByParams(params);
    }
    /**
     * 根据companyId查询部门
     * @param map
     * @return
     */
    @Override
    public List<Department> selectDepartmentByCompanyId(Map<String,Object> map){
        return departmentMapper.selectDepartmentByCompanyId(map);
    }

    @Override
    public JSONArray queryTreeData(Company company, List<Department> list) {

        JSONArray jsonarray = new JSONArray();
        JSONObject object=new JSONObject();
        object.put("id", company.getIntId());
        object.put("pId", "0");//根节点
        object.put("name", company.getVarName());
        object.put("open", true);
        object.put("companyId", company.getIntId());
        object.put("departmentId","");
        object.put("nocheck",true);
        //object.put("doCheck",false);
        jsonarray.add(object);
        JSONObject obj=null;
        for(Department department : list){
            obj=new JSONObject();
            obj.put("id", department.getId());
            obj.put("pId", department.getParentId());
            obj.put("name", department.getDeptName());
            obj.put("open", true);
            obj.put("companyId", company.getIntId());
            obj.put("departmentId", department.getId());
            jsonarray.add(obj);
        }
        return jsonarray;
    }

    //使用递归查询当前部门下所包含的所有 部门
    @Override
    public List<String> getTreeDepartment(Map<String, Object> params,List<String> departmentLists) {
            /*List<String> departmentLists=new ArrayList<String>();*/
            if(null==params.get("departmentId")){
                return null;
            }
            if(!"".equals(params.get("departmentId"))||null!=params.get("departmentId")){
                departmentLists.add(params.get("departmentId").toString());
                //调用查询当前部门id下是否还有子节点的方法
                params.put("parentId",params.get("departmentId").toString());
                List<Department> departments= selectDepartmentByCompanyId(params);
                if(departments.size()>0){
                    for (Department department : departments) {
                        departmentLists.add(department.getId());
                        //如果varParentId不为null那就再调用自身方法
                        if(!"".equals(department.getParentId())){
                            Map<String, Object> param = new HashMap<String, Object>();
                            param.put("departmentId",department.getId() );
                            param.put("companyId", params.get("companyId"));
                            getTreeDepartment(param,departmentLists);
                        }
                    }
                }

            }
            return departmentLists;
        }

    @Override
    public String selectByUserId(String userId) {
        return departmentMapper.selectByUserId(userId);
    }

    /**
     * 递归查询该部门的上级部门名称
     * @param map
     * @return
     */
    @Override
    public String  getParentDepartmentName(Map<String,Object> map,String deptName){
      Department department=departmentMapper.selectOne(map) ;
      if(null==department ){
          return deptName;
      }
      if(!"".equals(deptName)){
         deptName=department.getDeptName()+"/"+deptName;
      }else{
          deptName= department.getDeptName();
      }
      Map<String,Object> params=new HashMap<String,Object>();
     /* params.put("companyId",department.getCompanyId());*/
      params.put("id",department.getParentId());
      params.put("isValid",department.getIsValid());

      deptName=getParentDepartmentName(params,deptName);
      return deptName;
    }

    /**
     * 将受训单位数据组组装到json数组，便于前端select2插件展示
     * @param departments
     * @return
     */
    @Override
    public Object assembleDepartmentData(List<Department> departments){
        JSONArray jsonArray = new JSONArray();
        for (Department department:departments) {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",department.getId());
            jsonObject.put("text",department.getDeptName());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public int insert(Department department) {
        return departmentMapper.insert(department);
    }

    @Override
    public Department buildDept(Department dept,User user) {
        dept.setIsValid(Department.IsValid.TYPE_1.getValue());
        dept.setCreateTime(DateUtils.formatDateTime(new Date()));
        dept.setCreateUser(user.getUserName());
        dept.setOperTime(DateUtils.formatDateTime(new Date()));
        dept.setOperUser(user.getUserName());
        dept.setDirector(user.getUserName());
        return dept;
    }

    @Override
    public int queryMaxOrder(String companyId) {
        return departmentMapper.getMaxOrder(companyId);
    }

    @Override
    public int update(Department dept) {
        return departmentMapper.update(dept);
    }

    @Override
    public Department getPreNode(Map<String, Object> params) {
        return departmentMapper.getPreNode(params);
    }

    @Override
    public Department getNextNode(Map<String, Object> params) {
        return departmentMapper.getNextNode(params);
    }

    @Override
    public int changeOrderNum(Map<String, Object> params) {
        return departmentMapper.changeOrderNum(params);
    }

    @Override
    public Department selectOne(Map<String, Object> map) {
        return departmentMapper.selectOne(map);
    }

    @Override
    public JSONArray queryDeptTreeData(List<Department> list,String companyId,String path) {
        JSONArray jsonarray = new JSONArray();
        JSONObject obj=null;
        if(list.size()>0){
            for(Department department : list){
                obj=new JSONObject();
                obj.put("id", department.getId());
                obj.put("pId", department.getParentId());
                obj.put("name", department.getDeptName());
                obj.put("text", department.getDeptName());
                obj.put("open", false);
                obj.put("isParent", false);
                obj.put("companyId", department.getCompanyId());
                obj.put("type", TreeTypeEnum.TREE_TYPE_3.getValue());
                obj.put("departmentId", department.getId());
                obj.put("icon", path+ PropertiesUtils.getValue("ztree_img_url")+"department.png");
                jsonarray.add(obj);
            }
        }
        return jsonarray;
    }
}


