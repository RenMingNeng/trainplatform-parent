package com.bossien.train.domain.dto;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/16.
 */
public class CourseMessage implements Serializable {

    private static final long serialVersionUID = 4582788720354994695L;
    /*
        * 课程消息队列的javaBean
        * */
    private String companyCode;//公司编码
    private String courseId;//课程ID",
    private String courseTypeId;//课程类型ID
    private String courseCode;//课程编号 唯一
    private String courseName;//课程名称
    private String courseDesc;//课程描述
    private String courseClassHour;//课程学时
    private String courseCoverInfo;//
    private String course_Type;//课程类型(1：组合管理、2：附件管理)
    private String companyId;//公司id
    private String coursePlatformType;//"平台类型(01.全部 02. 培训平台03.APP 04.工具箱
    private String courseStatus;//课程状态(01:未发布，02：已发布)
    private String courseSource;//课程类别来源(I:内部，O：外部)
    private String intCompanyId;
    private String createUser;//创建人
    private String datCreateDate;//创建时间
    private String operUser;//修改人
    private String datOperDate;//修改时间
    private String courseCompanyId;//修改时间
    private Integer courseQuestionNumber;//试题数量
    private Integer courseAttachmentNumber;//课件数量

    public Integer getCourseQuestionNumber() {
        return courseQuestionNumber;
    }

    public void setCourseQuestionNumber(Integer courseQuestionNumber) {
        this.courseQuestionNumber = courseQuestionNumber;
    }

    public Integer getCourseAttachmentNumber() {
        return courseAttachmentNumber;
    }

    public void setCourseAttachmentNumber(Integer courseAttachmentNumber) {
        this.courseAttachmentNumber = courseAttachmentNumber;
    }



    public String getCourseCompanyId() {
        return courseCompanyId;
    }

    public void setCourseCompanyId(String courseCompanyId) {
        this.courseCompanyId = courseCompanyId;
    }

    public String getCourseCoverInfo() {
        return courseCoverInfo;
    }

    public void setCourseCoverInfo(String courseCoverInfo) {
        this.courseCoverInfo = courseCoverInfo;
    }

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

    public String getIntCompanyId() {
        return intCompanyId;
    }

    public void setIntCompanyId(String intCompanyId) {
        this.intCompanyId = intCompanyId;
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

    public String getCourseTypeId() {
        return courseTypeId;
    }

    public void setCourseTypeId(String courseTypeId) {
        this.courseTypeId = courseTypeId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }

    public String getCourseClassHour() {
        return courseClassHour;
    }

    public void setCourseClassHour(String courseClassHour) {
        this.courseClassHour = courseClassHour;
    }

    public String getCourse_Type() {
        return course_Type;
    }

    public void setCourse_Type(String course_Type) {
        this.course_Type = course_Type;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCoursePlatformType() {
        return coursePlatformType;
    }

    public void setCoursePlatformType(String coursePlatformType) {
        this.coursePlatformType = coursePlatformType;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getCourseSource() {
        return courseSource;
    }

    public void setCourseSource(String courseSource) {
        this.courseSource = courseSource;
    }
}
