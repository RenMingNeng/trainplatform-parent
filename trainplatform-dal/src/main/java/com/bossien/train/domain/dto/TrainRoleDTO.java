package com.bossien.train.domain.dto;

import java.util.List;

/**
 * Created by zhaoli on 2017/8/3.
 */
public class TrainRoleDTO {

    private String platformCode;
    private String cmdType;
    private List<TrainRoleMessage> dataObj;

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public List<TrainRoleMessage> getDataObj() {
        return dataObj;
    }

    public void setDataObj(List<TrainRoleMessage> dataObj) {
        this.dataObj = dataObj;
    }

    public String getCmdType() {
        return cmdType;
    }

    public void setCmdType(String cmdType) {
        this.cmdType = cmdType;
    }


 /*   @Override
    public String toString() {
        return "TrainSubjectDTO{" +
                "platformCode='" + platformCode + '\'' +
                ", cmdType='" + cmdType + '\'' +
                ", dataObj=" + dataObj +
                '}';
    }*/
}
