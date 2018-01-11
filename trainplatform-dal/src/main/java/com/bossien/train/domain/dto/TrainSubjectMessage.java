package com.bossien.train.domain.dto;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/16.
 */
public class TrainSubjectMessage implements Serializable {

    private static final long serialVersionUID = -5044675244890684204L;
    private String companyCode;//公司编码
    private String companyId;//公司编码
    private String id;//培训主题主键Id 唯一
    private String isValid;//是否有效, 1 有效 2 无效
    private String source;//来源  1：系统自带 2：企业自制
    private String subjectDesc;//培训主题描述
    private String operDate;//修改时间
    private String operUser;//"修改用户"
    private String createDate;//创建时间"
    private String createUser;//创建用户
    private String subjectName;//培训主题名称
    private String fId;//图片
    private String order;//顺序
    private String businessCode;
    private String subjectTypeId;

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getSubjectTypeId() {
        return subjectTypeId;
    }

    public void setSubjectTypeId(String subjectTypeId) {
        this.subjectTypeId = subjectTypeId;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getfId() {
        return fId;
    }

    public void setfId(String fId) {
        this.fId = fId;
    }

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

    public String getSubjectDesc() {
        return subjectDesc;
    }

    public void setSubjectDesc(String subjectDesc) {
        this.subjectDesc = subjectDesc;
    }

    public String getOperDate() {
        return operDate;
    }

    public void setOperDate(String operDate) {
        this.operDate = operDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getOperUser() {
        return operUser;
    }

    public void setOperUser(String operUser) {
        this.operUser = operUser;
    }
}
