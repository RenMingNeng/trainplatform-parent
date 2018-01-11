package com.bossien.train.service;

import com.bossien.train.domain.TrainRole;
import com.bossien.train.domain.User;
import com.bossien.train.domain.UserTrainRole;
import com.bossien.train.domain.UserWork;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/31.
 */
public interface IUserManagerService {

     List<User> queryAllUserList(Map<String,Object> params);

     int queryAllUserCount(Map<String,Object> params);

     void changeUserDept(String companyId,String deptId,List<String> userIds, User user);

     void changeUserQuit(String deptId,List<String> userIds, User user);

     User queryUserInfo(User user);

     List<UserWork> queryUserWorkList(UserWork userWork);

     int updateUserInfo(User user);

     void updateUser(User user,String roleIds,UserWork uWork,UserTrainRole usertrole);

     List<UserTrainRole> queryUserRoles(UserTrainRole userTrainRole);

     List<Map<String,Object>> queryAllRoleByParams(Map<String,Object> params);

     UserTrainRole selectOne(UserTrainRole userTrainRole);

     int insert(UserTrainRole userTrainRole);

     int update(UserTrainRole userTrainRole);

     TrainRole selectRoleNameById(TrainRole trainRole);

     /**
      * 获取用户集合
      * @param params
      * @return
      */
     List<User> queryUser(Map<String, Object> params);

     /**
      * 根据部门Id 和 公司Id查询用户信息
      * @param params
      * @return
      */
     List<Map<String, Object>> selectUserByDeptId(Map<String, Object> params);

     /**
      * 根据部门Ids 和 公司Id查询用户信息
      * @param params
      * @return
      */
     List<Map<String, Object>> selectUserAndDeptId(Map<String, Object> params);

     /**
         * 获取公司下的userIds
     *
         * @param params
     * @return
         */
     List<String> selectUserIds(Map<String, Object> params);

     int deleteUser(User user,User operUser);

     int batchDelUser(List<String> ulist,User user);

      String queryUserTroleNames(List<UserTrainRole> utrList);

     List<UserTrainRole> utrListselectRoleByUserId(UserTrainRole utr);

     int updateDeptName(User user);

     int batchDeleteUser(Map<String, Object> params);
     /**
      * 根据集团公司id集合查询人员总数
      * @param param
      * @return
      */
     Integer selectUserListCount(Map<String,Object> param);

     /**
      * 根据集团公司id集合查询人员集合
      * @param param
      * @return
      */
     List<Map<String,Object>> selectUserList(Map<String,Object> param);
}
