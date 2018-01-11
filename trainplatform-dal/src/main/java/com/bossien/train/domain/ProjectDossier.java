package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by A on 2017/7/25.
 */
public class ProjectDossier implements Serializable{

    private String id;
    private String projectId;
    private String projectName;
    private String companyId;
    private String projectType;//项目类型编号：1：培训2：练习3：考试4：培训，练习5：培训，考试6：练习，考试 7：培训，练习，考试
    private String projectStartTime; //项目开始时间中最小时间
    private String projectEndTime; //项目结束时间中最大时间
    private int personCount;  //人数
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
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

    public int getPersonCount() {
        return personCount;
    }

    public void setPersonCount(int personCount) {
        this.personCount = personCount;
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
