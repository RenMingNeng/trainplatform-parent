package com.bossien.train.domain;

/**
 * Created by A on 2017/12/16.
 *
 * 消息内容表
 *
 */
public class MsgText {
    //消息id
    private String msgId;
    //消息title
    private String msgTitle;
    //消息内容
    private String msgContent;
    //附件
    private String msgFile;
    //发布时间
    private String publishTime;
    //是否置顶
    private String isTop;
    //是否hot:1否2是
    private String isHot;
    //创建时间
    private String createDate;

    public MsgText(){}

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getMsgFile() {
        return msgFile;
    }

    public void setMsgFile(String msgFile) {
        this.msgFile = msgFile;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public String getIsHot() {
        return isHot;
    }

    public void setIsHot(String isHot) {
        this.isHot = isHot;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public enum IsTop{
        status_1("1", "否"),status_2("2", "是");

        private String key;
        private String value;
        IsTop(String value, String key){
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

    public enum IsHot{
        status_1("1", "否"),status_2("2", "是");

        private String key;
        private String value;
        IsHot(String value, String key){
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
