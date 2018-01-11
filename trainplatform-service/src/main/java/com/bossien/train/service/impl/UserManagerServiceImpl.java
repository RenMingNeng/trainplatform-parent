package com.bossien.train.service.impl;

import com.bossien.train.dao.ap.CompanyMapper;
import com.bossien.train.dao.ap.TrainRoleMapper;
import com.bossien.train.dao.tp.*;
import com.bossien.train.domain.*;
import com.bossien.train.service.ICompanyTjService;
import com.bossien.train.service.IUserManagerService;
import com.bossien.train.util.UUIDGenerator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/7/31.
 */
@Service(value = "userManagerService")
public  class UserManagerServiceImpl implements IUserManagerService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserWorkMapper userWorkMapper;
    @Autowired
    private DepartmentMapper deptMapper;
    @Autowired
    private UserTrainRoleMapper userTrainRoleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private TrainRoleMapper trainRoleMapper;
    @Autowired
    private PersonDossierMapper personDossierMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private ICompanyTjService companyTjService;
    @Override
    public List<User> queryAllUserList(Map<String, Object> params) {
        List<User> users=userMapper.queryAllUserList(params);

        return users;
    }

    @Override
    public int queryAllUserCount(Map<String, Object> params) {

        return userMapper.selectAllUserCount(params);
    }

    @Override
    public void changeUserDept(String companyId,String deptId, List<String> userIds, User user) {
        if(null==userIds || userIds.isEmpty()) {
            return;
        }
        User user_=null;
        Department depart = null;
        UserWork userWork = new UserWork();
        Map<String,Object> params=new HashMap<String,Object>();
        Map<String,Object> deptMap=new HashMap<String,Object>();


        if(StringUtils.isNotEmpty(deptId)){
            deptMap.put("id",deptId);
            depart = deptMapper.selectByParams(deptMap);
        }

        for(String userId : userIds){
            // 查询人员
            params.put("id",userId);
            user_= userMapper.selectOne(params);
            //变更到公司节点下面
            if(StringUtils.isEmpty(deptId) ){
                moveUser(null,user_,companyId);
            }
            //验证学员是不是在需要转移的部门  不存在就转移
            if(StringUtils.isNotEmpty(deptId) && !deptId.equals(user_.getDepartmentId())){
                moveUser(depart,user_,companyId);
            }

            //更新统计信息
            if(!companyId.equals(user_.getCompanyId())){
                //变更原公司的统计信息
                int count = -1;
                companyTjService.updateCompanyTj(count, user_.getCompanyId(), user.getUserName());
                //变更后更新新公司统计信息
                count = 1;
                companyTjService.updateCompanyTj(count, companyId, user.getUserName());
            }


        }//for
    }

    /**
     * 转移人员
     * @param dept
     * @param user
     */
    public void moveUser(Department dept,User user,String companyId){
        String deptId = "";
        String deptName = "";

        if(dept!=null){
            deptId = dept.getId();
            deptName = dept.getDeptName();
        }else{
            deptId = companyId;
        }
        // 当前时间
        String sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // 人员部门id-更新
        User u=new User();
        u.setId(user.getId());
        u.setOperDate(sdr);
        u.setCompanyId(companyId);
        u.setDepartmentId(deptId);
        u.setDepartmentName(deptName);
        u.setIsValid(User.IsValid.TYPE_1.getValue());// 用户是否有效 1：有效、2：无效
        userMapper.updateById(u);

        UserWork userWork =null;
        //更新用户工作信息
        userWork = bulidUserWork(user);
        userWork.setUserId(user.getId());// 用户序号
        userWork.setCompanyId(companyId);// 所属单位
        userWork.setDeptId(deptId);// 所属部门
        //userWork.setEntryTime(user_.getCreateDate());// 入职时间
        userWork.setOperater(UserWork.Operater.TYPE_2.getValue()); // 操作记录(1.人员登记/2：人员转移/3：人员退厂)
        if(dept!=null){
            userWork.setDeptName(StringUtils.isEmpty(deptId)?"":dept.getDeptName());// 所属各级部门名称
        }
        userWorkMapper.insert(userWork);

        //人员管理下人员转移后修改人员档案的部门名称
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("deptId",deptId);
        para.put("deptName",deptName);
        para.put("userId",user.getId());
        if(!companyId.equals(user.getCompanyId())){
            para.put("companyId",companyId);
            Company company = companyMapper.selectOne(para);
            para.put("companyName",company!=null?company.getVarName():"");
        }
        personDossierMapper.updateByUserIds(para);
    }



    @Override
    public void changeUserQuit(String deptId, List<String> userIds, User user) {
        if(null==userIds || userIds.isEmpty()) {
            return;
        }
        User user_ = null;
        UserWork userWork = new UserWork();
        Map<String,Object> params=new HashMap<String,Object>();
        // 当前时间
        String sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        for(String userId : userIds){

            // 查询人员
            params.put("id",userId);
            user_= userMapper.selectOne(params);


            if(user_!=null){
                // 人员状态-更新
                User u=new User();
                u.setId(userId);
                u.setDepartmentId(deptId);
                u.setIsValid(User.IsValid.TYPE_2.getValue());// 用户是否有效 1：有效、2：无效
                userMapper.update(u);

            }

            // 人员变动记录-新增
            userWork = bulidUserWork(user_);
            userWork.setUserId(userId);// 用户序号
            userWork.setCompanyId(user_.getCompanyId());// 所属单位
            userWork.setDeptId(deptId);// 所属部门
            userWork.setDeptName("");// 所属各级部门名称
            userWork.setEntryTime(user_.getCreateDate());// 入职时间
            userWork.setQuitTime(sdr);
            userWork.setOperater(UserWork.Operater.TYPE_3.getValue()); // 操作记录(1.人员登记/2：人员转移/3：人员退厂)
            userWorkMapper.insert(userWork);

        }

    }

    @Override
    public User queryUserInfo(User user) {
        return userMapper.selectUserInfo(user);
    }

    @Override
    public List<UserWork> queryUserWorkList(UserWork userWork) {
        return userWorkMapper.selectList(userWork);
    }

    @Override
    public int updateUserInfo(User user) {
        return userMapper.update(user);
    }

    @Override
    public void updateUser(User user,String roleIds,UserWork uWork,UserTrainRole usertrole) {
        //更新用户信息
        userMapper.update(user);
        //更新用户工作信息
        UserTrainRole utr=null;
        uWork.setOperater(UserWork.Operater.TYPE_4.getValue());
        uWork.setUserId(user.getId());
        uWork.setCompanyId(user.getCompanyId());
        uWork.setDeptId(user.getDepartmentId());
        uWork.setDeptName(user.getDepartmentName());
        userWorkMapper.insert(uWork);

        if(StringUtils.isNotEmpty(roleIds)){
            //更新用户角色信息
            String[] ids=roleIds.split(",");
            if(ids.length>0) {
                for (int i = 0; i < ids.length; i++) {
                    utr = new UserTrainRole();
                    utr.setUserId(user.getId());
                    utr.setTrainRoleId(ids[i]);
                    UserTrainRole ustr = userTrainRoleMapper.selectOne(utr);
                    if (ustr == null) {
                        usertrole.setIsValid(UserTrainRole.IsValid.TYPE_1.getValue());
                        usertrole.setUserId(user.getId());
                        usertrole.setTrainRoleId(ids[i]);
                        TrainRole tr = new TrainRole();
                        tr.setVarId(ids[i]);
                        tr = selectRoleNameById(tr);
                        if (tr != null) {
                            usertrole.setRoleName(tr.getRoleName());
                        }
                        userTrainRoleMapper.insert(usertrole);
                    }

                }
            }
        }
    }

    @Override
    public List<UserTrainRole> queryUserRoles(UserTrainRole userTrainRole) {
        return userTrainRoleMapper.selectUserRoles(userTrainRole);
    }

    @Override
    public List<Map<String, Object>> queryAllRoleByParams(Map<String, Object> params) {
        return trainRoleMapper.selectByParams(params);
    }

    @Override
    public UserTrainRole selectOne(UserTrainRole userTrainRole) {
        return userTrainRoleMapper.selectOne(userTrainRole);
    }

    @Override
    public int insert(UserTrainRole userTrainRole) {
        return userTrainRoleMapper.insert(userTrainRole);
    }

    @Override
    public int update(UserTrainRole userTrainRole) {
        return userTrainRoleMapper.update(userTrainRole);
    }

    @Override
    public TrainRole selectRoleNameById(TrainRole trainRole) {
        return trainRoleMapper.selectOne(trainRole);
    }

    @Override
    public List<User> queryUser(Map<String, Object> params) {

        return userMapper.queryUser(params);
    }

    @Override
    public List<Map<String, Object>> selectUserByDeptId(Map<String, Object> params) {
        return userMapper.selectUserByDeptId(params);
    }

    @Override
    public List<Map<String, Object>> selectUserAndDeptId(Map<String, Object> params) {
        return userMapper.selectUserAndDeptId(params);
    }

    @Override
    public List<String> selectUserIds(Map<String, Object> params) {
        return userMapper.selectUserIds(params);
    }

    @Override
    public int deleteUser(User user,User operUser) {
        int result=0;
        //删除人员 真删除
       /* user.setIsValid(User.IsValid.TYPE_2.getValue());*/
        result=userMapper.deleteByUser(user.getId());
        //删除人员档案
        personDossierMapper.delete(user.getId());
        //删除用户角色
        UserRole ur=new UserRole();
        ur.setUserId(user.getId());
        result=userRoleMapper.delete(ur);
        //删除用户受训角色
        UserTrainRole utr=new UserTrainRole();
        utr.setUserId(user.getId());
        result=userTrainRoleMapper.delete(utr);
        //删除用户工作信息
        UserWork uk=new UserWork();
        uk.setUserId(user.getId());
        result= userWorkMapper.delete(uk);
        //更新单位统计表
        if(result > 0 ){
            int count = result;
            companyTjService.updateCompanyTj(-count, operUser.getCompanyId() , operUser.getUserName());
        }
        return result;
    }

    @Override
    public int batchDelUser(List<String> userIds,User user1) {
        int result=0;
        Map<String, Object> params=new HashMap<String, Object>();
        User user=null;
       if(userIds.size()>0){
           params.put("userIds",userIds);
           //批量删除人员信息
           userMapper.batchDeleteUser(params);
           //批量删除人员档案信息
           personDossierMapper.batchDelete(params);
       }
        userRoleMapper.batchDelUserRole(params);
        userTrainRoleMapper.batchDelUtrole(params);
        result= userWorkMapper.batchDelUserWork(params);

        //更新单位统计表
        if(result>0 &&  userIds.size() >0){
           int count = userIds.size();
            companyTjService.updateCompanyTj(-count, user1.getCompanyId() , user1.getUserName());
        }
        return result;
    }

    UserWork bulidUserWork(User user) {
        // 当前时间
        String sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // userWork
        UserWork userWork = new UserWork();
        // 序号
        userWork.setId(UUIDGenerator.genID()+UUIDGenerator.genID());
        // 创建用户
        userWork.setCreateUser(user.getUserName());
        // 创建日期
        userWork.setCreateTime(sdr);
        // 操作用户
        userWork.setOperUser(user.getUserName());
        userWork.setEntryTime(sdr);
        // 操作日期
        userWork.setOperTime(sdr);
        // return
        return userWork;
    }

    @Override
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

    @Override
    public List<UserTrainRole> utrListselectRoleByUserId(UserTrainRole utr) {
        return userTrainRoleMapper.selectRoleByUserId(utr);
    }

    @Override
    public int updateDeptName(User user) {
        return userMapper.updateDeptName(user);
    }

    @Override
    public int batchDeleteUser(Map<String, Object> params) {
        return userMapper.batchDeleteUser(params);
    }

    @Override
    public Integer selectUserListCount(Map<String, Object> param) {
        return userMapper.selectUserListCount(param);
    }

    @Override
    public List<Map<String,Object>> selectUserList(Map<String, Object> param) {
        List<User> userList = userMapper.selectUserList(param);
        List<Map<String,Object>> userMap = new ArrayList<Map<String, Object>>();
        if(userList.size()>0){
            for (User user:userList) {
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("id",user.getId());
                map.put("userType",user.getUserType());
                map.put("userAccount",user.getUserAccount());
                map.put("userName",user.getUserName());
                map.put("departmentName",user.getDepartmentName());
                map.put("registDate",user.getRegistDate());
                map.put("isValid",user.getIsValid());
                map.put("companyId",user.getCompanyId());
                map.put("departmentId",user.getDepartmentId());
                Company company = companyMapper.selectOne(map);
                map.put("companyName",company!=null?company.getVarName():"");
                userMap.add(map);
            }


        }

        return userMap;
    }
}
