package com.bossien.train.domain;

import java.io.Serializable;

public class Course implements Serializable {

    private static final long serialVersionUID = 6818817814841137707L;
    private String intId;           // 主键
    private String intTypeId;       // 课程类别ID
    private String varCode;         // 课程编号
    private String varName;         // 课程名称
    private String varDesc;         // 课程描述
    private String intClassHour;    // 课时
    private String chrType;         // 课程类型(1：组合管理、2：附件管理)
    private String chrPlatformType; // 适用平台/01.全部 02. 培训平台 03.APP 04.工具箱
    private String chrStatus;       // 课程状态/01:未发布，02：已发布
    private String varCreateUser;   // 创建用户
    private String datCreateDate;   // 创建时间
    private String varOperUser;     // 操作用户
    private String datOperDate;     // 操作时间
    private String intCompanyId;          //公司id
    private String chrSource;                //来源
    private String varCoverInfo;                //封面

    public String getVarCoverInfo() {
        return varCoverInfo;
    }

    public void setVarCoverInfo(String varCoverInfo) {
        this.varCoverInfo = varCoverInfo;
    }

    public String getChrSource() {
        return chrSource;
    }

    public void setChrSource(String chrSource) {
        this.chrSource = chrSource;
    }

    public String getIntCompanyId() {
        return intCompanyId;
    }

    public void setIntCompanyId(String intCompanyId) {
        this.intCompanyId = intCompanyId;
    }


    public String getIntClassHour() {
        return intClassHour;
    }

    public void setIntClassHour(String intClassHour) {
        this.intClassHour = intClassHour;
    }

    public String getIntId() {
        return intId;
    }

    public void setIntId(String intId) {
        this.intId = intId;
    }

    public String getIntTypeId() {
        return intTypeId;
    }

    public void setIntTypeId(String intTypeId) {
        this.intTypeId = intTypeId;
    }

    public String getVarCode() {
        return varCode;
    }

    public void setVarCode(String varCode) {
        this.varCode = varCode;
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

    public String getChrType() {
        return chrType;
    }

    public void setChrType(String chrType) {
        this.chrType = chrType;
    }

    public String getChrPlatformType() {
        return chrPlatformType;
    }

    public void setChrPlatformType(String chrPlatformType) {
        this.chrPlatformType = chrPlatformType;
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
