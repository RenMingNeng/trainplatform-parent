package com.bossien.train.domain;

import java.io.Serializable;

/**
 * 学员个人自学统计类
 * Created by A on 2017/11/28.
 */
public class StudySelf implements Serializable{
    String id;
    String user_id;            //用户Id
    String user_name;          //用户名称
    String course_id;          //课程Id
    String course_no;          //课程编号
    String course_name;        //课程名称
    Integer class_hour;        //课时（分）
    Long study_time;           //自学学时（秒）
    String create_time;        //创建时间
    String oper_time;          //操作时间

    public StudySelf() {

    }

    public StudySelf(String id, String user_id, String user_name, String course_id, String course_no, String course_name, Integer class_hour, Long study_time, String create_time, String oper_time) {
        this.id = id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.course_id = course_id;
        this.course_no = course_no;
        this.course_name = course_name;
        this.class_hour = class_hour;
        this.study_time = study_time;
        this.create_time = create_time;
        this.oper_time = oper_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_no() {
        return course_no;
    }

    public void setCourse_no(String course_no) {
        this.course_no = course_no;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public Integer getClass_hour() {
        return class_hour;
    }

    public void setClass_hour(Integer class_hour) {
        this.class_hour = class_hour;
    }

    public Long getStudy_time() {
        return study_time;
    }

    public void setStudy_time(Long study_time) {
        this.study_time = study_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getOper_time() {
        return oper_time;
    }

    public void setOper_time(String oper_time) {
        this.oper_time = oper_time;
    }
}
