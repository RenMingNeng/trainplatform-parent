package com.bossien.train.controller;

import com.bossien.framework.interceptor.SignInterceptor;
import com.bossien.train.domain.*;
import com.bossien.train.exception.Code;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.*;
import com.bossien.train.shiro.SsoRealm;
import com.bossien.train.util.MapUtils;
import com.bossien.train.util.PropertiesUtils;
import com.bossien.train.util.StringUtil;
import com.bossien.train.util.UploadFileUtil;
import com.google.common.collect.Sets;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class UserController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

    @Autowired
    private IUserService userService;
    @Autowired
    private ICompanyService companyService;
    @Autowired
    private IUserManagerService userManagerService;
    // 操作mongodb的集合名,等同于mysql中的表
    private final String collectionName = "user_login_info";

    @Autowired
    private Role super_admin;
    @Autowired
    private Role super_vise;
    @Autowired
    private Role company_admin;
    @Autowired
    private Role company_user;

    @Value("${session_user}")
    private String session_user;
    @Value("${session_role}")
    private String session_role;

    @Value("${shiro_roles}")
    private String shiro_roles;
    @Value("${user_login_info}")
    private String user_login_info;

    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping("/user_login_info")
    @ResponseBody
    public Object login(
            @RequestParam(value="user_id", required = true, defaultValue = "") String user_id
        ) {
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());

        if(StringUtils.isEmpty(user_id)) {
            return resp;
        }

        UserLoginInfo userLoginInfo = mongoTemplate.findOne(new Query().addCriteria(new Criteria("userId").is(user_id)), UserLoginInfo.class, collectionName);
        resp.setResult(userLoginInfo);

        return resp;
    }

    /**
     * 去修改密码页面
     * @param request
     * @param userId
     * @return
     */
    @RequestMapping(value = "/modifyPass")
    public ModelAndView modifyPassWord(HttpServletRequest request, String userId){
        User user=new User();
        user.setId(userId);
        user=userManagerService.queryUserInfo(user);
        ModelAndView mv = new ModelAndView();
        mv.addObject("user",user);
        mv.setViewName("modify_password");
        return mv;
    }

    /**
     * 学员修改密码
     * @return
     */
    @RequestMapping("/updatePassWord")
    @ResponseBody
    public Response<Object> updatePass(String userId, String userPasswd) {
        Response<Object> resp = new Response<Object>();
        if (org.apache.commons.lang3.StringUtils.isEmpty(userPasswd)) {
            resp.setMessage("密码为空");
            return resp;
        }
        User u=new User();
        u.setId(userId);
        u.setUserPasswd(userPasswd);
        u.setOperDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        int rt=userManagerService.updateUserInfo(u);
        if(rt>0){
            // result
            resp.setResult(true);
            resp.setMessage("密码修改成功");
        }else{
            resp.setResult(false);
            resp.setMessage("密码修改失败");
        }
        return resp;
    }

    @RequestMapping("/switch/role")
    @ResponseBody
    public Object switchRole(
            @RequestParam(value="role", required = true, defaultValue = "") String role
    ) {

        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());

        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        SsoRealm ssoRealm = (SsoRealm) rsm.getRealms().iterator().next();
        Subject subject = SecurityUtils.getSubject();
        String realmName = subject.getPrincipals().getRealmNames().iterator().next();
        SimplePrincipalCollection principals = new SimplePrincipalCollection(subject.getPrincipal(), realmName);
        subject.runAs(principals);
        // realm删除principals
        ssoRealm.getAuthorizationCache().remove(subject.getPrincipals());
        // 切换身份-刷新
        subject.releaseRunAs();

        // 刷新session相关信息
        Session session = subject.getSession();
        session.setAttribute(shiro_roles, Sets.newHashSet(role));
        session.setAttribute(session_role, this.findRoleByName(role));

        return resp;
    }

    private Role findRoleByName(String roleName) {
        if(StringUtils.isEmpty(roleName)) {
            return null;
        }
        if(roleName.equals(company_user.getRoleName())) {
            return company_user;
        }
        if(roleName.equals(company_admin.getRoleName())) {
            return company_admin;
        }
        if(roleName.equals(super_vise.getRoleName())) {
            return super_vise;
        }
        if(roleName.equals(super_admin.getRoleName())) {
            return super_admin;
        }
        return null;
    }

    // 解绑：统一账户与培训平台账户解除绑定
    @RequestMapping("/account/unbind")
    @ResponseBody
    public Object accountUnbind(
            @RequestParam(value="user_id", required = true, defaultValue = "") String user_id
    ) {

        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        // 解绑,清除user的openid
        userService.accountUnbind(user_id);
        return resp;
    }

    // 查找单位：varCode 单位编号
    @RequestMapping("/find_company_by_varcode")
    @ResponseBody
    public Object findCompanyByVarCode(
            @RequestParam(value="varCode", required = true, defaultValue = "") String varCode
    ) {

        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());

        // params
        Map<String, Object> params = MapUtils.newHashMap();
        params.put("varCode", varCode);

        // 根据varCode查询单位
        Company company = companyService.selectOne(params);

        // resp
        resp.setResult(company);

        return resp;
    }

    // 更换单位：intId 单位id
    @RequestMapping("/move_to_new_company")
    @ResponseBody
    public Object moveToNewCompany(HttpServletRequest request,
            @RequestParam(value="messageId", required = true, defaultValue = "") String messageId,
            @RequestParam(value="userId", required = true, defaultValue = "") String userId,
            @RequestParam(value="companyId", required = true, defaultValue = "") String companyId,
            @RequestParam(value="userName", required = true, defaultValue = "") String userName
    ) {
        User user = getCurrUser(request);
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());

        // 将用户移动到新的单位 注意：department_id department_name清空
       userService.moveToNewCompany(userId, companyId,messageId,userName,user);

        // resp
        return resp;
    }

    /**
     * 发送变更申请
     * @param request
     * @param companyId
     * @return
     */
    @RequestMapping("/sendMessage")
    @ResponseBody
    public Object saveCompany(HttpServletRequest request,
                              @RequestParam(value="companyId", required = true, defaultValue = "") String companyId,
                              @RequestParam(value="userName", required = true, defaultValue = "") String userName
                              ) {

        User user = this.getCurrUser(request);
        if(null == user){
            return new Response<>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }
        user.setUserName(userName);
        userService.sendMessage(user, companyId);

        return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

}
