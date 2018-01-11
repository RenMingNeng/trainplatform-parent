package com.bossien.train.domain.dto;

import com.bossien.train.domain.CompanyUser;
import com.bossien.train.domain.TrainSubject;

import java.io.Serializable;
import java.util.List;


public class CompanyUserDTO implements Serializable {

    private String platformCode;
    private String cmdType;
    private List<CompanyUserMessage> dataObj;

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getCmdType() {
        return cmdType;
    }

    public void setCmdType(String cmdType) {
        this.cmdType = cmdType;
    }

    public List<CompanyUserMessage> getDataObj() {
        return dataObj;
    }

    public void setDataObj(List<CompanyUserMessage> dataObj) {
        this.dataObj = dataObj;
    }
}
