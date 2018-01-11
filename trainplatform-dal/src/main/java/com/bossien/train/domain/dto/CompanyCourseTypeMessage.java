package com.bossien.train.domain.dto;

import java.io.Serializable;

/**
 * 课程类型
 * Created by Administrator on 2017/8/16.
 */
public class CompanyCourseTypeMessage implements Serializable {

    private static final long serialVersionUID = -7591096967235750640L;
    private String companyCode;//公司编码
    private String companyCourseTypeId;//"课程类别ID"，
    private String companyCourseTypeName;//"课程类别名称",
    private String companyCourseTypeDesc;//课程类别描述
    private String companyCourseTypePid;//父节点ID
    private String companyCourseTypeOrder;//顺序号
    private String companyCourseTypeLevel;//层级
    private String companyId;//公司id
    private String companyCourseTypeSource;
    private String companyCourseTypeStatus;
    private String companyCourseTypeCode;
    private String createUser;//创建人
    private String datCreateDate;//创建时间
    private String operUser;//修改人
    private String datOperDate;//修改时间

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getDatCreateDate() {
        return datCreateDate;
    }

    public void setDatCreateDate(String datCreateDate) {
        this.datCreateDate = datCreateDate;
    }

    public String getOperUser() {
        return operUser;
    }

    public void setOperUser(String operUser) {
        this.operUser = operUser;
    }

    public String getDatOperDate() {
        return datOperDate;
    }

    public void setDatOperDate(String datOperDate) {
        this.datOperDate = datOperDate;
    }

    public String getCompanyCourseTypeCode() {
        return companyCourseTypeCode;
    }

    public void setCompanyCourseTypeCode(String companyCourseTypeCode) {
        this.companyCourseTypeCode = companyCourseTypeCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyCourseTypeId() {
        return companyCourseTypeId;
    }

    public void setCompanyCourseTypeId(String companyCourseTypeId) {
        this.companyCourseTypeId = companyCourseTypeId;
    }

    public String getCompanyCourseTypeName() {
        return companyCourseTypeName;
    }

    public void setCompanyCourseTypeName(String companyCourseTypeName) {
        this.companyCourseTypeName = companyCourseTypeName;
    }

    public String getCompanyCourseTypeDesc() {
        return companyCourseTypeDesc;
    }

    public void setCompanyCourseTypeDesc(String companyCourseTypeDesc) {
        this.companyCourseTypeDesc = companyCourseTypeDesc;
    }

    public String getCompanyCourseTypePid() {
        return companyCourseTypePid;
    }

    public void setCompanyCourseTypePid(String companyCourseTypePid) {
        this.companyCourseTypePid = companyCourseTypePid;
    }

    public String getCompanyCourseTypeOrder() {
        return companyCourseTypeOrder;
    }

    public void setCompanyCourseTypeOrder(String companyCourseTypeOrder) {
        this.companyCourseTypeOrder = companyCourseTypeOrder;
    }

    public String getCompanyCourseTypeLevel() {
        return companyCourseTypeLevel;
    }

    public void setCompanyCourseTypeLevel(String companyCourseTypeLevel) {
        this.companyCourseTypeLevel = companyCourseTypeLevel;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyCourseTypeSource() {
        return companyCourseTypeSource;
    }

    public void setCompanyCourseTypeSource(String companyCourseTypeSource) {
        this.companyCourseTypeSource = companyCourseTypeSource;
    }

    public String getCompanyCourseTypeStatus() {
        return companyCourseTypeStatus;
    }

    public void setCompanyCourseTypeStatus(String companyCourseTypeStatus) {
        this.companyCourseTypeStatus = companyCourseTypeStatus;
    }
}
