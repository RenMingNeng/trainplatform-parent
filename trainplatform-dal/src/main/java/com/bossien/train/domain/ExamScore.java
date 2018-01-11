package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/27.
 */
public class ExamScore implements Serializable {
    private String id;            //考试成绩序号
    private String projectId;     //项目Id
    private String examNo;        //考试编号
    private String userId;        //用户Id
    private String examType;      //考试类型 1:模拟考试 2:正式考试
    private Integer score;        //考试成绩
    private String examTime;      //考试开始时间
    private String isPassed;      //是否合格 1:合格 2:不合格
    private Double examDuration;  //考试用时
    private String createTime;    //创建时间
    private String createUser;    //创建用户
    private String operTime;      //操作时间
    private String operUser;      //操作用户

    public ExamScore(){}

    public ExamScore(String projectId, String examNo, String userId, String examType, Integer score,
                      String examTime, String isPassed, Double examDuration){
        this.projectId = projectId;
        this.examNo = examNo;
        this.userId = userId;
        this.examType = examType;
        this.score = score;
        this.examTime = examTime;
        this.isPassed = isPassed;
        this.examDuration = examDuration;
    }

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

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public String getIsPassed() {
        return isPassed;
    }

    public void setIsPassed(String isPassed) {
        this.isPassed = isPassed;
    }

    public Double getExamDuration() {
        return examDuration;
    }

    public void setExamDuration(Double examDuration) {
        this.examDuration = examDuration;
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
