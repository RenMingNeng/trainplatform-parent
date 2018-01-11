package com.bossien.train.domain;

import java.io.Serializable;

public class ClassHours implements Serializable {

    private String project_id;      // 项目id
    private String course_id;       // 课程id
    private String user_id;         // 用户id
    private String source;          // 学时来源-1：学习、1：练习答题（正确）、2：练习答题（错误）、3：考试答题（正确）、4：考试答题（错误）',
    private Long study_time;      // 累加学时
    private String company_id;
    private String create_user;     // 创建用户
    private String create_time;     // 创建日期

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getStudy_time() {
        return study_time;
    }

    public void setStudy_time(Long study_time) {
        this.study_time = study_time;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public ClassHours(String project_id, String course_id, String user_id, String source, Long study_time, String company_id, String create_user, String create_time) {
        this.project_id = project_id;
        this.course_id = course_id;
        this.user_id = user_id;
        this.source = source;
        this.study_time = study_time;
        this.company_id = company_id;
        this.create_user = create_user;
        this.create_time = create_time;
    }
}
