package com.bossien.train.domain;

import java.io.Serializable;
import java.util.Date;

public class CompanyUser implements Serializable {

    private static final long serialVersionUID = -1809625615033532685L;
    private String intId;

    private String varAccount;

    private String varPasswd;

    private String varName;

    private String chrSex;

    private String chrIdType;

    private String varIdNumber;

    private String chrSupporter;

    private String chrUserType;

    private String varMobileNo;

    private String intCompanyId;

    private String varDepartmentId;

    private String chrRegistType;

    private String datRegistDate;

    private String chrIsValid;

    private String varOpenId;

    private String varCreateUser;

    private String datCreateDate;

    private String varOperUser;

    private String datOperDate;

    public String getIntId() {
        return intId;
    }

    public void setIntId(String intId) {
        this.intId = intId;
    }

    public String getVarAccount() {
        return varAccount;
    }

    public void setVarAccount(String varAccount) {
        this.varAccount = varAccount;
    }

    public String getVarPasswd() {
        return varPasswd;
    }

    public void setVarPasswd(String varPasswd) {
        this.varPasswd = varPasswd;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getChrSex() {
        return chrSex;
    }

    public void setChrSex(String chrSex) {
        this.chrSex = chrSex;
    }

    public String getChrIdType() {
        return chrIdType;
    }

    public void setChrIdType(String chrIdType) {
        this.chrIdType = chrIdType;
    }

    public String getVarIdNumber() {
        return varIdNumber;
    }

    public void setVarIdNumber(String varIdNumber) {
        this.varIdNumber = varIdNumber;
    }

    public String getChrSupporter() {
        return chrSupporter;
    }

    public void setChrSupporter(String chrSupporter) {
        this.chrSupporter = chrSupporter;
    }

    public String getChrUserType() {
        return chrUserType;
    }

    public void setChrUserType(String chrUserType) {
        this.chrUserType = chrUserType;
    }

    public String getVarMobileNo() {
        return varMobileNo;
    }

    public void setVarMobileNo(String varMobileNo) {
        this.varMobileNo = varMobileNo;
    }

    public String getIntCompanyId() {
        return intCompanyId;
    }

    public void setIntCompanyId(String intCompanyId) {
        this.intCompanyId = intCompanyId;
    }

    public String getVarDepartmentId() {
        return varDepartmentId;
    }

    public void setVarDepartmentId(String varDepartmentId) {
        this.varDepartmentId = varDepartmentId;
    }

    public String getChrRegistType() {
        return chrRegistType;
    }

    public void setChrRegistType(String chrRegistType) {
        this.chrRegistType = chrRegistType;
    }

    public String getDatRegistDate() {
        return datRegistDate;
    }

    public void setDatRegistDate(String datRegistDate) {
        this.datRegistDate = datRegistDate;
    }

    public String getChrIsValid() {
        return chrIsValid;
    }

    public void setChrIsValid(String chrIsValid) {
        this.chrIsValid = chrIsValid;
    }

    public String getVarOpenId() {
        return varOpenId;
    }

    public void setVarOpenId(String varOpenId) {
        this.varOpenId = varOpenId;
    }

    public String getVarCreateUser() {
        return varCreateUser;
    }

    public void setVarCreateUser(String varCreateUser) {
        this.varCreateUser = varCreateUser;
    }

    public String getDatCreateDate() {
        return datCreateDate;
    }

    public void setDatCreateDate(String datCreateDate) {
        this.datCreateDate = datCreateDate;
    }

    public String getVarOperUser() {
        return varOperUser;
    }

    public void setVarOperUser(String varOperUser) {
        this.varOperUser = varOperUser;
    }

    public String getDatOperDate() {
        return datOperDate;
    }

    public void setDatOperDate(String datOperDate) {
        this.datOperDate = datOperDate;
    }
}