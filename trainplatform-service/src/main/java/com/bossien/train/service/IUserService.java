package com.bossien.train.service;

import com.bossien.train.domain.User;
import com.bossien.train.domain.dto.CompanyUserMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/31.
 */
public interface IUserService {

    User selectOne(Map<String, Object> params);

    User selectById(Map<String, Object> params);

    int insertMessage(CompanyUserMessage companyUserMessage);

    int updateMessage(CompanyUserMessage companyUserMessage,String userId);

    int deleteByUser(String codes);

    List<String> selectUserbyUserAccount(String account);

    Map<String, Object> queryUserInfoById(Map<String, Object> params);

    /**
     * 统计单位下的学员数量
     *
     * @param params
     * @return
     */
    int selectUserCount(Map<String, Object> params);

    /**
     * 获取公司下的userIds
     *
     * @param params
     * @return
     */
    List<String> selectUserIds(Map<String, Object> params);

    Integer selectCount(Map<String, Object> params);

    List<User> selectList(Map<String, Object> params);

    /**
     * 修改用户状态
     *
     * @param isValid
     * @param userId
     */
    void updateUserValid(String isValid, String userId);
    /**
     * 更新最后一次登录时间
     * @param user
     * @return
     */
    int updateLastLoginTime (User user);

    /**
     * openId获取账户信息
     * @param openId
     * @return User
     */
    User selectByOpenId(String openId);

    Integer insert(User user);

    Integer updateOpenId(String id, String open_id);

    Integer accountUnbind(String user_id);

    Integer moveToNewCompany(String userId, String companyId,String messageId,String userName,User user);

    /**
     * 发送申请变更公司
     * @param user
     * @param companyId
     */
    void sendMessage(User user,String companyId);

    Integer updateUserBySso(String userId, String userName, String userPasswd, String mobileNo, String sex);

    /**
     * 查询用户userAccount集合
     * @param params
     * @return
     */
    List<String> selectUserAccountList(Map<String, Object> params);
}
