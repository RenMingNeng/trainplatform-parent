package com.bossien.train.domain;

/**
 * Created by Administrator on 2017/7/25.
 */
public class UserLoginInfo {

    private String userId;
    private Integer loginCount;
    private Integer duration;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public UserLoginInfo(String userId, Integer loginCount, Integer duration) {
        this.userId = userId;
        this.loginCount = loginCount;
        this.duration = duration;
    }
}
