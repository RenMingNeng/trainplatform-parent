package com.bossien.train.domain;

import java.io.Serializable;

public class SubjectCourse implements Serializable {
    private static final long serialVersionUID = 4087168852621148922L;
    private String intId;

    private String intTrainSubjectId;

    private String intCourseId;

    public String getIntId() {
        return intId;
    }

    public void setIntId(String intId) {
        this.intId = intId;
    }

    public String getIntTrainSubjectId() {
        return intTrainSubjectId;
    }

    public void setIntTrainSubjectId(String intTrainSubjectId) {
        this.intTrainSubjectId = intTrainSubjectId;
    }

    public String getIntCourseId() {
        return intCourseId;
    }

    public void setIntCourseId(String intCourseId) {
        this.intCourseId = intCourseId;
    }
}
