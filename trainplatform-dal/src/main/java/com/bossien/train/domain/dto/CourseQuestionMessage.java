package com.bossien.train.domain.dto;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/16.
 */
public class CourseQuestionMessage  implements Serializable {
    private static final long serialVersionUID = -5965564028019177271L;
    private String companyCode;//公司编码
    private String courseId;//课程ID
    private String courseQuestionId;//课程题库ID
    private String companyId;//公司id

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCourseQuestionId() {
        return courseQuestionId;
    }

    public void setCourseQuestionId(String courseQuestionId) {
        this.courseQuestionId = courseQuestionId;
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
