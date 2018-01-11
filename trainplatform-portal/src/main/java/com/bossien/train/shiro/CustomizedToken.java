package com.bossien.train.shiro;

import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

/**
 * Created by Administrator on 2017/9/25.
 */
public class CustomizedToken  implements HostAuthenticationToken, RememberMeAuthenticationToken {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码, in char[] format
     */
    private char[] password;

    /**
     * 是否记住我
     * 默认值：<code>false</code>
     */
    private boolean rememberMe = false;

    /**
     * 主机名称或ip
     */
    private String host;

    /**
     * 用户类型
     */
    private String usertype;

    /**
     * 登录类型，判断是sso登录，还是普通登录
     */
    private boolean isSupportSso;

    public CustomizedToken () {
    }

    /**
     * 构造方法
     * @param username 用户名
     * @param password 密码（char[]）
     * @param rememberMe 是否记住我
     * @param host 主机或ip
     * @param usertype 用户类型
     */
    public CustomizedToken (final String username, final char[] password,
                                         final boolean rememberMe, final String host,
                                         final String usertype, final boolean isSupportSso) {

        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
        this.host = host;
        this.usertype = usertype;
        this.isSupportSso = isSupportSso;
    }

    /**
     * 构造方法
     * @param username 用户名
     * @param password 密码（String）
     * @param rememberMe 是否记住我
     * @param host 主机或ip
     * @param usertype 用户类型
     */
    public CustomizedToken (final String username, final String password,
                                         final boolean rememberMe, final String host,
                                         final String usertype, final boolean isSupportSso) {

        this(username, password != null ? password.toCharArray() : null , rememberMe, host, usertype, isSupportSso);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public boolean isSupportSso() {
        return isSupportSso;
    }

    public void setSupportSso(boolean supportSso) {
        isSupportSso = supportSso;
    }

    @Override
    public String getHost() {
        return null;
    }

    @Override
    public boolean isRememberMe() {
        return false;
    }

    @Override
    public Object getPrincipal() {
        return getUsername();
    }

    @Override
    public Object getCredentials() {
        return getPassword();
    }
}
