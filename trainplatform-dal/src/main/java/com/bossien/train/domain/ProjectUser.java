package com.bossien.train.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * project_user实体
 * Created by zhaoli on 2017/7/25.
 */
public class ProjectUser implements Serializable{

    private String id;
    private String projectId;
    private String userId;
    private String userName;
    private String roleId;
    private String roleName;
    private String departmentName;
    private String createUser;
    private String createTime;
    private String operUser;
    private String operTime;

    public ProjectUser() {
    }

    public ProjectUser(String projectId, String userId, String userName, String roleId, String roleName, String departmentName) {
        this.projectId = projectId;
        this.userId = userId;
        this.userName = userName;
        this.roleId = roleId;
        this.roleName = roleName;
        this.departmentName = departmentName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

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

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOperUser() {
        return operUser;
    }

    public void setOperUser(String operUser) {
        this.operUser = operUser;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }
}
