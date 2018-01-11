package com.bossien.train.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/26.
 */
public class ProjectInfo implements Serializable {

    private String id;
    private String subjectName;
    private String projectName;
    private String roleName;
    private String projectStartTime;
    private String projectEndTime;
    private String projectTrainTime;
    private String projectExerciseTime;
    private String projectExamTime;
    private Integer intTrainPeriod;
    private Integer intRetestTime;
    private Integer personCount;
    private String projectMode;
    private String projectType;
    private String projectStatus;
    private String createTime;
    private String createUser;
    private String operTime;
    private String operUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @JsonFormat(locale="zh",timezone="GMT+8",pattern = "yyyy-MM-dd")
    public String getProjectStartTime() {
        return projectStartTime;
    }

    public void setProjectStartTime(String projectStartTime) {
        this.projectStartTime = projectStartTime;
    }

    @JsonFormat(locale="zh",timezone="GMT+8",pattern = "yyyy-MM-dd")
    public String getProjectEndTime() {
        return projectEndTime;
    }

    public void setProjectEndTime(String projectEndTime) {
        this.projectEndTime = projectEndTime;
    }

    @JsonFormat(locale="zh",timezone="GMT+8",pattern = "yyyy-MM-dd")
    public String getProjectTrainTime() {
        return projectTrainTime;
    }

    public void setProjectTrainTime(String projectTrainTime) {
        this.projectTrainTime = projectTrainTime;
    }

    @JsonFormat(locale="zh",timezone="GMT+8",pattern = "yyyy-MM-dd")
    public String getProjectExerciseTime() {
        return projectExerciseTime;
    }

    public void setProjectExerciseTime(String projectExerciseTime) {
        this.projectExerciseTime = projectExerciseTime;
    }

    @JsonFormat(locale="zh",timezone="GMT+8",pattern = "yyyy-MM-dd")
    public String getProjectExamTime() {
        return projectExamTime;
    }

    public void setProjectExamTime(String projectExamTime) {
        this.projectExamTime = projectExamTime;
    }

    public Integer getIntTrainPeriod() {
        return intTrainPeriod;
    }

    public void setIntTrainPeriod(Integer intTrainPeriod) {
        this.intTrainPeriod = intTrainPeriod;
    }

    public Integer getIntRetestTime() {
        return intRetestTime;
    }

    public void setIntRetestTime(Integer intRetestTime) {
        this.intRetestTime = intRetestTime;
    }

    public Integer getPersonCount() {
        return personCount;
    }

    public void setPersonCount(Integer personCount) {
        this.personCount = personCount;
    }

    public String getProjectMode() {
        return projectMode;
    }

    public void setProjectMode(String projectMode) {
        this.projectMode = projectMode;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    @JsonFormat(locale="zh",timezone="GMT+8",pattern = "yyyy-MM-dd")
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

    @JsonFormat(locale="zh",timezone="GMT+8",pattern = "yyyy-MM-dd")
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

    // 项目状态 1：未发布、2：未开始、3：进行中、4：已结束
    public enum ProjectStatus {

        TYPE_1("未发布", "1"), TYPE_2("未开始", "2"), TYPE_3("进行中", "3"), TYPE_4("已结束", "4");
        // 枚举中文
        private String name;
        // 枚举值
        private String value;

        // 枚举翻译
        public static ProjectStatus getEnum(String value) {
            ProjectStatus[] is = ProjectStatus.values();
            for (ProjectStatus i : is) {
                if (!value.equals(i.getValue())) {
                    continue;
                }
                return i;
            }
            return null;
        }

        // 构造
        ProjectStatus(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
