package com.bossien.train.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/25.
 */
public class ProjectCourseInfo implements Serializable{

    private String id;   //主键
    private String projectId; //项目序号
    private String courseId;  //课程序号
    private String courseName; //课程名称
    private Integer classHour;  //课时
    private String userId;  //用户编号
    private Long requirementStudyTime;  //应修学时(单位秒）
    private Long totalStudyTime;  //已修学时(单位秒）
    private Long answerStudyTime;  //答题学时(单位秒）
    private Long trainStudyTime;   //培训学时(单位秒）
    private Integer totalQuestion;   //总题量
    private Integer yetAnswered;   //已答题量
    private Integer correctAnswered;  //答对题量
    private Double correctRate;  //正确率
    private String finishStatus; //完成状态
    private String createUser; //创建用户
    private String createTime; //创建日期
    private String operUser; //操作用户
    private String operTime;//操作日期

    public ProjectCourseInfo() {
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

    public Integer getClassHour() {
        return classHour;
    }

    public void setClassHour(Integer classHour) {
        this.classHour = classHour;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getFinishStatus() {
        return finishStatus;
    }

    public void setFinishStatus(String finishStatus) {
        this.finishStatus = finishStatus;
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

    public ProjectCourseInfo(String id, String projectId, String courseId, String courseName, Integer classHour,
                             String userId, Long requirementStudyTime, Long totalStudyTime, Long answerStudyTime,
                             Long trainStudyTime, Integer totalQuestion, Integer yetAnswered, Integer correctAnswered,
                             Double correctRate, String finishStatus, String createUser, String createTime,
                             String operUser, String operTime) {
        this.id = id;
        this.projectId = projectId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.classHour = classHour;
        this.userId = userId;
        this.requirementStudyTime = requirementStudyTime;
        this.totalStudyTime = totalStudyTime;
        this.answerStudyTime = answerStudyTime;
        this.trainStudyTime = trainStudyTime;
        this.totalQuestion = totalQuestion;
        this.yetAnswered = yetAnswered;
        this.correctAnswered = correctAnswered;
        this.correctRate = correctRate;
        this.finishStatus = finishStatus;
        this.createUser = createUser;
        this.createTime = createTime;
        this.operUser = operUser;
        this.operTime = operTime;
    }
}
