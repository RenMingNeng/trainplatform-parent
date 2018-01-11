package com.bossien.train.domain;

import java.io.Serializable;

public class CompanyTrainSubject implements Serializable {
    private static final long serialVersionUID = -5272723554813580688L;
    private String intId;

    private String intCompanyId;

    private String intTrainSubjectId;

    public String getIntId() {
        return intId;
    }

    public void setIntId(String intId) {
        this.intId = intId;
    }


    public String getIntCompanyId() {
        return intCompanyId;
    }

    public void setIntCompanyId(String intCompanyId) {
        this.intCompanyId = intCompanyId;
    }

    public String getIntTrainSubjectId() {
        return intTrainSubjectId;
    }

    public void setIntTrainSubjectId(String intTrainSubjectId) {
        this.intTrainSubjectId = intTrainSubjectId;
    }
}