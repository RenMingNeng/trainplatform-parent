package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/25.
 */
public class OnlineUser implements Serializable {

    private String userId;
    private String userName;
    private Long loginTime;
    private String sessionId;
    private String ip;
    private String hostname;
    private String country;
    private String city;
    private String province;
    private String useragent;
    private String opersystem;
    private String browser;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getUseragent() {
        return useragent;
    }

    public void setUseragent(String useragent) {
        this.useragent = useragent;
    }

    public String getOpersystem() {
        return opersystem;
    }

    public void setOpersystem(String opersystem) {
        this.opersystem = opersystem;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public OnlineUser() {
        super();
    }

    public OnlineUser(String sessionId) {
        this.sessionId = sessionId;
    }
}
