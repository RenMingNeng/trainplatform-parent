package com.bossien.train.domain;

import java.io.Serializable;

/**
 * 授权课程类别表
 * Created by Administrator on 2017/8/2.
 */
public class CompanyCourseType implements Serializable {
    private static final long serialVersionUID = 1688425017063645838L;
    private String varId;
    private String varName;               //课程类别名称
    private String varDesc;               //类别描述
    private String intCompanyId;          //公司id
    private String intPid;                //父id
    private String intOrder;              //顺序号
    private String intLevel;              //层级
    private String chrStatus;             //是否可用（Y:可用，N：不可用）
    private String varCreateUser;         //创建用户
    private String datCreateDate;         //创建时间
    private String varOperUser;           //操作用户
    private String datOperDate;           //操作时间
    private String chrSource;                //来源

    public String getChrSource() {
        return chrSource;
    }

    public void setChrSource(String chrSource) {
        this.chrSource = chrSource;
    }

    public String getVarId() {
        return varId;
    }

    public void setVarId(String varId) {
        this.varId = varId;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getVarDesc() {
        return varDesc;
    }

    public void setVarDesc(String varDesc) {
        this.varDesc = varDesc;
    }

    public String getIntCompanyId() {
        return intCompanyId;
    }

    public void setIntCompanyId(String intCompanyId) {
        this.intCompanyId = intCompanyId;
    }

    public String getIntPid() {
        return intPid;
    }

    public void setIntPid(String intPid) {
        this.intPid = intPid;
    }

    public String getIntOrder() {
        return intOrder;
    }

    public void setIntOrder(String intOrder) {
        this.intOrder = intOrder;
    }

    public String getIntLevel() {
        return intLevel;
    }

    public void setIntLevel(String intLevel) {
        this.intLevel = intLevel;
    }

    public String getChrStatus() {
        return chrStatus;
    }

    public void setChrStatus(String chrStatus) {
        this.chrStatus = chrStatus;
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
