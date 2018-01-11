package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by huangzhaoyong on 2017/7/31.
 * 试卷详情表
 */
public class ExamQuestion implements Serializable{
    String projectId;      //项目编号
    String examNo;         //试卷编号
    String userId;         //用户编号
    String questionsId;    //题库id
    String createUser;
    String createTime;
    String operUser;
    String operTime;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getExamNo() {
        return examNo;
    }

    public void setExamNo(String examNo) {
        this.examNo = examNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuestionsId() {
        return questionsId;
    }

    public void setQuestionsId(String questionsId) {
        this.questionsId = questionsId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOperUser() {
        return operUser;
    }

    public void setOperUser(String operUser) {
        this.operUser = operUser;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }
}
