package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/31.
 */
public class Role implements Serializable {

    private static final long serialVersionUID = 135827495823657812L;
    private String id;
    private String roleName;
    private String roleDesc;
    private String createUser;
    private String createDate;
    private String operUser;
    private String operDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
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

    public Role(String id, String roleName, String roleDesc) {
        this.id = id;
        this.roleName = roleName;
        this.roleDesc = roleDesc;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        Role other = (Role) obj;
        if(this.getId().equals(other.getId()) && this.getRoleName().equals(other.getRoleName())) {
            return true;
        }
        return false;
    }
}
