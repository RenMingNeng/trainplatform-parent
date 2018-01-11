package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/26.
 */
public class ProjectBasic implements Serializable {

    private String id;
    private String projectName;
    private String subjectId;
    private String projectType;
    private Integer trainPeriod;
    private String projectMode;
    private String projectStatus;
    private String projectTrainInfo;
    private String projectExerciseInfo;
    private String projectExamInfo;
    private String projectOpen;
    private String createUser;
    private String createTime;
    private String operUser;
    private String operTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public Integer getTrainPeriod() {
        return trainPeriod;
    }

    public void setTrainPeriod(Integer trainPeriod) {
        this.trainPeriod = trainPeriod;
    }

    public String getProjectMode() {
        return projectMode;
    }

    public void setProjectMode(String projectMode) {
        this.projectMode = projectMode;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getProjectTrainInfo() {
        return projectTrainInfo;
    }

    public void setProjectTrainInfo(String projectTrainInfo) {
        this.projectTrainInfo = projectTrainInfo;
    }

    public String getProjectExerciseInfo() {
        return projectExerciseInfo;
    }

    public void setProjectExerciseInfo(String projectExerciseInfo) {
        this.projectExerciseInfo = projectExerciseInfo;
    }

    public String getProjectExamInfo() {
        return projectExamInfo;
    }

    public void setProjectExamInfo(String projectExamInfo) {
        this.projectExamInfo = projectExamInfo;
    }

    public String getProjectOpen() {
        return projectOpen;
    }

    public void setProjectOpen(String projectOpen) {
        this.projectOpen = projectOpen;
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
