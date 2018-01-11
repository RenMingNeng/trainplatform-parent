package com.bossien.train.domain.dto;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/16.
 */
public class CourseAttachmentMessage  implements Serializable {
    private static final long serialVersionUID = 784822033571855775L;
    private String companyCode;//公司编码
    private String courseId;//课程ID
    private String courseAttachmentId;//课程课件ID
    private String companyId;//公司id

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCourseAttachmentId() {
        return courseAttachmentId;
    }

    public void setCourseAttachmentId(String courseAttachmentId) {
        this.courseAttachmentId = courseAttachmentId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }


    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
