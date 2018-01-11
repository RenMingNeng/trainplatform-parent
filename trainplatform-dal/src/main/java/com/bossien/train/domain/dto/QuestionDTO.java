package com.bossien.train.domain.dto;


import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */
public class QuestionDTO {
    private String platformCode;
    private String cmdType;
    private List<QuestionMessage> dataObj;

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

    public List<QuestionMessage> getDataObj() {
        return dataObj;
    }

    public void setDataObj(List<QuestionMessage> dataObj) {
        this.dataObj = dataObj;
    }
}
