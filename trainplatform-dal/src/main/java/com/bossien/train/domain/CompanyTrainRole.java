package com.bossien.train.domain;

import java.io.Serializable;

public class CompanyTrainRole implements Serializable {
    private static final long serialVersionUID = -595736274465560456L;
    private String intId;

    private String intTrainRoleId;

    private String intCompanyId;

    public String getIntId() {
        return intId;
    }

    public void setIntId(String intId) {
        this.intId = intId;
    }


    public String getIntTrainRoleId() {
        return intTrainRoleId;
    }

    public void setIntTrainRoleId(String intTrainRoleId) {
        this.intTrainRoleId = intTrainRoleId;
    }

    public String getIntCompanyId() {
        return intCompanyId;
    }

    public void setIntCompanyId(String intCompanyId) {
        this.intCompanyId = intCompanyId;
    }
}