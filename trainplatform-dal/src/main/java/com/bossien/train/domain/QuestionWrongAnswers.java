package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by A on 2017/8/1.
 */
public class QuestionWrongAnswers implements Serializable {
    String id;
    String project_id;      //项目编号
    String question_id;    //题目编号
    String user_id;         //用户
    String create_user;     //创建人
    String create_time;     //创建时间
    String oper_user;       //操作人
    String oper_time;        //操作时间

    public QuestionWrongAnswers(){}

    public QuestionWrongAnswers(String project_id, String question_id, String user_id, String create_user, String create_time) {
        this.project_id = project_id;
        this.question_id = question_id;
        this.user_id = user_id;
        this.create_user = create_user;
        this.create_time = create_time;
        this.oper_user = create_user;
        this.oper_time = create_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getOper_user() {
        return oper_user;
    }

    public void setOper_user(String oper_user) {
        this.oper_user = oper_user;
    }

    public String getOper_time() {
        return oper_time;
    }

    public void setOper_time(String oper_time) {
        this.oper_time = oper_time;
    }
}
