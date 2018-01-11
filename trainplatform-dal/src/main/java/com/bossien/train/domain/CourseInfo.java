package com.bossien.train.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程信息表（项目详情）
 * Created by Administrator on 2017/7/25.
 */
public class CourseInfo implements Serializable {
    private static final long serialVersionUID = -5472179665045067300L;
    private String id;
    private String courseId;             //课程编号
    private String courseNo;             //课程编号
    private String courseName;           //课程名称
    private String courseDesc;            //课程简介
    private String courseTypeId;          //课程类型id
    private String courseTypeName;        //课程类型名称
    private Integer classHour;            //课时
    private Integer questionCount;        //题数量
    private String createTime;           //创建时间
    private String createUser;           //操作用户
    private String operTime;          //操作时间
    private String operUser;         //s操作用户

    public String getCourseTypeId() {
        return courseTypeId;
    }

    public void setCourseTypeId(String courseTypeId) {
        this.courseTypeId = courseTypeId;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseTypeName() {
        return courseTypeName;
    }

    public void setCourseTypeName(String courseTypeName) {
        this.courseTypeName = courseTypeName;
    }

    public Integer getClassHour() {
        return classHour;
    }

    public void setClassHour(Integer classHour) {
        this.classHour = classHour;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public String getOperUser() {
        return operUser;
    }

    public void setOperUser(String operUser) {
        this.operUser = operUser;
    }

    public CourseInfo() {
    }

    public CourseInfo(String id, String courseId, String courseNo, String courseName, String courseDesc, String courseTypeId, String courseTypeName, Integer classHour, Integer questionCount, String createTime, String createUser, String operTime, String operUser) {
        this.id = id;
        this.courseId = courseId;
        this.courseNo = courseNo;
        this.courseName = courseName;
        this.courseDesc = courseDesc;
        this.courseTypeId = courseTypeId;
        this.courseTypeName = courseTypeName;
        this.classHour = classHour;
        this.questionCount = questionCount;
        this.createTime = createTime;
        this.createUser = createUser;
        this.operTime = operTime;
        this.operUser = operUser;
    }

    public CourseInfo(String courseId, String courseNo, String courseName, String courseDesc, String courseTypeId, String courseTypeName, Integer classHour, Integer questionCount, String createTime, String createUser, String operTime, String operUser) {

        this.courseId = courseId;
        this.courseNo = courseNo;
        this.courseName = courseName;
        this.courseDesc = courseDesc;
        this.courseTypeId = courseTypeId;
        this.courseTypeName = courseTypeName;
        this.classHour = classHour;
        this.questionCount = questionCount;
        this.createTime = createTime;
        this.createUser = createUser;
        this.operTime = operTime;
        this.operUser = operUser;
    }
}
