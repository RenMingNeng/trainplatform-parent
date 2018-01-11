package com.bossien.train.dao.tp;


import com.bossien.train.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper {

    List<User> queryAllUserList(Map<String, Object> params);

    /**
     * 获取用户集合
     *
     * @param params
     * @return
     */
    List<User> queryUser(Map<String, Object> params);

    Map<String, Object> queryUserInfoById(Map<String, Object> params);

    int insert(User apuser);

    int update(User apuser);

    int updateMessage(User apuser);

    int selectAllUserCount(Map<String, Object> params);

    User selectOne(Map<String, Object> params);

    User selectById(Map<String, Object> params);

    int deleteByUser(@Param(value = "codes") String codes);

    List<String> selectUserbyUserAccount(@Param(value = "account") String account);

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

    List<Map<String, Object>> selectExcelUser(Map<String, Object> params);

    User selectUserInfo(User user);

    Integer selectCount(Map<String, Object> params);

    List<User> selectList(Map<String, Object> params);

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

    int updateDeptName(User user);
    int updateById(User user);

    int batchDeleteUser(Map<String, Object> params);

    List<String> selectByDeptId(String depId);

    /**
     * 更新最后一次登录时间
     * @param user
     * @return
     */
    int updateLastLoginTime (User user);

    User selectByOpenId(@Param(value = "open_id") String openId);

    Integer updateOpenId(@Param(value = "id") String id, @Param(value = "open_id") String open_id);

    Integer accountUnbind(@Param(value = "id")String user_id);

    Integer moveToNewCompany(@Param(value = "id")String userId, @Param(value = "company_id")String companyId,@Param(value = "user_name")String user_name);

    Integer updateUserBySso(@Param(value = "id") String userId, @Param(value = "user_name") String userName, @Param(value = "user_passwd") String userPasswd, @Param(value = "mobile_no") String mobileNo, @Param(value = "sex")String sex);

    /**
     * 查询企业根节点上的用户id
     * @return
     */
    List<String> selectUserIdsByDefaultDept(@Param(value = "deptId") String deptId,
                                            @Param(value = "companyId") String companyId);

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
    List<User> selectUserList(Map<String,Object> param);

    /**
     * 查询用户userAccount集合
     * @param params
     * @return
     */
    List<String> selectUserAccountList(Map<String, Object> params);

}
