package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by A on 2017/7/25.
 */
public class PersonDossier implements Serializable{

    private String id;
    private String userId;
    private String userName;
    private String roleId;
    private String roleName;
    private String deptId;
    private String deptName;
    private String companyId;
    private String companyName;
    private long yearStudytime;
    private long totalStudytime;
    private long yearStudySelf;
    private long totalStudySelf;
    private Integer trainCount;
    private String createUser;
    private String createTime;
    private String operUser;
    private String operTime;

    public PersonDossier(){}

    public PersonDossier(String userId, String userName, String roleId, String roleName, String deptId,
                         String deptName, String companyId, String companyName, long yearStudytime,
                         long totalStudytime, Integer trainCount){
        this.userId = userId;
        this.userName = userName;
        this.roleId = roleId;
        this.roleName = roleName;
        this.deptId = deptId;
        this.deptName = deptName;
        this.companyId = companyId;
        this.companyName = companyName;
        this.yearStudytime = yearStudytime;
        this.totalStudytime = totalStudytime;
        this.trainCount = trainCount;
    }

    public PersonDossier(String userId, String userName, String roleId, String roleName,
                         String deptName, String companyId, String companyName, long yearStudytime,
                         long totalStudytime, Integer trainCount, long yearStudySelf, long totalStudySelf){
        this.userId = userId;
        this.userName = userName;
        this.roleId = roleId;
        this.roleName = roleName;
        this.deptName = deptName;
        this.companyId = companyId;
        this.companyName = companyName;
        this.yearStudytime = yearStudytime;
        this.totalStudytime = totalStudytime;
        this.trainCount = trainCount;
        this.yearStudySelf = yearStudySelf;
        this.totalStudySelf = totalStudySelf;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public Integer getTrainCount() {
        return trainCount;
    }

    public void setTrainCount(Integer trainCount) {
        this.trainCount = trainCount;
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public long getYearStudytime() {
        return yearStudytime;
    }

    public void setYearStudytime(long yearStudytime) {
        this.yearStudytime = yearStudytime;
    }

    public long getTotalStudytime() {
        return totalStudytime;
    }

    public void setTotalStudytime(long totalStudytime) {
        this.totalStudytime = totalStudytime;
    }

    public long getYearStudySelf() {
        return yearStudySelf;
    }

    public void setYearStudySelf(long yearStudySelf) {
        this.yearStudySelf = yearStudySelf;
    }

    public long getTotalStudySelf() {
        return totalStudySelf;
    }

    public void setTotalStudySelf(long totalStudySelf) {
        this.totalStudySelf = totalStudySelf;
    }
}
