package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-07-31.
 */
public class CompanySupervise implements Serializable {

    private String id;          // 被监管单位
    private String pid;         // 父节点id
    private String companyName; // 节点名称
    private String companyId;   // 单位编号
    private Integer orderNum;   // 排序号

    public CompanySupervise(){}

    public CompanySupervise(String companyId, String companyName, String id, String pid, Integer orderNum){
        this.companyId = companyId;
        this.companyName = companyName;
        this.id = id;
        this.pid = pid;
        this.orderNum = orderNum;
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

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
