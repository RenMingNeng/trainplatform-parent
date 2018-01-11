package com.bossien.train.domain;

import java.io.Serializable;

/**
 * project_role实体
 * Created by wuhan li on 2017/7/31
 */
public class ProjectRole implements Serializable{

    private String id;         //项目角色Id
    private String projectId;  //项目Id
    private String roleId;     //角色Id
    private String roleName;   //角色名称
    private String createUser; //创建用户
    private String createTime; //创建时间
    private String operUser;   //操作用户
    private String operTime;   //操作时间

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
