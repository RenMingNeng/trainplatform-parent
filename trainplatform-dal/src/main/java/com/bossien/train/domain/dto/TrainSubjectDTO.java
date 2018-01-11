package com.bossien.train.domain.dto;
import java.util.List;

/**
 * Created by zhaoli on 2017/8/3.
 */
public class TrainSubjectDTO {

    private String platformCode;
    private String cmdType;
    private List<TrainSubjectMessage> dataObj;

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public List<TrainSubjectMessage> getDataObj() {
        return dataObj;
    }

    public void setDataObj(List<TrainSubjectMessage> dataObj) {
        this.dataObj = dataObj;
    }

    public String getCmdType() {
        return cmdType;
    }

    public void setCmdType(String cmdType) {
        this.cmdType = cmdType;
    }

}
