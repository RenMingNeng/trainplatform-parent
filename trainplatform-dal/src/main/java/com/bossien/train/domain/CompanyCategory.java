package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/13.
 */
public class CompanyCategory implements Serializable {

    private Integer intId;          // 主键，自增
    private String varName;         // 单位类型名称
    private Integer intPid;         // 上级id
    private Integer intOrder;       // 顺序号
    private String varCreateUser;   // 创建用户
    private String datCreateDate;   // 创建时间
    private String varOperUser;     // 操作用户
    private String datOperDate;     // 操作时间

    public Integer getIntId() {
        return intId;
    }

    public void setIntId(Integer intId) {
        this.intId = intId;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public Integer getIntPid() {
        return intPid;
    }

    public void setIntPid(Integer intPid) {
        this.intPid = intPid;
    }

    public Integer getIntOrder() {
        return intOrder;
    }

    public void setIntOrder(Integer intOrder) {
        this.intOrder = intOrder;
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
