package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by A on 2017/7/25.
 * 受训角色表
 */
public class TrainSubject implements Serializable {

    private static final long serialVersionUID = -1205095626783782841L;
    private String varId;
    private String subjectName;
    private String subjectDesc;
    private String source; //来源:1：系统自带 2：企业自制
    private String isValid; //1 有效 2 无效
    private String logo; //照片
    private String order; //照片
    private String createDate;
    private String createUser;
    private String operUser;
    private String operDate;
    private String subjectCode;
    private  String courseCount;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getCourseCount() {
        return courseCount;
    }

    public void setCourseCount(String courseCount) {
        this.courseCount = courseCount;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getVarId() {
        return varId;
    }

    public void setVarId(String varId) {
        this.varId = varId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectDesc() {
        return subjectDesc;
    }

    public void setSubjectDesc(String subjectDesc) {
        this.subjectDesc = subjectDesc;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }
}
