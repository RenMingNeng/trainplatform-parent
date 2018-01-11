package com.bossien.train.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bossien.train.dao.ap.CompanyMapper;
import com.bossien.train.dao.tp.DepartmentMapper;
import com.bossien.train.domain.Company;
import com.bossien.train.domain.Department;
import com.bossien.train.domain.eum.TreeTypeEnum;
import com.bossien.train.service.ICompanyService;
import com.bossien.train.service.IDepartmentService;
import com.bossien.train.util.PropertiesUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-07-31.
 */
@Service
public class CompanyServiceImpl implements ICompanyService {
    private String imgUrl = PropertiesUtils.getValue("ztree_img_url");

    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private IDepartmentService departmentService;

    @Override
    public List<Map<String, Object>> selectList(Map<String, Object> params) {

        return companyMapper.selectList(params);
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {
        return companyMapper.selectCount(params);
    }

    @Cacheable(value = "companyCache#(60 * 60)", key = "'selectOne'.concat('_').concat(#p0.hashCode())")
    @Override
    public Company selectOne(Map<String, Object> params) {
        return companyMapper.selectOne(params);
    }

    @Override
    public Company selectByOne(Map<String, Object> params) {

        return companyMapper.selectByOne(params);
    }

    @Override
    public Integer select_count(Map<String, Object> params) {
        return companyMapper.select_count(params);
    }

    @Override
    public List<Map<String, Object>> select_list(Map<String, Object> params) {
        return companyMapper.select_list(params);
    }

    @Override
    public int insert(Map<String, Object> params) {
        return companyMapper.insert(params);
    }

    @Override
    public int insertSelective(Map<String, Object> params) {
        return companyMapper.insertSelective(params);
    }

    @Override
    public int updateByPrimaryKeySelective(Map<String, Object> params) {
        return companyMapper.updateByPrimaryKeySelective(params);
    }

    @Override
    public int insertBatch(List<Map<String, Object>> list) {
        return companyMapper.insertBatch(list);
    }

    @Override
    public int updateBatch(List<Map<String, Object>> list) {
        return companyMapper.updateBatch(list);
    }

    @Override
    public Company selectById(Map<String, Object> params) {
        return companyMapper.selectById(params);
    }

    @Override
    public void updateCompanyValid(Map<String, Object> params) {

        companyMapper.updateCompanyValid(params);
    }

    @Override
    public String getChildCompanyIds(String companyId) {
        String ids = companyMapper.getChildCompanyIds(companyId);
        if (null == ids || ids.equals("")) {
            return "";
        }

        ids = ids.replace("$,", "");
        return ids;
    }

    @Override
    public List<Company> getChildCompanyList(List<String> ids) {
        if (null == ids || ids.size() < 1) {
            return new ArrayList<Company>();
        }
        return companyMapper.getChildCompanyList(ids);
    }

    @Override
    public String getCompanyTree(String companyId) {

        List<Map<String, Object>> companies = companyMapper.select_list(new HashedMap());

        JSONArray jsonArray = new JSONArray(companies.size());
        for (Map<String, Object> company : companies) {
            jsonArray.add(getCompanyTreeNode(company));
        }

        if (companies.size() > 0) {
            return jsonArray.toJSONString();
        }
        return "";
    }

    @Override
    public Map<String, Object> getCompanyInfo(String companyId) {
        return companyMapper.getCompanyInfo(companyId);
    }


    @Override
    public JSONArray queryCompanyTreeData(List<Company> list,String companyId,String path) {
        JSONArray jsonarray = new JSONArray();
        Map<String,Object> params=new HashMap<String,Object>();
        Map<String,Object> para=new HashMap<String,Object>();
        List<Department> deptList=new ArrayList<Department>();
        JSONObject object=new JSONObject();
        JSONObject obj=null;
        JSONObject job=null;
        JSONObject jobo=null;

        //查询单位下的直属部门
        jsonarray = getDeptByCompanyId(jsonarray,companyId,path);
        if(list.size()>0){
            for(Company company:list){
               if(companyId.equals(String.valueOf(company.getIntId()))){
                   object.put("id", company.getIntId());
                   object.put("pId", "0");//根节点
                   object.put("name", company.getVarName());
                   object.put("text", company.getVarName());
                   object.put("open", true);
                   object.put("isParent", false);
                   object.put("companyId", company.getIntId());
                   object.put("departmentId", company.getIntId());
                   object.put("type", TreeTypeEnum.TREE_TYPE_1.getValue());
                   object.put("icon", path+imgUrl+"group.png");
                   jsonarray.add(object);

               }else{
                   obj=new JSONObject();
                   obj.put("id", company.getIntId());
                   obj.put("pId", company.getIntPid());
                   obj.put("name", company.getVarName());
                   obj.put("text", company.getVarName());
                   obj.put("open", false);
                   obj.put("isParent", true);
                   obj.put("companyId", company.getIntId());
                   obj.put("departmentId", "");
                   obj.put("type", TreeTypeEnum.TREE_TYPE_2.getValue());
                   obj.put("icon", path+imgUrl+"company.png");
                   jsonarray.add(obj);

                   //查询子节点下每个单位下是否有直属部门
                   jsonarray=getDeptByCompanyId(jsonarray,String.valueOf(company.getIntId()),path);
               }
            }
        }

        return jsonarray;
    }

    /**
     * 根据单位id查询下属部门
     * @param companyId
     * @param path
     * @return
     */
    JSONArray getDeptByCompanyId(JSONArray jsonarray,String companyId,String path){
        JSONObject job=null;
        Map<String,Object> para=new HashMap<String,Object>();
        List<Department> deptList=new ArrayList<Department>();
        //查询单位下的直属部门
        para.put("companyId", companyId);
        para.put("isValid", Department.IsValid.TYPE_1.getValue());
        deptList = departmentMapper.selectDepartmentByCompanyId(para);
        if(deptList!=null && deptList.size()>0){
            for(Department dept:deptList){
                job=new JSONObject();
                job.put("id", dept.getId());
                job.put("pId", dept.getParentId());
                job.put("name", dept.getDeptName());
                job.put("text", dept.getDeptName());
                job.put("open", false);
                job.put("isParent", false);
                job.put("companyId", dept.getCompanyId());
                job.put("departmentId", dept.getId());
                job.put("type", TreeTypeEnum.TREE_TYPE_3.getValue());
                job.put("icon", path+imgUrl+"department.png");
                jsonarray.add(job);
            }
        }
        return jsonarray;
    }

    @Override
    public List<String> getAllCompanyId(String cids) {
        List<String> clist=new ArrayList<String>();
        if(cids!=null){
            String[] ids=cids.split(",");
            if(ids.length>0){
                for(int n=0;n<ids.length;n++){
                    clist.add(ids[n]);
                }
            }
        }
        return clist;
    }

    public Map<String, Object> getCompanyTreeNode(Map<String, Object> company) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", company.get("intId"));
        map.put("name", company.get("varName"));
        map.put("open", false);
        map.put("pId", company.get("intPid"));
        return map;
    }

    @Override
    public Object getDepts(List<Company> companyList){
        if(companyList.size() == 0){
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for(Company company:companyList){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",company.getIntId());
            jsonObject.put("type","");
            jsonObject.put("text",company.getVarName());
            jsonArray.add(jsonObject);

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("companyId", company.getIntId());
            //根据查询公司的部门
            List<Department> departments = departmentService.selectDepartmentByCompanyId(params);
            if(departments.size() == 0 ){
                continue;
            }

            for (Department department:departments) {
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("id",department.getId());
                    jsonObject1.put("type",company.getIntId());
                    jsonObject1.put("text",department.getDeptName());
                    jsonArray.add(jsonObject1);
           }


        }
        return jsonArray;
    }
}
