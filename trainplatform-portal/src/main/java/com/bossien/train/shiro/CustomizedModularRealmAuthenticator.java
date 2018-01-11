package com.bossien.train.shiro;

import com.google.common.collect.Lists;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.Collection;

/**
 * Created by Administrator on 2017/11/21.
 * 业务场景：对于是否sso登录, realm不一样, 涉及到多realm
 * 然而, 认证器是shiro自带的org.apache.shiro.authc.pam.ModularRealmAuthenticator 当配置了多个Realm时，会使用所有配置的Realm
 * 所以这里进行重写
 */
public class CustomizedModularRealmAuthenticator extends ModularRealmAuthenticator {

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 判断getRealms()是否返回为空
        assertRealmsConfigured();
        // 强制转换回自定义的CustomizedToken
        CustomizedToken customizedToken = (CustomizedToken) authenticationToken;
        // sso检测
        boolean isSupportSso = customizedToken.isSupportSso();
        // 登录类型
        String username = customizedToken.getUsername();
        // 所有Realm
        Collection<Realm> realms = getRealms();
        // 登录类型对应的所有Realm
        Collection<Realm> typeRealms = Lists.newArrayList();
        for (Realm realm : realms) {
            if(realm instanceof SsoRealm && isSupportSso && !"admin".equals(username)) {
                typeRealms.add(realm); break;
            }
            if (!"admin".equals(username) && realm instanceof UserRealm) {
                typeRealms.add(realm); break;
            }
            if ("admin".equals(username) && realm instanceof SuperRealm) {
                typeRealms.add(realm); break;
            }
        }

//        // 判断是单Realm还是多Realm
//        if (typeRealms.size() == 1)
//            return doSingleRealmAuthentication(typeRealms.iterator().next(), customizedToken);
//        else
//            return doMultiRealmAuthentication(typeRealms, customizedToken);

        // 此次不用判断是单Realm还是多Realm, 上方保证直走一个Realm
        return doSingleRealmAuthentication(typeRealms.iterator().next(), customizedToken);
    }
}
