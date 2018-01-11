package com.bossien.train.domain.dto;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/16.
 */
public class CompanyCourseMessage implements Serializable {
    private static final long serialVersionUID = 5853400161200948456L;
    private String companyCode;//公司编码
    private String companyCourseTypeId;//课程类别ID
    private String courseId;//公司编码
    private String companyId;//公司id

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

    public String getCompanyCourseTypeId() {
        return companyCourseTypeId;
    }

    public void setCompanyCourseTypeId(String companyCourseTypeId) {
        this.companyCourseTypeId = companyCourseTypeId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
