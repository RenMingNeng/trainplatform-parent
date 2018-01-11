package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by A on 2017/7/25.
 * 受训角色表
 */
public class TrainRole implements Serializable {

    private static final long serialVersionUID = -598986690279656764L;
    private String varId;
    private String roleName;
    private String roleDesc;
    private String source; //来源:1：系统自带 2：企业自制
    private String companyId;//所属单位
    private String isValid; //1 有效 2 无效
    private String createDate;
    private String createUser;
    private String operUser;
    private String operDate;
    private String roleCode;//受训角色编号
    private String roleId;//授权角色id

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getVarId() {
        return varId;
    }

    public void setVarId(String varId) {
        this.varId = varId;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
}
