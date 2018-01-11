package com.bossien.train.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目课程表
 * Created by Administrator on 2017/7/25.
 */
public class ProjectCourse implements Serializable {
    private String id;
    private String projectId;        //项目编号
    private String courseId;          //课程Id
    private String courseNo;          //课程编号
    private String courseName;        //课程名称
    private String roleId;            //角色id
    private String roleName;          //角色名称
    private Integer requirement;       //学时要求
    private Integer classHour;       //课时
    private Integer questionCount;       //题量
    private Integer selectCount;       //必选题量
    private String createTime;          //创建时间
    private String createUser;          //操作用户
    private String operTime;         //操作时间

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
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

    private String operUser;        //操作用户

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRequirement() {
        return requirement;
    }

    public void setRequirement(Integer requirement) {
        this.requirement = requirement;
    }

    public Integer getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(Integer selectCount) {
        this.selectCount = selectCount;
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
}
