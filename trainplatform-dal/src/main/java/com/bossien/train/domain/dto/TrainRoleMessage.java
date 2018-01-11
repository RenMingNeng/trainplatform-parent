package com.bossien.train.domain.dto;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/16.
 */
public class TrainRoleMessage  implements Serializable {
    private static final long serialVersionUID = -7575315818869747046L;
    private String companyCode;//公司编码
    private String companyId;//公司编码
    private String id;//ID
    private String roleName;//受训角色名称
    private String roleDesc;//受训角色描述
    private String isValid;//是否有效, 1 有效 2 无效
    private String source;//来源 1：系统自带 2：企业自制
    private String createUser;//创建用户
    private String createDate;//创建时间
    private String operUser;//修改用户
    private String operDate;//"修改时间"

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

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

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
