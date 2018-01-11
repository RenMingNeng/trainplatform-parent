package com.bossien.train.shiro;

import com.bossien.framework.interceptor.SignInterceptor;
import com.bossien.train.domain.Role;
import com.bossien.train.domain.User;
import com.bossien.train.util.PropertiesUtils;
import com.google.common.collect.Sets;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class SuperRealm extends AuthorizingRealm {

    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

    private static final String session_user = PropertiesUtils.getValue("session_user");
    private static final String session_role = PropertiesUtils.getValue("session_role");
    private static final String shiro_roles = PropertiesUtils.getValue("shiro_roles");
    private static final String shiro_permissions = PropertiesUtils.getValue("shiro_permissions");
    private static final String user_id = PropertiesUtils.getValue("user_id");
    private static final String is_support_sso = PropertiesUtils.getValue("is_support_sso");
    private static final String sso_switch = PropertiesUtils.getValue("sso_switch");

    @Autowired
    private User admin;
    @Autowired
    private Role super_admin;

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

        // roles
        Set<String> roles = Sets.newConcurrentHashSet();
        roles.add(super_admin.getRoleName());
        // permissions
        Set<String> permissions = Sets.newConcurrentHashSet();
        // session
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(session_user, admin);
        session.setAttribute(session_role, super_admin);
        session.setAttribute(shiro_roles, roles);
        session.setAttribute(shiro_permissions, permissions);
        session.setAttribute(user_id, admin.getId());
        session.setAttribute(is_support_sso, isSupportSso(sso_switch));
        return new SimpleAuthenticationInfo(
                admin.getUserAccount(), admin.getUserPasswd(), admin.toString()
        );

    }

    // 检测是否开启sso
    private boolean isSupportSso(String ssoSwitch) {

        return "1".equals(ssoSwitch) || "true".equalsIgnoreCase(ssoSwitch) || "on".equalsIgnoreCase(ssoSwitch);
    }


}
