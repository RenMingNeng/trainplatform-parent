package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/27.
 */
public class ProjectExerciseOrder implements Serializable {
    private String id;                //编号(练习排行表)
    private String projectId;         //项目Id
    private String userId;            //用户编号
    private String userName;          //用户名
    private String roleId;            //角色Id
    private String roleName;          //受训角色名称
    private String deptName;          //单位名称
    private Integer totalQuestion;    //总题量
    private Integer yetAnswered;      //已答题量
    private Integer notAnswered;      //未答题量
    private Integer correctAnswered;  //答对题量
    private Integer failAnswered;     //答错题量
    private Double correctRate;       //总答正确率：答对总题量/已答题量*100
    private Long answerStudyTime;     //答题学时(单位秒）
    private String createUser;        //创建用户
    private String createTime;        //创建日期
    private String operUser;          //操作用户
    private String operTime;          //操作日期

    public ProjectExerciseOrder(){}

    public ProjectExerciseOrder(String projectId, String userId, Integer totalQuestion, Integer yetAnswered,
                                Integer notAnswered, Integer correctAnswered, Integer failAnswered,
                                Double correctRate, Long answerStudyTime, String operUser, String operTime){
        this.projectId = projectId;
        this.userId = userId;
        this.totalQuestion = totalQuestion;
        this.yetAnswered = yetAnswered;
        this.notAnswered = notAnswered;
        this.correctAnswered = correctAnswered;
        this.failAnswered = failAnswered;
        this.correctRate = correctRate;
        this.answerStudyTime = answerStudyTime;
        this.operUser = operUser;
        this.operTime = operTime;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(Integer totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public Integer getYetAnswered() {
        return yetAnswered;
    }

    public void setYetAnswered(Integer yetAnswered) {
        this.yetAnswered = yetAnswered;
    }

    public Integer getNotAnswered() {
        return notAnswered;
    }

    public void setNotAnswered(Integer notAnswered) {
        this.notAnswered = notAnswered;
    }

    public Integer getCorrectAnswered() {
        return correctAnswered;
    }

    public void setCorrectAnswered(Integer correctAnswered) {
        this.correctAnswered = correctAnswered;
    }

    public Integer getFailAnswered() {
        return failAnswered;
    }

    public void setFailAnswered(Integer failAnswered) {
        this.failAnswered = failAnswered;
    }

    public Double getCorrectRate() {
        return correctRate;
    }

    public void setCorrectRate(Double correctRate) {
        this.correctRate = correctRate;
    }

    public Long getAnswerStudyTime() {
        return answerStudyTime;
    }

    public void setAnswerStudyTime(Long answerStudyTime) {
        this.answerStudyTime = answerStudyTime;
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
