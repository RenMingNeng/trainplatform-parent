package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/31.
 */
public class UserRole implements Serializable {

    private static final long serialVersionUID = 384313112458416302L;
    private String id;
    private String userId;
    private String roleId;
    private String createUser;
    private String createDate;
    private String operUser;
    private String operDate;

    public UserRole() {
    }

    public UserRole(String id, String userId, String roleId, String createUser, String createDate, String operUser, String operDate) {
        this.id = id;
        this.userId = userId;
        this.roleId = roleId;
        this.createUser = createUser;
        this.createDate = createDate;
        this.operUser = operUser;
        this.operDate = operDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getOperUser() {
        return operUser;
    }

    public void setOperUser(String operUser) {
        this.operUser = operUser;
    }

    public String getOperDate() {
        return operDate;
    }

    public void setOperDate(String operDate) {
        this.operDate = operDate;
    }
}
