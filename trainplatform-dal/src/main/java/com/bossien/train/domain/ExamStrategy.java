package com.bossien.train.domain;

import java.io.Serializable;

/**
 * 组卷策略表
 * Created by Administrator on 2017/7/27.
 */
public class ExamStrategy implements Serializable {
    private String  id;
    private String  projectId;            //项目id
    private String  roleId;               //角色id
    private String  roleName;             //受训角色名称
    private Integer totalScore;           //总分
    private Integer necessaryHour;        //学时要求
    private Integer examDuration;         //考试时长
    private Integer passScore;            //合格分
    private Integer singleCount;          //单选题量
    private Integer singleScore;          //单选题分值
    private Integer singleAllCount;     // 单选题总题量
    private Integer manyCount;            //多选题量
    private Integer manyScore;            //多选题分值
    private Integer manyAllCount;       //多选总题量
    private Integer judgeCount;           //判断题量
    private Integer judgeScore;           //判断题分值
    private Integer judgeAllCount;       //判断总题量
    private Integer filloutCount;         //填空题量
    private Integer filloutScore;         //填空题分值
    private Integer quesAnsCount;         //问题题量
    private Integer quesAnsScore;         //问答题分值
    private String   createTime;            //创建时间
    private String createUser;            //创建用户
    private String   operTime;           //操作时间
    private String operUser;           //创建用户

    //辅助字段
    private Integer selectAllCount;           //必选题数量

    public Integer getSelectAllCount() {
        return selectAllCount;
    }

    public void setSelectAllCount(Integer selectAllCount) {
        this.selectAllCount = selectAllCount;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getId() {
        return id;
    }

    public Integer getSingleAllCount() {
        return singleAllCount;
    }

    public void setSingleAllCount(Integer singleAllCount) {
        this.singleAllCount = singleAllCount;
    }

    public Integer getManyAllCount() {
        return manyAllCount;
    }

    public void setManyAllCount(Integer manyAllCount) {
        this.manyAllCount = manyAllCount;
    }

    public Integer getJudgeAllCount() {
        return judgeAllCount;
    }

    public void setJudgeAllCount(Integer judgeAllCount) {
        this.judgeAllCount = judgeAllCount;
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

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getNecessaryHour() {
        return necessaryHour;
    }

    public void setNecessaryHour(Integer necessaryHour) {
        this.necessaryHour = necessaryHour;
    }

    public Integer getExamDuration() {
        return examDuration;
    }

    public void setExamDuration(Integer examDuration) {
        this.examDuration = examDuration;
    }

    public Integer getPassScore() {
        return passScore;
    }

    public void setPassScore(Integer passScore) {
        this.passScore = passScore;
    }

    public Integer getSingleCount() {
        return singleCount;
    }

    public void setSingleCount(Integer singleCount) {
        this.singleCount = singleCount;
    }

    public Integer getSingleScore() {
        return singleScore;
    }

    public void setSingleScore(Integer singleScore) {
        this.singleScore = singleScore;
    }

    public Integer getManyCount() {
        return manyCount;
    }

    public void setManyCount(Integer manyCount) {
        this.manyCount = manyCount;
    }

    public Integer getManyScore() {
        return manyScore;
    }

    public void setManyScore(Integer manyScore) {
        this.manyScore = manyScore;
    }

    public Integer getJudgeCount() {
        return judgeCount;
    }

    public void setJudgeCount(Integer judgeCount) {
        this.judgeCount = judgeCount;
    }

    public Integer getJudgeScore() {
        return judgeScore;
    }

    public void setJudgeScore(Integer judgeScore) {
        this.judgeScore = judgeScore;
    }

    public Integer getFilloutCount() {
        return filloutCount;
    }

    public void setFilloutCount(Integer filloutCount) {
        this.filloutCount = filloutCount;
    }

    public Integer getFilloutScore() {
        return filloutScore;
    }

    public void setFilloutScore(Integer filloutScore) {
        this.filloutScore = filloutScore;
    }

    public Integer getQuesAnsCount() {
        return quesAnsCount;
    }

    public void setQuesAnsCount(Integer quesAnsCount) {
        this.quesAnsCount = quesAnsCount;
    }

    public Integer getQuesAnsScore() {
        return quesAnsScore;
    }

    public void setQuesAnsScore(Integer quesAnsScore) {
        this.quesAnsScore = quesAnsScore;
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
