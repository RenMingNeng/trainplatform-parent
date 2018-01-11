package com.bossien.train.shiro;

import com.bossien.framework.interceptor.SignInterceptor;
import com.bossien.train.domain.Company;
import com.bossien.train.domain.Role;
import com.bossien.train.domain.User;
import com.bossien.train.domain.UserRole;
import com.bossien.train.exception.*;
import com.bossien.train.service.ICompanyService;
import com.bossien.train.service.IUserRoleService;
import com.bossien.train.service.IUserService;
import com.bossien.train.util.PropertiesUtils;
import com.google.common.collect.Sets;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SsoRealm extends AuthorizingRealm {

    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

    private static final String sso_switch = PropertiesUtils.getValue("sso_switch");
    private static final String is_support_sso = PropertiesUtils.getValue("is_support_sso");
    private static final String session_user = PropertiesUtils.getValue("session_user");
    private static final String session_role = PropertiesUtils.getValue("session_role");
    private static final String session_roles = PropertiesUtils.getValue("session_roles");
    private static final String shiro_roles = PropertiesUtils.getValue("shiro_roles");
    private static final String shiro_permissions = PropertiesUtils.getValue("shiro_permissions");
    private static final String user_id = PropertiesUtils.getValue("user_id");
    private static final String company_id = PropertiesUtils.getValue("company_id");
    private static final String company_name = PropertiesUtils.getValue("company_name");

    @Autowired
    private Role super_admin;
    @Autowired
    private Role super_vise;
    @Autowired
    private Role company_admin;
    @Autowired
    private Role company_user;
    @Autowired
    private HashSet<String> permissions;
    @Autowired
    private IUserService userService;
    @Autowired
    private ICompanyService companyService;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 授权访问
     * {@link SimpleAuthorizationInfo SimpleAuthorizationInfo}, as it is suitable in most cases.
     *
     * @param principals the primary identifying principals of the AuthorizationInfo that should be retrieved.
     * @return the AuthorizationInfo associated with this principals.
     * @see SimpleAuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Session session = SecurityUtils.getSubject().getSession();

        Set<String> roles = (Set) session.getAttribute(shiro_roles);
        Set<String> permissions = (Set) session.getAttribute(shiro_permissions);

        info.addRoles(roles);
        info.addStringPermissions(permissions);

        return info;
    }

    /**
     * 登录认证
     * <p/>
     * For most dataSources, this means just 'pulling' authentication data for an associated subject/user and nothing
     * more and letting Shiro do the rest.  But in some systems, this method could actually perform EIS specific
     * log-in logic in addition to just retrieving data - it is up to the Realm implementation.
     * <p/>
     * A {@code null} return value means that no account could be associated with the specified token.
     *
     * @param authToken the authentication token containing the user's principal and credentials.
     * @return an {@link AuthenticationInfo} object containing account data resulting from the
     * authentication ONLY if the lookup is successful (i.e. account exists and is valid, etc.)
     * @throws AuthenticationException if there is an error acquiring data or performing
     *                                 realm-specific authentication logic for the specified <tt>token</tt>
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {

        CustomizedToken token = (CustomizedToken) authToken;

        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("user_account", token.getUsername());
        // 账户是否存在
        User user = userService.selectOne(params);
        if(null == user) {
            throw new UnknownAccountException();        // 未找到账号
        }
        // 账户是否锁定
        if(!User.IsValid.TYPE_1.getValue().equals(user.getIsValid())) {
            throw new LockedAccountException();         // 账号被锁定
        }
        // 密码是否正确
        if(!String.valueOf(token.getPassword()).equals(user.getUserPasswd())) {
            throw new IncorrectPasswordException();     // 密码错误
        }
        // companyId
        String companyId = user.getCompanyId();
        if(StringUtils.isEmpty(companyId)) {
            throw new CompanyNotFoundException();       // 无归属单位
        }
        // 查询单位
        params.clear();
        params.put("companyId", user.getCompanyId());
        Company company = companyService.selectOne(params);
        if(null == company) {
            throw new CompanyNotFoundException();       // 无归属单位
        }
        if(!Company.ChrIsValid.TYPE_1.getValue().equals(company.getChrIsValid())) {
            throw new CompanyNotValidException();       // 归属单位被禁用
        }

        // 查询角色list
        params.clear();
        params.put("userId", user.getId());
        List<UserRole> userRoles = userRoleService.selectList(params);
        if(null == userRoles || userRoles.size() == 0) {
            throw new RoleNotFoundException();          // 角色不存在
        }
//        // 验证重复登录
//        String winValue = (String) redisTemplate.opsForValue().get("app-online-windows-" + user.getId());
//        if(!StringUtils.isEmpty(winValue)) {
//            throw new AlreadyLoginException();       // 请勿重复登录
//        }
//        String macValue = (String) redisTemplate.opsForValue().get("app-online-mac-" + user.getId());
//        if(!StringUtils.isEmpty(macValue)) {
//            throw new AlreadyLoginException();       // 请勿重复登录
//        }
        // roles
        Set<Role> roles = findRolesByList(userRoles);
        // role
        Role role = this.findRoleByName(roles.iterator().next().getRoleName());
        // session
        Session session = SecurityUtils.getSubject().getSession();

        session.setAttribute(is_support_sso, isSupportSso(sso_switch));
        session.setAttribute(session_user, user);
        session.setAttribute(session_role, role);
        session.setAttribute(session_roles, roles);
        session.setAttribute(shiro_roles, Sets.newHashSet(role.getRoleName()));
        session.setAttribute(shiro_permissions, permissions);
        session.setAttribute(user_id, user.getId());
        session.setAttribute(company_id, user.getCompanyId());
        session.setAttribute(company_name, company.getVarName());
        return new SimpleAuthenticationInfo(
                user.getUserAccount(), user.getUserPasswd(), user.toString()
        );
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

    // 根据账户的角色list转换为roles 默认顺序：学员、单位管理员、监管、超级管理员
    private Set<Role> findRolesByList(List<UserRole> userRoles) {
        Set<Role> roles = Sets.newLinkedHashSet();
        if(null == userRoles || 0 == userRoles.size()) {
            return roles;
        }

        String roleId = "";
        Role companyUser = null;Role companyAdmin = null;Role superVise = null;Role superAdmin = null;

        for(UserRole userRole : userRoles) {
            roleId = userRole.getRoleId();
            if(StringUtils.isEmpty(roleId)) {
                continue;
            }
            if(roleId.equals(company_user.getId())) {
//                roles.add(company_user);
                companyUser = company_user;
                continue;
            }
            if(roleId.equals(company_admin.getId())) {
//                roles.add(company_admin);
                companyAdmin = company_admin;
                continue;
            }
            if(roleId.equals(super_vise.getId())) {
//                roles.add(super_vise);
                superVise = super_vise;
                continue;

            }
            if(roleId.equals(super_admin.getId())) {
//                roles.add(super_admin);
                superAdmin = super_admin;
                continue;
            }
            continue;
        }

        // 按顺序添加角色, 保证默认进入角色是学员
        if(null != companyUser) {
            roles.add(companyUser);
        }
        if(null != companyAdmin) {
            roles.add(companyAdmin);
        }
        if(null != superVise) {
            roles.add(superVise);
        }
        if(null != superAdmin) {
            roles.add(superAdmin);
        }
        return roles;
    }

    // 检测是否开启sso
    private boolean isSupportSso(String ssoSwitch) {

        return "1".equals(ssoSwitch) || "true".equalsIgnoreCase(ssoSwitch) || "on".equalsIgnoreCase(ssoSwitch);
    }
}
