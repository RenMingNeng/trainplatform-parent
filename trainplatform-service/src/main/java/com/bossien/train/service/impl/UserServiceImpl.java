package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.MessageMapper;
import com.bossien.train.dao.tp.UserMapper;
import com.bossien.train.dao.tp.UserRoleMapper;
import com.bossien.train.domain.*;
import com.bossien.train.domain.dto.CompanyUserMessage;
import com.bossien.train.service.*;
import com.bossien.train.util.MD5Utils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;


//@Service
@Service("userService") // ("userService") sso需要注入,不要删除
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ISequenceService sequenceService;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private Role company_user;
    @Autowired
    private Role company_admin;
    @Autowired
    private Role super_vise;
    @Autowired
    private IBaseService<Message> messageBaseService;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private IMessageService messageService;
    @Autowired
    private ICompanyService companyService;
    @Autowired
    private IPersonDossierService personDossierService;
    @Autowired
    private ICompanyTjService companyTjService;


    //@Cacheable(value = "userCache#(60 * 60)", key = "'selectOne'.concat('_').concat(#p0.hashCode())")
    @Override
    public User selectOne(Map<String, Object> params) {
        return userMapper.selectOne(params);
    }

    @Override
    public User selectById(Map<String, Object> params) {
        return userMapper.selectById(params);
    }

    /*
        * 消息队列的消息加入数据库当中
        * */
    @Override
    public int insertMessage(CompanyUserMessage companyUserMessage) {

        // 校验-单位id
        String companyId = companyUserMessage.getCompanyId();
        if (StringUtils.isEmpty(companyId)) {
            return 0;
        }
        // 校验-单位存在性
        Map params = new HashMap();
        params.put("intId", companyId);
        Company company = companyService.selectByOne(params);
        if (null == company) {
            return 0;
        }

        User apuser = new User();
        UserRole userRole = new UserRole();
        apuser.setId(companyUserMessage.getId());//id
        apuser.setUserAccount(companyUserMessage.getAccount());//账号
        apuser.setUserPasswd(MD5Utils.encoderByMd5With32Bit(companyUserMessage.getPasswd()));//密码
        apuser.setUserName(companyUserMessage.getName());//用户姓名
        apuser.setSex(companyUserMessage.getSex());//sex
        apuser.setSupporter(companyUserMessage.getSupporter());//培训载体
        apuser.setMobileNo(companyUserMessage.getMobileNo());//手机号码
        apuser.setIsValid(companyUserMessage.getIsValid());
        apuser.setCreateUser(companyUserMessage.getCreateUser());//创建用户
        apuser.setCreateDate(companyUserMessage.getCreateDate());//创建时间
        apuser.setOperDate(companyUserMessage.getOperDate());//修改时间
        apuser.setOperUser(companyUserMessage.getOperUser());//修改用户
        apuser.setCompanyId(companyId);
        apuser.setDepartmentId(companyId);
       // apuser.setDepartmentName(company.getVarName());
        apuser.setRegistType(companyUserMessage.getRegistType());
        apuser.setRegistDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String userType = companyUserMessage.getUserType();
        if(!org.springframework.util.StringUtils.isEmpty(userType)) {
            if ("1".equals(userType)) { // 管理员
                apuser.setUserType("1,3");
            } else if("2".equals(userType)) { // 监管
                apuser.setUserType("2,3");
            }
        } else {
            apuser.setUserType("3");
        }
        userMapper.insert(apuser);
              /*
              * 向user_role表中跟新数据
              * */

          /*先删除权限*/
        UserRole userRoleNew = new UserRole();
        Map<String, Object> map = new HashMap<>();
        map.put("userId", companyUserMessage.getId());
        List<String> roleId = new ArrayList<>();
        roleId.add(company_user.getId());/*学员*/
        if (companyUserMessage.getUserType().equals("1")) {
            roleId.add(company_admin.getId());/*管理员*/
        } else if (companyUserMessage.getUserType().equals("2")) {
            roleId.add(super_vise.getId());/*监管*/
        }
        map.put("roleId", roleId);
        int result = userRoleMapper.deleteByRoleId(map);//删除
        // 插入管理员或者监管
        userRole.setId(sequenceService.generator());
        userRole.setUserId(apuser.getId());
        if (companyUserMessage.getUserType().equals("1")) {
            userRole.setRoleId(company_admin.getId());
        } else {
            userRole.setRoleId(super_vise.getId());
        }
        userRole.setOperUser(companyUserMessage.getOperUser());
        userRole.setOperDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        userRole.setCreateUser(companyUserMessage.getCreateUser());
        userRole.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        userRoleMapper.insert(userRole);
        // 默认培训平台账户初始拥有学员角色
        userRole.setId(sequenceService.generator());
        userRole.setUserId(apuser.getId());
        userRole.setRoleId(company_user.getId());
        userRole.setOperUser(companyUserMessage.getOperUser());
        userRole.setOperDate(companyUserMessage.getOperDate());
        userRole.setCreateUser(companyUserMessage.getCreateUser());
        userRole.setCreateDate(companyUserMessage.getCreateDate());
        userRoleMapper.insert(userRole);

        int count = 1;
        //更新统计表
        companyTjService.updateCompanyTj(count, companyId, apuser.getUserName());
        return 0;
    }

    @Override
    public int updateMessage(CompanyUserMessage companyUserMessage, String useId) {
        // 校验-单位id
        String companyId = companyUserMessage.getCompanyId();
        if (StringUtils.isEmpty(companyId)) {
            return 0;
        }
        // 校验-单位存在性
        Map params = new HashMap();
        params.put("intId", companyId);
        Company company = companyService.selectByOne(params);
        if (null == company) {
            return 0;
        }
        User apuser = new User();
        UserRole userRole = new UserRole();
        apuser.setId(useId);
        apuser.setUserAccount(companyUserMessage.getAccount());//账号
        apuser.setUserPasswd(MD5Utils.encoderByMd5With32Bit(companyUserMessage.getPasswd()));//密码
        apuser.setUserName(companyUserMessage.getName());//用户姓名
        apuser.setSex(companyUserMessage.getSex());//性别
        apuser.setSupporter(companyUserMessage.getSupporter());//培训载体
        apuser.setMobileNo(companyUserMessage.getMobileNo());//手机号码
        apuser.setIsValid(companyUserMessage.getIsValid());
        apuser.setCreateUser(companyUserMessage.getCreateUser());//创建用户
        apuser.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//创建时间
        apuser.setOperDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//修改时间
        apuser.setOperUser(companyUserMessage.getOperUser());//修改用户
        apuser.setCompanyId(companyUserMessage.getCompanyId());//所属单位
        apuser.setDepartmentId(companyId);                     //所属部门
        //apuser.setDepartmentName(company.getVarName());       //部门名称
        apuser.setRegistDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String userType = companyUserMessage.getUserType();
        if(!org.springframework.util.StringUtils.isEmpty(userType)) {
            if ("1".equals(userType)) { // 管理员
                apuser.setUserType("1,3");
            } else if("2".equals(userType)) { // 监管
                apuser.setUserType("2,3");
            }
        } else {
            apuser.setUserType("3");
        }
        userMapper.updateMessage(apuser);
             /*
              * 向user_role表中跟新数据
              * */

          /*先删除权限*/
        UserRole userRoleNew = new UserRole();
        Map<String, Object> map = new HashMap<>();
        map.put("userId", useId);
        List<String> roleId = new ArrayList<>();
        roleId.add(company_user.getId());/*学员*/
        roleId.add(company_admin.getId());/*管理员*/
        map.put("roleId", roleId);
        int result = userRoleMapper.deleteByRoleId(map);//删除
        // 插入管理员或者监管
        userRole.setId(sequenceService.generator());
        userRole.setUserId(apuser.getId());
        if (companyUserMessage.getUserType().equals("1")) {
            userRole.setRoleId(company_admin.getId());
        } else {
            userRole.setRoleId(super_vise.getId());
        }
        userRole.setOperUser(companyUserMessage.getOperUser());
        userRole.setOperDate(companyUserMessage.getOperDate());
        userRole.setCreateUser(companyUserMessage.getCreateUser());
        userRole.setCreateDate(companyUserMessage.getCreateDate());
        userRoleMapper.insert(userRole);
        // 默认培训平台账户初始拥有学员角色
        userRole.setId(sequenceService.generator());
        userRole.setUserId(apuser.getId());
        userRole.setRoleId(company_user.getId());
        userRole.setOperUser(companyUserMessage.getOperUser());
        userRole.setOperDate(companyUserMessage.getOperDate());
        userRole.setCreateUser(companyUserMessage.getCreateUser());
        userRole.setCreateDate(companyUserMessage.getCreateDate());
        userRoleMapper.insert(userRole);

        return 0;
    }


    @Override
    public int deleteByUser(String codes) {
        return userMapper.deleteByUser(codes);
    }

    @Override
    public List<String> selectUserbyUserAccount(String account) {
        return userMapper.selectUserbyUserAccount(account);
    }

    @Override
    public Map<String, Object> queryUserInfoById(Map<String, Object> params) {
        return userMapper.queryUserInfoById(params);
    }

    /**
     * 统计单位下的学员数量
     */
    @Override
    public int selectUserCount(Map<String, Object> params) {
        params.put("isValid", "1");                //有效
        params.put("userType", "3");                //学员
        return userMapper.selectUserCount(params);
    }

    /**
     * 获取公司下的userIds
     */
    @Override
    public List<String> selectUserIds(Map<String, Object> params) {
        params.put("isValid", "1");                //有效
        params.put("userType", "3");                //学员
        return userMapper.selectUserIds(params);
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {
        return userMapper.selectCount(params);
    }

    @Override
    public List<User> selectList(Map<String, Object> params) {
        return userMapper.selectList(params);
    }

    @Override
    public void updateUserValid(String isValid, String userId) {
        User user = new User();
        user.setIsValid(isValid);
        user.setId(userId);
        userMapper.update(user);
    }

    /**
     * 更新最后一次登录时间
     */
    @Override
    public int updateLastLoginTime(User user) {
        return userMapper.updateLastLoginTime(user);
    }

    /**
     * openId获取账户信息
     *
     * @return User
     */
    @Override
    public User selectByOpenId(String openId) {
        return userMapper.selectByOpenId(openId);
    }

    @Override
    public Integer insert(User user) {
        return userMapper.insert(user);
    }

    @Override
    public Integer updateOpenId(String id, String open_id) {
        return userMapper.updateOpenId(id, open_id);
    }

    @Override
    public Integer accountUnbind(String user_id) {
        return userMapper.accountUnbind(user_id);
    }

    @Override
    public Integer moveToNewCompany(String userId, String companyId, String messageId, String userName, User user) {
        int count = 0;

        //更新消息状态
        Map<String, Object> params = new HashedMap();
        params.put("id", messageId);
        Message message = messageService.selectOne(params);
        params.put("messageStatus", "2");
        String messageContent = message.getMessageContent().replace("href", "_href");
        messageContent = messageContent.replace("move_yes", "");
        messageContent = messageContent.replace("move_no", "");
        params.put("messageContent", messageContent);
        messageService.update(params);

        String send_id = message.getSendId();
        String change_result = "拒绝";
        if (StringUtils.isEmpty(companyId)) {
            //给学员发送消息通知
            sendMessageToStudent(send_id, user.getUserName(), change_result);
            return 0;
        }

        //给学员发送消息通知
        change_result = "同意,请重新登录";
        sendMessageToStudent(userId, user.getUserName(), change_result);

        //根据companyId查询单位
        params.clear();
        params.put("companyId", companyId);
        Company company = companyService.selectOne(params);
        params.put("companyName", company != null ? company.getVarName() : "");
        params.put("userId", userId);
        params.put("deptId", companyId);

        //更新前公司的统计信息
        updateCompanyTj(userId, userName);

        //变更公司单位
        count = userMapper.moveToNewCompany(userId, companyId, userName);
        //变更个人档案表中的公司
        personDossierService.updateCompanyId(params);
        //更新现公司统计表
        companyTjService.updateCompanyTj(count, companyId, userName);
        return count;
    }

    /**
     * 更新变更前所在公司的统计信息
     */
    private int updateCompanyTj(String userId, String userName) {
        int count = 0;
        Map<String, Object> param = new HashMap<>();
        param.put("id", userId);
        User user = this.selectOne(param);
        if (null == user) {
            return count;
        }
        param.put("companyId", user.getCompanyId());
        //根据companyId查询公司
        CompanyTj companyTj = companyTjService.selectOne(param);
        if (null == companyTj) {
            return count;
        }
        if ("0".equals(companyTj.getCountUser())) {
            return count;
        }
        count = -1;
        companyTjService.updateCompanyTj(count, user.getCompanyId(), userName);
        return 0;
    }

    /**
     * 给管理员发送消息
     */
    @Override
    public void sendMessage(User user, String companyId) {
        String messageTitle = Message.MSG_TPL_TITLE_0002;
        String messageContent = Message.MSG_TPL_CONTENT_0002;
        Message message = messageBaseService.build(user.getUserName(),
            new Message("",
                companyId,
                messageTitle,
                messageContent,
                user.getId(),
                user.getUserName()
            ));

        String yes =
            "messageId='" + message.getId() + "' companyId='" + companyId + "' userId = '" + user.getId() + "' userName= '" + user.getUserName()
                + "'";
        String not = "messageId='" + message.getId() + "'";
        message.setMessageContent(MessageFormat.format(messageContent, user.getUserName(), "账号:" + user.getUserAccount(), yes, not));
        messageMapper.insert(message);
    }

    /**
     * 给学员发送消息
     */
    public void sendMessageToStudent(String userId, String userName, String change_result) {
        String messageTitle = Message.MSG_TPL_TITLE_0003;
        String messageContent = Message.MSG_TPL_CONTENT_0003;
        Message message = messageBaseService.build(userName,
            new Message(userId,
                "",
                messageTitle,
                messageContent,
                userId,
                userName
            ));

        message.setMessageContent(MessageFormat.format(messageContent, change_result));
        messageMapper.insert(message);
    }

    @Override
    public Integer updateUserBySso(String userId, String userName, String userPasswd, String mobileNo, String sex) {
        return userMapper.updateUserBySso(userId, userName, userPasswd, mobileNo, sex);
    }

    @Override
    public List<String> selectUserAccountList(Map<String, Object> params) {

        return userMapper.selectUserAccountList(params);
    }
}
