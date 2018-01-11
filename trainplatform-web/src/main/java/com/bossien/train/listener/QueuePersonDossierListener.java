package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.dao.tp.UserTrainRoleMapper;
import com.bossien.train.domain.*;
import com.bossien.train.service.*;
import com.bossien.train.util.CollectionUtils;
import com.bossien.train.util.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 按需复写父类方法，真实业务请直接继承AbstractListener
 * que 监听器
 *
 * 人员档案 监听器
 *
 * @author DF
 *
 */
@Component("queuePersonDossierListener")
public class QueuePersonDossierListener extends AbstractListener {
    public static final Logger logger = LoggerFactory.getLogger(QueuePersonDossierListener.class);

    @Autowired
    private MongoOperations mongoTemplate;
    @Autowired
    private IUserManagerService userManagerService;
    @Autowired
    private UserTrainRoleMapper userTrainRoleMapper;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private ICompanyService companyService;
    @Autowired
    private IPersonDossierService personDossierService;
    @Autowired
    private IBaseService<PersonDossier> baseService;
    @Autowired
    private ICompanyTjService companyTjService;

    @Override
    public <V> void mapMessageHander(Map<String, V> map) throws ParseException {
        super.mapMessageHander(map);

        List<String> userIdList = new ArrayList<>();
        Object userIds = map.get("userIds");
        String userName = (String) map.get("userName");
        if(null == userIds || userIds.toString().equals("")){
            return;
        }

        if(userIds instanceof List){
            userIdList = (List<String>)userIds;
        }
        if(userIdList.size() < 1){
            return;
        }

        //查询用户集合
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("userIds", userIdList);
        List<User> users = userManagerService.queryUser(params);
        if(null == users || users.size() < 1){
            return;
        }

        //查询人员档案
        List<Map<String, Object>> personDossierList = personDossierService.selectList(params);
        Map<String, Map<String, Object>> personDossierMap = CollectionUtils.listToMapObject(personDossierList, "user_id");

        //查询用户角色
        params.put("isValid", "1");
        List<UserTrainRole> userTrainRoles = userTrainRoleMapper.selectList(params);
        Map<String, UserTrainRole> userTrainRoleMap = CollectionUtils.listToMapObject(userTrainRoles, "userId");

        //单位
        List<Department> departments = null;
        Map<String, Department> departmentMap = null;
        Company company = null;

        List<PersonDossier> insert = new ArrayList<PersonDossier>();
        List<Map<String, Object>> update = new ArrayList<Map<String, Object>>();
        //修改之前的公司--数据集合
        Map<String, Integer> companyOrCount = MapUtils.newHashMap();
        for(User user : users){
            String cId = user.getCompanyId();
            params.clear();
            params.put("companyId", cId);
            //查询部门集合
            if(null == departments && !user.getDepartmentId().equals(cId)){
                departments = departmentService.selectDepartmentByCompanyId(params);
                departmentMap = CollectionUtils.listToMapObject(departments, "id");
            }

            //查询公司
            if(null == company){
                company = companyService.selectOne(params);
            }

            getPersonDossier(personDossierMap, userTrainRoleMap, user, departmentMap, company, insert, update, companyOrCount);
        }

        //新增
        if(insert.size() > 0){
            personDossierService.insertBatch(insert);
        }

        //修改
        if(update.size() > 0){
            for(Map<String, Object> person: update){
                personDossierService.update(person);
            }
        }

        //更新本公司统计表
        companyTjService.updateCompanyTj(insert.size() + update.size(), company.getIntId().toString(), userName);
        //修改其余公司的统计
        Iterator<String> it = companyOrCount.keySet().iterator();
        while (it.hasNext()){
            String cid = it.next();
            companyTjService.updateCompanyTj(companyOrCount.get(cid), cid, userName);
        }
    }

    public void getPersonDossier(Map<String, Map<String, Object>> personDossierMap, Map<String, UserTrainRole> userTrainRoleMap,
             User user, Map<String, Department> departmentMap, Company company,
                                 List<PersonDossier> insert, List<Map<String, Object>> update, Map<String, Integer> companyOrCount){
        String userId = user.getId();
        UserTrainRole userTrainRole = userTrainRoleMap.get(userId);
        //判断重复--修改
        Map<String, Object> personMap;
        if(null != personDossierMap.get(userId)){
            personMap = personDossierMap.get(userId);
            Object companyId = personMap.get("company_id");
            if(null == companyId || companyId.toString().equals("")){
                return;
            }
            //记录需要修改的公司集合
            int count = -1;
            if(null != companyOrCount.get(companyId.toString())){
                count += companyOrCount.get(companyId.toString());
            }
            companyOrCount.put(companyId.toString(), count);

            //更新档案
            personMap.put("userId", userId);
            personMap.put("userName", user.getUserName());
            personMap.put("roleId", userTrainRole.getTrainRoleId());
            personMap.put("roleName", userTrainRole.getRoleName());
            personMap.put("deptId", user.getDepartmentId());
            personMap.put("deptName", getParentDepartmentName(departmentMap, user.getDepartmentId(), user.getDepartmentName()));
            personMap.put("companyId", user.getCompanyId());
            personMap.put("companyName", company.getVarName());
            personMap.put("operUser", user.getOperUser());
            personMap.put("operTime", user.getOperDate());
            update.add(personMap);
        }else{
            PersonDossier personDossier = new PersonDossier(
                    userId,
                    user.getUserName(),
                    userTrainRole.getTrainRoleId(),
                    userTrainRole.getRoleName(),
                    user.getDepartmentId(),
                    getParentDepartmentName(departmentMap, user.getDepartmentId(), user.getDepartmentName()),
                    user.getCompanyId(),
                    company.getVarName(),
                    0,
                    0,
                    0
            );
            insert.add(baseService.build(user.getUserName(), personDossier));
        }
    }


    /**
     * 获取部门路径
     * @param map
     * @param deptId
     * @return
     */
    public String getParentDepartmentName(Map<String, Department> map,String deptId, String deptName){
        if(StringUtils.isEmpty(deptId)){
            return "";
        }

        if(null == map || null == map.get(deptId)){
            return null == deptName ? "" : deptName;
        }

        Department department = map.get(deptId);
        String result = "";
        String pName = getParentDepartmentName(map, department.getParentId(),null);
        if(null != pName && !pName.equals("")){
            result = pName + "/" + department.getDeptName();
        }else {
            result = department.getDeptName();
        }
        return result;
    }
}
