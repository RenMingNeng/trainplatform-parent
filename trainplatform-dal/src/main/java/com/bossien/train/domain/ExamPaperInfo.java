package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by huangzhaoyong on 2017/7/31.
 * 试卷详情表
 */
public class ExamPaperInfo implements Serializable{
    String examNo;         //试卷编号
    String userId;         //用户编号
    String projectId;      //项目编号
    String roleId;         //角色编号
    String examType;        //试卷类型 1模拟2正式
    Integer singleScore;       //单选题分数
    Integer manyScore;         //多选题分数
    Integer judgeScore;        //判断题分数
    Integer filloutScore;      //填空题分数
    Integer quesAnsScore;     //问答题分数
    Integer passScore;         //合格分数
    Integer totalScore;        //总分数
    Integer examDuration;        //考试时长
    String necessaryHour;      //学时要求
    String examStatus;     //考试状态：1未考试 2考试
    String createTime;      //创建考卷时间

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public Integer getSingleScore() {
        return singleScore;
    }

    public void setSingleScore(Integer singleScore) {
        this.singleScore = singleScore;
    }

    public Integer getManyScore() {
        return manyScore;
    }

    public void setManyScore(Integer manyScore) {
        this.manyScore = manyScore;
    }

    public Integer getJudgeScore() {
        return judgeScore;
    }

    public void setJudgeScore(Integer judgeScore) {
        this.judgeScore = judgeScore;
    }

    public Integer getFilloutScore() {
        return filloutScore;
    }

    public void setFilloutScore(Integer filloutScore) {
        this.filloutScore = filloutScore;
    }

    public Integer getQuesAnsScore() {
        return quesAnsScore;
    }

    public void setQuesAnsScore(Integer quesAnsScore) {
        this.quesAnsScore = quesAnsScore;
    }

    public Integer getPassScore() {
        return passScore;
    }

    public void setPassScore(Integer passScore) {
        this.passScore = passScore;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getExamDuration() {
        return examDuration;
    }

    public void setExamDuration(Integer examDuration) {
        this.examDuration = examDuration;
    }

    public String getNecessaryHour() {
        return necessaryHour;
    }

    public void setNecessaryHour(String necessaryHour) {
        this.necessaryHour = necessaryHour;
    }

    public String getExamStatus() {
        return examStatus;
    }

    public void setExamStatus(String examStatus) {
        this.examStatus = examStatus;
    }
}
