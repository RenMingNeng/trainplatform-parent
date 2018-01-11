package com.bossien.train.domain;

import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-07-26.
 */
public class ProjectStatisticsInfo implements Serializable {
    private String id;      //主键
    private String projectId;      //项目序号
    private String projectName;     //项目名称
    private String projectStartTime;      //项目开始时间
    private String projectEndTime;     //项目结束时间
    private String userId;     //用户序号
    @Excel(name = "用户名称")
    private String userName;     //用户名称
    private String roleId;     //角色序号
    private String roleName;       //角色名称
    @Excel(name = "单位名称")
    private String deptName;       //单位名称
    @Excel(name = "应修学时(单位分)")
    private Long requirementStudyTime;     //应修学时(单位秒）
    @Excel(name = "已修总学时(单位分)")
    private Double updateTotalStudyTime;       //已修总学时(单位秒）
    private Long totalStudyTime;       //已修总学时(单位秒）
    private Long answerStudyTime;      //答题学时(单位秒）
    private Long trainStudyTime;       //培训学时(单位秒）
    private Integer totalQuestion;     //总题量
    @Excel(name = "已答题量")
    private Integer yetAnswered;       //已答题量
    @Excel(name = "答对题量")
    private Integer correctAnswered;       //答对题量
    @Excel(name = "答题正确率(%)")
    private Double correctRate;        //答题正确率
    private String trainStatus;    //培训完成状态
    private String examNo;     //试卷编号
    private String examTimeInfo;      //用户考试时间：开始时间+结束时间
    @Excel(name = "考试成绩")
    private String examScore;      //考试成绩
    private String examStatus;     //考试状态:1未考试2合格3不合格
    private String createUser;     //创建用户
    private String createTime;     //创建日期
    private String operUser;       //操作用户
    private String operTime;       //操作日期

    public ProjectStatisticsInfo() {

    }

    public Double getUpdateTotalStudyTime() {
        return updateTotalStudyTime;
    }

    public void setUpdateTotalStudyTime(Double updateTotalStudyTime) {
        this.updateTotalStudyTime = updateTotalStudyTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTrainStatus() {
        return trainStatus;
    }

    public void setTrainStatus(String trainStatus) {
        this.trainStatus = trainStatus;
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

    public String getProjectStartTime() {
        return projectStartTime;
    }

    public void setProjectStartTime(String projectStartTime) {
        this.projectStartTime = projectStartTime;
    }

    public String getProjectEndTime() {
        return projectEndTime;
    }

    public void setProjectEndTime(String projectEndTime) {
        this.projectEndTime = projectEndTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Long getRequirementStudyTime() {
        return requirementStudyTime;
    }

    public void setRequirementStudyTime(Long requirementStudyTime) {
        this.requirementStudyTime = requirementStudyTime;
    }

    public Long getTotalStudyTime() {
        return totalStudyTime;
    }

    public void setTotalStudyTime(Long totalStudyTime) {
        this.totalStudyTime = totalStudyTime;
    }

    public Long getAnswerStudyTime() {
        return answerStudyTime;
    }

    public void setAnswerStudyTime(Long answerStudyTime) {
        this.answerStudyTime = answerStudyTime;
    }

    public Long getTrainStudyTime() {
        return trainStudyTime;
    }

    public void setTrainStudyTime(Long trainStudyTime) {
        this.trainStudyTime = trainStudyTime;
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

    public Integer getCorrectAnswered() {
        return correctAnswered;
    }

    public void setCorrectAnswered(Integer correctAnswered) {
        this.correctAnswered = correctAnswered;
    }

    public Double getCorrectRate() {
        return correctRate;
    }

    public void setCorrectRate(Double correctRate) {
        this.correctRate = correctRate;
    }

    public String getExamNo() {
        return examNo;
    }

    public void setExamNo(String examNo) {
        this.examNo = examNo;
    }

    public String getExamTimeInfo() {
        return examTimeInfo;
    }

    public void setExamTimeInfo(String examTimeInfo) {
        this.examTimeInfo = examTimeInfo;
    }

    public String getExamScore() {
        return examScore;
    }

    public void setExamScore(String examScore) {
        this.examScore = examScore;
    }

    public String getExamStatus() {
        return examStatus;
    }

    public void setExamStatus(String examStatus) {
        this.examStatus = examStatus;
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

    public ProjectStatisticsInfo(String id, String projectId, String projectStartTime, String projectEndTime,
                                 String userId, String roleId, String roleName, String deptName,
                                 Long requirementStudyTime, Long totalStudyTime, Long answerStudyTime,
                                 Long trainStudyTime, Integer totalQuestion, Integer yetAnswered,
                                 Integer correctAnswered, Double correctRate, String trainStatus, String examNo, String examTimeInfo,
                                 String examScore, String examStatus, String createUser, String createTime,
                                 String operUser, String operTime) {
        this.id = id;
        this.projectId = projectId;
        this.projectStartTime = projectStartTime;
        this.projectEndTime = projectEndTime;
        this.userId = userId;
        this.roleId = roleId;
        this.roleName = roleName;
        this.deptName = deptName;
        this.requirementStudyTime = requirementStudyTime;
        this.totalStudyTime = totalStudyTime;
        this.answerStudyTime = answerStudyTime;
        this.trainStudyTime = trainStudyTime;
        this.totalQuestion = totalQuestion;
        this.yetAnswered = yetAnswered;
        this.correctAnswered = correctAnswered;
        this.correctRate = correctRate;
        this.trainStatus = trainStatus;
        this.examNo = examNo;
        this.examTimeInfo = examTimeInfo;
        this.examScore = examScore;
        this.examStatus = examStatus;
        this.createUser = createUser;
        this.createTime = createTime;
        this.operUser = operUser;
        this.operTime = operTime;
    }


}
