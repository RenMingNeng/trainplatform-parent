package com.bossien.train.domain;

import com.bossien.train.util.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by A on 2017/12/16.
 *
 * 发送者与接收者关联关系
 *
 */
public class Msg implements Serializable {
    //发送者
    private String sendId;
    //发送人姓名
    private String sendName;
    //接受者
    private String recId;
    //消息id
    private String msgId;
    //阅读状态:1未读2已读
    private String status;
    //发送时间
    private String createDate;

    public Msg(){}

    public Msg(String sendId, String sendName, String recId, String msgId){
        this.sendId = sendId;
        this.sendName = sendName;
        this.recId = recId;
        this.msgId = msgId;
        this.status = MsgStatus.status_1.getValue();
        this.createDate = DateUtils.formatDateTime(new Date());
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public enum MsgStatus{
        status_1("1", "未读"),status_2("2", "已读");

        private String key;
        private String value;
        MsgStatus(String value, String key){
            this.value = value;
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
