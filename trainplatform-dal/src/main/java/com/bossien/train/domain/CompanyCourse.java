package com.bossien.train.domain;

import java.io.Serializable;

/**
 * 公司授权课程表
 * Created by Administrator on 2017/8/3.
 */
public class CompanyCourse implements Serializable {
    private static final long serialVersionUID = -7836635638050657742L;
    private String varId;
     private String intCompanyId;            //公司id
     private String intCompanyCourseTypeId;  //授权课程分类id
     private String intCourseId;             //课程id

    public String getVarId() {
        return varId;
    }

    public void setVarId(String varId) {
        this.varId = varId;
    }

    public String getIntCompanyId() {
        return intCompanyId;
    }

    public void setIntCompanyId(String intCompanyId) {
        this.intCompanyId = intCompanyId;
    }

    public String getIntCourseId() {
        return intCourseId;
    }

    public void setIntCourseId(String intCourseId) {
        this.intCourseId = intCourseId;
    }

    public String getIntCompanyCourseTypeId() {
        return intCompanyCourseTypeId;
    }

    public void setIntCompanyCourseTypeId(String intCompanyCourseTypeId) {
        this.intCompanyCourseTypeId = intCompanyCourseTypeId;
    }
}
