package com.bossien.train.shiro;

import com.bossien.framework.interceptor.SignInterceptor;
import com.bossien.train.domain.Company;
import com.bossien.train.domain.Role;
import com.bossien.train.domain.User;
import com.bossien.train.domain.UserRole;
import com.bossien.train.exception.CompanyNotFoundException;
import com.bossien.train.exception.CompanyNotValidException;
import com.bossien.train.exception.IncorrectPasswordException;
import com.bossien.train.exception.RoleNotFoundException;
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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserRealm extends AuthorizingRealm {

    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

    private static final String session_user = PropertiesUtils.getValue("session_user");
    private static final String session_role = PropertiesUtils.getValue("session_role");
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

        // 查询角色
        params.clear();
        params.put("userId", user.getId());
        params.put("roleId", token.getUsertype());
        UserRole userRole = userRoleService.selectOne(params);
        if(null == userRole) {
            throw new RoleNotFoundException();          // 角色不存在
        }
        String roleId = userRole.getRoleId();
        // 匹配对应角色
        Role role = findRoleById(roleId);
        if(null == role) {
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
        Set<String> roles = Sets.newConcurrentHashSet();
        roles.add(role.getRoleName());
        // session
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(session_user, user);
        session.setAttribute(session_role, role);
        session.setAttribute(shiro_roles, roles);
        session.setAttribute(shiro_permissions, permissions);
        session.setAttribute(user_id, user.getId());
        session.setAttribute(company_id, user.getCompanyId());
        session.setAttribute(company_name, company.getVarName());
        return new SimpleAuthenticationInfo(
                user.getUserAccount(), user.getUserPasswd(), user.toString()
        );
    }

    private Role findRoleById(String roleId) {
        if(roleId.equals(super_admin.getId())) {
            return super_admin;
        }
        if(roleId.equals(super_vise.getId())) {
            return super_vise;
        }
        if(roleId.equals(company_admin.getId())) {
            return company_admin;
        }
        if(roleId.equals(company_user.getId())) {
            return company_user;
        }
        return null;
    }

}
